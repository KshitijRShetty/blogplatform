package com.teamA.blogplatform.controller;

import com.teamA.blogplatform.dto.CommentRequest;
import com.teamA.blogplatform.model.Comment;
import com.teamA.blogplatform.model.User;
import com.teamA.blogplatform.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> createComment(@Valid @RequestBody CommentRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Comment comment = commentService.createComment(request.getBlogPostId(), request.getContent(), user.getId());
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, 
                                               @Valid @RequestBody CommentRequest request, 
                                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Comment comment = commentService.updateComment(commentId, request.getContent(), user.getId());
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        commentService.deleteComment(commentId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUser(@PathVariable Long userId) {
        List<Comment> comments = commentService.getCommentsByUser(userId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }
}
