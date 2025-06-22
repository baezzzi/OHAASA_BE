package org.example.DTO;

import lombok.Getter;
import lombok.Setter;
import org.example.Entity.FriendEntity;

import java.time.LocalDate;

@Getter
@Setter
public class FriendDTO {
    private int id;
    private String email;
    private String name;
    private LocalDate birth;
    private String zodiac;
    private int idx;

    public FriendEntity toEntity() {
        return FriendEntity.builder()
                .email(this.email)
                .name(this.name)
                .birth(this.birth)
                .zodiac(this.zodiac)
                .idx(this.idx)
                .build();
    }
}
