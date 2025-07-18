package org.example.DTO;

import lombok.Getter;
import lombok.Setter;
import org.example.Entity.PredictEntity;

@Getter
@Setter
public class PredictDTO {

    private String email;
    private String rank;
    private String state;

    public PredictEntity toEntity() {
        return PredictEntity.builder()
                .rank(this.rank)
                .state(this.state)
                .build();
    }
}
