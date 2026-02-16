package com.remington.security.service;

import com.remington.security.dto.AuthResponse;
import com.remington.security.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Servicio para gestionar la autenticación y autorización.
 * Centraliza la lógica de negocio relacionada con seguridad.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserService userService;
    
    /**
     * Procesa el login del usuario (llamado después de autenticación exitosa)
     */
    public AuthResponse processSuccessfulLogin(String username) {
        log.info("Procesando login exitoso para: {}", username);
        
        // Registrar el login
        userService.recordLogin(username);
        
        // Obtener información del usuario
        UserDTO userDTO = userService.getUserInfo(username);
        
        // Determinar URL de redirección según rol
        String redirectUrl = determineRedirectUrl(userDTO);
        
        return AuthResponse.builder()
                .success(true)
                .message("Login exitoso")
                .user(userDTO)
                .redirectUrl(redirectUrl)
                .build();
    }
    
    /**
     * Procesa un login fallido
     */
    public AuthResponse processFailedLogin(String username, Exception exception) {
        log.warn("Login fallido para usuario: {}. Razón: {}", username, exception.getMessage());
        
        return AuthResponse.builder()
                .success(false)
                .message("Credenciales inválidas")
                .redirectUrl("/login?error")
                .build();
    }
    
    /**
     * Verifica si el usuario actual tiene acceso a un recurso
     */
    public boolean hasAccess(String resource) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        // Lógica de autorización por recurso
        return switch (resource) {
            case "admin" -> authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            case "user" -> authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER") || 
                                     auth.getAuthority().equals("ROLE_ADMIN"));
            default -> true;
        };
    }
    
    /**
     * Obtiene el usuario autenticado actual
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        return authentication.getName();
    }
    
    /**
     * Verifica si el usuario está autenticado
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }
    
    /**
     * Determina la URL de redirección según el rol del usuario
     */
    private String determineRedirectUrl(UserDTO user) {
        if (user.isAdmin()) {
            return "/admin";
        } else if (user.getRoles().contains("USER")) {
            return "/user";
        }
        return "/public";
    }
}
