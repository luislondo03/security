package com.remington.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO para transferir información de usuario sin exponer datos sensibles.
 * Se usa para enviar información del usuario a las vistas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private String username;
    private String email;
    private String fullName;
    private Set<String> roles;
    private String primaryRole;
    private boolean isAdmin;
    
    /**
     * Obtiene el rol principal del usuario (el más alto en jerarquía)
     */
    public String getPrimaryRole() {
        if (roles == null || roles.isEmpty()) {
            return "USER";
        }
        return roles.contains("ADMIN") ? "ADMIN" : "USER";
    }
}
