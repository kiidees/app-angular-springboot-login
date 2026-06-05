package com.api.ubiqconfig;

import com.ubiqsecurity.UbiqCredentials;
import com.ubiqsecurity.UbiqConfiguration;
import com.ubiqsecurity.UbiqFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UbiqConfig {



    @Value("${ubiq.access.key.id}")
    private String accessKeyId;

    @Value("${ubiq.secret.signing.key}")
    private String secretSigningKey;

    @Value("${ubiq.secret.crypto.key}")
    private String secretCryptoKey;

    @Value("${ubiq.idp.ubiq-customer-id}")
    private String ubiqCustomerId;

    @Value("${ubiq.idp.idp-client-secret}")
    private String idpClientSecret;

    @Bean
    public UbiqCredentials ubiqCredentials() {
        try {
            // 1. Creamos las credenciales pasando las 3 llaves y 3 nulls finales (según tu Readme)
            UbiqCredentials credentials = UbiqFactory.createCredentials(
                null, null, null, null, 
                ubiqCustomerId,
                idpClientSecret
            );

            // try {
            //     UbiqConfiguration ubiqConfig = UbiqFactory.readConfigurationFromFile("../config.json"); 
            //     String cipherText = UbiqStructuredEncryptDecrypt.encrypt(encrypt_jwt, ubiqConfig, "TEST_NAME", "LuisManuel", null);
           
            //     String pt = UbiqStructuredEncryptDecrypt.decrypt(decrypt_jwt, ubiqConfig, "TEST_NAME", cipherText, null);
            // } catch (Exception e) {
            //     throw new RuntimeException(e.getMessage(), e.getCause());
            // } finally {
            //     UbiqStructuredEncryptDecrypt.closeJwt();
            // }

        //     // 2. Cargamos la configuración por defecto (o desde archivo si existiera)
        //     UbiqConfiguration cfg = UbiqFactory.defaultConfiguration();

        //     // 3. PASO CRÍTICO: Vinculamos la configuración a las credenciales para activarlas
        //     credentials.init(cfg);

        //     System.out.println("--- UBIQ CREDENTIALS INITIALIZED SUCCESSFULLY (v2.3.1) ---");
        //     return credentials;

        // } catch (Exception e) {
        //     throw new RuntimeException("Error al inicializar el handshake de Ubiq: " + e.getMessage(), e);
        // }
        // Cargamos la configuración y activamos las credenciales (esto dispara el handshake de tokens)
            UbiqConfiguration cfg = UbiqFactory.defaultConfiguration();
            credentials.init(cfg);

            System.out.println("--- UBIQ IDP SESSION INITIALIZED SUCCESSFULLY VIA ENTRA ID ---");
            return credentials;
        } catch (Exception e) {
            throw new RuntimeException("Error al autenticar sesión IDP con Ubiq: " + e.getMessage(), e);
        }
    }
}