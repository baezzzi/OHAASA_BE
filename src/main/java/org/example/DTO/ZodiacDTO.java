package org.example.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ZodiacDTO {

    private int rank;

    private String name;

    private String content;

    public ZodiacDTO(int rank, String name, String content) {
        this.rank = rank;
        this.name = name;
        this.content = content;
    }
}
