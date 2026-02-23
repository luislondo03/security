package com.remington.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.time.Duration;
import java.util.Locale;

/**
 * Configuración de internacionalización (i18n) para la aplicación.
 * Soporta cambio dinámico de idioma mediante parámetro 'lang' en la URL.
 * Idiomas soportados: Español (es), Inglés (en), Portugués (pt)
 */
@Configuration
public class InternationalizationConfig implements WebMvcConfigurer {

    /**
     * Define cómo se determina y almacena el idioma del usuario.
     * Usa cookies para persistir la preferencia de idioma.
     * 
     * @return LocaleResolver configurado con cookie
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver("language");
        
        // Idioma por defecto: Español
        localeResolver.setDefaultLocale(Locale.of("es"));
        
        // Cookie válida por 1 año
        localeResolver.setCookieMaxAge(Duration.ofDays(365));
        
        localeResolver.setCookiePath("/");
        
        return localeResolver;
    }

    /**
     * Interceptor que detecta cambios de idioma mediante parámetro 'lang' en la URL.
     * Ejemplo: /public?lang=en cambia el idioma a inglés
     * 
     * @return LocaleChangeInterceptor configurado
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        
        // Nombre del parámetro URL para cambiar idioma
        interceptor.setParamName("lang");
        
        return interceptor;
    }

    /**
     * Registra el interceptor de cambio de idioma en la cadena de interceptores.
     * 
     * @param registry Registro de interceptores
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
