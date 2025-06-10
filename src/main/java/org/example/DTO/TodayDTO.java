package org.example.DTO;

import lombok.Getter;
import lombok.Setter;
import org.example.Entity.TodayEntity;

import java.time.LocalDate;

@Getter
@Setter
public class TodayDTO {

    private String name;
    private String content;
    private String lucky;
    private int rank;
    private LocalDate date;

    public TodayEntity toEntity() {
        return TodayEntity.builder()
                .name(this.name)
                .content(this.content)
                .lucky(this.lucky)
                .rank(this.rank)
                .build();

    }
}
