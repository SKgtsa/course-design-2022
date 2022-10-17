package com.clankalliance.backbeta.repository.userRepository.sub;

import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.repository.userRepository.UserRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends UserRepository {

    @Query("from Manager m where m.userNumber=?1")
    Optional<Manager> findByUserNumber(long userNumber);

    @Query("from Manager m where m.id=?1")
    Optional<Manager> findUserById(long id);

}
