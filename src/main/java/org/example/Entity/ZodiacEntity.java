package org.example.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ZodiacEntity {

    @Id
    private int id;

    private String name;

    @Builder
    public ZodiacEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
