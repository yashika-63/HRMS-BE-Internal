package com.spectrum.CompanyEmployeeConfig.EmployeeConfig.controller;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.CompanyConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.CompanyConfigRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/company-config")
public class CompanyConfigController {

    @Autowired
    private CompanyConfigRepository companyConfigRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @PostMapping("/save/{companyId}")
    public ResponseEntity<?> saveCompanyConfig(@PathVariable Long companyId, @RequestBody CompanyConfig companyConfig) {
        Optional<CompanyRegistration> companyOptional = companyRegistrationRepository.findById(companyId);
        
        if (companyOptional.isPresent()) {
            CompanyRegistration company = companyOptional.get();
            companyConfig.setCompany(company);
            // companyConfig.setFeedbackDate(LocalDate.now()); // Set the current date

            CompanyConfig savedConfig = companyConfigRepository.save(companyConfig);
            return ResponseEntity.ok(savedConfig);
        } else {
            return ResponseEntity.badRequest().body("Company not found with ID: " + companyId);
        }
    }
}
