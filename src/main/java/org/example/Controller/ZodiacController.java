package org.example.Controller;

import org.example.Service.ZodiacService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ZodiacController {
    private final ZodiacService zodiacService;

    public ZodiacController(ZodiacService zodiacService) {
        this.zodiacService = zodiacService;
    }

    @GetMapping("/zodiac/today")
    public ArrayList<Object> getTodayZodiacInfo() {
        return zodiacService.getTodayZodiacRankings();
    }
}
