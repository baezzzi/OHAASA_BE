package org.example.Service;

import org.example.Component.ZodiacComponent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ZodiacService {

    private final ZodiacComponent zodiacComponent;

    public ZodiacService(ZodiacComponent zodiacComponent) {
        this.zodiacComponent = zodiacComponent;
    }

    public ArrayList<Object> getTodayZodiacRankings() {
        return zodiacComponent.getTodayZodiacRankings();
    }
}
