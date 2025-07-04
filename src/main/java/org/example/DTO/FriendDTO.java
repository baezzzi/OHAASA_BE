package org.example.DTO;

import lombok.Getter;
import lombok.Setter;
import org.example.Entity.FriendEntity;

import java.util.Date;

@Getter
@Setter
public class FriendDTO {

    private String email;
    private String name;
    private Date birth;
    private String zodiac;

    public FriendEntity toEntity() {
        return FriendEntity.builder()
                .email(this.email)
                .name(this.name)
                .birth(this.birth)
                .zodiac(this.zodiac)
                .build();
    }
}
