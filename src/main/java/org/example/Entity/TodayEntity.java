package org.example.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Today")
@Embeddable
public class TodayEntity {

    @EmbeddedId
    private TodayPK todayPK;

    @Column(name = "zodiac_content")
    private String content;

    @Column(name = "zodiac_lucky")
    private String lucky;


    @Builder
    public TodayEntity(TodayPK todayPk, String content, String lucky) {
        this.todayPK = todayPk;
        this.content = content;
        this.lucky = lucky;
    }
}
