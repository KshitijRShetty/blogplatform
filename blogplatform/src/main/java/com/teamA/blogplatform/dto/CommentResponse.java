package com.teamA.blogplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserSummary user;
    private Long blogPostId;
}
