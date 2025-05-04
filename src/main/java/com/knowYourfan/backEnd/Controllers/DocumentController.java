package com.knowYourfan.backEnd.Controllers;

import com.knowYourfan.backEnd.DTOs.DocumentResponseDTO;
import com.knowYourfan.backEnd.Entities.Document;
import com.knowYourfan.backEnd.Entities.User;
import com.knowYourfan.backEnd.Repositories.UserRepository;
import com.knowYourfan.backEnd.Services.DocumentService;
import com.knowYourfan.backEnd.Services.OcrService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/document") @RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;
    private final UserRepository userRepository;
    private final OcrService ocrService;

    @PostMapping
    public Document create(@RequestBody Document d) { return documentService.create(d); }

    @GetMapping
    public List<Document> list() { return documentService.list(); }

    @Transactional
    @PostMapping("/upload")
    public DocumentResponseDTO uploadDoc(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        String extractedText = ocrService.extractText(file);
        return documentService.saveDocument(file, extractedText, user);
    }

}