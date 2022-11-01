package com.clankalliance.backbeta.repository.userRepository.sub;

import com.clankalliance.backbeta.entity.user.sub.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

    @Query("from Student s where s.userNumber=?1")
    Optional<Student> findByUserNumber(long userNumber);

    @Query("from Student  s where s.phone=?1")
    Optional<Student> findByPhone(long phone);

    @Query("from Student s where s.id=?1")
    Optional<Student> findUserById(long id);
}
