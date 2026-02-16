-- =====================================================
-- Script de creación de esquema de base de datos
-- Sistema de Seguridad - Uniremington
-- =====================================================

-- Eliminar tablas si existen (para desarrollo)
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;

-- =====================================================
-- Tabla: users
-- Almacena la información de usuarios del sistema
-- =====================================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    full_name VARCHAR(100),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP
);

-- Índices para mejorar rendimiento de búsquedas
CREATE INDEX idx_username ON users(username);
CREATE INDEX idx_email ON users(email);
CREATE INDEX idx_enabled ON users(enabled);

-- =====================================================
-- Tabla: user_roles
-- Almacena los roles asignados a cada usuario
-- Relación muchos-a-muchos entre users y roles
-- =====================================================
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    
    -- Clave primaria compuesta
    PRIMARY KEY (user_id, role),
    
    -- Clave foránea hacia users
    CONSTRAINT fk_user_roles_user 
        FOREIGN KEY (user_id) 
        REFERENCES users(id) 
        ON DELETE CASCADE
);

-- Índice para consultas por rol
CREATE INDEX idx_role ON user_roles(role);

-- =====================================================
-- Fin del script de schema
-- =====================================================
