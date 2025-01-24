package com.document_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.document_service"})
@EntityScan("com.document_service.Model")
@EnableJpaRepositories("com.document_service.Repository")
public class DocumentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DocumentServiceApplication.class, args);
    }
}
