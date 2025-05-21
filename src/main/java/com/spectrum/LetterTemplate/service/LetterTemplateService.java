package com.spectrum.LetterTemplate.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.LetterTemplate.model.LetterTemplate;
import com.spectrum.LetterTemplate.repository.LetterTemplateRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;

@Service
public class LetterTemplateService {

    @Autowired
    private LetterTemplateRepository letterTemplateRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    public LetterTemplate saveLetterTemplate(LetterTemplate template, Long companyId) {
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
    
        // Step 1: Check for existing active template with same identity key and company
        LetterTemplate existingActiveTemplate = letterTemplateRepository
            .findByCompanyIdAndTemplateIdentityKeyAndStatusTrue(companyId, template.getTemplateIdentityKey());
    
        // Step 2: If found, deactivate it
        if (existingActiveTemplate != null) {
            existingActiveTemplate.setStatus(false);
            letterTemplateRepository.save(existingActiveTemplate);
        }
    
        // Step 3: Save new template with status = true
        template.setCompany(company);
        template.setStatus(true);
        return letterTemplateRepository.save(template);
    }
    




    public Optional<LetterTemplate> getActiveTemplateByCompanyAndKey(Long companyId, String templateIdentityKey) {
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
    
        return letterTemplateRepository.findByStatusTrueAndCompanyAndTemplateIdentityKey(company, templateIdentityKey);
    }
    



    public LetterTemplate updateLetterTemplate(Long id, LetterTemplate updatedTemplate) {
        LetterTemplate existingTemplate = letterTemplateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("LetterTemplate not found with ID: " + id));
    
        if (updatedTemplate.getTemplateIdentityKey() != null) {
            existingTemplate.setTemplateIdentityKey(updatedTemplate.getTemplateIdentityKey());
        }
        if (updatedTemplate.getSubject() != null) {
            existingTemplate.setSubject(updatedTemplate.getSubject());
        }
        if (updatedTemplate.getBody() != null) {
            existingTemplate.setBody(updatedTemplate.getBody());
        }
        if (updatedTemplate.getCompany() != null) {
            existingTemplate.setCompany(updatedTemplate.getCompany());
        }
        
        // Optional: explicitly update status if passed
// existingTemplate.setStatus(updatedTemplate.isStatus());
    
        return letterTemplateRepository.save(existingTemplate);
    }
    


    public LetterTemplate updateLetterTemplateStatus(Long id, boolean status) {
        LetterTemplate template = letterTemplateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("LetterTemplate not found with ID: " + id));
    
        template.setStatus(status);
        return letterTemplateRepository.save(template);
    }



    public List<LetterTemplate> getTemplatesByCompanyIdAndStatus(Long companyId, boolean status) {
        return letterTemplateRepository.findByCompanyIdAndStatus(companyId, status);
    }
    
    
}
 