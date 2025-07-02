package com.teamA.blogplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull(message = "Blog post ID is required")
    private Long blogPostId;
    
    @NotBlank(message = "Comment content is required")
    private String content;
}
