package com.spectrum.Document.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.Document.model.Document;
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

}
