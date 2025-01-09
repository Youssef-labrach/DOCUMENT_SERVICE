package com.document_service.Service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class OcrService {


    public String extractText(MultipartFile file) throws IOException, TesseractException {
        // Set the TESSDATA_PREFIX environment variable
        System.setProperty("TESSDATA_PREFIX", "C:\\Program Files\\Tesseract-OCR\\");

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata"); // Update the path to Tesseract language data files
        //.setLanguage("eng"); // Set the language to English

        // Save the file temporarily for OCR processing
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        File tempFile = File.createTempFile("temp", fileExtension);
        file.transferTo(tempFile);


        // Perform OCR
        String extractedText = tesseract.doOCR(tempFile);




        return extractedText;
    }
}