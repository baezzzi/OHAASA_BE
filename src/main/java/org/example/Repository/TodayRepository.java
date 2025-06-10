package org.example.Repository;

import org.example.Entity.TodayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TodayRepository extends JpaRepository<TodayEntity, Integer> {
    List<TodayEntity> findByDate(LocalDate date);
}
