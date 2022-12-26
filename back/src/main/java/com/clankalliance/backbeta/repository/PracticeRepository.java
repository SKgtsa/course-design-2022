package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PracticeRepository extends JpaRepository<Practice,Integer> {
    @Query("from Practice p where p.id=?1")
    Optional<Practice> findById(long id);
}
