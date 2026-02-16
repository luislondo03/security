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

import java.util.List;

/**
 * Controlador para la página de administración.
 * Solo accesible para usuarios con rol ADMIN.
 * Sigue el patrón MVC con separación de responsabilidades.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    
    private final UserService userService;
    private final AuthService authService;

    /**
     * Muestra el panel de administración con información del usuario y estadísticas
     */
    @GetMapping
    public String admin(Model model) {
        log.info("Acceso al panel de administración por: {}", authService.getCurrentUsername());
        
        // Obtener usuario actual desde el servicio
        UserDTO currentUser = userService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
        
        // Obtener lista de todos los usuarios para el panel admin
        List<UserDTO> allUsers = userService.getAllUsers();
        
        // Agregar datos al modelo
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("fullName", currentUser.getFullName());
        model.addAttribute("email", currentUser.getEmail());
        model.addAttribute("roles", currentUser.getRoles());
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("totalUsers", allUsers.size());
        
        log.debug("Panel de admin cargado para: {} con {} usuarios en el sistema", 
                 currentUser.getUsername(), allUsers.size());
        
        return "admin";
    }
}
