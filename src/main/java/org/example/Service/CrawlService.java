package org.example.Service;

import org.example.Component.SeleniumCrawler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CrawlService {
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
}
