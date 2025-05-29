package org.example.Entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class TodayPK implements Serializable {

    private Integer id;
    private LocalDate today;

    public TodayPK() {}

    public TodayPK(Integer id, LocalDate today) {
        this.id = id;
        this.today = today;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodayPK)) return false;
        TodayPK todayPK = (TodayPK) o;
        return Objects.equals(id, todayPK.id) && Objects.equals(today, todayPK.today);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, today);
    }
}
