package com.teamA.blogplatform.repository;

import com.teamA.blogplatform.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    
    Optional<PostLike> findByUserIdAndBlogPostId(Long userId, Long blogPostId);
    
    boolean existsByUserIdAndBlogPostId(Long userId, Long blogPostId);
    
    int countByBlogPostId(Long blogPostId);
    
    void deleteByUserIdAndBlogPostId(Long userId, Long blogPostId);
}
