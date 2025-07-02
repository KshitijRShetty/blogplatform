package com.teamA.blogplatform.dto;

import com.teamA.blogplatform.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String username;
    private String email;
    private List<User.Role> roles;
}
