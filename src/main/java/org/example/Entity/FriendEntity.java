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
@Table(name="friend")
public class FriendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;

    private String name;

    private LocalDate birth;

    private String zodiac;

    private int idx;

    @Builder
    public FriendEntity(String email, String name, LocalDate birth, String zodiac, int idx) {
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.zodiac = zodiac;
        this.idx = idx;
    }
}
