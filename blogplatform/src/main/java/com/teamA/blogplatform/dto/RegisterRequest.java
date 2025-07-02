package com.teamA.blogplatform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Collections;
import java.util.List;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    // Support both "roles" (array) and "role" (single)
    private List<String> roles;
    private String role;

    public List<String> getEffectiveRoles() {
        if (roles != null && !roles.isEmpty()) {
            return roles;
        } else if (role != null && !role.isBlank()) {
            return Collections.singletonList(role);
        } else {
            return Collections.singletonList("READER");
        }
    }
}
