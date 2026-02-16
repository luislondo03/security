# Arquitectura MVC - Spring Security App

## 📐 Estructura del Proyecto

Este proyecto implementa una arquitectura **MVC (Model-View-Controller)** robusta con separación clara de responsabilidades siguiendo las mejores prácticas de Spring Boot.

## 🏗️ Capas de la Arquitectura

### 1. **Capa de Presentación (View Layer)**
**Ubicación:** `src/main/resources/static/`

Archivos HTML estáticos con Bootstrap 5 y HTMX:
- `login.html` - Formulario de autenticación
- `admin.html` - Panel de administración
- `user.html` - Área de usuario
- `public.html` - Página pública de bienvenida
- `access-denied.html` - Página de error 403

**Tecnologías:** HTML5, Bootstrap 5, HTMX, JavaScript

---

### 2. **Capa de Controladores (Controller Layer)**
**Ubicación:** `src/main/java/com/remington/security/controller/`

Maneja las peticiones HTTP y coordina entre las vistas y los servicios:

#### `AuthController`
- **Ruta:** `/login`
- **Responsabilidad:** Gestión de login y CSRF tokens
- **Servicios usados:** `AuthService`

#### `AdminController`
- **Ruta:** `/admin`
- **Responsabilidad:** Panel de administración
- **Servicios usados:** `UserService`, `AuthService`
- **Seguridad:** Solo rol ADMIN

#### `UserController`
- **Ruta:** `/user`
- **Responsabilidad:** Área de usuario
- **Servicios usados:** `UserService`, `AuthService`
- **Seguridad:** Roles USER y ADMIN

#### `PublicController`
- **Ruta:** `/`, `/public`
- **Responsabilidad:** Páginas públicas sin autenticación
- **Servicios usados:** `UserService`, `AuthService`

---

### 3. **Capa de Servicios (Service Layer)**
**Ubicación:** `src/main/java/com/remington/security/service/`

Contiene la lógica de negocio:

#### `AuthService`
**Responsabilidades:**
- Procesamiento de login exitoso/fallido
- Verificación de acceso a recursos
- Determinación de URLs de redirección por rol
- Gestión del usuario autenticado

**Métodos principales:**
```java
processSuccessfulLogin(String username)
hasAccess(String resource)
getCurrentUsername()
isAuthenticated()
```

#### `UserService`
**Responsabilidades:**
- Gestión de usuarios
- Conversión de entidades a DTOs
- Obtención del usuario actual
- Registro de logins

**Métodos principales:**
```java
getCurrentUser()
findByUsername(String username)
getAllUsers()
getUserInfo(String username)
recordLogin(String username)
```

---

### 4. **Capa de Repositorios (Repository Layer)**
**Ubicación:** `src/main/java/com/remington/security/repository/`

#### `UserRepository`
**Tipo:** Repositorio en memoria (ConcurrentHashMap)
**Responsabilidad:** Persistencia y recuperación de usuarios

**Métodos principales:**
```java
findByUsername(String username): Optional<User>
findAll(): List<User>
save(User user): User
updateLastLogin(String username)
```

**Nota:** En producción, esto sería reemplazado por Spring Data JPA con base de datos real.

---

### 5. **Capa de Modelo (Model/DTO Layer)**
**Ubicación:** `src/main/java/com/remington/security/`

#### Entidades (`model/`)
- **`User`**: Entidad de dominio que representa un usuario
  - Atributos: username, password, email, fullName, roles, enabled, createdAt, lastLoginAt
  - Usa Lombok para reducir boilerplate

#### DTOs (`dto/`)
- **`UserDTO`**: Transferencia de datos de usuario sin información sensible
- **`AuthResponse`**: Respuesta de autenticación con usuario y redirección
- **`LoginRequest`**: Encapsula credenciales de login
- **`ErrorResponse`**: Estandariza respuestas de error

---

### 6. **Manejo de Excepciones**
**Ubicación:** `src/main/java/com/remington/security/exception/`

#### `GlobalExceptionHandler`
**Anotación:** `@ControllerAdvice`

Captura y maneja excepciones globalmente:

| Excepción | Código HTTP | Vista |
|-----------|-------------|-------|
| `ResourceNotFoundException` | 404 | error.html |
| `AccessDeniedException` | 403 | access-denied.html |
| `AuthenticationException` | Redirect | login?error |
| `UnauthorizedException` | 403 | access-denied.html |
| `Exception` (genérica) | 500 | error.html |

