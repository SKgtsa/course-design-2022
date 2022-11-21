package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {

    @Query("from Course c where c.id=?1")
    Optional<Course> findById(long id);

    @Query("from Course c where c.year=?1 and c.semester=?2")
    Optional<Course> findByTime(Integer year,String semester);

}
