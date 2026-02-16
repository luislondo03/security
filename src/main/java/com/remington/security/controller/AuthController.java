package com.remington.security.controller;

import com.remington.security.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para autenticación (login).
 * Maneja la visualización del formulario de login y post-login.
 * Sigue el patrón MVC con separación de responsabilidades.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final AuthService authService;

    /**
     * Muestra el formulario de login personalizado.
     * Si hay un error de autenticación, se muestra un mensaje.
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        log.info("Acceso a página de login");
        
        // Si el usuario ya está autenticado, redirigir
        if (authService.isAuthenticated()) {
            String username = authService.getCurrentUsername();
            log.info("Usuario {} ya autenticado, redirigiendo", username);
            return "redirect:/public";
        }
        
        // Obtener el token CSRF para incluirlo en el formulario
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            model.addAttribute("_csrf", csrfToken);
            model.addAttribute("csrfToken", csrfToken.getToken());
            model.addAttribute("csrfHeader", csrfToken.getHeaderName());
            log.debug("CSRF token agregado al modelo de login");
        }
        
        // Verificar si hubo un error de login
        String error = request.getParameter("error");
        if (error != null) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "Usuario o contraseña incorrectos");
            log.warn("Error en intento de login");
        }
        
        // Verificar si el usuario cerró sesión
        String logout = request.getParameter("logout");
        if (logout != null) {
            model.addAttribute("logout", true);
            model.addAttribute("logoutMessage", "Has cerrado sesión exitosamente");
            log.info("Sesión cerrada exitosamente");
        }
        
        return "login";
    }
}
