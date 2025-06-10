package org.example.Controller;

import org.example.Service.CrawlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequestMapping("/crawl")
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

    // 번역하기 위한 엔드포인트
    @PostMapping("/translate")
    public String translate(@RequestParam String source,@RequestParam String target,@RequestParam String text) {
        return crawlService.translate(source, target, text);
    }

    @PostMapping("/horoscope/translated")
    public List<Map<String, String>> getTranslatedHoroscopes() throws IOException {
        return crawlService.translateApply();
    }

}
