package com.teamA.blogplatform.service;

import com.teamA.blogplatform.model.BlogPost;
import com.teamA.blogplatform.model.User;
import com.teamA.blogplatform.repository.BlogPostRepository;
import com.teamA.blogplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class DataInitializationService implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        try {
            // Check if users already exist
            if (userRepository.count() == 0) {
                initializeTestData();
            }
        } catch (Exception e) {
            // If tables don't exist yet, wait and try again
            Thread.sleep(1000);
            if (userRepository.count() == 0) {
                initializeTestData();
            }
        }
    }

    private void initializeTestData() {
        // Create test users
        User editor = User.builder()
                .username("editor")
                .email("editor@example.com")
                .password(passwordEncoder.encode("password123"))
                .roles(List.of(User.Role.EDITOR))
                .build();

        User blogger = User.builder()
                .username("blogger")
                .email("blogger@example.com")
                .password(passwordEncoder.encode("password123"))
                .roles(List.of(User.Role.BLOGGER))
                .build();

        User reader = User.builder()
                .username("reader")
                .email("reader@example.com")
                .password(passwordEncoder.encode("password123"))
                .roles(List.of(User.Role.READER))
                .build();

        // Save users
        editor = userRepository.save(editor);
        blogger = userRepository.save(blogger);
        reader = userRepository.save(reader);

        // Create test blog posts
        BlogPost post1 = BlogPost.builder()
                .title("Welcome to Our Blog Platform!")
                .content("This is the first post on our amazing blog platform. Here you can share your thoughts, follow other users, and engage with content.")
                .author(blogger)
                .status(BlogPost.PostStatus.APPROVED)
                .tags(Arrays.asList("welcome", "introduction", "blog"))
                .creationDate(LocalDateTime.now().minusDays(5))
                .lastModifiedDate(LocalDateTime.now().minusDays(5))
                .build();

        BlogPost post2 = BlogPost.builder()
                .title("Getting Started with Spring Boot")
                .content("Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications. In this post, we'll explore the basics of Spring Boot development.")
                .author(blogger)
                .status(BlogPost.PostStatus.APPROVED)
                .tags(Arrays.asList("spring-boot", "java", "development"))
                .creationDate(LocalDateTime.now().minusDays(3))
                .lastModifiedDate(LocalDateTime.now().minusDays(3))
                .build();

        BlogPost post3 = BlogPost.builder()
                .title("Database Design Best Practices")
                .content("Good database design is crucial for application performance and maintainability. Let's discuss some key principles and best practices.")
                .author(blogger)
                .status(BlogPost.PostStatus.PENDING)
                .tags(Arrays.asList("database", "postgresql", "design"))
                .creationDate(LocalDateTime.now().minusDays(1))
                .lastModifiedDate(LocalDateTime.now().minusDays(1))
                .build();

        // Save blog posts
        blogPostRepository.saveAll(Arrays.asList(post1, post2, post3));

        System.out.println("Test data initialized successfully!");
        System.out.println("Test users created:");
        System.out.println("- Editor: username='editor', password='password123'");
        System.out.println("- Blogger: username='blogger', password='password123'");
        System.out.println("- Reader: username='reader', password='password123'");
    }
}
