package com.spectrum.PreOnboarding.EmployeeVerification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spectrum.PreOnboarding.EmployeeVerification.model.EmployeeVerificationDocument;

public interface EmployeeVerificationDocumentRepository extends JpaRepository<EmployeeVerificationDocument, Long> {
    // You can add custom query methods here if needed
}
