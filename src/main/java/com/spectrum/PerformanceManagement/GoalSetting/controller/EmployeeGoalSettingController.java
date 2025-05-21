package com.spectrum.PerformanceManagement.GoalSetting.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.PerformanceManagement.GoalSetting.model.EmployeeGoalSetting;
import com.spectrum.PerformanceManagement.GoalSetting.repository.EmployeeGoalSettingRepository;
import com.spectrum.PerformanceManagement.GoalSetting.service.EmployeeGoalSettingService;

@RestController
@RequestMapping("/api/goalSetting")
public class EmployeeGoalSettingController {

    @Autowired
    private EmployeeGoalSettingService employeeGoalSettingService;

    @Autowired
    private EmployeeGoalSettingRepository employeeGoalSettingRepository;

    @PostMapping("/saveMultiple/{employeeId}/{reportingManagerId}")
    public ResponseEntity<String> saveMultipleEmployeeGoalSettings(
            @PathVariable Long employeeId,
            @PathVariable Long reportingManagerId,
            @RequestBody List<EmployeeGoalSetting> goalSettings) {

        try {
            employeeGoalSettingService.saveMultipleEmployeeGoalSettings(employeeId, reportingManagerId, goalSettings);
            return ResponseEntity.status(HttpStatus.CREATED).body("Goal settings saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


      @GetMapping("/getByEmployeeId/{employeeId}")
    public ResponseEntity<List<EmployeeGoalSetting>> getGoalSettingsByEmployeeId(@PathVariable Long employeeId) {
        try {
            List<EmployeeGoalSetting> goalSettings = employeeGoalSettingService.getGoalSettingsByEmployeeId(employeeId);
            return ResponseEntity.ok(goalSettings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/getGoals")
    public ResponseEntity<List<EmployeeGoalSetting>> getGoals(
            @RequestParam int regionId,
            @RequestParam Long employeeId,
            @RequestParam int departmentId,
            @RequestParam boolean goalType) {
        try {
            List<EmployeeGoalSetting> goalSettings = employeeGoalSettingService.getGoalsByEmployeeAndDepartment(regionId, employeeId, departmentId, goalType);
            return ResponseEntity.ok(goalSettings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


// this is tnhe api we use to get complete year feedback
    @GetMapping("/getByEmployeeAndYearDetailForReview")
    public ResponseEntity<List<Map<String, Object>>> getGoalSettingsByEmployeeAndYear(
            @RequestParam Long employeeId, @RequestParam int year) {
        List<Map<String, Object>> response = employeeGoalSettingService.getGoalSettingsWithRatings(employeeId, year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/{employeeId}")
    public ResponseEntity<List<EmployeeGoalSetting>> getActiveGoalsByEmployeeId(@PathVariable Long employeeId) {
        List<EmployeeGoalSetting> goals = employeeGoalSettingRepository
                .findByEmployeeIdAndStatusTrueAndReviewStatusTrue(employeeId);
        return ResponseEntity.ok(goals);
    }

    ///////////////////////////
    ///
    ///
    // this is the section where we are creating api for get goals by employeeid as
    /////////////////////////// reporitng manager id to get reporitng managers
    // goals for setting own goals for all department, rigion, goal type, type,

    @GetMapping("/getGoalsDetail")
    public ResponseEntity<List<EmployeeGoalSetting>> getGoalsForAssign(
            @RequestParam int regionId,
            @RequestParam Long employeeId,
            @RequestParam int departmentId,
            @RequestParam int typeId,
            @RequestParam boolean status,
            @RequestParam boolean goalType) {
        try {
            List<EmployeeGoalSetting> goalSettings = employeeGoalSettingService
                    .getGoalsByEmployeeAndDepartmentAndReginANdGoaltTypeAndStatusAndGoalType(
                            regionId, employeeId, departmentId, typeId, status, goalType);
            return ResponseEntity.ok(goalSettings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    ///// this is the reporting manager can update goal beore employee approval
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployeeGoal(@PathVariable Long id, @RequestBody EmployeeGoalSetting updatedGoal) {
        Optional<EmployeeGoalSetting> optionalGoal = employeeGoalSettingRepository.findById(id);
        
        if (optionalGoal.isPresent()) {
            EmployeeGoalSetting existingGoal = optionalGoal.get();
    
            // Prevent update if employeeApproval is true
            if (existingGoal.isEmployeeApproval()) {
                return ResponseEntity.badRequest().body("Update not allowed: Goal has been approved and cannot be changed.");
            }
    
            // Update only fields that are provided in the request, keeping old values for missing fields
            if (updatedGoal.getGoal() != null) existingGoal.setGoal(updatedGoal.getGoal());
            // if (updatedGoal.getGoalDescription() != null) existingGoal.setGoalDescription(updatedGoal.getGoalDescription());
            if (updatedGoal.getRegionId() != 0) existingGoal.setRegionId(updatedGoal.getRegionId());
            if (updatedGoal.getRegion() != null) existingGoal.setRegion(updatedGoal.getRegion());
            if (updatedGoal.getTypeId() != 0) existingGoal.setTypeId(updatedGoal.getTypeId());
            if (updatedGoal.getType() != null) existingGoal.setType(updatedGoal.getType());
            if (updatedGoal.getDepartmentId() != 0) existingGoal.setDepartmentId(updatedGoal.getDepartmentId());
            if (updatedGoal.getDepartment() != null) existingGoal.setDepartment(updatedGoal.getDepartment());
    
            // Update boolean fields only if they are different from the existing values
            if (updatedGoal.isGoalType() != existingGoal.isGoalType()) existingGoal.setGoalType(updatedGoal.isGoalType());
            if (updatedGoal.isReviewStatus() != existingGoal.isReviewStatus()) existingGoal.setReviewStatus(updatedGoal.isReviewStatus());
            if (updatedGoal.isEmployeeApproval() != existingGoal.isEmployeeApproval()) existingGoal.setEmployeeApproval(updatedGoal.isEmployeeApproval());
    
            // Save updated goal
            employeeGoalSettingRepository.save(existingGoal);
            return ResponseEntity.ok("Goal updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Goal not found.");
        }
    }


    ///// this is the method for get data by employee and specific year
    /// 
    /// 
    /// 
    @GetMapping("/getByEmployeeAndYear")
    public List<EmployeeGoalSetting> getByEmployeeAndYear(@RequestParam Long employeeId, @RequestParam int year) {
        return employeeGoalSettingService.getEmployeeGoalsByYear(employeeId, year);



      
    }
    
    @PutMapping("/updateApprovalStatus")
public String updateApprovalStatus(@RequestParam Long employeeId) {
    return employeeGoalSettingService.updateEmployeeApprovalStatus(employeeId);
}

    
}