package com.banking.banking_system_backend.controller;

import com.banking.banking_system_backend.dto.RegisterRequest;
import com.banking.banking_system_backend.model.Role;
import com.banking.banking_system_backend.model.User;
import com.banking.banking_system_backend.repository.UserRepository;
import com.banking.banking_system_backend.security.JwtUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthControllers {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ================= LOGIN =================
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        System.out.println("🔥 Login request received: " + request.getUsername());

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            System.out.println("✔ Authentication success");
        } catch (Exception e) {
            System.out.println("❌ Authentication failed: " + e.getClass().getName());
            e.printStackTrace();
            throw e;
        }

        // ✅ Fetch user from DB to get the role
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Generate JWT with username + role
        String token = jwtUtils.generateJwtToken(
                user.getUsername(),
                user.getRole().name()
        );

        return new LoginResponse(token);
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Default role is USER (only admins can assign ADMIN role later)
        user.setRole(Role.USER);

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    // ================= DTO CLASSES =================
    @Data
    static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    static class LoginResponse {
        private final String token;
    }
}
