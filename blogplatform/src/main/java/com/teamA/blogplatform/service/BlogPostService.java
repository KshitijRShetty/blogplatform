package com.teamA.blogplatform.service;

import com.teamA.blogplatform.dto.BlogPostRequest;
import com.teamA.blogplatform.dto.BlogPostResponse;
import com.teamA.blogplatform.dto.UserSummary;
import com.teamA.blogplatform.exception.ResourceNotFoundException;
import com.teamA.blogplatform.exception.UnauthorizedException;
import com.teamA.blogplatform.model.BlogPost;
import com.teamA.blogplatform.model.User;
import com.teamA.blogplatform.repository.BlogPostRepository;
import com.teamA.blogplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    public BlogPost createPost(BlogPostRequest request, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BlogPost post = BlogPost.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .status(BlogPost.PostStatus.DRAFT)
                .tags(request.getTags() != null ? request.getTags() : new ArrayList<>())
                .creationDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        return blogPostRepository.save(post);
    }

    public BlogPost updatePost(Long postId, BlogPostRequest request, Long userId) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new UnauthorizedException("You can only edit your own posts");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        if (request.getTags() != null) {
            post.setTags(request.getTags());
        }
        post.setLastModifiedDate(LocalDateTime.now());

        return blogPostRepository.save(post);
    }

    public void deletePost(Long postId, Long userId) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new UnauthorizedException("You can only delete your own posts");
        }

        blogPostRepository.delete(post);
    }

    public BlogPost getPostById(Long postId) {
        return blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    public List<BlogPost> getAllApprovedPosts() {
        return blogPostRepository.findByStatusOrderByCreationDateDesc(BlogPost.PostStatus.APPROVED);
    }

    // ✅ NEW METHOD: return BlogPostResponse with author info
    public List<BlogPostResponse> getAllApprovedPostResponses() {
        List<BlogPost> posts = blogPostRepository.findByStatusOrderByCreationDateDesc(BlogPost.PostStatus.APPROVED);

        return posts.stream().map(post -> {
            User author = post.getAuthor();
            UserSummary authorSummary = new UserSummary(
                author.getId(),
                author.getUsername()
            );

            return new BlogPostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getStatus().toString(),
                post.getTags() != null ? post.getTags() : new ArrayList<>(),
                post.getCreationDate(),
                post.getLastModifiedDate(),
                authorSummary
            );
        }).collect(Collectors.toList());
    }

    public List<BlogPost> getPostsByAuthor(Long authorId) {
        return blogPostRepository.findByAuthorIdOrderByCreationDateDesc(authorId);
    }

    public List<BlogPost> getPendingPosts() {
        List<BlogPost> posts = blogPostRepository.findByStatusOrderByCreationDateDesc(BlogPost.PostStatus.PENDING);
        if (posts == null) {
            posts = new ArrayList<>();
        }
        return posts;
    }

    public List<BlogPostResponse> getPendingPostResponses() {
        List<BlogPost> posts = blogPostRepository.findByStatusOrderByCreationDateDesc(BlogPost.PostStatus.PENDING);
        if (posts == null) {
            posts = new ArrayList<>();
        }

        return posts.stream().map(post -> {
            User author = post.getAuthor();
            UserSummary authorSummary = new UserSummary(
                author.getId(),
                author.getUsername()
            );

            return new BlogPostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getStatus().toString(),
                post.getTags() != null ? post.getTags() : new ArrayList<>(),
                post.getCreationDate(),
                post.getLastModifiedDate(),
                authorSummary
            );
        }).collect(Collectors.toList());
    }

    public BlogPost approvePost(Long postId, Long editorId) {
        User editor = userRepository.findById(editorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!editor.getRoles().contains(User.Role.EDITOR)) {
            throw new UnauthorizedException("Only editors can approve posts");
        }

        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        post.setStatus(BlogPost.PostStatus.APPROVED);
        post.setLastModifiedDate(LocalDateTime.now());

        return blogPostRepository.save(post);
    }

    public BlogPost rejectPost(Long postId, Long editorId) {
        User editor = userRepository.findById(editorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!editor.getRoles().contains(User.Role.EDITOR)) {
            throw new UnauthorizedException("Only editors can reject posts");
        }

        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        post.setStatus(BlogPost.PostStatus.REJECTED);
        post.setLastModifiedDate(LocalDateTime.now());

        return blogPostRepository.save(post);
    }

    public BlogPost submitForReview(Long postId, Long userId) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new UnauthorizedException("You can only submit your own posts");
        }

        post.setStatus(BlogPost.PostStatus.PENDING);
        post.setLastModifiedDate(LocalDateTime.now());

        return blogPostRepository.save(post);
    }

    public List<BlogPost> searchPosts(String query) {
        return blogPostRepository.findByTitleOrContentOrAuthorUsernameContainingIgnoreCaseAndStatus(
                query, BlogPost.PostStatus.APPROVED);
    }
    
    // Search posts by author username only
    public List<BlogPost> searchPostsByAuthor(String username) {
        return blogPostRepository.findByAuthorUsernameContainingIgnoreCaseAndStatus(
                username, BlogPost.PostStatus.APPROVED);
    }
    
    // Enhanced search that returns BlogPostResponse objects
    public List<BlogPostResponse> searchPostResponses(String query) {
        List<BlogPost> posts = blogPostRepository.findByTitleOrContentOrAuthorUsernameContainingIgnoreCaseAndStatus(
                query, BlogPost.PostStatus.APPROVED);
        
        return posts.stream().map(post -> {
            User author = post.getAuthor();
            UserSummary authorSummary = new UserSummary(
                author.getId(),
                author.getUsername()
            );
            
            return new BlogPostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getStatus().toString(),
                post.getTags() != null ? post.getTags() : new ArrayList<>(),
                post.getCreationDate(),
                post.getLastModifiedDate(),
                authorSummary
            );
        }).collect(Collectors.toList());
    }
}
