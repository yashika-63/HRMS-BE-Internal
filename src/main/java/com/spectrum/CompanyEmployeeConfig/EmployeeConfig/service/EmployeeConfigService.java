package com.spectrum.CompanyEmployeeConfig.EmployeeConfig.service;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.EmployeeConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.EmployeeConfigRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository; // Assuming the EmployeeRepository exists
import com.spectrum.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeConfigService {

    @Autowired
    private EmployeeConfigRepository employeeConfigRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Autowired
    private EmailService emailService;

//    // Save EmployeeConfig mapped to an employee ID
// public EmployeeConfig saveEmployeeConfig(Long employeeId, EmployeeConfig employeeConfig) {
//     // Check if the employee exists
//     Employee employee = employeeRepository.findById(employeeId)
//             .orElseThrow(() -> new RuntimeException("Employee with ID " + employeeId + " not found."));

//     // Check if an EmployeeConfig already exists for this employee
//     Optional<EmployeeConfig> existingConfig = employeeConfigRepository.findByEmployee_Id(employeeId).stream().findFirst();
//     if (existingConfig.isPresent()) {
//         // Update the existing record
//         EmployeeConfig configToUpdate = existingConfig.get();
//         configToUpdate.setEmployeeType(employeeConfig.getEmployeeType());
//         configToUpdate.setWorkingHours(employeeConfig.getWorkingHours());
//         return employeeConfigRepository.save(configToUpdate);
//     }

//     // Create a new record if none exists
//     employeeConfig.setEmployee(employee);
//     return employeeConfigRepository.save(employeeConfig);
// }


public Map<String, Object> saveEmployeeConfig(Long employeeId, EmployeeConfig employeeConfig) {
    Map<String, Object> response = new HashMap<>();

    // Check if the employee exists
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee with ID " + employeeId + " not found."));

    // Check if an EmployeeConfig already exists for this employee
    Optional<EmployeeConfig> existingConfig = employeeConfigRepository.findByEmployee_Id(employeeId).stream().findFirst();
    if (existingConfig.isPresent()) {
        // Update the existing record
        EmployeeConfig configToUpdate = existingConfig.get();
        configToUpdate.setEmployeeType(employeeConfig.getEmployeeType());
        configToUpdate.setWorkingHours(employeeConfig.getWorkingHours());
        configToUpdate.setWorkStateCode(employeeConfig.getWorkStateCode());
        configToUpdate.setWorkCategoryCode(employeeConfig.getWorkCategoryCode());
        configToUpdate.setEmployeeSpecialHolidayAccess(employeeConfig.isEmployeeSpecialHolidayAccess());
        configToUpdate.setOvertimeApplicable(employeeConfig.isOvertimeApplicable());
        configToUpdate.setOvertimeRate(employeeConfig.getOvertimeRate());
        configToUpdate.setAllowableOvertimeHours(employeeConfig.getAllowableOvertimeHours());
        configToUpdate.setAllowableOvertimedays(employeeConfig.getAllowableOvertimedays());
        configToUpdate.setWorkState(employeeConfig.getWorkState());
        configToUpdate.setWorkCategory(employeeConfig.getWorkCategory());
        configToUpdate.setRegionId(employeeConfig.getRegionId());
        configToUpdate.setRegion(employeeConfig.getRegion());
        configToUpdate.setTypeId(employeeConfig.getTypeId());
        configToUpdate.setType(employeeConfig.getType());
        configToUpdate.setDepartmentId(employeeConfig.getDepartmentId());
        configToUpdate.setDepartment(employeeConfig.getDepartment());
        configToUpdate.setLevelCode(employeeConfig.getLevelCode());
        configToUpdate.setLevel(employeeConfig.getLevel());

        // Update reporting manager
        configToUpdate.setReportingManager(employeeConfig.getReportingManager());

        employeeConfigRepository.save(configToUpdate);

        response.put("message", "Existing configuration updated successfully.");
        response.put("data", configToUpdate);
        return response;
    }

    // Create a new record if none exists
    employeeConfig.setEmployee(employee);
    EmployeeConfig savedConfig = employeeConfigRepository.save(employeeConfig);

    response.put("message", "New configuration saved successfully.");
    response.put("data", savedConfig);
    return response;
}


    // Get EmployeeConfig records by employee ID
    public List<EmployeeConfig> getByEmployeeId(Long employeeId) {
        return employeeConfigRepository.findByEmployee_Id(employeeId);
    }

    // Delete EmployeeConfig by ID
    public void deleteById(Long id) {
        if (!employeeConfigRepository.existsById(id)) {
            throw new RuntimeException("EmployeeConfig with ID " + id + " not found.");
        }
        employeeConfigRepository.deleteById(id);
    }




    public EmployeeConfig updateEmployeeConfig(Long id, EmployeeConfig updatedConfig) {
        // Check if the EmployeeConfig with the given ID exists
        EmployeeConfig existingConfig = employeeConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EmployeeConfig with ID " + id + " not found."));
    
        // Update fields only if they are provided in the request
        if (updatedConfig.getEmployeeType() != null) {
            existingConfig.setEmployeeType(updatedConfig.getEmployeeType());
        }
        if (updatedConfig.getWorkingHours() != 0) {
            existingConfig.setWorkingHours(updatedConfig.getWorkingHours());
        }
        if (updatedConfig.getWorkStateCode() != 0) {
            existingConfig.setWorkStateCode(updatedConfig.getWorkStateCode());
        }
        if (updatedConfig.getWorkCategoryCode() != 0) {
            existingConfig.setWorkCategoryCode(updatedConfig.getWorkCategoryCode());
        }
        if (updatedConfig.getWorkState() != null) {
            existingConfig.setWorkState(updatedConfig.getWorkState());
        }
        if (updatedConfig.getWorkCategory() != null) {
            existingConfig.setWorkCategory(updatedConfig.getWorkCategory());
        }
        
        if (updatedConfig.isEmployeeSpecialHolidayAccess() != existingConfig.isEmployeeSpecialHolidayAccess()) {
            existingConfig.setEmployeeSpecialHolidayAccess(updatedConfig.isEmployeeSpecialHolidayAccess());
        }
        if (updatedConfig.isOvertimeApplicable() != existingConfig.isOvertimeApplicable()) {
            existingConfig.setOvertimeApplicable(updatedConfig.isOvertimeApplicable());
        }
        if (updatedConfig.getOvertimeRate() != 0) {
            existingConfig.setOvertimeRate(updatedConfig.getOvertimeRate());
        }
        if (updatedConfig.getAllowableOvertimeHours() != 0) {
            existingConfig.setAllowableOvertimeHours(updatedConfig.getAllowableOvertimeHours());
        }
        if (updatedConfig.getAllowableOvertimedays() != 0) {
            existingConfig.setAllowableOvertimedays(updatedConfig.getAllowableOvertimedays());
        }
    
        // New fields update only if provided
        if (updatedConfig.getRegionId() != 0) {
            existingConfig.setRegionId(updatedConfig.getRegionId());
        }
        if (updatedConfig.getRegion() != null) {
            existingConfig.setRegion(updatedConfig.getRegion());
        }
        if (updatedConfig.getTypeId() != 0) {
            existingConfig.setTypeId(updatedConfig.getTypeId());
        }
        if (updatedConfig.getType() != null) {
            existingConfig.setType(updatedConfig.getType());
        }
        if (updatedConfig.getDepartmentId() != 0) {
            existingConfig.setDepartmentId(updatedConfig.getDepartmentId());
        }
        if (updatedConfig.getDepartment() != null) {
            existingConfig.setDepartment(updatedConfig.getDepartment());
        }
        if (updatedConfig.getLevelCode() != 0) {
            existingConfig.setLevelCode(updatedConfig.getLevelCode());
        }
        if (updatedConfig.getLevel() != null) {
            existingConfig.setLevel(updatedConfig.getLevel());
        }
    
        // Update reporting manager only if a new value is provided
        if (updatedConfig.getReportingManager() != null && updatedConfig.getReportingManager().getId() != null) {
            existingConfig.setReportingManager(updatedConfig.getReportingManager());
        }
    
        // Save and return the updated entity
        return employeeConfigRepository.save(existingConfig);
    }
    

    // public EmployeeConfigService(EmployeeConfigRepository employeeConfigRepository) {
    //     this.employeeConfigRepository = employeeConfigRepository;
    // }
    // public Optional<EmployeeConfig> getEmployeeConfigByReportingManager(Long reportingManagerId) {
    //     return employeeConfigRepository.findByReportingManagerId(reportingManagerId);
    // }

    public List<EmployeeConfig> getEmployeeConfigByReportingManager(Long reportingManagerId) {
        return employeeConfigRepository.findByReportingManagerId(reportingManagerId);
    }
    


    public EmployeeConfig saveEmployeeConfig(Long employeeId, Long reportingManagerId, EmployeeConfig employeeConfig) {
        // Fetch Employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        // Fetch Reporting Manager
        Employee reportingManager = employeeRepository.findById(reportingManagerId)
                .orElseThrow(() -> new RuntimeException("Reporting Manager not found with ID: " + reportingManagerId));

        // Set values in EmployeeConfig
        employeeConfig.setEmployee(employee);
        employeeConfig.setReportingManager(reportingManager);

        // Save and return EmployeeConfig
        return employeeConfigRepository.save(employeeConfig);
    }









    /////////////////////////////////////////
    /// 
    /// 
    /// 
    /// 
    
    // private boolean isSameDay(Date date1, Date date2) {
    //     Calendar cal1 = Calendar.getInstance();
    //     cal1.setTime(date1);
    
    //     Calendar cal2 = Calendar.getInstance();
    //     cal2.setTime(date2);
    
    //     return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
    //             && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    // }
    




    @Scheduled(cron = "0 0 9 * * *") // Every day at 9 AM
    public void checkProbationEndApproaching() {
        List<EmployeeConfig> configs = employeeConfigRepository.findByConfirmationStatusFalse();
        Date
         today = new Date();

        for (EmployeeConfig config : configs) {
            Date joiningDate = config.getEmployee().getJoiningDate();
            int probationMonths = config.getProbationMonth();

            if (joiningDate == null || probationMonths <= 0) continue;

            Calendar probationEnd = Calendar.getInstance();
            probationEnd.setTime(joiningDate);
            probationEnd.add(Calendar.MONTH, probationMonths);

            Calendar reminderDate = (Calendar) probationEnd.clone();
            reminderDate.add(Calendar.MONTH, -1); // one month before end

            if (isSameDay(reminderDate.getTime(), today)) {
                String subject = "Probation Completion Reminder";
                String body = String.format("Employee %s (%s) is nearing the end of their probation. Please begin confirmation process.",
                        config.getEmployee().getFirstName(), config.getEmployee().getEmployeeId());

                String managerEmail = config.getReportingManager().getEmail();
                if (managerEmail != null && !managerEmail.isEmpty()) {
                    emailService.sendEmail(managerEmail, subject, body);
                }
            }
        }
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }


}
