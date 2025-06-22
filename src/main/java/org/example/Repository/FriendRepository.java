package org.example.Repository;

import org.example.Entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, Integer> {
    List<FriendEntity> findByEmail(String email);
}
