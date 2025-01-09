package com.document_service.Controller;

import com.document_service.Model.Document;
import com.document_service.Repository.DocumentRepository;
import com.document_service.Service.OcrService;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final OcrService ocrService;
    public DocumentController(DocumentRepository documentRepository, OcrService ocrService) {
        this.documentRepository = documentRepository;
        this.ocrService = ocrService;

    }

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {
        try {
            // Extract text using OCR
            String extractedText = ocrService.extractText(file);


            // Create a new Document object and set the user
            Document document = new Document();
            document.setFileName(file.getOriginalFilename());
            document.setExtractedData(extractedText);
            document.setUploadDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));


            // Save the document to the database
            documentRepository.save(document);

            return ResponseEntity.ok(document);
        } catch (IOException | TesseractException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        return documentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentRepository.findAll());
    }
}
