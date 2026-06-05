package com.api.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // TODO: Si tienes una clase encargada de generar tus tokens (ej. JwtProvider o TokenService),
    // inyéctala aquí eliminando los comentarios de abajo:

    private final JwtProvider jwtProvider;
    
    public CustomAuthenticationSuccessHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        // 1. Obtenemos el usuario autenticado desde el contexto de OAuth2
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        // 2. Microsoft Entra ID (v2.0) envía el correo principal en 'preferred_username'.
        // Usamos un fallback a 'email' por si la configuración del Tenant varía.
        String email = oAuth2User.getAttribute("preferred_username");
        if (email == null) {
            email = oAuth2User.getAttribute("email");
        }
        
        String name = oAuth2User.getAttribute("name");

        // 3. Generación de tu Token Interno.
        // REEMPLAZA esta línea simulada por la llamada a tu generador de JWT real utilizando el 'email'.
        // Ejemplo: 
        String miJwtToken = jwtProvider.generateToken(email);

        // 4. Construimos la URL apuntando al flujo de recepción de Angular.
        // Pasamos el JWT de nuestra app como un query parameter para que el frontend lo capture.
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:4200/login")
                .queryParam("token", miJwtToken)
                .build().toUriString();

        System.out.println("Token generado: " + miJwtToken);

        // 5. Limpiamos los atributos temporales de autenticación de la sesión de Spring
        clearAuthenticationAttributes(request);

        // 6. Redirigimos al usuario de vuelta a la aplicación Angular
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}