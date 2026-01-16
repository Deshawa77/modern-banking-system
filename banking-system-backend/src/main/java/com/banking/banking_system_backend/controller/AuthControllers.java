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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthControllers {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    // ================= LOGIN =================
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtils.generateJwtToken(
                user.getUsername(),
                user.getRole().name()
        );

        return new LoginResponse(token, user.getRole().name());
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(request.getPassword()));

        // ✅ Assign role from request, default to USER if null
        Role role = request.getRole() != null ? request.getRole() : Role.USER;
        user.setRole(role);

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully with role: " + role.name());
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
        private final String role;
    }
}
