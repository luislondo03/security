package com.remington.security.exception;

/**
 * Excepción personalizada para errores de autorización.
 * Se lanza cuando un usuario no tiene permisos para acceder a un recurso.
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
