package com.spectrum.Document.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.Document.model.DocumentExpenseManagement;

@Repository
public interface DocumentExpenseManagementRepository extends JpaRepository<DocumentExpenseManagement, Long>{

}
