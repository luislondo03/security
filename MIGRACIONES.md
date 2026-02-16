# 🗄️ Migraciones de Base de Datos - Spring Security App

## 📋 Descripción

Este proyecto utiliza **scripts SQL automáticos** que se ejecutan al levantar la aplicación Spring Boot. Los archivos de migración se encuentran en `src/main/resources/` y se aplican en el siguiente orden:

1. **`schema.sql`** - Crea las tablas (DDL - Data Definition Language)
2. **`data.sql`** - Inserta datos iniciales (DML - Data Manipulation Language)

---

## 🗂️ Estructura de Base de Datos

### Tabla: `users`

Almacena la información principal de los usuarios.

| Columna | Tipo | Constraints | Descripción |
|---------|------|-------------|-------------|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `username` | VARCHAR(50) | NOT NULL, UNIQUE | Nombre de usuario |
| `password` | VARCHAR(255) | NOT NULL | Contraseña encriptada (BCrypt) |
| `email` | VARCHAR(100) | - | Email del usuario |
| `full_name` | VARCHAR(100) | - | Nombre completo |
| `enabled` | BOOLEAN | NOT NULL, DEFAULT TRUE | Usuario activo/inactivo |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT NOW | Fecha de creación |
| `last_login_at` | TIMESTAMP | - | Último login exitoso |

**Índices:**
- `idx_username` - Búsquedas por username
- `idx_email` - Búsquedas por email
- `idx_enabled` - Filtrar usuarios activos

---

### Tabla: `user_roles`

Almacena los roles asignados a cada usuario (relación muchos-a-muchos).

| Columna | Tipo | Constraints | Descripción |
|---------|------|-------------|-------------|
| `user_id` | BIGINT | PRIMARY KEY (parte 1), FK → users.id | ID del usuario |
| `role` | VARCHAR(50) | PRIMARY KEY (parte 2) | Nombre del rol |

**Constraints:**
- Clave primaria compuesta: `(user_id, role)`
- Foreign Key: `user_id` → `users.id` ON DELETE CASCADE
- Índice: `idx_role` para búsquedas por rol

---

## 👥 Usuarios Creados por Defecto

El archivo `data.sql` crea los siguientes usuarios de prueba:

### 1. **Administrador**
```
Username: admin
Password: admin123
Rol: ADMIN
Estado: Activo
Email: admin@remington.com
```

### 2. **Usuario Estándar**
```
Username: user
Password: user123
Rol: USER
Estado: Activo
Email: user@remington.com
```

### 3. **Usuario Inactivo** (para pruebas)
```
Username: inactive
Password: test123
Rol: USER
Estado: Inactivo
Email: inactive@remington.com
```

### 4. **Super Administrador** (múltiples roles)
```
Username: superadmin
Password: super123
Roles: ADMIN + USER
Estado: Activo
Email: superadmin@remington.com
```

---

## 🔐 Contraseñas Encriptadas con BCrypt

Las contraseñas en `data.sql` están **hasheadas con BCrypt**. Para generar nuevos hashes:

```java
// En Java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String hashedPassword = encoder.encode("miContraseña123");
System.out.println(hashedPassword);
```

O desde terminal con Maven:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--password=tuPassword"
```

---

## ⚙️ Configuración en application.yaml

```yaml
spring:
  sql:
    init:
      mode: always  # Ejecutar scripts SQL siempre
      schema-locations: classpath:schema.sql
      data-locations: classpath:data.sql
      continue-on-error: false

  jpa:
    hibernate:
      ddl-auto: none  # Deshabilitado: usamos schema.sql
    show-sql: true  # Ver SQL en consola
```

---

## 🚀 Ejecución de Migraciones

### Automático (al iniciar la aplicación)

Las migraciones se ejecutan **automáticamente** cada vez que inicias Spring Boot:

```bash
mvn spring-boot:run
```

**Orden de ejecución:**
1. Spring Boot inicia
2. Se conecta a H2 (base de datos en memoria)
3. Ejecuta `schema.sql` → crea tablas
4. Ejecuta `data.sql` → inserta usuarios
5. La aplicación queda lista

### Verificar en Consola H2

Puedes ver las tablas y datos en la consola H2:

1. Abre: http://localhost:8080/h2-console
2. JDBC URL: `jdbc:h2:mem:securitydb`
3. Username: `sa`
4. Password: (dejar vacío)
5. Click en "Connect"

**Consultas útiles:**
```sql
-- Ver todos los usuarios
SELECT * FROM users;

