# 🔐 Spring Security Application

Aplicación web de seguridad desarrollada con Spring Boot 4.0.2 que implementa autenticación basada en sesiones, autorización por roles, y gestión de usuarios con encriptación BCrypt.

## 📋 Descripción

Este proyecto es una aplicación web completa que demuestra las mejores prácticas de seguridad en Spring Boot, incluyendo:

- ✅ Autenticación basada en formularios con sesiones HTTP
- ✅ Autorización basada en roles (ADMIN/USER)
- ✅ Encriptación de contraseñas con BCrypt
- ✅ Protección CSRF para formularios web
- ✅ REST API para gestión de usuarios
- ✅ Base de datos H2 en memoria
- ✅ Templates dinámicos con Thymeleaf
- ✅ Interactividad con HTMX
- ✅ Arquitectura limpia en capas
- ✅ **Internacionalización (i18n)** - Soporte para Español, Inglés y Portugués

## 🌍 Internacionalización (i18n)

La aplicación soporta **3 idiomas**:
- 🇪🇸 **Español (es)** - Idioma por defecto
- 🇺🇸 **Inglés (en)**
- 🇧🇷 **Portugués (pt)**

### Cambio de Idioma

El usuario puede cambiar el idioma en cualquier momento:
- **Mediante botones del selector**: Disponibles en todas las páginas (esquina superior derecha)
- **Mediante parámetro URL**: Agrega `?lang=en`, `?lang=es` o `?lang=pt` a cualquier URL
- **Persistencia**: El idioma seleccionado se guarda en una cookie y persiste entre sesiones

### Archivos de Mensajes

Los textos están centralizados en archivos `.properties`:
- `messages.properties` - Inglés (idioma por defecto del framework)
- `messages_es.properties` - Español
- `messages_pt.properties` - Portugués

Todos los textos de la aplicación (títulos, etiquetas, mensajes) están internacionalizados.

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 21** - Lenguaje de programación
- **Spring Boot 4.0.2** - Framework principal
- **Spring Security 6** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **Spring Web MVC** - Controladores y REST API

### Frontend
- **Thymeleaf** - Motor de plantillas server-side
- **Thymeleaf Spring Security 6** - Integración con seguridad
- **HTMX 5.0.0** - Interactividad sin JavaScript
- **Bootstrap 5** - Framework CSS

### Base de Datos
- **H2 Database** - Base de datos en memoria

### Herramientas de Desarrollo
- **Lombok** - Reducción de código boilerplate
- **Spring Boot DevTools** - Hot reload
- **Maven** - Gestión de dependencias

## 🏗️ Arquitectura

El proyecto sigue una **arquitectura en capas (Layered Architecture)** con separación clara de responsabilidades:

```
┌─────────────────────────────────────────┐
│  PRESENTATION LAYER (Controllers)       │ ← Controladores MVC y REST
├─────────────────────────────────────────┤
│  BUSINESS LAYER (Services)              │ ← Lógica de negocio
├─────────────────────────────────────────┤
│  PERSISTENCE LAYER (Repositories)       │ ← Acceso a datos
├─────────────────────────────────────────┤
│  DATA LAYER (H2 Database)               │ ← Almacenamiento
└─────────────────────────────────────────┘
```

### Patrones de Diseño Implementados

- **MVC (Model-View-Controller)** - Separación de presentación, lógica y datos
- **Repository Pattern** - Abstracción del acceso a datos
- **Service Layer Pattern** - Encapsulación de lógica de negocio
- **DTO Pattern** - Transferencia de datos entre capas
- **Dependency Injection** - Inversión de control con Spring
- **Builder Pattern** - Configuración fluida (Spring Security)
- **Filter Pattern** - Interceptores de peticiones HTTP
- **Facade Pattern** - Simplificación de APIs complejas

## 📁 Estructura del Proyecto

```
src/main/java/com/remington/security/
├── config/                          # Configuraciones
│   ├── SecurityConfig.java         # Configuración de Spring Security
│   ├── SessionDebugFilter.java     # Filtro de debugging de sesiones
│   └── InternationalizationConfig.java  # Configuración i18n
│
├── controller/                      # Controladores MVC
│   ├── AuthController.java         # Login/Logout
│   ├── PublicController.java       # Páginas públicas
│   ├── AdminController.java        # Panel de administración (ADMIN)
│   ├── UserController.java         # Área de usuario (USER/ADMIN)
│   └── api/
│       └── UserRestController.java # REST API para usuarios
│
├── service/                         # Lógica de negocio
│   ├── UserService.java            # Gestión de usuarios
│   └── AuthService.java            # Servicios de autenticación
│
├── repository/                      # Acceso a datos
│   └── UserRepository.java         # Repositorio JPA de usuarios
│
├── model/                           # Entidades JPA
│   ├── User.java                   # Entidad de usuario
│   └── Role.java                   # Enum de roles
│
├── dto/                             # Data Transfer Objects
│   ├── UserDTO.java                # DTO de usuario (sin contraseña)
│   └── RegisterUserRequest.java   # DTO para registro
│
├── exception/                       # Excepciones personalizadas
│   └── ...
│
└── SecurityApplication.java         # Clase principal

src/main/resources/
├── templates/                       # Plantillas Thymeleaf
│   ├── login.html                  # Formulario de login
│   ├── public.html                 # Página pública
│   ├── admin.html                  # Dashboard de administración
│   ├── user.html                   # Área de usuario
│   └── access-denied.html          # Error 403
│
├── static/                          # Recursos estáticos
│   └── js/                         # JavaScript (si aplica)
│messages.properties              # Mensajes en inglés (default)
├── messages_es.properties           # Mensajes en español
├── messages_pt.properties           # Mensajes en portugués
├── 
├── application.yaml                 # Configuración de la aplicación
└── schema.sql                       # Esquema de base de datos
```

