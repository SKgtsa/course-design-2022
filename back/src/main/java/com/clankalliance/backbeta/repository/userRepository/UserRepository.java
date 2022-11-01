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

    //跨表联查没用 逻辑问题




}
