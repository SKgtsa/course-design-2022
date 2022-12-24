package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.course.ClassTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ClassTimeRepository extends JpaRepository<ClassTime,Integer>{
    @Query("from ClassTime cT where cT.id=?1")
    Optional<ClassTime> findById(long id);

    @Query("from ClassTime cT where cT.id=?1")
    Optional<ClassTime> deleteById(long id);
}
