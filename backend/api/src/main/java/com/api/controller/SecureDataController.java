package com.api.controller;

import com.api.ubiqservice.EncryptionService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/secure") 
public class SecureDataController {

    private final EncryptionService encryptionService;

    public SecureDataController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping("/tociphertext") 
    public String procesarDatoSensible(@RequestParam String dato, @RequestParam String jwt) {
        String datoCifrado = encryptionService.encryptData(dato, jwt);
        System.out.println("Texto estructurado cifrado por Ubiq: " + datoCifrado);
        
        return "Flujo completado. Texto cifrado: " + datoCifrado;
    }

    @PostMapping("/toplaintext") 
    public String liberarDatoSensible(@RequestParam String dato, @RequestParam String jwt) {
        String datosOriginales = encryptionService.decryptData(dato, jwt);
        System.out.println("Texto estructurado descifrado por Ubiq: " + datosOriginales);
        
        return "Flujo completado. Texto descifrado: " + datosOriginales;
    }
}