package com.clankalliance.backbeta.repository.blogRepository;

import com.clankalliance.backbeta.entity.blog.Comment;
import com.clankalliance.backbeta.entity.blog.Post;
import com.clankalliance.backbeta.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Integer>  {

    @Query("from Comment c where c.id=?1")
    Optional<Comment> findById(String id);

}
