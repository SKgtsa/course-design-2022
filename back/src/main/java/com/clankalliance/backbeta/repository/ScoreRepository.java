package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository {
    @Query("from Score s where s.id=?1")
    Optional<Score> findById(long id);

    /**
     * 通过成绩对应的课程id与学生id查找
     * @param courseId 课程id
     * @param studentId 学生id
     * @return
     */
    @Query("from Score s where s.course.id=?1 and s.student.id=?2")
    Optional<Score> findByCourseStudentId(long courseId, long studentId);
}
