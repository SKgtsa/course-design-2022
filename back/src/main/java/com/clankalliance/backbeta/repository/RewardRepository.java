package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardRepository extends JpaRepository<Reward,Integer> {
    @Query("from Reward r where r.id=?1")
    Optional<Reward> findById(long id);
}
