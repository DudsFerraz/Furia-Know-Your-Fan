package com.knowYourfan.backEnd.Services;

import com.knowYourfan.backEnd.DTOs.DocumentResponseDTO;
import com.knowYourfan.backEnd.Entities.Document;
import com.knowYourfan.backEnd.Entities.User;
import com.knowYourfan.backEnd.Repositories.DocumentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

    private DocumentResponseDTO generateDocumentResponseDTO(Document document) {
        return new DocumentResponseDTO(document.getValidationStatus(), document.getExtractedText());
    }
    private String validate(String extractedText, User user) throws IOException {
        String cpfDigits = user.getCpf().getDigitos();

        if(!extractedText.toLowerCase().contains(user.getName().toLowerCase())) return "Nome do usuário diferente do nome no documento";
        if(!extractedText.replaceAll("\\D", "").contains(cpfDigits)) return "CPF do usuário diferente do CPF no documento";
        return "VALID";
    }

    public Document create(Document d) { return documentRepository.save(d); }

    public List<Document> list() { return documentRepository.findAll(); }

    @Transactional
    public DocumentResponseDTO saveDocument(MultipartFile file, String extractedText, User user) throws IOException {
        String validation = validate(extractedText, user);
        if(!validation.equals("VALID")){
            return new DocumentResponseDTO(validation, extractedText);
        }

        Document doc = new Document();
        doc.setUser(user);
        doc.setFilePath(file.getOriginalFilename());
        doc.setExtractedText(extractedText);
        doc.setValidationStatus("VALID");
        Document document = documentRepository.save(doc);
        user.setCpfVerified(true);
        user.increaseXp(1000);
        return generateDocumentResponseDTO(document);
    }


}