**Excepciones personalizadas:**
- `ResourceNotFoundException`
- `AuthenticationException`
- `UnauthorizedException`

---

### 7. **Configuración y Seguridad**
**Ubicación:** `src/main/java/com/remington/security/config/`

#### `SecurityConfig`
**Configuración de Spring Security:**

- **Password Encoder:** BCrypt
- **Usuarios en memoria:**
  - `admin/admin123` → rol ADMIN
  - `user/user123` → rol USER
- **Autorización:**
  - `/public/**` → Público
  - `/admin/**` → ADMIN
  - `/user/**` → USER o ADMIN
- **Session Management:**
  - Session creation: IF_REQUIRED
  - Session fixation: migrate
  - Max sessions: 1 por usuario
- **Cookies seguras:**
  - HttpOnly: true
  - Secure: configurable
  - SameSite: Lax

---

## 🔄 Flujo de Datos

```
1. Usuario → HTTP Request → Controller
2. Controller → delega lógica → Service
3. Service → accede datos → Repository
4. Repository → retorna → Entity
5. Service → convierte → DTO
6. Controller → agrega a Model → DTO
7. View Engine → renderiza → HTML
8. HTML ← HTTP Response ← Usuario
```

---

## 🎯 Principios Aplicados

### SOLID
- **Single Responsibility:** Cada clase tiene una única responsabilidad
- **Open/Closed:** Extensible mediante interfaces
- **Dependency Inversion:** Controllers dependen de abstracciones (Services)

### Separation of Concerns
- Lógica de presentación → Views
- Lógica de negocio → Services
- Lógica de acceso a datos → Repositories
- Configuración → Config classes

### Dependency Injection
- Uso de `@RequiredArgsConstructor` (Lombok) para inyección de dependencias
- Todos los componentes se inyectan a través del constructor

---

## 📊 Componentes por Capa

| Capa | Componentes | Dependencias |
|------|-------------|--------------|
| **View** | 5 archivos HTML | Bootstrap, HTMX |
| **Controller** | 4 controladores + 1 handler | Services |
| **Service** | 2 servicios | Repositories |
| **Repository** | 1 repositorio | - |
| **Model** | 1 entidad + 4 DTOs | Lombok |
| **Config** | 1 configuración | Spring Security |
| **Exception** | 3 excepciones + 1 handler | - |

---

## 🔐 Seguridad Implementada

1. **Form-based Authentication** con Spring Security
2. **Session Management** con cookies seguras (JSESSIONID)
3. **CSRF Protection** habilitada en todos los formularios
4. **Password Encoding** con BCrypt
5. **Role-based Authorization** en rutas
6. **Session Fixation Protection** mediante migración
7. **HttpOnly Cookies** para prevenir XSS

---

## 🚀 Ventajas de esta Arquitectura

✅ **Mantenibilidad:** Código organizado y fácil de mantener  
✅ **Testabilidad:** Cada capa puede testearse independientemente  
✅ **Escalabilidad:** Fácil agregar nuevas funcionalidades  
✅ **Reusabilidad:** Servicios y repositorios reutilizables  
✅ **Claridad:** Responsabilidades bien definidas  
✅ **Profesionalismo:** Sigue estándares de la industria  

---

## 📦 Estructura de Paquetes

```
com.remington.security
├── config/              # Configuración (Security, etc.)
├── controller/          # Controladores MVC
├── service/             # Lógica de negocio
├── repository/          # Acceso a datos
├── model/               # Entidades de dominio
├── dto/                 # Data Transfer Objects
└── exception/           # Manejo de excepciones
```

---

## 🔄 Próximos Pasos para Producción

1. Reemplazar `UserRepository` por Spring Data JPA
2. Integrar base de datos real (PostgreSQL, MySQL)
3. Implementar tests unitarios e integración
4. Agregar cache con Redis para sesiones
5. Implementar JWT para APIs REST
6. Configurar HTTPS y cookies secure=true
7. Agregar auditoría y logging avanzado
8. Implementar rate limiting y protección contra brute force

---

**Desarrollado con:** Spring Boot 4.0.2, Java 21, Spring Security, HTMX, Bootstrap 5
