package com.remington.security.config;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filtro para depurar cookies y sesiones
 * Loguea información sobre cookies en cada request
 */
@Component
@Slf4j
public class SessionDebugFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Log de cookies recibidas
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            log.debug("=== COOKIES RECIBIDAS en {} ===", httpRequest.getRequestURI());
            for (Cookie cookie : cookies) {
                log.debug("Cookie: {} = {} (HttpOnly: {}, Secure: {}, Path: {}, MaxAge: {})", 
                    cookie.getName(), 
                    cookie.getValue(), 
                    cookie.isHttpOnly(), 
                    cookie.getSecure(),
                    cookie.getPath(),
                    cookie.getMaxAge());
            }
        } else {
            log.debug("=== NO HAY COOKIES en {} ===", httpRequest.getRequestURI());
        }
        
        // Log de sesión
        if (httpRequest.getSession(false) != null) {
            log.debug("Session ID: {}", httpRequest.getSession().getId());
        } else {
            log.debug("No hay sesión activa");
        }
        
        chain.doFilter(request, response);
    }
}
