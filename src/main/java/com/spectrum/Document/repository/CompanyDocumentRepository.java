package com.spectrum.Document.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.Document.model.CompanyDocument;

public interface CompanyDocumentRepository extends JpaRepository<CompanyDocument, Long> {


    CompanyDocument findByDocumentIdentityKeyAndStatus(String documentIdentityKey, boolean status);

    Optional<CompanyDocument> findByCompanyIdAndDocumentIdentityKeyAndStatus(Long companyId, String documentIdentityKey, boolean status);

    Optional<CompanyDocument> findByCompanyIdAndStatus(Long companyId, boolean status);

    List<CompanyDocument> findAllByCompanyIdAndStatus(Long companyId, boolean status);

    List<CompanyDocument> findByCompanyIdAndStatusTrue(Long companyId);


    List<CompanyDocument> findAllByCompanyIdAndDocumentIdentityKeyAndStatus(Long companyId, String documentIdentityKey, boolean status);

}
