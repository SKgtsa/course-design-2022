package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.Activity;
import com.clankalliance.backbeta.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,Integer> {
    @Query("from Activity a where a.id=?1")
    Optional<Activity> findById(long id);

    /**
     * 根据学生id查找与他相关联的所有Activity
     * @param studentId 学生id
     * @return
     */
    @Query("from Activity a where a.student.id=?1")
    List<Activity> findByStudentId(long studentId);
}
