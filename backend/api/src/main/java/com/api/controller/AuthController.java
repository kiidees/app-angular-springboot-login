package com.api.controller;

import com.api.models.User;
import com.api.repository.UserRepository;
import com.api.security.JwtProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${google.client-id}")
    private String googleClientId;

    /*private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public AuthController(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }
    */

   private final JwtProvider jwtProvider;
   public AuthController( JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
        String idTokenString = body.get("token");

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                System.out.println("Email: " + email);
                System.out.println("Name: " + name);

/** 
                // Buscar o registrar usuario en PostgreSQL
                User user = userRepository.findByEmail(email).orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    return userRepository.save(newUser);
                });
**/
                // Generar token propio de la aplicación
                //String token = jwtProvider.generateToken(user.getEmail());
                String token = jwtProvider.generateToken(email);

                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de Google inválido");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la validación");
        }
    }
}
