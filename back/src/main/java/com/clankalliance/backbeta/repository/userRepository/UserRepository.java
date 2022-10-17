package com.clankalliance.backbeta.repository.userRepository;

import com.clankalliance.backbeta.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * User父类接口
 * 不再实例化，通过NoRepository注解
 * 所有属性会映射到子类中
 */

@NoRepositoryBean
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional findByUserNumber(long userNumber);

    Optional findUserById(long id);


}
