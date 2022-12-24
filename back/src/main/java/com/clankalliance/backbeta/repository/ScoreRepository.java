package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.Score;
import com.clankalliance.backbeta.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ScoreRepository extends JpaRepository<Score,Integer> {
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

    @Query("from Score s where s.student.id=?1 and s.course.id=?2 and s.course.year=?3 and s.course.semester=?4")
    Optional<Score> findByTime(long studentId, long courseId ,Integer year, String semester);

}
