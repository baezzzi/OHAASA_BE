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
@Table(name = "User")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(length = 10)
    private String nickname;


    private int zodiac;

    private String image;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "fcm_token")
    private String fcmToken;

    private String uid;

    @Builder
    public UserEntity(int id, String nickname, String email, int zodiac, String image, LocalDate birth, String fcmToken, String uid) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.zodiac = zodiac;
        this.image = image;
        this.birth = birth;
        this.fcmToken = fcmToken;
        this.uid = uid;
    }


}
