package com.teamA.blogplatform.controller;

import com.teamA.blogplatform.model.User;
import com.teamA.blogplatform.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "*")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @PostMapping("/toggle/{postId}")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long postId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        boolean isLiked = postLikeService.toggleLike(postId, user.getId());
        int likeCount = postLikeService.getLikeCount(postId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("isLiked", isLiked);
        response.put("likeCount", likeCount);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{postId}")
    public ResponseEntity<Map<String, Object>> getLikeStatus(@PathVariable Long postId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        boolean isLiked = postLikeService.isLikedByUser(postId, user.getId());
        int likeCount = postLikeService.getLikeCount(postId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("isLiked", isLiked);
        response.put("likeCount", likeCount);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<Map<String, Object>> getLikeCount(@PathVariable Long postId) {
        int likeCount = postLikeService.getLikeCount(postId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", likeCount);
        
        return ResponseEntity.ok(response);
    }
}
