package com.teamA.blogplatform.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BlogPostResponse {
    private Long id;
    private String title;
    private String content;
    private String status;
    private List<String> tags; // if you have tags as list of strings
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private UserSummary author;

    // Constructors
    public BlogPostResponse() {}

    public BlogPostResponse(Long id, String title, String content, String status,
                            List<String> tags, LocalDateTime creationDate,
                            LocalDateTime lastModifiedDate, UserSummary author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.tags = tags;
        this.creationDate = creationDate;
        this.lastModifiedDate = lastModifiedDate;
        this.author = author;
    }

    // Getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public LocalDateTime getLastModifiedDate() { return lastModifiedDate; }
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }

    public UserSummary getAuthor() { return author; }
    public void setAuthor(UserSummary author) { this.author = author; }
}
