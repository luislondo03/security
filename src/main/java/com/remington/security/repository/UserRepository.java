package com.remington.security.repository;

import com.remington.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para gestionar usuarios en la base de datos.
 * Extiende JpaRepository para heredar operaciones CRUD básicas.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Busca un usuario por username
     * @param username nombre de usuario a buscar
     * @return Optional con el usuario si existe
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Busca un usuario por email
     * @param email email a buscar
     * @return Optional con el usuario si existe
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Verifica si existe un usuario con el username dado
     * @param username nombre de usuario a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsername(String username);
    
    /**
     * Busca usuarios por rol
     * @param role rol a buscar (ej: "ADMIN", "USER")
     * @return lista de usuarios con ese rol
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") String role);
    
    /**
     * Busca usuarios activos
     * @return lista de usuarios activos
     */
    List<User> findByEnabledTrue();
    
    /**
     * Busca usuarios inactivos
     * @return lista de usuarios inactivos
     */
    List<User> findByEnabledFalse();
    
    /**
     * Actualiza el último login de un usuario
     * @param username nombre del usuario
     * @param lastLoginAt fecha y hora del último login
     */
    @Modifying
    @Query("UPDATE User u SET u.lastLoginAt = :lastLoginAt WHERE u.username = :username")
    void updateLastLogin(@Param("username") String username, @Param("lastLoginAt") LocalDateTime lastLoginAt);
    
    /**
     * Busca usuarios creados después de una fecha específica
     * @param date fecha a partir de la cual buscar
     * @return lista de usuarios creados después de la fecha
     */
    List<User> findByCreatedAtAfter(LocalDateTime date);
    
    /**
     * Elimina un usuario por username
     * @param username nombre del usuario a eliminar
     */
    void deleteByUsername(String username);
    
    /**
     * Cuenta usuarios activos
     * @return número de usuarios activos
     */
    long countByEnabledTrue();
    
    /**
     * Busca usuarios por nombre completo (case insensitive)
     * @param fullName nombre completo a buscar
     * @return lista de usuarios que coincidan
     */
    List<User> findByFullNameContainingIgnoreCase(String fullName);
}
