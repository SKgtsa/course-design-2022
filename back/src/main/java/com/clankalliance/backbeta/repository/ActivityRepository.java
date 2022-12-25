package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,Integer> {
    @Query("from Activity a where a.id=?1")
    Optional<Activity> findById(long id);
}
