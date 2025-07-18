package org.example.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PredictEntity {

    private String rank;
    private String state;

    @Builder
    public PredictEntity(String rank, String state) {
        this.rank = rank;
        this.state = state;
    }
}
