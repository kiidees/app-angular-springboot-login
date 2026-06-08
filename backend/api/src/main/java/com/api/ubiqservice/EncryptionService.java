package com.api.ubiqservice;

import com.ubiqsecurity.UbiqConfiguration;
import com.ubiqsecurity.UbiqCredentials;
import com.ubiqsecurity.UbiqFactory;
import com.ubiqsecurity.UbiqStructuredEncryptDecrypt;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class EncryptionService {

    private final String datasetName = "TEST_NAME";

    public String encryptData(String plainText, String jwt) {
        try {
            File configFile = new ClassPathResource("config.json").getFile();
            UbiqConfiguration ubiqConfig = UbiqFactory.readConfigurationFromFile(configFile.getAbsolutePath());

            // UbiqCredentials credentials = UbiqFactory.createCredentials(null, null, null, null, null, null);
            UbiqCredentials credentials = UbiqFactory.createCredentialsJwt(jwt, null);
            credentials.init(ubiqConfig);

            try (UbiqStructuredEncryptDecrypt ubiqEncryptDecrypt = new UbiqStructuredEncryptDecrypt(credentials)) {
                
                return ubiqEncryptDecrypt.encrypt(datasetName, plainText, null);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        } finally {
            UbiqStructuredEncryptDecrypt.closeJwt();
        }
    }

    public String decryptData(String cipherText, String jwt) {

        try {
            File configFile = new ClassPathResource("config.json").getFile();
            UbiqConfiguration ubiqConfig = UbiqFactory.readConfigurationFromFile(configFile.getAbsolutePath());

            // UbiqCredentials credentials = UbiqFactory.createCredentials(null, null, null, null, null, null);
            UbiqCredentials credentials = UbiqFactory.createCredentialsJwt(jwt, null);
            credentials.init(ubiqConfig);

            try (UbiqStructuredEncryptDecrypt ubiqEncryptDecrypt = new UbiqStructuredEncryptDecrypt(credentials)) {
                
                return ubiqEncryptDecrypt.decrypt(datasetName, cipherText, null);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        } finally {
            UbiqStructuredEncryptDecrypt.closeJwt();
        }
    }
}