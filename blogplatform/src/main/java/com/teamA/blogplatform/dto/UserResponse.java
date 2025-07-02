package com.teamA.blogplatform.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private List<String> blogPostTitles;
}