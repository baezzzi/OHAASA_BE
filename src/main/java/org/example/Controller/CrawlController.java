package org.example.Controller;

import org.example.Service.CrawlService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CrawlController {
    private final CrawlService crawlService;

    public CrawlController(CrawlService crawlService){
        this.crawlService = crawlService;
    }

    @GetMapping("/horoscope")
    public List<Map<String, String>> getHoroscopes() {
        return crawlService.runCrawler();
    }
}
