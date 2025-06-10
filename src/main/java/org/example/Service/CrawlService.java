package org.example.Service;

import org.example.Entity.TodayEntity;
import org.example.Repository.TodayRepository;
import org.springframework.beans.factory.annotation.Value;
import lombok.Getter;
import lombok.Setter;
import org.example.Component.SeleniumCrawler;
import org.example.DTO.TranslateResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.IOException;

@Service
public class CrawlService {

    @Setter
    @Value("${naver.client-id}")
    private String clientId;

    @Getter
    @Value("${naver.client-secret}")
    private String clientSecret;

    private final SeleniumCrawler crawler;
    private final TodayRepository todayRepository;

    public CrawlService(SeleniumCrawler crawler, TodayRepository todayRepository) {
        this.crawler = crawler;
        this.todayRepository = todayRepository;
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
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(TranslateResponseDTO.class)
                .block();
        System.out.println(response);
        return Objects.requireNonNull(response).getMessage().getResult().getTranslatedText();
    }

    // 번역 결과 저장 로직
    public void translateSaveAndApply() throws IOException {
        List<Map<String, String>> result = crawler.crawl();
        int index = 1; // rank 나타냄

        for (Map<String, String> item : result) {
            String mmdd = item.get("date");
            String todayMMdd = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"));

            if (!mmdd.equals(todayMMdd)) {
                System.out.println(todayMMdd+ "날짜 다름");
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

            TodayEntity today = TodayEntity.builder()
                    .name(name)
                    .content(content)
                    .lucky(lucky)
                    .date(LocalDate.now())
                    .rank(index)
                    .build();

            todayRepository.save(today);
            System.out.println("DB 저장완료");
            index++;
        }
    }

    // DB에서 랭킹 가져오는 로직
    public List<Map<String, String>> getTodayRank(LocalDate date) {
        List<TodayEntity> todayEntities = todayRepository.findByDate(date);
        List<Map<String, String>> result = new ArrayList<>();
        for (TodayEntity entity : todayEntities) {
            Map<String, String> item = new HashMap<>();
            item.put("name", entity.getName());
            item.put("content", entity.getContent());
            item.put("lucky", entity.getLucky());
            item.put("rank", String.valueOf(entity.getRank()));
            result.add(item);
        }
        return result;
    }
}
