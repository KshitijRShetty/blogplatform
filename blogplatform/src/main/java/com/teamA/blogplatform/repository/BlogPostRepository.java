package com.teamA.blogplatform.repository;

import com.teamA.blogplatform.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByStatusOrderByCreationDateDesc(BlogPost.PostStatus status);
    List<BlogPost> findByAuthorIdOrderByCreationDateDesc(Long authorId);
    
    @Query("SELECT p FROM BlogPost p WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :content, '%'))) AND p.status = :status")
    List<BlogPost> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseAndStatus(
            @Param("title") String title, 
            @Param("content") String content, 
            @Param("status") BlogPost.PostStatus status);
    
    // Enhanced search that includes author username
    @Query("SELECT p FROM BlogPost p WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.author.username) LIKE LOWER(CONCAT('%', :query, '%'))) AND p.status = :status ORDER BY p.creationDate DESC")
    List<BlogPost> findByTitleOrContentOrAuthorUsernameContainingIgnoreCaseAndStatus(
            @Param("query") String query, 
            @Param("status") BlogPost.PostStatus status);
    
    // Search posts by author username only
    @Query("SELECT p FROM BlogPost p WHERE LOWER(p.author.username) LIKE LOWER(CONCAT('%', :username, '%')) AND p.status = :status ORDER BY p.creationDate DESC")
    List<BlogPost> findByAuthorUsernameContainingIgnoreCaseAndStatus(
            @Param("username") String username,
            @Param("status") BlogPost.PostStatus status);
}
