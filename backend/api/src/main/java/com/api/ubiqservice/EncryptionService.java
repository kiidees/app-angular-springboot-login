package com.api.ubiqservice;

import com.ubiqsecurity.UbiqCredentials;
import com.ubiqsecurity.UbiqStructuredEncryptDecrypt;
import org.springframework.stereotype.Service;
import jakarta.annotation.PreDestroy;

@Service
public class EncryptionService {

    // Instancia única del cliente de cifrado estructurado para toda la aplicación
    private final UbiqStructuredEncryptDecrypt ubiqEncryptDecrypt;
    
    // Cambia "SSN" por el nombre exacto de tu dataset en el tablero de Ubiq
    private final String datasetName = "TEST_NAME"; 

    public EncryptionService(UbiqCredentials credentials) {
        // CORRECCIÓN 2.3.1: Instanciamos el objeto directamente pasándole las credenciales.
        // Ya no se utiliza UbiqFactory para obtener esta instancia.
        this.ubiqEncryptDecrypt = new UbiqStructuredEncryptDecrypt(credentials);
    }

    /**
     * Cifra un campo de texto estructurado de acuerdo a tu dataset
     */
    public String encryptData(String plainText) {
        try {
            // Sintaxis oficial: dataset, texto plano, y datos de autenticación (null)
            return ubiqEncryptDecrypt.encrypt(datasetName, plainText, null);
        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar con Ubiq 2.3.1: " + e.getMessage(), e);
        }
    }

    /**
     * Descifra un campo estructurado regresando su valor real
     */
    public String decryptData(String cipherText) {
        try {
            return ubiqEncryptDecrypt.decrypt(datasetName, cipherText, null);
        } catch (Exception e) {
            throw new RuntimeException("Error al descifrar con Ubiq 2.3.1: " + e.getMessage(), e);
        }
    }

    @PreDestroy
    public void cleanUp() {
        // Cerramos de forma segura los hilos asíncronos de métricas del SDK antes de apagar el contenedor
        if (this.ubiqEncryptDecrypt != null) {
            this.ubiqEncryptDecrypt.close();
        }
    }
}