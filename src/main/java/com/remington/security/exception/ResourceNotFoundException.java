package com.remington.security.exception;

/**
 * Excepción personalizada para recursos no encontrados.
 * Se lanza cuando no se encuentra un recurso solicitado.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
