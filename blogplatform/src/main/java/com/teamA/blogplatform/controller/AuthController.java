package com.teamA.blogplatform.controller;

import com.teamA.blogplatform.config.JwtUtil;
import com.teamA.blogplatform.dto.AuthRequest;
import com.teamA.blogplatform.dto.AuthResponse;
import com.teamA.blogplatform.dto.RegisterRequest;
import com.teamA.blogplatform.model.User;
import com.teamA.blogplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // Check if user already exists
            if (userService.existsByUsername(request.getUsername())) {
                return ResponseEntity.badRequest()
                        .body("Error: Username is already taken!");
            }

            if (userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                        .body("Error: Email is already in use!");
            }

            // Create new user
            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .roles(List.of(User.Role.READER)) // Default role
                    .build();

            // Add additional roles if specified
            if (request.getRoles() != null && !request.getRoles().isEmpty()) {
                user.setRoles(request.getRoles());
            }

            User savedUser = userService.createUser(user);

            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            User user = userService.findByUsername(request.getUsername());

            return ResponseEntity.ok(new AuthResponse(jwt, user.getId(), user.getUsername(), user.getEmail(), user.getRoles()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error: Invalid username or password!");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtUtil.extractUsername(token);
                UserDetails userDetails = userService.loadUserByUsername(username);
                
                if (jwtUtil.validateToken(token, userDetails)) {
                    User user = userService.findByUsername(username);
                    return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail(), user.getRoles()));
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed");
        }
    }
}
