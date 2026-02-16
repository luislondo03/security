package com.remington.security.config;

import com.remington.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;

/**
 * Configuración de Spring Security para autenticación basada en cookies seguras
 * con usuarios en base de datos y protección de rutas por roles.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final UserRepository userRepository;

    /**
     * Define el encoder de contraseñas usando BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura UserDetailsService que carga usuarios desde la base de datos
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            com.remington.security.model.User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
            
            // Verificar si el usuario está habilitado
            if (!user.isEnabled()) {
                throw new UsernameNotFoundException("Usuario deshabilitado: " + username);
            }
            
            // Construir UserDetails desde la entidad User
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .disabled(!user.isEnabled())
                    .build();
        };
    }

    /**
     * Configura la cadena de filtros de seguridad con:
     * - Autorización por rutas según roles
     * - Login personalizado con form-based authentication
     * - Logout desde backend
     * - Protección CSRF habilitada
     * - Cookies de sesión seguras
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configuración de autorización
            .authorizeHttpRequests(authorize -> authorize
                // Rutas públicas - accesibles sin autenticación
                .requestMatchers("/", "/public/**", "/login", "/access-denied", "/css/**", "/js/**", "/h2-console/**", "/favicon.ico", "/error").permitAll()
                
                // API REST para registro de usuarios - sin autenticación
                .requestMatchers("/api/users/register").permitAll()
                
                // Ruta admin - solo para rol ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Ruta user - para roles USER y ADMIN
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                
                // Todas las demás rutas requieren autenticación
                .anyRequest().authenticated()
            )
            
            // Configuración de login personalizado
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/public", true)
                .permitAll()
            )
            
            // Configuración de manejo de errores de acceso
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/access-denied")
            )
            
            // Configuración de logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/public?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll()
            )
            
            // Configuración de sesiones
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().migrateSession() // Protección contra session fixation
                .maximumSessions(1) // Un usuario, una sesión activa
            )
            
            // Configuración CSRF: deshabilitar para API REST y H2 Console
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**", "/api/**")
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );
            
            // CSRF habilitado para formularios web, deshabilitado para API REST y H2 Console

        return http.build();
    }
}
