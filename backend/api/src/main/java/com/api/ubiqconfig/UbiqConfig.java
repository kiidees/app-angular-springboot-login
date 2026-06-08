package com.api.ubiqconfig;

import com.ubiqsecurity.UbiqCredentials;
import com.ubiqsecurity.UbiqConfiguration;
import com.ubiqsecurity.UbiqFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UbiqConfig {



    // @Value("${ubiq.access.key.id}")
    // private String accessKeyId;

    // @Value("${ubiq.secret.signing.key}")
    // private String secretSigningKey;

    // @Value("${ubiq.secret.crypto.key}")
    // private String secretCryptoKey;

    @Value("${ubiq.idp.ubiq-customer-id}")
    private String ubiqCustomerId;

    @Value("${ubiq.idp.idp-client-secret}")
    private String idpClientSecret;

    @Bean
    public UbiqCredentials ubiqCredentials() {
        try {
            // 1. Creamos las credenciales pasando las 3 llaves y 3 nulls finales (según tu Readme)
            UbiqCredentials credentials = UbiqFactory.createCredentials(
                null, 
                null, 
                null, 
                null, 
                ubiqCustomerId,
                idpClientSecret
            );

            UbiqConfiguration cfg = UbiqFactory.defaultConfiguration();
            credentials.init(cfg);

            System.out.println("--- UBIQ IDP SESSION INITIALIZED SUCCESSFULLY VIA ENTRA ID ---");

            System.out.println("CREEDENCIALESSSSSSSS: "+credentials);
            return credentials;
        } catch (Exception e) {
            throw new RuntimeException("Error al autenticar sesión IDP con Ubiq: " + e.getMessage(), e);
        }
    }
}