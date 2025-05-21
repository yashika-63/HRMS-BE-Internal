package com.spectrum.PerformanceManagement.Feedback.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.PerformanceManagement.Feedback.model.FeedBackGoalSetting;
import com.spectrum.PerformanceManagement.Feedback.service.FeedBackGoalSettingService;
import com.spectrum.PerformanceManagement.GoalSetting.model.EmployeeGoalSetting;
import com.spectrum.PerformanceManagement.GoalSetting.repository.EmployeeGoalSettingRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

 @RestController
@RequestMapping("/api/feedback")
public class FeedBackGoalSettingController {

    @Autowired
    private  FeedBackGoalSettingService feedBackGoalSettingService;
   
    @Autowired
    private  EmployeeRepository employeeRepository;
    
    @Autowired
    private  EmployeeGoalSettingRepository employeeGoalSettingRepository;

   
    @PostMapping("/save-multiple/{reportingManagerId}/{employeeId}")
public ResponseEntity<?> saveMultipleFeedbackForGoal(
        @PathVariable Long reportingManagerId,
        @PathVariable Long employeeId,
        @RequestBody List<FeedBackGoalSetting> feedbackList) {

    Optional<Employee> reportingManager = employeeRepository.findById(reportingManagerId);
    Optional<Employee> employee = employeeRepository.findById(employeeId);

    if (reportingManager.isEmpty() || employee.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid reportingManagerId or employeeId");
    }

    for (FeedBackGoalSetting feedback : feedbackList) {
        Optional<EmployeeGoalSetting> employeeGoalSettingOpt = 
                employeeGoalSettingRepository.findById(feedback.getEmployeeGoalSetting().getId());

        if (employeeGoalSettingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invalid employeeGoalSettingId: " + feedback.getEmployeeGoalSetting().getId());
        }

        EmployeeGoalSetting employeeGoalSetting = employeeGoalSettingOpt.get();

        // Update reviewStatus to false
        employeeGoalSetting.setReviewStatus(false);
        employeeGoalSettingRepository.save(employeeGoalSetting);

        feedback.setEmployees(reportingManager.get());
        feedback.setEmployee(employee.get());
        feedback.setEmployeeGoalSetting(employeeGoalSetting);
    }

    List<FeedBackGoalSetting> savedFeedbackList = feedBackGoalSettingService.saveMultipleFeedbackForGoal(feedbackList);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedFeedbackList);
}

    
}
