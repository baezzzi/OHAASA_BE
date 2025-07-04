package org.example.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    private String email;
    private String nickname;
    private int zodiac;
    private String image;
    private Date birth;
    private String fcmToken;
    private String uid;
    private boolean firstLogin = true;

    @Builder
    public UserEntity(String nickname, String email, int zodiac, String image, Date birth, String fcmToken, String uid, boolean firstLogin) {
        this.email = email;
        this.nickname = nickname;
        this.zodiac = zodiac;
        this.image = image;
        this.birth = birth;
        this.fcmToken = fcmToken;
        this.uid = uid;
        this.firstLogin = firstLogin;
    }
}
