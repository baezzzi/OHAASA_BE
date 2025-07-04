package org.example.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TodayEntity {

    private String name;

    private String content;
    private String lucky;
    private Date date;
    private int rank;

    @Builder
    public TodayEntity(String name, String content, String lucky, Date date, int rank) {
        this.name = name;
        this.content = content;
        this.lucky = lucky;
        this.date = date;
        this.rank = rank;
    }
}
