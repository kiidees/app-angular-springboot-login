package com.api.security;

import com.api.security.CustomAuthenticationSuccessHandler; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAuthenticationSuccessHandler successHandler;

    // Inyectamos el filtro y el nuevo handler de éxito para OAuth2
    public SecurityConfig(JwtFilter jwtFilter, CustomAuthenticationSuccessHandler successHandler) {
        this.jwtFilter = jwtFilter;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            
            // Mantenemos STATELESS porque tus endpoints de negocio (/api/**) validan por JWT.
            // Nota: Spring Security manejará una sesión temporal en memoria SOLO durante el handshake de OAuth2.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos de login local o el inicio/callback de OAuth2
                .requestMatchers("/api/auth/**", "/login/**", "/oauth2/**").permitAll() 
                .anyRequest().authenticated()
            )
            
            // CONFIGURACIÓN OAUTH2 PARA ENTRA ID
            .oauth2Login(oauth2 -> oauth2
                // Manejador que se dispara cuando el usuario se loguea con éxito en Microsoft
                .successHandler(successHandler)
            )
            
            // Filtro para validar los JWT propios de tu app en las peticiones subsiguientes
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(List.of("http://localhost:4200"));
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(List.of("*"));
        cors.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
}