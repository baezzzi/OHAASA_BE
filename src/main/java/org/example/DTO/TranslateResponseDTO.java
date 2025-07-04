package org.example.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class TranslateResponseDTO {

    private Message message;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Message{
        private Result result;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Result{
        private String translatedText;
    }
}
