package com.knowYourfan.backEnd.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class GoogleCredentialsInitializer {

    @PostConstruct
    public void init() {
        try {
            String jsonContent = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON");

            if (jsonContent == null || jsonContent.isBlank()) {
                throw new RuntimeException("❌ GOOGLE_APPLICATION_CREDENTIALS_JSON não está definida.");
            }

            Path tempFile = Files.createTempFile("google-credentials", ".json");
            Files.writeString(tempFile, jsonContent);

            System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", tempFile.toAbsolutePath().toString());
            System.out.println("✅ GOOGLE_APPLICATION_CREDENTIALS setado com sucesso: " + tempFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao configurar Google Vision API", e);
        }
    }
}


