package com.spectrum.Document.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.Document.model.CompanyDocument;
import com.spectrum.Document.repository.CompanyDocumentRepository;

@Service
public class CompanyDocumentService {


@Autowired
private CompanyDocumentRepository companyDocumentRepository;

      public List<CompanyDocument> getActiveDocumentsByCompanyId(Long companyId) {
        return companyDocumentRepository.findByCompanyIdAndStatusTrue(companyId);
    }
}
