package org.example.Component;

import org.example.Service.CrawlService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Component
public class HoroscopeSaveScheduler {

    private final CrawlService crawlService;

    public HoroscopeSaveScheduler(CrawlService crawlService) {
        this.crawlService = crawlService;
    }

    @Scheduled(cron = "0 42 17 ? * MON-FRI", zone = "Asia/Seoul")
    public void saveScheduler() throws IOException, InterruptedException, ExecutionException {
        crawlService.translateSaveAndApply();
        System.out.println("번역 완료 및 결과 저장");
    }
}
