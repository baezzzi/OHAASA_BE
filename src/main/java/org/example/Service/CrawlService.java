package org.example.Service;

import org.springframework.beans.factory.annotation.Value;
import lombok.Getter;
import lombok.Setter;
import org.example.Component.SeleniumCrawler;
import org.example.DTO.TranslateResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public List<Map<String, String>> runCrawler() {
        try {
            return crawler.crawl();
        } catch (IOException e) {
            System.out.println("Crawl Failed" + e.getMessage());
        }
        return new ArrayList<>(); // 빈 리스트 반환
    }

    // 번역 서비스 papago 연동
    public String translate(String source, String target, String text) {
        String data = "source=" + source + "&target=" + target + "&text=" + URLEncoder.encode(text, StandardCharsets.UTF_8);
        WebClient webClient = WebClient.builder()
                .baseUrl("https://papago.apigw.ntruss.com/nmt/v1")
                .build();

        TranslateResponseDTO response = webClient.post()
                .uri("/translation")
                .header("x-ncp-apigw-api-key-id", clientId)
                .header("x-ncp-apigw-api-key", clientSecret)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(TranslateResponseDTO.class)
                .block();
        System.out.println(response);
        return Objects.requireNonNull(response).getMessage().getResult().getTranslatedText();
    }

    // 번역 결과 적용
    public List<Map<String, String>> translateApply() throws IOException {
        List<Map<String, String>> result = crawler.crawl();
        for (Map<String, String> item : result) {
            String horoTxt = item.get("horoText");
            String translatedText = translate("ja","ko",horoTxt);
            item.put("translatedText", translatedText);
        }
        return result;
    }
}
