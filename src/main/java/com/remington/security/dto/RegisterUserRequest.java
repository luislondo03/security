package com.remington.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO para la solicitud de registro de nuevo usuario
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {
    
    private String username;
    private String password;
    private String email;
    private String fullName;
    private Set<String> roles;
    private Boolean enabled;
}
