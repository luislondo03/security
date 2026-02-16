package com.remington.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para las respuestas de autenticación.
 * Encapsula el resultado de un intento de login.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private boolean success;
    private String message;
    private UserDTO user;
    private String redirectUrl;
}
