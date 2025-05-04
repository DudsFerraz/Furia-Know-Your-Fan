package com.knowYourfan.backEnd.Services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class OcrService {
    private final ImageAnnotatorClient client;

    public OcrService() throws IOException {
        String credentialsJson = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON");
        if (credentialsJson == null || credentialsJson.isEmpty()) {
            throw new IllegalStateException("A variável de ambiente GOOGLE_APPLICATION_CREDENTIALS_JSON não está definida.");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8))
        );

        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build();

        this.client = ImageAnnotatorClient.create(settings);
    }

    public String extractText(MultipartFile file) throws IOException {
        ByteString imgBytes = ByteString.readFrom(file.getInputStream());

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();

        List<AnnotateImageResponse> responses = client.batchAnnotateImages(List.of(request)).getResponsesList();
        StringBuilder result = new StringBuilder();
        for (AnnotateImageResponse res : responses) {
            if (res.hasError()) {
                throw new IOException("Erro Vision API: " + res.getError().getMessage());
            }
            List<EntityAnnotation> annotations = res.getTextAnnotationsList();
            if (!annotations.isEmpty()) {
                result.append(annotations.getFirst().getDescription());
            }
        }

        return result.toString();
    }

}

