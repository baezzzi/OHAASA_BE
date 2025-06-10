package org.example.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Today")
public class TodayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    private String name;

    @Column(name = "zodiac_content")
    private String content;

    @Column(name = "zodiac_lucky")
    private String lucky;

    @Column(name = "today")
    private LocalDate date;

    @Column(name = "ranking")
    private int rank;

    @Builder
    public TodayEntity(String name, String content, String lucky, LocalDate date, int rank) {
        this.name = name;
        this.content = content;
        this.lucky = lucky;
        this.date = date;
        this.rank = rank;
    }
}
