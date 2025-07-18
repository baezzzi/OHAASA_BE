package org.example.Controller;

import org.example.DTO.PredictDTO;
import org.example.Service.PredictService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/quiz")
public class PredictController {

    private final PredictService predictService;

    public PredictController(PredictService predictService) {
        this.predictService = predictService;
    }

    // 예측 저장
    @PostMapping("/save-predict")
    public void savePredict(@RequestParam String email, @RequestBody PredictDTO predictDTO) {
        predictService.savePredict(email, predictDTO);
    }

    // 예측 가져오기
    @GetMapping("/get-predict")
    public Map<String, String> getPredict(@RequestParam String email, @RequestParam String date) {
        return predictService.getPredict(email, date);
    }
}
