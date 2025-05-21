package com.spectrum.PerformanceManagement.Appraisal.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.CompanyConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.CompanyConfigRepository;
import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalGoal;
import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalKpi;
import com.spectrum.PerformanceManagement.Appraisal.repository.AppraisalGoalRepository;
import com.spectrum.PerformanceManagement.GoalSetting.model.EmployeeGoalSetting;
import com.spectrum.PerformanceManagement.GoalSetting.repository.EmployeeGoalSettingRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.transaction.Transactional;

@Service
public class AppraisalGoalService {


 
    @Autowired
    private CompanyConfigRepository companyConfigRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeGoalSettingRepository employeeGoalSettingRepository;

    @Autowired
    private AppraisalGoalRepository appraisalGoalRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void generateAppraisalGoals() {
        LocalDate today = LocalDate.now();
    
        List<CompanyConfig> companyConfigs = companyConfigRepository.findByAppraisalStartDate(today);
    
        for (CompanyConfig companyConfig : companyConfigs) {
            Long companyId = companyConfig.getCompany().getId();
    
            List<Employee> employees = employeeRepository.findByCompanyRegistration_Id(companyId)
                                                         .orElse(Collections.emptyList());
    
            if (employees.isEmpty()) {
                continue;
            }
    
            List<AppraisalGoal> appraisalGoals = new ArrayList<>();
            Map<Employee, Integer> employeeGoalCountMap = new HashMap<>();
    
            for (Employee employee : employees) {
                List<EmployeeGoalSetting> activeGoals = employeeGoalSettingRepository.findByEmployeeAndStatus(employee, true);
    
                if (activeGoals.isEmpty()) {
                    continue;
                }
    
                for (EmployeeGoalSetting goalSetting : activeGoals) {
                    AppraisalGoal appraisalGoal = new AppraisalGoal();
                    appraisalGoal.setEmployee(employee);
                    appraisalGoal.setEmployees(goalSetting.getEmployees()); // Reporting manager
                    appraisalGoal.setEmployeeGoalSetting(goalSetting);
    
                    appraisalGoal.setEmployeeSelfRating(0.0);
                    appraisalGoal.setEmployeeManagerRating(0.0);
                    appraisalGoal.setEmployeeModeratedFinalRating(0.0);
    
                    appraisalGoal.setStatus(false);
                    appraisalGoal.setParameterType(true);
    
                    appraisalGoal.setAppraisalYear(today.getYear());
    
                    appraisalGoals.add(appraisalGoal);
                    employeeGoalCountMap.put(employee, employeeGoalCountMap.getOrDefault(employee, 0) + 1);
                }
            }
    
            if (!appraisalGoals.isEmpty()) {
                appraisalGoalRepository.saveAll(appraisalGoals);
            }
    
            for (Map.Entry<Employee, Integer> entry : employeeGoalCountMap.entrySet()) {
                Employee employee = entry.getKey();
                int goalCount = entry.getValue();
    
                String subject = "Complete Your Appraisal Goal Process";
                String body = "Dear " + employee.getFirstName() + ",\n\n" +
                        "Your appraisal goal-setting process has started for the year " + today.getYear() + ". " +
                        "A total of " + goalCount + " goal(s) have been assigned to you. " +
                        "Please complete the self-assessment within the next 7 days.\n\n" +
                        "Best regards,\nHR Team";
    
                emailService.sendEmail(employee.getEmail(), subject, body);
            }
        }
    }





        
        /////
        

        public String updateMultipleAppraisalGoals(List<AppraisalGoal> updatedAppraisalGoals) {
            List<AppraisalGoal> updatedRecords = updatedAppraisalGoals.stream().map(updatedGoal -> {
                Optional<AppraisalGoal> existingGoalOpt = appraisalGoalRepository.findById(updatedGoal.getId());
        
                if (existingGoalOpt.isPresent()) {
                    AppraisalGoal existingGoal = existingGoalOpt.get();
        
                    // Only update fields that are provided (non-null)
                    if (updatedGoal.getEmployeeSelfRating() != 0) 
                        existingGoal.setEmployeeSelfRating(updatedGoal.getEmployeeSelfRating());
        
                    if (updatedGoal.getEmployeeManagerRating() != 0) 
                        existingGoal.setEmployeeManagerRating(updatedGoal.getEmployeeManagerRating());
        
                    if (updatedGoal.getEmployeeModeratedFinalRating() != 0) 
                        existingGoal.setEmployeeModeratedFinalRating(updatedGoal.getEmployeeModeratedFinalRating());
        
                    if (updatedGoal.getAppraisalYear() != 0) 
                        existingGoal.setAppraisalYear(updatedGoal.getAppraisalYear());
        
                    // existingGoal.setStatus(updatedGoal.isStatus());  // Boolean can't be null
                    // existingGoal.setParameterType(updatedGoal.isParameterType()); // Boolean can't be null
        
                    if (updatedGoal.isEmployeeRatingStatus()) 
                        existingGoal.setEmployeeRatingStatus(updatedGoal.isEmployeeRatingStatus());
        
                    if (updatedGoal.isReportingRatingStatus()) 
                        existingGoal.setReportingRatingStatus(updatedGoal.isReportingRatingStatus());
        
                    if (updatedGoal.isReportingModratedRatingStatus()) 
                        existingGoal.setReportingModratedRatingStatus(updatedGoal.isReportingModratedRatingStatus());
        
                        if (updatedGoal.isStatus()) 
                        existingGoal.setStatus(updatedGoal.isStatus());
        
                        if (updatedGoal.isReportingModratedRatingStatus()) 
                        existingGoal.setReportingModratedRatingStatus(updatedGoal.isReportingModratedRatingStatus());

                    return existingGoal;
                } else {
                    return null; // If not found, ignore
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        
            // Save all updated records in one batch
            if (!updatedRecords.isEmpty()) {
                appraisalGoalRepository.saveAll(updatedRecords);
            }
            
            return "AppraisalGoal records updated successfully";
        }




    ////////
    /// 
    /// 
    /// 
    /// 
    // Temporary storage for OTPs (In real cases, use Redis or DB)

}    