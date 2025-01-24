package com.document_service.Controller;

import com.document_service.Model.Document;
import com.document_service.Repository.DocumentRepository;
import com.document_service.Service.OcrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private OcrService ocrService;

    @Autowired
    private DocumentRepository documentRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Please upload an image file");
            }

            logger.info("Processing file: {}, size: {} bytes", file.getOriginalFilename(), file.getSize());
            Map<String, String> extractedData = ocrService.extractText(file);
            
            if (extractedData.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "warning", "No text could be extracted from the image",
                    "data", new HashMap<String, String>()
                ));
            }
            
            // Create and save document
            Document document = new Document();
            document.setPays(extractedData.get("pays"));
            document.setCin(extractedData.get("cin"));
            document.setNom(extractedData.get("nom"));
            document.setPrenom(extractedData.get("prenom"));
            document.setDateNaissance(extractedData.get("date_naissance"));
            document.setLieuNaissance(extractedData.get("lieu_naissance"));
            
            documentRepository.save(document);
            
            logger.info("Document processed and saved successfully. CIN: {}", document.getCin());
            
            return ResponseEntity.ok(Map.of(
                "message", "Text extracted successfully",
                "data", extractedData
            ));
            
        } catch (Exception e) {
            logger.error("Error processing document: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to process document",
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        try {
            List<Document> documents = documentRepository.findAll();
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Error retrieving documents: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{cin}")
    public ResponseEntity<Document> getDocumentByCin(@PathVariable String cin) {
        try {
            return documentRepository.findByCin(cin)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error retrieving document by CIN {}: {}", cin, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
