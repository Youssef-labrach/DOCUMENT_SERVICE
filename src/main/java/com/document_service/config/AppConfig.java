package com.document_service.config;

import net.sourceforge.tess4j.Tesseract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private static final String TESSDATA_PATH = "C:\\Program Files\\Tesseract-OCR\\tessdata";

    @Bean
    public Tesseract tesseract() {
        Tesseract tesseract = new Tesseract();
        
        // Check if tessdata directory exists
        File tessDataDir = new File(TESSDATA_PATH);
        if (!tessDataDir.exists() || !tessDataDir.isDirectory()) {
            logger.error("Tesseract data directory not found at: {}. Please install Tesseract and language data files.", TESSDATA_PATH);
            throw new RuntimeException("Tesseract data directory not found. Please install Tesseract properly.");
        }

        // Set the data path
        tesseract.setDatapath(TESSDATA_PATH);
        logger.info("Using Tesseract data path: {}", TESSDATA_PATH);

        return tesseract;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
