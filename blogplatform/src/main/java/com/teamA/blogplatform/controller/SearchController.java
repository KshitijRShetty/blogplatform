package com.teamA.blogplatform.controller;

import com.teamA.blogplatform.dto.BlogPostResponse;
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

    // Enhanced search that includes author username in the search
    @GetMapping("/posts")
    public ResponseEntity<List<BlogPostResponse>> searchPosts(@RequestParam String q) {
        List<BlogPostResponse> posts = blogPostService.searchPostResponses(q);
        return ResponseEntity.ok(posts);
    }
    
    // Search posts by specific author username
    @GetMapping("/posts/by-author")
    public ResponseEntity<List<BlogPost>> searchPostsByAuthor(@RequestParam String username) {
        List<BlogPost> posts = blogPostService.searchPostsByAuthor(username);
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
        List<BlogPostResponse> posts = blogPostService.searchPostResponses(q);
        List<User> users = userService.findAllUsers().stream()
                .filter(user -> user.getUsername().toLowerCase().contains(q.toLowerCase()) ||
                               user.getEmail().toLowerCase().contains(q.toLowerCase()))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new SearchResults(posts, users));
    }

    public static class SearchResults {
        public List<BlogPostResponse> posts;
        public List<User> users;

        public SearchResults(List<BlogPostResponse> posts, List<User> users) {
            this.posts = posts;
            this.users = users;
        }
    }
}
