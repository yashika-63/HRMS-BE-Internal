package com.spectrum.PerformanceManagement.GoalSetting.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spectrum.PerformanceManagement.GoalSetting.model.EmployeeGoalSetting;
import com.spectrum.PerformanceManagement.GoalSetting.model.OrganizationGoalSettingDetail;
import com.spectrum.PerformanceManagement.GoalSetting.model.OrganizationGoalSettingHeader;
import com.spectrum.PerformanceManagement.GoalSetting.repository.OrganizationGoalSettingHeaderRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;

import jakarta.transaction.Transactional;

@Service

public class OrganizationGoalSettingHeaderService {

@Autowired
private OrganizationGoalSettingHeaderRepository organizationGoalSettingHeaderRepository;


    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Transactional
    public OrganizationGoalSettingHeader saveOrganizationGoalSettingHeader(
            OrganizationGoalSettingHeader organizationGoalSettingHeader, Long companyId) {

        // Fetch the company based on companyId
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));

        // Set company to the header
        organizationGoalSettingHeader.setCompany(company);

        // Ensure status is always set to true
        organizationGoalSettingHeader.setStatus(true);

        // Set header reference for each detail
        if (organizationGoalSettingHeader.getOrgenizOrganizationGoalSettingDetails() != null) {
            for (OrganizationGoalSettingDetail detail : organizationGoalSettingHeader.getOrgenizOrganizationGoalSettingDetails()) {
                detail.setOrganizationGoalSettingHeader(organizationGoalSettingHeader);
            }
        }

        // Save the header (details will be saved due to cascading)
        return organizationGoalSettingHeaderRepository.save(organizationGoalSettingHeader);
    }



//     public List<OrganizationGoalSettingHeader> getGoalsByFilters(
//         Long companyId, boolean goalType, boolean status, int departmentId, int levelId, int regionId, int typeCode) {
//     return organizationGoalSettingHeaderRepository.findGoalsByFilters(
//             companyId, goalType, status, departmentId, levelId, regionId, typeCode);
// }



public List<OrganizationGoalSettingHeader> getByCompanyIdAndGoalType(Long companyId) {
    return organizationGoalSettingHeaderRepository.findByCompanyIdAndGoalType(companyId, true);
}



@Scheduled(cron = "0 0 0 31 12 ?") // Runs at midnight on December 31 every year
    @Transactional
    public void updateGoalTypeAtYearEnd() {
        List<OrganizationGoalSettingHeader> goalSettings = organizationGoalSettingHeaderRepository.findByGoalType(true);
        
        if (!goalSettings.isEmpty()) {
            for (OrganizationGoalSettingHeader goalSetting : goalSettings) {
                goalSetting.setGoalType(false);
            }
            organizationGoalSettingHeaderRepository.saveAll(goalSettings);
        }
    }



    @Transactional
    public OrganizationGoalSettingHeader updateGoalSettingHeader(Long id, OrganizationGoalSettingHeader updatedHeader) {
        // Fetch the existing header
        OrganizationGoalSettingHeader existingHeader = organizationGoalSettingHeaderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal Setting Header not found with ID: " + id));

        // Update fields (excluding company)
        existingHeader.setLevelCode(updatedHeader.getLevelCode());
        existingHeader.setLevel(updatedHeader.getLevel());
        existingHeader.setRegionId(updatedHeader.getRegionId());
        existingHeader.setRegion(updatedHeader.getRegion());
        existingHeader.setTypeId(updatedHeader.getTypeId());
        existingHeader.setType(updatedHeader.getType());
        existingHeader.setDepartmentId(updatedHeader.getDepartmentId());
        existingHeader.setDepartment(updatedHeader.getDepartment());
        existingHeader.setGoalType(updatedHeader.isGoalType());

        // Update details: Remove old, add new, and update existing ones
        existingHeader.getOrgenizOrganizationGoalSettingDetails().clear();
        if (updatedHeader.getOrgenizOrganizationGoalSettingDetails() != null) {
            for (OrganizationGoalSettingDetail detail : updatedHeader.getOrgenizOrganizationGoalSettingDetails()) {
                detail.setOrganizationGoalSettingHeader(existingHeader); // Set parent reference
                existingHeader.getOrgenizOrganizationGoalSettingDetails().add(detail);
            }
        }

        // Save and return the updated header
        return organizationGoalSettingHeaderRepository.save(existingHeader);
    }




    public List<OrganizationGoalSettingHeader> getByCompanyIdAndYear(Long companyId, int year) {
        return organizationGoalSettingHeaderRepository.findByCompanyIdAndYear(companyId, year);
    }


    public List<OrganizationGoalSettingHeader> getGoalsByFilters(
        Long companyId, boolean goalType, boolean status, int departmentId, int levelId, int regionId, int typeCode) {
    return organizationGoalSettingHeaderRepository.findGoalsByFilters(
            companyId, goalType, status, departmentId, levelId, regionId, typeCode);
}

public List<OrganizationGoalSettingHeader> getGoalsByFiltersNew(
        Long companyId, boolean status, int departmentId, int levelId, int regionId, int typeCode) {
    return organizationGoalSettingHeaderRepository.findGoalsByFiltersNew(
            companyId, status, departmentId, levelId, regionId, typeCode);
}

}
