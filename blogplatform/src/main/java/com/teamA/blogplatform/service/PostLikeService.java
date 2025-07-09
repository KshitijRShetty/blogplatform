package com.teamA.blogplatform.service;

import com.teamA.blogplatform.model.BlogPost;
import com.teamA.blogplatform.model.PostLike;
import com.teamA.blogplatform.model.User;
import com.teamA.blogplatform.repository.BlogPostRepository;
import com.teamA.blogplatform.repository.PostLikeRepository;
import com.teamA.blogplatform.repository.UserRepository;
import com.teamA.blogplatform.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean toggleLike(Long postId, Long userId) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Optional<PostLike> existingLike = postLikeRepository.findByUserIdAndBlogPostId(userId, postId);
        
        if (existingLike.isPresent()) {
            // Unlike the post
            postLikeRepository.delete(existingLike.get());
            return false; // Post was unliked
        } else {
            // Like the post
            PostLike newLike = PostLike.builder()
                    .user(user)
                    .blogPost(post)
                    .build();
            postLikeRepository.save(newLike);
            return true; // Post was liked
        }
    }

    public boolean isLikedByUser(Long postId, Long userId) {
        return postLikeRepository.existsByUserIdAndBlogPostId(userId, postId);
    }

    public int getLikeCount(Long postId) {
        return postLikeRepository.countByBlogPostId(postId);
    }
}
