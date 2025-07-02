package com.teamA.blogplatform.service;

import com.teamA.blogplatform.exception.ResourceNotFoundException;
import com.teamA.blogplatform.exception.UnauthorizedException;
import com.teamA.blogplatform.model.BlogPost;
import com.teamA.blogplatform.model.Comment;
import com.teamA.blogplatform.model.User;
import com.teamA.blogplatform.repository.BlogPostRepository;
import com.teamA.blogplatform.repository.CommentRepository;
import com.teamA.blogplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    public Comment createComment(Long postId, String content, Long userId) {
        BlogPost blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = Comment.builder()
                .content(content)
                .blogPost(blogPost)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, String content, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You can only edit your own comments");
        }

        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByBlogPostId(postId);
    }

    public List<Comment> getCommentsByUser(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
    }
}
