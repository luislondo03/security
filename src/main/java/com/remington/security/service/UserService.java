package com.remington.security.service;

import com.remington.security.dto.RegisterUserRequest;
import com.remington.security.dto.UserDTO;
import com.remington.security.model.User;
import com.remington.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de usuarios.
 * Implementa la lógica de negocio relacionada con usuarios.
 * Todas las operaciones de escritura están en transacciones.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Obtiene el usuario actualmente autenticado
     */
    public Optional<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }
        
        String username = authentication.getName();
        return findByUsername(username);
    }
    
    /**
     * Busca un usuario por username y lo convierte a DTO
     */
    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDTO);
    }
    
    /**
     * Obtiene todos los usuarios como DTOs
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    /**
     * Obtiene información del usuario con sus roles
     */
    public UserDTO getUserInfo(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }
    
    /**
     * Registra el último login del usuario
     */
    @Transactional
    public void recordLogin(String username) {
        log.info("Registrando login para usuario: {}", username);
        userRepository.updateLastLogin(username, LocalDateTime.now());
    }
    
    /**
     * Verifica si un usuario tiene un rol específico
     */
    public boolean hasRole(String username, String role) {
        return userRepository.findByUsername(username)
                .map(user -> user.hasRole(role))
                .orElse(false);
    }
    
    /**
     * Obtiene los roles del usuario autenticado desde el contexto de seguridad
     */
    public Set<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            return Set.of();
        }
        
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.replace("ROLE_", "")) // Spring Security agrega ROLE_ prefix
                .collect(Collectors.toSet());
    }
    
    /**
     * Obtiene usuarios por rol
     */
    public List<UserDTO> getUsersByRole(String role) {
        return userRepository.findByRole(role).stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    /**
     * Obtiene solo usuarios activos
     */
    public List<UserDTO> getActiveUsers() {
        return userRepository.findByEnabledTrue().stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    /**
     * Cuenta usuarios activos
     */
    public long countActiveUsers() {
        return userRepository.countByEnabledTrue();
    }
    
    /**Registra un nuevo usuario
     * Hashea la contraseña automáticamente con BCrypt
     */
    @Transactional
    public UserDTO registerUser(RegisterUserRequest request) {
        log.info("Registrando nuevo usuario: {}", request.getUsername());
        
        // Crear entidad User
        User user = new User();
        user.setUsername(request.getUsername());
        
        // IMPORTANTE: Hashear la contraseña con BCrypt
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setRoles(request.getRoles());
        user.setEnabled(request.getEnabled() != null ? request.getEnabled() : true);
        
        // Guardar en la base de datos
        User savedUser = userRepository.save(user);
        
        log.info("Usuario {} registrado exitosamente con contraseña hasheada", savedUser.getUsername());
        
        return convertToDTO(savedUser);
    }
    
    /**
     * 
     * Convierte una entidad User a UserDTO
     */
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(user.getRoles())
                .primaryRole(user.isAdmin() ? "ADMIN" : "USER")
                .isAdmin(user.isAdmin())
                .build();
    }
}
