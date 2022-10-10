package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "from User u where u.loginName=?1")
    Optional<User> findUserByLoginName(String loginName);

    @Query(value = "from User u where u.token=?1")
    Optional<User> findUserByToken(String token);
}
