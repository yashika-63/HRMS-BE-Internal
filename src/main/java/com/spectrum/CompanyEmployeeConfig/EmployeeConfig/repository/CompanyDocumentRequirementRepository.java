package com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.CompanyDocumentRequirement;

public interface CompanyDocumentRequirementRepository extends JpaRepository<CompanyDocumentRequirement, Long> {


    List<CompanyDocumentRequirement> findByStatusTrueAndCompanyId(Long companyId);

}
