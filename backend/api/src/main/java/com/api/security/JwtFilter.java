package com.api.security; 

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;


@Component
public class JwtFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. Extraer el encabezado Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Quitar la palabra "Bearer "

            try {
                Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                
                // 2. Validar la firma del JWT y leer su contenido
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();

                if (email != null) {
                    // 3. AUTENTICACIÓN DIRECTA: Al no haber BD, creamos un usuario virtual en memoria
                    // Pasamos el email, sin contraseña (null), y una lista vacía de permisos
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            email, null, Collections.emptyList());
                    
                    // Autorizar la petición en Spring Security
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                // Si el token es inválido o expiró, nos aseguramos de limpiar la sesión
                SecurityContextHolder.clearContext();
            }
        }

        // Continuar con la petición HTTP hacia el Controlador
        filterChain.doFilter(request, response);
    }
}
