package org.example.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TodayDTO {

    private String name;
    private String content;
    private String lucky;
    private int rank;
    private Date date;

}
