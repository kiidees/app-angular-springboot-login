package com.api.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    // 1. Inyectamos el servicio cliente de OAuth2 para acceder a los tokens crudos de Azure
    private final OAuth2AuthorizedClientService authorizedClientService;
    
    public CustomAuthenticationSuccessHandler(JwtProvider jwtProvider, OAuth2AuthorizedClientService authorizedClientService) {
        this.jwtProvider = jwtProvider;
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        // 2. Obtenemos el usuario autenticado desde el contexto de OAuth2
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        // 3. RECUPERAR EL TOKEN JWT DE ENTRA ID (Token solicitado por tu proveedor)
        String entraIdJwtToken = null;
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            
            // Buscamos el cliente autorizado usando el ID del cliente registrado y el nombre del usuario
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    oauthToken.getAuthorizedClientRegistrationId(), 
                    oauthToken.getName()
            );
            
            if (client != null && client.getAccessToken() != null) {
                // Aquí tienes el string del JWT crudo emitido por Microsoft Entra ID
                entraIdJwtToken = client.getAccessToken().getTokenValue();
                System.out.println(">>> TOKEN JWT DE MICROSOFT ENTRA ID DETECTADO: " + entraIdJwtToken);
            }
        }

        // 4. Tu lógica de negocio existente para extraer el email
        String email = oAuth2User.getAttribute("preferred_username");
        if (email == null) {
            email = oAuth2User.getAttribute("email");
        }
        
        // Generas el token de tu app
        String miJwtToken = jwtProvider.generateToken(email);

        // 5. REDIRECCIÓN A ANGULAR
        // Si necesitas que Angular conozca el token de Entra ID para enviarlo al controlador, 
        // puedes pasarlo también como query parameter:
    
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:4200/login")
                .queryParam("token", miJwtToken)
                .queryParam("entra_token", entraIdJwtToken) // <--- Opcional: Angular lo recibe aquí
                .build().toUriString();

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}