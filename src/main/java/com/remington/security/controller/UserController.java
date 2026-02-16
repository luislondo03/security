package com.remington.security.controller;

import com.remington.security.dto.UserDTO;
import com.remington.security.service.AuthService;
import com.remington.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para la página de usuario.
 * Accesible para usuarios con rol USER o ADMIN.
 * Sigue el patrón MVC con separación de responsabilidades.
 */
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    
    private final UserService userService;
    private final AuthService authService;

    /**
     * Muestra la página de usuario con información personalizada
     */
    @GetMapping
    public String user(Model model) {
        log.info("Acceso al área de usuario por: {}", authService.getCurrentUsername());
        
        // Obtener usuario actual desde el servicio
        UserDTO currentUser = userService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
        
        // Verificar acceso usando el servicio de autenticación
        if (!authService.hasAccess("user")) {
            log.warn("Intento de acceso no autorizado por: {}", currentUser.getUsername());
            return "redirect:/access-denied";
        }
        
        // Agregar información del usuario al modelo
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("fullName", currentUser.getFullName());
        model.addAttribute("email", currentUser.getEmail());
        model.addAttribute("roles", currentUser.getRoles());
        model.addAttribute("primaryRole", currentUser.getPrimaryRole());
        model.addAttribute("isAdmin", currentUser.isAdmin());
        
        log.debug("Página de usuario cargada para: {} (rol: {})", 
                 currentUser.getUsername(), currentUser.getPrimaryRole());
        
        return "user";
    }
}
