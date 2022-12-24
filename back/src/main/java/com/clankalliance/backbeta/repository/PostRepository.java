package com.clankalliance.backbeta.repository;

import com.clankalliance.backbeta.entity.blog.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer>  {

    @Query("from Post p where p.id=?1")
    Optional<Post> findById(long id);

}
