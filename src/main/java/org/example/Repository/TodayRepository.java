package org.example.Repository;

import org.example.Entity.TodayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodayRepository extends JpaRepository<TodayEntity, Integer> {
}
