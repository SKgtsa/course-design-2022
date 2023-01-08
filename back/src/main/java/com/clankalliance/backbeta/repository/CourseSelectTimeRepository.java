package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.CourseSelectTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSelectTimeRepository extends JpaRepository<CourseSelectTime,Integer> {
}
