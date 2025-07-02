package com.teamA.blogplatform.controller;

import com.teamA.blogplatform.model.Follow;
import com.teamA.blogplatform.model.User;
import com.teamA.blogplatform.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
@CrossOrigin(origins = "*")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/{userId}")
    public ResponseEntity<Follow> followUser(@PathVariable Long userId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Follow follow = followService.followUser(currentUser.getId(), userId);
        return ResponseEntity.ok(follow);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long userId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        followService.unfollowUser(currentUser.getId(), userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/following")
    public ResponseEntity<List<User>> getFollowing(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<User> following = followService.getFollowing(currentUser.getId());
        return ResponseEntity.ok(following);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<User>> getFollowers(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<User> followers = followService.getFollowers(currentUser.getId());
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<List<User>> getUserFollowing(@PathVariable Long userId) {
        List<User> following = followService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<User>> getUserFollowers(@PathVariable Long userId) {
        List<User> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/check/{userId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long userId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        boolean isFollowing = followService.isFollowing(currentUser.getId(), userId);
        return ResponseEntity.ok(isFollowing);
    }

    @GetMapping("/stats/{userId}")
    public ResponseEntity<FollowStats> getFollowStats(@PathVariable Long userId) {
        int followersCount = followService.getFollowersCount(userId);
        int followingCount = followService.getFollowingCount(userId);
        return ResponseEntity.ok(new FollowStats(followersCount, followingCount));
    }

    public static class FollowStats {
        public int followersCount;
        public int followingCount;

        public FollowStats(int followersCount, int followingCount) {
            this.followersCount = followersCount;
            this.followingCount = followingCount;
        }
    }
}
