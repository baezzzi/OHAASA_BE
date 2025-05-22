package org.example.DTO;

import org.example.Entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String id;
    private String pw;
    private String checkpw;
    private String nickname;
    private String email;
    private int zodiac;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(this.id)
                .pw(this.pw)
                .nickname(this.nickname)
                .email(this.email)
                .zodiac(this.zodiac)
                .build();
    }
}
