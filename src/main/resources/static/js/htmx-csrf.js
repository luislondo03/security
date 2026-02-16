/**
 * Script para manejar tokens CSRF con HTMX
 * Facilita la integración de Spring Security CSRF con peticiones HTMX
 */

/**
 * Obtiene el token CSRF desde meta tag o cookie
 * @returns {string} El token CSRF o cadena vacía si no se encuentra
 */
function getCsrfToken() {
    // Intentar obtener token desde meta tag
    const metaToken = document.querySelector('meta[name="_csrf"]');
    if (metaToken) {
        return metaToken.getAttribute('content');
    }
    
    // Intentar obtener token desde cookie XSRF-TOKEN
    const csrfCookie = getCookie('XSRF-TOKEN');
    if (csrfCookie) {
        return csrfCookie;
    }
    
    // Buscar en el DOM (formularios)
    const csrfInput = document.querySelector('input[name="_csrf"]');
    if (csrfInput) {
        return csrfInput.value;
    }
    
    console.warn('CSRF token no encontrado');
    return '';
}

/**
 * Obtiene el nombre del header CSRF
 * @returns {string} El nombre del header
 */
function getCsrfHeaderName() {
    const metaHeader = document.querySelector('meta[name="_csrf_header"]');
    return metaHeader ? metaHeader.getAttribute('content') : 'X-CSRF-TOKEN';
}

/**
 * Helper para obtener una cookie por nombre
 * @param {string} name - Nombre de la cookie
 * @returns {string|null} Valor de la cookie o null
 */
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
        return parts.pop().split(';').shift();
    }
    return null;
}

/**
 * Maneja el logout exitoso redirigiendo a la página pública
 */
function handleLogout() {
    // Pequeño delay para asegurar que el logout se procese
    setTimeout(() => {
        window.location.href = '/public';
    }, 100);
}

/**
 * Configuración global de HTMX para incluir CSRF token automáticamente
 */
document.addEventListener('DOMContentLoaded', function() {
    // Agregar evento HTMX antes de cada request
    document.body.addEventListener('htmx:configRequest', function(event) {
        const csrfToken = getCsrfToken();
        const csrfHeader = getCsrfHeaderName();
        
        if (csrfToken) {
            event.detail.headers[csrfHeader] = csrfToken;
        }
    });

    // Manejar respuestas de logout
    document.body.addEventListener('htmx:afterRequest', function(event) {
        if (event.detail.pathInfo.requestPath === '/logout' && event.detail.successful) {
            window.location.href = '/public';
        }
    });

    // Manejar errores 403 (Forbidden) - típico cuando CSRF falla
    document.body.addEventListener('htmx:responseError', function(event) {
        if (event.detail.xhr.status === 403) {
            console.error('Error 403: Posible problema con token CSRF');
            alert('Error de autenticación. Por favor, recarga la página e intenta nuevamente.');
        }
    });

    console.log('HTMX-CSRF handler inicializado correctamente');
});

/**
 * Agregar meta tags CSRF al head si no existen (útil para páginas sin formulario)
 */
function addCsrfMetaTags() {
    // Buscar token en el DOM
    const csrfInput = document.querySelector('input[name="_csrf"]');
    if (csrfInput && !document.querySelector('meta[name="_csrf"]')) {
        const tokenMeta = document.createElement('meta');
        tokenMeta.name = '_csrf';
        tokenMeta.content = csrfInput.value;
        document.head.appendChild(tokenMeta);

        const headerMeta = document.createElement('meta');
        headerMeta.name = '_csrf_header';
        headerMeta.content = 'X-CSRF-TOKEN';
        document.head.appendChild(headerMeta);
    }
}

// Ejecutar al cargar la página
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', addCsrfMetaTags);
} else {
    addCsrfMetaTags();
}
