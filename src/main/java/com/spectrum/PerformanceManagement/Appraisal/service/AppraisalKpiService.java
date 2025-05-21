package com.spectrum.PerformanceManagement.Appraisal.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.CompanyConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.CompanyConfigRepository;
import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalKpi;
import com.spectrum.PerformanceManagement.Appraisal.repository.AppraisalKpiRepository;
import com.spectrum.PerformanceManagement.KpiManagement.model.EmployeeKpiSetting;
import com.spectrum.PerformanceManagement.KpiManagement.repository.EmployeeKpiSettingRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.transaction.Transactional;

@Service
public class AppraisalKpiService {




    
    @Autowired
    private CompanyConfigRepository companyConfigRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeKpiSettingRepository employeeKpiSettingRepository;

    @Autowired
    private AppraisalKpiRepository appraisalKpiRepository;

    @Autowired
    private EmailService emailService; // Assuming an email service is available

    @Transactional
    public void generateAppraisalKpis() {
        LocalDate today = LocalDate.now();
    
        // Get all company configurations where today is the appraisal start date
        List<CompanyConfig> companyConfigs = companyConfigRepository.findByAppraisalStartDate(today);
    
        for (CompanyConfig companyConfig : companyConfigs) {
            Long companyId = companyConfig.getCompany().getId();
    
            // Fetch all employees of this company
            List<Employee> employees = employeeRepository.findByCompanyRegistration_Id(companyId)
                                                         .orElse(Collections.emptyList());
    
            if (employees.isEmpty()) {
                continue; // Skip processing if no employees are found
            }
    
            List<AppraisalKpi> appraisalKpis = new ArrayList<>();
            Map<Employee, Integer> employeeKpiCountMap = new HashMap<>(); // Store KPI count per employee
    
            for (Employee employee : employees) {
                // Fetch active KPI settings for the employee
                List<EmployeeKpiSetting> activeKpis = employeeKpiSettingRepository.findByEmployeeAndStatus(employee, true);
    
                if (activeKpis.isEmpty()) {
                    continue; // Skip if no active KPIs
                }
    
                for (EmployeeKpiSetting kpiSetting : activeKpis) {
                    AppraisalKpi appraisalKpi = new AppraisalKpi();
                    appraisalKpi.setEmployee(employee);
                    appraisalKpi.setEmployees(kpiSetting.getEmployees()); // Reporting manager
                    appraisalKpi.setEmployeeKpiSetting(kpiSetting);
    
                    // Initialize all ratings to 0
                    appraisalKpi.setEmployeeSelfRating(0.0);
                    appraisalKpi.setEmployeeManagerRating(0.0);
                    appraisalKpi.setEmployeeModeratedFinalRating(0.0);
    
                    // Set statuses to false
                    appraisalKpi.setEmployeeRatingStatus(false);
                    appraisalKpi.setReportingRatingStatus(false);
                    appraisalKpi.setReportingModratedRatingStatus(false);
    
                    // Set appraisal year
                    appraisalKpi.setAppraisalYear(today.getYear());
    
                    appraisalKpis.add(appraisalKpi);
    
                    // Track KPI count per employee
                    employeeKpiCountMap.put(employee, employeeKpiCountMap.getOrDefault(employee, 0) + 1);
                }
            }
    
            // Save all generated appraisal KPIs in batch
            if (!appraisalKpis.isEmpty()) {
                appraisalKpiRepository.saveAll(appraisalKpis);
            }
    
            // Send one email per employee
            for (Map.Entry<Employee, Integer> entry : employeeKpiCountMap.entrySet()) {
                Employee employee = entry.getKey();
                int kpiCount = entry.getValue(); // Total KPIs assigned to this employee
    
                String subject = "Complete Your Appraisal Process";
                String body = "Dear " + employee.getFirstName() + ",\n\n" +
                        "Your appraisal process has started for the year " + today.getYear() + ". " +
                        "A total of " + kpiCount + " KPI(s) have been assigned to you. " +
                        "Please complete the self-assessment within the next 15 days.\n\n" +
                        "Best regards,\nHR Team";
    
                emailService.sendEmail(employee.getEmail(), subject, body);
            }
        }
    }
    
    //////////////////
    /// 
    /// 
    




 public String updateMultipleAppraisalKpis(List<AppraisalKpi> updatedAppraisalKpis) {
        List<AppraisalKpi> updatedRecords = updatedAppraisalKpis.stream().map(updatedKpi -> {
            Optional<AppraisalKpi> existingKpiOpt = appraisalKpiRepository.findById(updatedKpi.getId());
    
            if (existingKpiOpt.isPresent()) {
                AppraisalKpi existingKpi = existingKpiOpt.get();
    
                // Only update fields that are provided (non-default values)
                if (updatedKpi.getEmployeeSelfRating() != 0) 
                    existingKpi.setEmployeeSelfRating(updatedKpi.getEmployeeSelfRating());
    
                if (updatedKpi.getEmployeeManagerRating() != 0) 
                    existingKpi.setEmployeeManagerRating(updatedKpi.getEmployeeManagerRating());
    
                if (updatedKpi.getEmployeeModeratedFinalRating() != 0) 
                    existingKpi.setEmployeeModeratedFinalRating(updatedKpi.getEmployeeModeratedFinalRating());
    
                if (updatedKpi.getAppraisalYear() != 0) 
                    existingKpi.setAppraisalYear(updatedKpi.getAppraisalYear());
    
                if (updatedKpi.isEmployeeRatingStatus()) 
                    existingKpi.setEmployeeRatingStatus(updatedKpi.isEmployeeRatingStatus());
    
                if (updatedKpi.isReportingRatingStatus()) 
                    existingKpi.setReportingRatingStatus(updatedKpi.isReportingRatingStatus());
    
                if (updatedKpi.isReportingModratedRatingStatus()) 
                    existingKpi.setReportingModratedRatingStatus(updatedKpi.isReportingModratedRatingStatus());
    
                if (updatedKpi.isStatus()) 
                    existingKpi.setStatus(updatedKpi.isStatus());
    
                return existingKpi;
            } else {
                return null; // Ignore if not found
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    
        // Save all updated records in one batch
        if (!updatedRecords.isEmpty()) {
            appraisalKpiRepository.saveAll(updatedRecords);
        }
        
        return "AppraisalKpi records updated successfully";
    }





    
}