-- Ver roles por usuario
SELECT u.username, u.full_name, r.role 
FROM users u 
JOIN user_roles r ON u.id = r.user_id;

-- Ver usuarios activos con rol ADMIN
SELECT u.* FROM users u 
JOIN user_roles r ON u.id = r.user_id 
WHERE r.role = 'ADMIN' AND u.enabled = TRUE;
```

---

## 📝 Modificar Datos Iniciales

### Agregar un Nuevo Usuario

Edita `src/main/resources/data.sql`:

```sql
-- Insertar usuario
INSERT INTO users (id, username, password, email, full_name, enabled, created_at) 
VALUES (
    5,  -- ID único
    'nuevousuario',
    '$2a$10$...hash_bcrypt_aqui...',  -- Generar con BCrypt
    'nuevo@remington.com',
    'Nuevo Usuario',
    TRUE,
    CURRENT_TIMESTAMP
);

-- Asignar rol
INSERT INTO user_roles (user_id, role) VALUES (5, 'USER');
```

### Agregar un Nuevo Rol

```sql
-- Asignar rol adicional a un usuario existente
INSERT INTO user_roles (user_id, role) VALUES (1, 'MODERATOR');
```

---

## 🔄 Migración a Base de Datos Real (Producción)

Para usar PostgreSQL, MySQL u otra BD en producción:

### 1. Agregar dependencia en `pom.xml`

```xml
<!-- Para PostgreSQL -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 2. Configurar `application-prod.yaml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/securitydb
    username: postgres
    password: your_password
    driver-class-name: org.postgresql.Driver
  
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### 3. Ajustar scripts SQL si es necesario

- H2 usa `AUTO_INCREMENT`
- PostgreSQL usa `SERIAL` o `IDENTITY`
- MySQL usa `AUTO_INCREMENT`

---

## 🧪 Testing de Migraciones

### Test de Integración

```java
@SpringBootTest
@AutoConfigureTestDatabase
class DatabaseMigrationTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void shouldLoadUsersFromDatabase() {
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(4);
        
        Optional<User> admin = userRepository.findByUsername("admin");
        assertThat(admin).isPresent();
        assertThat(admin.get().getRoles()).contains("ADMIN");
    }
}
```

---

## 🛠️ Herramientas de Migración Avanzadas

Para proyectos más grandes, considera usar:

### **Flyway** (Recomendado)

```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

Scripts versionados:
```
src/main/resources/db/migration/
├── V1__Create_users_table.sql
├── V2__Insert_initial_users.sql
└── V3__Add_email_column.sql
```

### **Liquibase**

```xml
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>
```

---

## ⚠️ Consideraciones de Seguridad

### Para Producción:

1. ✅ **Eliminar usuarios de prueba** de `data.sql`
2. ✅ **Cambiar contraseñas** de usuarios predeterminados
3. ✅ **Deshabilitar consola H2**:
   ```yaml
   spring:
     h2:
       console:
         enabled: false
   ```
4. ✅ **Usar base de datos persistente** (PostgreSQL, MySQL)
5. ✅ **Implementar rotación de contraseñas**
6. ✅ **Agregar auditoría de cambios** en tablas

---

## 📊 Diagrama de Relaciones

```
┌─────────────────┐
│     users       │
├─────────────────┤     1:N
│ id (PK)         │◄────────┐
│ username        │         │
│ password        │         │
│ email           │         │
│ full_name       │         │
│ enabled         │         │
│ created_at      │         │
│ last_login_at   │         │
└─────────────────┘         │
                            │
                            │
                  ┌─────────────────┐
                  │  user_roles     │
                  ├─────────────────┤
                  │ user_id (PK,FK) │
                  │ role (PK)       │
                  └─────────────────┘
```

---

## 📚 Recursos Adicionales

- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [H2 Database Documentation](https://www.h2database.com/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [BCrypt Password Encoder](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html)

---

**Desarrollado para:** Uniremington - Ejercicio de Spring Security  
**Versión:** 1.0.0  
**Última actualización:** Febrero 2026
