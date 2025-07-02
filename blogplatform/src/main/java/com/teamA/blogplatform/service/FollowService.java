package com.teamA.blogplatform.service;

import com.teamA.blogplatform.exception.ResourceNotFoundException;
import com.teamA.blogplatform.model.Follow;
import com.teamA.blogplatform.model.User;
import com.teamA.blogplatform.repository.FollowRepository;
import com.teamA.blogplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    public Follow followUser(Long followerId, Long followedId) {
        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("You cannot follow yourself");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));

        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new ResourceNotFoundException("User to follow not found"));

        // Check if already following
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followedId)) {
            throw new IllegalArgumentException("You are already following this user");
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(followed)
                .build();

        return followRepository.save(follow);
    }

    public void unfollowUser(Long followerId, Long followedId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followedId)
                .orElseThrow(() -> new ResourceNotFoundException("Follow relationship not found"));

        followRepository.delete(follow);
    }

    public List<User> getFollowing(Long userId) {
        List<Follow> follows = followRepository.findByFollowerId(userId);
        return follows.stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());
    }

    public List<User> getFollowers(Long userId) {
        List<Follow> follows = followRepository.findByFollowingId(userId);
        return follows.stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList());
    }

    public boolean isFollowing(Long followerId, Long followedId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followedId);
    }

    public int getFollowersCount(Long userId) {
        return followRepository.findByFollowingId(userId).size();
    }

    public int getFollowingCount(Long userId) {
        return followRepository.findByFollowerId(userId).size();
    }
}
