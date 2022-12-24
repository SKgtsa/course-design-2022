package com.clankalliance.backbeta.repository.userRepository;

import com.clankalliance.backbeta.entity.blog.Comment;
import com.clankalliance.backbeta.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Integer>  {
}
