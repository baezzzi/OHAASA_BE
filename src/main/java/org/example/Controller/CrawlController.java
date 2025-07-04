package org.example.Controller;

import org.example.Service.CrawlService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/crawl")
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

    @GetMapping(value = "horoscope/ranking",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<Map<String, String>> getRanking() {
        LocalDate date = LocalDate.now();
        try {
            return crawlService.getTodayRank(date);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/content-lucky", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<Map<String, String>> getContentLucky(@RequestParam String name) {  // param name 은 en zodiac
        LocalDate date = LocalDate.now();
        try {
            return crawlService.getContentLucky(name, date);
        } catch(InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
