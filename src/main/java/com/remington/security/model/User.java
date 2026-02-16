package com.remington.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Entidad JPA que representa un usuario en el sistema.
 * Se mapea a la tabla 'users' en la base de datos.
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(nullable = false, length = 255)
    private String password;
    
    @Column(length = 100)
    private String email;
    
    @Column(name = "full_name", length = 100)
    private String fullName;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles;
    
    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    /**
     * Inicializa la fecha de creación antes de persistir
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    /**
     * Verifica si el usuario tiene un rol específico
     */
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
    
    /**
     * Verifica si el usuario es administrador
     */
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }
}
