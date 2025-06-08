package org.example.DTO;

import org.example.Entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {

    private String id;
    private String nickname;
    private String email;
    private int zodiac;
    private LocalDate birth;
    private String uid;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(this.email)
                .nickname(this.nickname)
                .zodiac(this.zodiac)
                .birth(this.birth)
                .uid(this.uid)
                .build();
    }
}