## 🚀 Requisitos Previos

- **Java 21** o superior
- **Maven 3.8+**
- **IDE** (IntelliJ IDEA, Eclipse, VS Code)
- **Git** (opcional)

## 📦 Instalación

### 1. Clonar el repositorio (o descargar)

```bash
git clone <repository-url>
cd security
```

### 2. Compilar el proyecto

```bash
mvn clean install
```

### 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

## 🔧 Configuración

### Base de Datos H2

La aplicación usa H2 en memoria. Puedes acceder a la consola web:

- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:securitydb`
- **Username**: `sa`
- **Password**: *(dejar en blanco)*

### application.yaml

```yaml
server:
  port: 8080
  servlet:
    session:
      timeout: 30m
      cookie:
        name: JSESSIONID
        http-only: true
        secure: false  # Cambiar a true en producción con HTTPS
        same-site: lax

spring:
  datasource:
    url: jdbc:h2:mem:securitydb;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
    username: sa
    password:
  
  h2:
    console:
      enabled: true
      path: /h2-console
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true

logging:
  level:
    org.springframework.security: DEBUG
    com.remington.security: DEBUG
```

## 📚 Uso

### Rutas Públicas (Sin Autenticación)

| Ruta | Método | Descripción |
|------|--------|-------------|
| `/` | GET | Redirecciona a `/public` |
| `/public` | GET | Página pública de bienvenida |
| `/login` | GET | Formulario de login |
| `/access-denied` | GET | Página de error 403 |
| `/h2-console` | GET | Consola de base de datos H2 |

### Rutas Protegidas (Requieren Autenticación)

| Ruta | Método | Roles | Descripción |
|------|--------|-------|-------------|
| `/admin` | GET | ADMIN | Dashboard de administración |
| `/user` | GET | USER, ADMIN | Área personal del usuario |
| `/logout` | POST | Todos | Cerrar sesión |

### REST API

#### Registrar Usuario (Público)

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "email": "admin@example.com",
    "fullName": "Administrator",
    "roles": ["ADMIN"],
    "enabled": true
  }'
```

**Respuesta:**
```json
{
  "message": "Usuario creado exitosamente",
  "user": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "fullName": "Administrator",
    "roles": ["ADMIN"],
    "enabled": true
  }
}
```

#### Listar Usuarios (Requiere Autenticación)

```bash
curl http://localhost:8080/api/users \
  -H "Cookie: JSESSIONID=<session-id>"
```

#### Buscar Usuario por Username (Requiere Autenticación)

```bash
curl http://localhost:8080/api/users/admin \
  -H "Cookie: JSESSIONID=<session-id>"
```

### Crear Usuarios de Ejemplo

**Usuario Administrador:**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "email": "admin@example.com",
    "fullName": "Administrator",
    "roles": ["ADMIN"],
    "enabled": true
  }'
```

**Usuario Regular:**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "password": "user123",
    "email": "user@example.com",
    "fullName": "Regular User",
    "roles": ["USER"],
    "enabled": true
  }'
```

## 🔒 Características de Seguridad

### Autenticación

- **Form-based Authentication**: Login mediante formulario HTML
- **Session-based**: Gestión de sesiones con cookies HttpOnly
- **BCrypt Password Encoding**: Contraseñas encriptadas con salt aleatorio
- **UserDetailsService**: Carga de usuarios desde base de datos H2

### Autorización

- **Role-based Access Control**: Permisos basados en roles (ADMIN/USER)
- **Method-level Security**: Validación en capa de servicio
- **URL-based Security**: Protección de rutas por patrón

### Protecciones Implementadas

| Protección | Descripción | Estado |
|------------|-------------|--------|
| **CSRF Protection** | Protección contra Cross-Site Request Forgery | ✅ Habilitado (formularios web) |
| **Session Fixation Protection** | Regeneración de sesión en login | ✅ Habilitado (migrateSession) |
| **XSS Protection** | Escape automático de HTML con Thymeleaf | ✅ Habilitado |
| **Clickjacking Protection** | X-Frame-Options: SAMEORIGIN | ✅ Habilitado |
| **HttpOnly Cookies** | Cookies inaccesibles desde JavaScript | ✅ Habilitado |
| **SameSite Cookies** | Protección contra CSRF adicional | ✅ Habilitado (Lax) |
| **Password Hashing** | BCrypt con salt aleatorio | ✅ Habilitado |
| **Session Timeout** | Expiración automática de sesiones | ✅ 30 minutos |
| **Maximum Sessions** | Límite de sesiones concurrentes | ✅ 1 sesión por usuario |

