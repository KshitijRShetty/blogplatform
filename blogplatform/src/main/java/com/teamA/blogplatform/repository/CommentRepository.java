package com.teamA.blogplatform.repository;

import com.teamA.blogplatform.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.blogPost.id = :blogPostId ORDER BY c.createdAt DESC")
    List<Comment> findByBlogPostId(Long blogPostId);
    
    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId ORDER BY c.createdAt DESC")
    List<Comment> findByUserId(Long userId);
}
