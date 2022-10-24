package com.clankalliance.backbeta.repository.userRepository;

import com.clankalliance.backbeta.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User父类接口
 * 不再实例化，通过NoRepository注解
 * 所有属性会映射到子类中
 */

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    //跨表联查

    @Query("from Student s, Teacher t, Manager m where s.userNumber=?1 or t.userNumber=?1 or m.userNumber=?1 ")
    Optional<User> findByUserNumber(long userNumber);

    @Query("from Student s, Teacher t, Manager m where s.id=?1 or t.id=?1 or m.id=?1 ")
    Optional<User> findUserById(long id);


}
