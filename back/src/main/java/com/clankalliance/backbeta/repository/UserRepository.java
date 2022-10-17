package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "from User u where u.userNumber=?1")
    Optional<User> findByUserNumber(long userNumber);

    @Query(value = "from User u where u.id=?1")
    Optional<User> findUserById(long id);
}
