package com.clankalliance.backbeta.repository.blogRepository;

import com.clankalliance.backbeta.entity.blog.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer>  {

    @Query("from Post p where p.id=?1")
    Optional<Post> findById(String id);

    @Query("from Post p where ?1='' or p.heading like %?1%")
    List<Post> findByHeading(String heading);

}
