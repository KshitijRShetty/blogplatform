package com.teamA.blogplatform.controller;

import com.teamA.blogplatform.dto.BlogPostRequest;
import com.teamA.blogplatform.dto.BlogPostResponse;
import com.teamA.blogplatform.dto.UserSummary;
import com.teamA.blogplatform.model.BlogPost;
import com.teamA.blogplatform.model.User;
import com.teamA.blogplatform.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/blogposts")
@CrossOrigin(origins = "*")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    // Public endpoints
    @GetMapping("/public/all")
    public ResponseEntity<List<BlogPostResponse>> getAllApprovedPosts() {
        List<BlogPostResponse> posts = blogPostService.getAllApprovedPostResponses();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/public/{postId}")
    public ResponseEntity<BlogPostResponse> getPostById(@PathVariable Long postId) {
        BlogPost post = blogPostService.getPostById(postId);
        User author = post.getAuthor();
        UserSummary authorSummary = new UserSummary(author.getId(), author.getUsername());
        BlogPostResponse response = new BlogPostResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getStatus().toString(),
            post.getTags(),
            post.getCreationDate(),
            post.getLastModifiedDate(),
            authorSummary
            );
        
        return ResponseEntity.ok(response);
    }


    // Protected endpoints
    @PostMapping
    public ResponseEntity<BlogPost> createPost(@Valid @RequestBody BlogPostRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        BlogPost createdPost = blogPostService.createPost(request, user.getId());
        return ResponseEntity.ok(createdPost);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<BlogPost> updatePost(@PathVariable Long postId, @Valid @RequestBody BlogPostRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        BlogPost updatedPost = blogPostService.updatePost(postId, request, user.getId());
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        blogPostService.deletePost(postId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<BlogPost>> getMyPosts(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<BlogPost> posts = blogPostService.getPostsByAuthor(user.getId());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<BlogPost>> getPendingPosts() {
        List<BlogPost> posts = blogPostService.getPendingPosts();
        if (posts == null) {
            posts = new ArrayList<>();
       }
        return ResponseEntity.ok(posts);
   }

    @PutMapping("/{postId}/approve")
    public ResponseEntity<BlogPost> approvePost(@PathVariable Long postId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        BlogPost post = blogPostService.approvePost(postId, user.getId());
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{postId}/reject")
    public ResponseEntity<BlogPost> rejectPost(@PathVariable Long postId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        BlogPost post = blogPostService.rejectPost(postId, user.getId());
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{postId}/submit")
    public ResponseEntity<BlogPost> submitForReview(@PathVariable Long postId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        BlogPost post = blogPostService.submitForReview(postId, user.getId());
        return ResponseEntity.ok(post);
    }
}
