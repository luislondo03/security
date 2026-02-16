package com.remington.security.controller;

import com.remington.security.service.AuthService;
import com.remington.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para páginas públicas accesibles sin autenticación.
 * Sigue el patrón MVC con separación de responsabilidades.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class PublicController {
    
    private final UserService userService;
    private final AuthService authService;

    /**
     * Página principal - redirecciona a /public
     */
    @GetMapping("/")
    public String home() {
        log.debug("Acceso a home, redirigiendo a /public");
        return "redirect:/public";
    }

    /**
     * Página pública de inicio
     * Muestra información diferente si el usuario está autenticado
     */
    @GetMapping("/public")
    public String publicPage(Model model) {
        log.info("Acceso a página pública");
        
        // Verificar si hay usuario autenticado
        boolean isAuthenticated = authService.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuthenticated);
        
        if (isAuthenticated) {
            // Si está autenticado, agregar info del usuario
            userService.getCurrentUser().ifPresent(user -> {
                model.addAttribute("currentUser", user);
                model.addAttribute("username", user.getUsername());
                log.debug("Usuario autenticado en página pública: {}", user.getUsername());
            });
        }
        
        return "public";
    }
    
    /**
     * Página de acceso denegado (error 403)
     */
    @GetMapping("/access-denied")
    public String accessDenied() {
        log.warn("Acceso denegado - error 403");
        return "access-denied";
    }
}
