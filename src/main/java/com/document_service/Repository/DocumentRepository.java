package com.document_service.Repository;

import com.document_service.Model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByCin(String cin);
}