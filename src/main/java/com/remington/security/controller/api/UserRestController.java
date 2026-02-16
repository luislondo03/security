package com.remington.security.controller.api;

import com.remington.security.dto.RegisterUserRequest;
import com.remington.security.dto.UserDTO;
import com.remington.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de usuarios
 * Proporciona endpoints REST para crear, consultar y gestionar usuarios
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserRestController {
    
    private final UserService userService;

    /**
     * Endpoint para registrar un nuevo usuario
     * El sistema se encarga de hashear la contraseña automáticamente con BCrypt
     * 
     * @param request Datos del usuario a registrar
     * @return Usuario creado con contraseña hasheada
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest request) {
        log.info("Solicitud de registro de usuario: {}", request.getUsername());
        
        try {
            // Validaciones básicas
            if (request.getUsername() == null || request.getUsername().isBlank()) {
                return ResponseEntity.badRequest().body(
                    Map.of("error", "El username es requerido")
                );
            }
            
            if (request.getPassword() == null || request.getPassword().isBlank()) {
                return ResponseEntity.badRequest().body(
                    Map.of("error", "La contraseña es requerida")
                );
            }
            
            if (request.getRoles() == null || request.getRoles().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    Map.of("error", "Debe especificar al menos un rol")
                );
            }
            
            // Verificar si el usuario ya existe
            if (userService.findByUsername(request.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("error", "El usuario ya existe: " + request.getUsername())
                );
            }
            
            // Registrar usuario (el servicio se encarga de hashear la contraseña)
            UserDTO createdUser = userService.registerUser(request);
            
            log.info("Usuario registrado exitosamente: {}", createdUser.getUsername());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario creado exitosamente");
            response.put("user", createdUser);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            log.error("Error al registrar usuario {}: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("error", "Error al crear usuario: " + e.getMessage())
            );
        }
    }
    
    /**
     * Endpoint para listar todos los usuarios (requiere autenticación)
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Solicitud de lista de usuarios");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Endpoint para obtener un usuario por username
     */
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        log.info("Solicitud de usuario por username: {}", username);
        
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
