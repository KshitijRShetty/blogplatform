package com.teamA.blogplatform.controller;

import com.teamA.blogplatform.model.BlogPost;
import com.teamA.blogplatform.model.User;
import com.teamA.blogplatform.service.BlogPostService;
import com.teamA.blogplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private UserService userService;

    @GetMapping("/posts")
    public ResponseEntity<List<BlogPost>> searchPosts(@RequestParam String q) {
        List<BlogPost> posts = blogPostService.searchPosts(q);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String q) {
        List<User> allUsers = userService.findAllUsers();
        List<User> matchingUsers = allUsers.stream()
                .filter(user -> user.getUsername().toLowerCase().contains(q.toLowerCase()) ||
                               user.getEmail().toLowerCase().contains(q.toLowerCase()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(matchingUsers);
    }

    @GetMapping("/all")
    public ResponseEntity<SearchResults> searchAll(@RequestParam String q) {
        List<BlogPost> posts = blogPostService.searchPosts(q);
        List<User> users = userService.findAllUsers().stream()
                .filter(user -> user.getUsername().toLowerCase().contains(q.toLowerCase()) ||
                               user.getEmail().toLowerCase().contains(q.toLowerCase()))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new SearchResults(posts, users));
    }

    public static class SearchResults {
        public List<BlogPost> posts;
        public List<User> users;

        public SearchResults(List<BlogPost> posts, List<User> users) {
            this.posts = posts;
            this.users = users;
        }
    }
}
