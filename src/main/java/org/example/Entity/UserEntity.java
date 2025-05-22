package org.example.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "User")
public class UserEntity {

    @Id
    @Column(length = 20, nullable = false)
    private String id;

    @Column(length = 15, nullable = false)
    private String pw;

    @Column(length = 10)
    private String nickname;

    @Column(length = 30, nullable = false)
    private String email;

    private int zodiac;

    @Builder
    public UserEntity(String id, String pw, String nickname, String email, int zodiac) {
        this.id = id;
        this.pw = pw;
        this.nickname = nickname;
        this.email = email;
        this.zodiac = zodiac;
    }
}
