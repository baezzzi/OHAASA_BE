package org.example.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import lombok.Getter;
import lombok.Setter;
import org.example.Component.SeleniumCrawler;
import org.example.DTO.TranslateResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class CrawlService {

    @Setter
    @Value("${naver.client-id}")
    private String clientId;

    @Getter
    @Value("${naver.client-secret}")
    private String clientSecret;

    private final SeleniumCrawler crawler;

    public CrawlService(SeleniumCrawler crawler) {
        this.crawler = crawler;
    }

    // 크롤링하는 거
    public List<Map<String, String>> runCrawler() {
        try {
            return crawler.crawl();
        } catch (IOException e) {
            System.out.println("Crawl Failed" + e.getMessage());
        }
        return new ArrayList<>(); // 빈 리스트 반환
    }

    // 번역 서비스,  papago 연동
    public String translate(String source, String target, String text) {
        String data = "source=" + source + "&target=" + target + "&text=" + URLEncoder.encode(text, StandardCharsets.UTF_8);
        WebClient webClient = WebClient.builder()
                .baseUrl("https://papago.apigw.ntruss.com/nmt/v1")
                .build();

        TranslateResponseDTO response = webClient.post()
                .uri("/translation")
                .header("x-ncp-apigw-api-key-id", clientId)
                .header("x-ncp-apigw-api-key", clientSecret)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(TranslateResponseDTO.class)
                .block();
        return Objects.requireNonNull(response).getMessage().getResult().getTranslatedText();
    }

    // 번역 결과 저장 로직
    public void translateSaveAndApply() throws IOException, InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference today = db.collection("today");

        List<Map<String, String>> result = crawler.crawl();
        int index = 1; // rank 나타냄

        for (Map<String, String> item : result) {
            String MMdd = item.get("date");
            String todayMMdd = LocalDate.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("MM-dd"));

            if (!MMdd.equals(todayMMdd)) {
                System.out.println(todayMMdd+ "날짜 다름");
                continue;
            }

            // 번역 실행
            String translatedText = translate("ja","ko", item.get("horoTxt"));
            String[] translatedLines = translatedText.split("\n");

            String lucky = translatedLines.length > 0 ? translatedLines[translatedLines.length - 1] : "";

            StringBuilder contentBuilder = new StringBuilder();
            for (int i = 0; i < translatedLines.length - 1; i++) {
                contentBuilder.append(translatedLines[i]);
                if (i != translatedLines.length - 2) {
                    contentBuilder.append("\n");
                }
            }
            String content = contentBuilder.toString();

            String name = item.get("name");

            // Firestore에 저장할 데이터 Map 생성
            Map<String, Object> todayData = Map.of(
                    "name", name,
                    "content", content,
                    "lucky", lucky,
                    "date", todayMMdd, // 또는 LocalDate.now() 등 원하는 형식
                    "rank", index
            );

            // Firestore에 저장 (name+date 조합을 문서 ID로 사용 가능)
            ApiFuture<?> writeResult = today
                    .document(todayMMdd)
                    .collection("zodiacList")
                    .document(name)
                    .set(todayData);

            // 결과를 기다려서 예외 발생 시 처리
            writeResult.get();

            index++;
        }
    }

    // DB에서 랭킹 가져오는 로직
    public List<Map<String, String>> getTodayRank(LocalDate date) throws InterruptedException, ExecutionException {
        // today 컬렉션 (테이블)

        Firestore db = FirestoreClient.getFirestore();

        // 날짜를 "MM-dd" 포맷으로 변환
        String dateDoc = date.format(DateTimeFormatter.ofPattern("MM-dd"));

        CollectionReference list = db
                .collection("today")
                .document(dateDoc)
                .collection("zodiacList");

        ApiFuture<QuerySnapshot> todayFuture = list.get();
        List<QueryDocumentSnapshot> documents = todayFuture.get().getDocuments();

        List<Map<String, String>> result = new ArrayList<>();
        for (QueryDocumentSnapshot todayDoc : documents) {
            Map<String, String> item = new HashMap<>();
            item.put("name", todayDoc.getString("name"));
            item.put("content", todayDoc.getString("content"));
            item.put("lucky", todayDoc.getString("lucky"));
            item.put("rank", String.valueOf(todayDoc.getLong("rank")));
            result.add(item);
        }

        return result;
    }

    public List<Map<String, String>> getContentLucky(String name, LocalDate date) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference today = db.collection("today");

        // 날짜를 "MM-dd" 포맷으로 변환
        String dateDoc = date.format(DateTimeFormatter.ofPattern("MM-dd"));

        // 문서 경로: today/{dateDoc}/zodiacList/{name}
        DocumentReference docRef = today
                .document(dateDoc)
                .collection("zodiacList")
                .document(name);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();

        if (!doc.exists()) {
            throw new IllegalArgumentException("오늘의 운세 정보가 없습니다.");
        }

        Map<String, String> result = new HashMap<>();
        result.put("content", doc.getString("content"));
        result.put("lucky", doc.getString("lucky"));
        result.put("rank", String.valueOf(doc.getLong("rank")));

        // List로 감싸서 반환
        List<Map<String, String>> resultList = new ArrayList<>();
        resultList.add(result);
        return resultList;
    }


    // 전 날 별자리 등수를 가져와야됨
    public String getYesterdayRank(String name, String date) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference today = db.collection("today");

            DocumentReference docRef = today
                    .document(date)
                    .collection("zodiacList")
                    .document(name);

            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot doc = future.get();

            if (!doc.exists()) {
                throw new IllegalArgumentException("오늘의 운세 정보가 없습니다.");
            }

            return String.valueOf(doc.getLong("rank"));

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
