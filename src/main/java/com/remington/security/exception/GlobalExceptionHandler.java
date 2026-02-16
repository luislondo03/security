package com.remington.security.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Manejador global de excepciones para la aplicación.
 * Captura excepciones y proporciona respuestas consistentes.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    private static final String ERROR_VIEW = "error";
    private static final String ACCESS_DENIED_VIEW = "access-denied.html";
    
    /**
     * Maneja excepciones de recursos no encontrados (404)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(
            ResourceNotFoundException ex, 
            HttpServletRequest request) {
        
        log.error("Recurso no encontrado: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        ModelAndView mav = new ModelAndView(ERROR_VIEW);
        mav.addObject("status", HttpStatus.NOT_FOUND.value());
        mav.addObject("error", "Recurso no encontrado");
        mav.addObject("message", ex.getMessage());
        mav.addObject("path", request.getRequestURI());
        mav.setStatus(HttpStatus.NOT_FOUND);
        
        return mav;
    }
    
    /**
     * Maneja excepciones de acceso denegado (403)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(
            AccessDeniedException ex, 
            HttpServletRequest request) {
        
        log.warn("Acceso denegado: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        ModelAndView mav = new ModelAndView(ACCESS_DENIED_VIEW);
        mav.setStatus(HttpStatus.FORBIDDEN);
        
        return mav;
    }
    
    /**
     * Maneja excepciones de autenticación personalizada
     */
    @ExceptionHandler(AuthenticationException.class)
    public ModelAndView handleAuthenticationException(
            AuthenticationException ex, 
            HttpServletRequest request) {
        
        log.error("Error de autenticación: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        return new ModelAndView("redirect:/login?error");
    }
    
    /**
     * Maneja excepciones de autorización personalizada
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView handleUnauthorizedException(
            UnauthorizedException ex, 
            HttpServletRequest request) {
        
        log.warn("No autorizado: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        ModelAndView mav = new ModelAndView(ACCESS_DENIED_VIEW);
        mav.setStatus(HttpStatus.FORBIDDEN);
        
        return mav;
    }
    
    /**
     * Maneja cualquier otra excepción no contemplada (500)
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(
            Exception ex, 
            HttpServletRequest request) {
        
        log.error("Error interno del servidor: {} - Path: {}", ex.getMessage(), request.getRequestURI(), ex);
        
        ModelAndView mav = new ModelAndView(ERROR_VIEW);
        mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        mav.addObject("error", "Error Interno del Servidor");
        mav.addObject("message", "Ha ocurrido un error inesperado. Por favor, intenta nuevamente.");
        mav.addObject("path", request.getRequestURI());
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        
        return mav;
    }
    
    /**
     * Maneja excepciones para peticiones API (JSON)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, 
            HttpServletRequest request) {
        
        log.error("Argumento ilegal: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(error);
    }
}
