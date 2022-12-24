package com.clankalliance.backbeta.repository.userRepository.sub;

import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Integer> {

    @Query("from Teacher t where t.userNumber=?1")
    Optional<Teacher> findByUserNumber(long userNumber);

    @Query("from Teacher  t where t.phone=?1")
    Optional<Teacher> findByPhone(long phone);

    @Query("from Teacher t where t.id=?1")
    Optional<Teacher> findUserById(long id);

}
