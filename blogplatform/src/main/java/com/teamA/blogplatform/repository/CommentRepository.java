package com.teamA.blogplatform.repository;

import com.teamA.blogplatform.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBlogPostId(Long blogPostId);
    List<Comment> findByUserId(Long userId);
}
