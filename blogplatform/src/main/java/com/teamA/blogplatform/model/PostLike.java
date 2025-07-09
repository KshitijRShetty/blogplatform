package com.teamA.blogplatform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "post_likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "blog_post_id"})
})
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-likes")
    private User user;

    @ManyToOne
    @JoinColumn(name = "blog_post_id", nullable = false)
    @JsonBackReference("post-likes")
    private BlogPost blogPost;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
