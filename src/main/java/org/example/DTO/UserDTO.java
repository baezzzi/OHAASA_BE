package org.example.DTO;

import org.example.Entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {

    private String id;
    private String pw;
    private String checkpw;
    private String nickname;
    private String email;
    private int zodiac;
    private LocalDate birth;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .pw(this.pw)
                .nickname(this.nickname)
                .email(this.email)
                .zodiac(this.zodiac)
                .birth(this.birth)
                .build();
    }
}
