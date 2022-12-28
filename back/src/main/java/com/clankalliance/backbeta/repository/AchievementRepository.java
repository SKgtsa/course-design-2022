package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement,Integer> {

    @Query("from Achievement a where a.id=?1")
    Optional<Achievement> findById(long id);

    @Query("from Achievement a where a.name=?1")
    List<Achievement> findByName(String name);

    @Query("from Achievement a where a.name=?1 and a.id < 100")
    Optional<Achievement> findDuplicate(String name);

}
