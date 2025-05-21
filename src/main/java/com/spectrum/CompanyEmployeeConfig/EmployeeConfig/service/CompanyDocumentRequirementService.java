package com.spectrum.CompanyEmployeeConfig.EmployeeConfig.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.CompanyDocumentRequirement;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.CompanyDocumentRequirementRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;

@Service
public class CompanyDocumentRequirementService {

    @Autowired
    private CompanyDocumentRequirementRepository requirementRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    public List<CompanyDocumentRequirement> saveRequirements(Long companyId, List<CompanyDocumentRequirement> requirements) {
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));
    
        for (CompanyDocumentRequirement req : requirements) {
            req.setCompany(company);
            req.setStatus(true); // ✅ Set status true for all before saving
        }
    
        return requirementRepository.saveAll(requirements);
    }



    public List<CompanyDocumentRequirement> getActiveRequirementsByCompanyId(Long companyId) {
        return requirementRepository.findByStatusTrueAndCompanyId(companyId);
    }
    


    public CompanyDocumentRequirement makeInactive(Long id) {
        Optional<CompanyDocumentRequirement> optionalRequirement = requirementRepository.findById(id);
    
        if (optionalRequirement.isPresent()) {
            CompanyDocumentRequirement requirement = optionalRequirement.get();
            requirement.setStatus(false);
            return requirementRepository.save(requirement);
        } else {
            throw new RuntimeException("Requirement not found with id: " + id);
        }
    }
}
