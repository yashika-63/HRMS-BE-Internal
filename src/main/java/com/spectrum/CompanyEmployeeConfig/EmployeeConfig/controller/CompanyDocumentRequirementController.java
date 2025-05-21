package com.spectrum.CompanyEmployeeConfig.EmployeeConfig.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.CompanyDocumentRequirement;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.service.CompanyDocumentRequirementService;

@RestController
@RequestMapping("/api/company-documents")
public class CompanyDocumentRequirementController {

    @Autowired
    private CompanyDocumentRequirementService requirementService;

    @PostMapping("/save/{companyId}")
    public List<CompanyDocumentRequirement> saveRequirements(
            @PathVariable Long companyId,
            @RequestBody List<CompanyDocumentRequirement> requirements) {
        return requirementService.saveRequirements(companyId, requirements);
    }

    @GetMapping("/active/{companyId}")
    public List<CompanyDocumentRequirement> getActiveRequirements(@PathVariable Long companyId) {
        return requirementService.getActiveRequirementsByCompanyId(companyId);
    }

    
    @PutMapping("/deactivate/{id}")
public CompanyDocumentRequirement deactivateRequirement(@PathVariable Long id) {
    return requirementService.makeInactive(id);
}

}