### Configuración de Sesiones

```java
.sessionManagement(session -> session
    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
    .sessionFixation().migrateSession()
    .maximumSessions(1)
)
```

- **SessionCreationPolicy.IF_REQUIRED**: Crea sesión solo cuando es necesario
- **migrateSession()**: Regenera session ID después de login (previene session fixation)
- **maximumSessions(1)**: Un usuario solo puede tener 1 sesión activa

### Flujo de Autenticación

```
1. Usuario accede a /login
2. Usuario ingresa credenciales
3. Spring Security intercepta POST /login
4. UserDetailsService carga usuario desde H2
5. PasswordEncoder valida contraseña con BCrypt
6. Si es válido:
   - Crea objeto Authentication
   - Regenera Session ID (session fixation protection)
   - Guarda Authentication en SecurityContext
   - Crea cookie JSESSIONID
   - Redirige a /public
7. Usuario autenticado puede acceder a rutas protegidas
```

## 💾 Base de Datos

### Esquema

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    full_name VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE user_roles (
    user_id BIGINT,
    role VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Relaciones

- **User** → **Roles**: Relación 1:N (un usuario puede tener múltiples roles)
- Roles disponibles: `ADMIN`, `USER`

### Datos de Ejemplo

Los usuarios se crean mediante la REST API `/api/users/register` (ver sección de Uso).

## 🧪 Testing

### Ejecutar Tests

```bash
mvn test
```

### Ejecutar con Cobertura

```bash
mvn clean test jacoco:report
```

Los reportes de cobertura se generan en: `target/site/jacoco/index.html`

## 📊 Endpoints Completos

### Aplicación Web (MVC)

| Endpoint | Método | Auth | Roles | Descripción |
|----------|--------|------|-------|-------------|
| `/` | GET | No | - | Redirecciona a `/public` |
| `/login` | GET | No | - | Formulario de login |
| `/login` | POST | No | - | Procesa login (Spring Security) |
| `/logout` | POST | Sí | Todos | Cierra sesión |
| `/public` | GET | No | - | Página pública |
| `/access-denied` | GET | No | - | Error 403 personalizado |
| `/admin` | GET | Sí | ADMIN | Dashboard de administración |
| `/user` | GET | Sí | USER, ADMIN | Área de usuario |

### REST API

| Endpoint | Método | Auth | Roles | Descripción |
|----------|--------|------|-------|-------------|
| `/api/users/register` | POST | No | - | Registrar nuevo usuario |
| `/api/users` | GET | Sí | Todos | Listar todos los usuarios |
| `/api/users/{username}` | GET | Sí | Todos | Buscar usuario por username |

### Consola de Administración

| Endpoint | Método | Auth | Descripción |
|----------|--------|------|-------------|
| `/h2-console` | GET | No | Acceso a consola H2 Database |

## 🐛 Debugging

### Logs de Seguridad

El proyecto incluye un filtro de debugging que loguea todas las cookies y sesiones:

```java
@Component
public class SessionDebugFilter implements Filter {
    // Loguea cookies, session ID, flags de seguridad
}
```

**Logs habilitados:**
- `org.springframework.security: DEBUG`
- `com.remington.security: DEBUG`

### Ver Sesiones Activas

Accede a `/h2-console` y ejecuta:

```sql
-- Ver usuarios registrados
SELECT * FROM users;

-- Ver roles asignados
SELECT u.username, ur.role 
FROM users u 
JOIN user_roles ur ON u.id = ur.user_id;
```

## 🚀 Despliegue

### Producción

#### 1. Cambiar configuración de seguridad

```yaml
server:
  servlet:
    session:
      cookie:
        secure: true  # HTTPS obligatorio
        same-site: strict  # Más restrictivo
```

#### 2. Cambiar base de datos

Reemplazar H2 por PostgreSQL, MySQL, etc.:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/securitydb
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate  # No auto-crear esquema en producción
```

#### 3. Compilar JAR

```bash
mvn clean package -DskipTests
```

#### 4. Ejecutar

```bash
java -jar target/security-0.0.1-SNAPSHOT.jar
```

### Docker (Opcional)

```dockerfile
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/security-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
docker build -t security-app .
docker run -p 8080:8080 security-app
```

## 📄 Licencia

Este proyecto fue desarrollado con fines educativos para UniRemington.

## 👥 Autor

**Remington Security Team**
- Universidad: UniRemington
- Curso: Seguridad en Spring Boot
- Año: 2026

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📞 Soporte

Para preguntas o issues:
- Crea un **Issue** en el repositorio
- Contacta al equipo de desarrollo

## 🎓 Recursos Adicionales

- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/index.html)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [HTMX Documentation](https://htmx.org/docs/)
- [BCrypt Explained](https://en.wikipedia.org/wiki/Bcrypt)

---

⭐ Si este proyecto te fue útil, considera darle una estrella en GitHub!
