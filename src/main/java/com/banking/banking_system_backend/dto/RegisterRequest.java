package com.banking.banking_system_backend.dto;

import com.banking.banking_system_backend.model.Role; // your Role enum
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
