package org.example.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class FriendEntity {

    private String email;
    private String name;
    private Date birth;
    private String zodiac;

    @Builder
    public FriendEntity(String email, String name, Date birth, String zodiac) {
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.zodiac = zodiac;
    }
}
