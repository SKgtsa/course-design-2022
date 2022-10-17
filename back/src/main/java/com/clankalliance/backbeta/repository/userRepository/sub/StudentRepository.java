package com.clankalliance.backbeta.repository.userRepository.sub;

import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.repository.userRepository.UserRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends UserRepository {

    @Query("from Student s where s.userNumber=?1")
    Optional<Student> findByUserNumber(long userNumber);

    @Query("from Student s where s.id=?1")
    Optional<Student> findUserById(long id);
}
