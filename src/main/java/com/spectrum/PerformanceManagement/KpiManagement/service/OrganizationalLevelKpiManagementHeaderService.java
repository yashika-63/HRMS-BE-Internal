package com.spectrum.PerformanceManagement.KpiManagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.PerformanceManagement.GoalSetting.model.EmployeeGoalSetting;
import com.spectrum.PerformanceManagement.KpiManagement.model.OrganizationalLevelKpiManagementDetails;
import com.spectrum.PerformanceManagement.KpiManagement.model.OrganizationalLevelKpiManagementHeader;
import com.spectrum.PerformanceManagement.KpiManagement.repository.OrganizationalLevelKpiManagementHeaderRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;

import jakarta.transaction.Transactional;

@Service

public class OrganizationalLevelKpiManagementHeaderService {

   @Autowired
    private OrganizationalLevelKpiManagementHeaderRepository organizationalLevelKpiManagementHeaderRepository;
  
    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;


    @Transactional
public OrganizationalLevelKpiManagementHeader saveOrganizationalLevelKpiManagementHeader(
        OrganizationalLevelKpiManagementHeader organizationalLevelKpiManagementHeader, Long companyId) {

    // Fetch the company based on companyId
    CompanyRegistration company = companyRegistrationRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));

    // Set company to the header
    organizationalLevelKpiManagementHeader.setCompany(company);

    // Ensure status is always set to true
    organizationalLevelKpiManagementHeader.setStatus(true);

    // Set header reference for each detail
    if (organizationalLevelKpiManagementHeader.getOrganizationalLevelKpiManagementDetails() != null) {
        for (OrganizationalLevelKpiManagementDetails detail : organizationalLevelKpiManagementHeader.getOrganizationalLevelKpiManagementDetails()) {
            detail.setOrganizationalLevelKpiManagementHeader(organizationalLevelKpiManagementHeader);
        }
    }

    // Save and return the entity
    return organizationalLevelKpiManagementHeaderRepository.save(organizationalLevelKpiManagementHeader);
}



public List<OrganizationalLevelKpiManagementHeader> getKpiByFilters(
        Long companyId, boolean kpiType, int departmentId, int levelCode, int regionId, int typeId) {
    return organizationalLevelKpiManagementHeaderRepository.findByFilters(
            companyId, kpiType, departmentId, levelCode, regionId, typeId);
}


public List<OrganizationalLevelKpiManagementHeader> getKpiByFiltersNew(
        Long companyId, int departmentId, int levelCode, int regionId, int typeId) {
    return organizationalLevelKpiManagementHeaderRepository.findByFiltersNew(
            companyId, departmentId, levelCode, regionId, typeId);
}


public List<OrganizationalLevelKpiManagementHeader> getKpiByCompanyIdAndYear(Long companyId, int year) {
    return organizationalLevelKpiManagementHeaderRepository.findByCompanyIdAndYear(companyId, year);
}



@Transactional
public OrganizationalLevelKpiManagementHeader updateKpiManagementHeader(Long id, OrganizationalLevelKpiManagementHeader updatedHeader) {
    // Fetch the existing header
    OrganizationalLevelKpiManagementHeader existingHeader = organizationalLevelKpiManagementHeaderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("KPI Header not found with ID: " + id));

    // Update fields (excluding company)
    existingHeader.setLevelCode(updatedHeader.getLevelCode());
    existingHeader.setLevel(updatedHeader.getLevel());
    existingHeader.setRegionId(updatedHeader.getRegionId());
    existingHeader.setRegion(updatedHeader.getRegion());
    existingHeader.setTypeId(updatedHeader.getTypeId());
    existingHeader.setType(updatedHeader.getType());
    existingHeader.setDepartmentId(updatedHeader.getDepartmentId());
    existingHeader.setDepartment(updatedHeader.getDepartment());
    existingHeader.setKpiType(updatedHeader.isKpiType());

    // Update details: Remove missing ones, update existing, and add new ones
    existingHeader.getOrganizationalLevelKpiManagementDetails().clear(); // Remove old details
    if (updatedHeader.getOrganizationalLevelKpiManagementDetails() != null) {
        for (OrganizationalLevelKpiManagementDetails detail : updatedHeader.getOrganizationalLevelKpiManagementDetails()) {
            detail.setOrganizationalLevelKpiManagementHeader(existingHeader); // Set parent reference
            existingHeader.getOrganizationalLevelKpiManagementDetails().add(detail);
        }
    }

    // Save and return the updated header
    return organizationalLevelKpiManagementHeaderRepository.save(existingHeader);
}

public List<OrganizationalLevelKpiManagementHeader> getKpisByCompanyAndDepartmentAndRegionAndKpiTypeAndStatus(
        int regionId, Long companyId, int departmentId, int typeId, boolean status, boolean kpiType) {
    return organizationalLevelKpiManagementHeaderRepository.findGoalsWithDepartmentOrZeroAndRegionIdOrZeroAndTypeIdOrZero(
        regionId, companyId, kpiType, departmentId, typeId, status
    );
}




public List<OrganizationalLevelKpiManagementHeader> getKpisByCompanyAndDepartmentAndRegionAndStatus(
        int regionId, Long companyId, int departmentId, int typeId, boolean status) {
    return organizationalLevelKpiManagementHeaderRepository.findGoalsWithDepartmentOrZeroAndRegionIdOrZeroAndTypeIdOrZero(
        regionId, companyId, departmentId, typeId, status
    );
}


}
