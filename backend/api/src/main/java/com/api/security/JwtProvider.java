package com.api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String email) {
        // 1. Convertimos tu clave secreta de application.properties a un objeto Key seguro
    String secreto = secret;
    Key key = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));

    // 2. Firmamos usando el objeto key seguro
    return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
}