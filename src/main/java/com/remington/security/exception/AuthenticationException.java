package com.remington.security.exception;

/**
 * Excepción personalizada para errores de autenticación.
 * Se lanza cuando falla la autenticación del usuario.
 */
public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
