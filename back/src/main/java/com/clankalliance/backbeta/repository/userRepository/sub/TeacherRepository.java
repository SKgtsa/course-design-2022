package com.clankalliance.backbeta.repository.userRepository.sub;

import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.userRepository.UserRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends UserRepository {

    @Query("from Teacher t where t.userNumber=?1")
    Optional<Teacher> findByUserNumber(long userNumber);

    @Query("from Teacher t where t.id=?1")
    Optional<Teacher> findUserById(long id);
}
