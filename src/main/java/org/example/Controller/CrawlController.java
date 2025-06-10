package org.example.Controller;

import org.example.Service.CrawlService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
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

    // 이거 스케줄러로 바꿔야됨 지금은 내가 요청해야지만 저장함
    @PostMapping("/horoscope/save")
    public String saveTranslatedHoroscopes() throws IOException {
        crawlService.translateSaveAndApply();
        return "크롤링 및 번역 결과 저장이 완료되었습니다.";
    }

    @GetMapping(value = "horoscope/ranking",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<Map<String, String>> getRanking() {
        LocalDate date = LocalDate.now();
        return crawlService.getTodayRank(date);
    }

}
