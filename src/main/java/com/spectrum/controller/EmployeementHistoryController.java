package com.spectrum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.model.Employee;
import com.spectrum.model.EmployeementHistory;
import com.spectrum.service.EmployeeService;
import com.spectrum.service.EmployeementHistoryService;

@RestController
// @CrossOrigin("http://localhost:3000")
@RequestMapping("/api/employeement-history")

public class EmployeementHistoryController {

    @Autowired
    private EmployeementHistoryService employeementHistoryService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/saveForEmployee")
    public ResponseEntity<String> saveEmployeeHistoryForEmployee(
            @RequestBody List<EmployeementHistory> employementHistory,
            @RequestParam Long employeeId) {
        try {
            // Check if the employee exists
            Employee employee = employeeService.getById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

            for (EmployeementHistory employeementHistory : employementHistory) {
                // Associate the education with the employee
                employeementHistory.setEmployee(employee);

                // Save the education
                employeementHistoryService.saveEmployeementHistory(employeementHistory);
            }

            return ResponseEntity.ok("EmployeementHistory saved successfully for employee with ID: " + employeeId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save EmployeementHistory for employee with ID: " + employeeId);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEmployeementHistory(@PathVariable Long id,
            @RequestBody EmployeementHistory employeementHistoryDetails) {
        try {
            EmployeementHistory existingEmployeementHistory = employeementHistoryService.getById(id)
                    .orElseThrow(() -> new IllegalArgumentException("EmployeementHistory not found with ID: " + id));

            // Update the fields
            existingEmployeementHistory.setCompanyName(employeementHistoryDetails.getCompanyName());
            existingEmployeementHistory.setJobRole(employeementHistoryDetails.getJobRole());
            existingEmployeementHistory.setResponsibilities(employeementHistoryDetails.getResponsibilities());
            existingEmployeementHistory.setStartDate(employeementHistoryDetails.getStartDate());
            existingEmployeementHistory.setEndDate(employeementHistoryDetails.getEndDate());
            existingEmployeementHistory.setJobDuration(employeementHistoryDetails.getJobDuration());
            existingEmployeementHistory.setLatestCtc(employeementHistoryDetails.getLatestCtc());
            existingEmployeementHistory.setSupervisorContact(employeementHistoryDetails.getSupervisorContact());
            existingEmployeementHistory.setReasonOfLeaving(employeementHistoryDetails.getReasonOfLeaving());
            existingEmployeementHistory.setAchievements(employeementHistoryDetails.getAchievements());
            existingEmployeementHistory.setEmployeementType(employeementHistoryDetails.getEmployeementType());
            existingEmployeementHistory.setLocation(employeementHistoryDetails.getLocation());
            existingEmployeementHistory.setIndustry(employeementHistoryDetails.getIndustry());
            existingEmployeementHistory.setCompanySize(employeementHistoryDetails.getCompanySize());
            existingEmployeementHistory.setLatestMonthGross(employeementHistoryDetails.getLatestMonthGross());
            existingEmployeementHistory.setTeamSize(employeementHistoryDetails.getTeamSize());

            employeementHistoryService.saveEmployeementHistory(existingEmployeementHistory);

            return ResponseEntity.ok("EmployeementHistory updated successfully for ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update EmployeementHistory for ID: " + id);
        }
    }





    @DeleteMapping("/delete/{id}")
public ResponseEntity<String> deleteEmployeementHistory(@PathVariable Long id) {
    try {
        EmployeementHistory employeementHistory = employeementHistoryService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("EmployeementHistory not found with ID: " + id));

        employeementHistoryService.deleteEmployeementHistory(employeementHistory);
        return ResponseEntity.ok("EmployeementHistory deleted successfully for ID: " + id);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to delete EmployeementHistory for ID: " + id);
    }
}




@GetMapping("/get/{id}")
public ResponseEntity<EmployeementHistory> getEmployeementHistoryById(@PathVariable Long id) {
    try {
        EmployeementHistory employeementHistory = employeementHistoryService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("EmployeementHistory not found with ID: " + id));

        return ResponseEntity.ok(employeementHistory);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}







@GetMapping("/getByEmployeeId/{employeeId}")
public ResponseEntity<List<EmployeementHistory>> getEmployeementHistoryByEmployeeId(@PathVariable Long employeeId) {
    try {
        List<EmployeementHistory> employeementHistoryList = employeementHistoryService.getByEmployeeId(employeeId);
        if (employeementHistoryList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employeementHistoryList);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}



}
