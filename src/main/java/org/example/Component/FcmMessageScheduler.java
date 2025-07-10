package org.example.Component;

import org.example.Service.FcmService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FcmMessageScheduler {

    private final FcmService fcmService;

    public FcmMessageScheduler(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @Scheduled(cron = "0 0 7 ? * MON-FRI", zone = "Asia/Seoul")
    public void sendWeekMessage() {
        fcmService.sendMessage(
                "OzO",
                "오늘의 오하아사가 도착했습니다!✨"
        );
    }

    @Scheduled(cron = "0 0 9 ? * SAT-SUN", zone = "Asia/Seoul")
    public void sendWeekendMessage() {
        fcmService.sendMessage(
                "OzO",
                "오늘의 오하아사가 1등이에요!"
        );
    }

    @Scheduled(cron = "0 42 17 ? * *", zone = "Asia/Seoul")
    public void sendMonthMessage() {
        fcmService.sendMessage(
                "OzO",
                "오늘의 오하아사가 도착했습니다!"
        );
    }
}
