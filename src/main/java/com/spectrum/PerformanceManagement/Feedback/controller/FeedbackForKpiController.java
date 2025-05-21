package com.spectrum.PerformanceManagement.Feedback.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.PerformanceManagement.Feedback.model.FeedbackForKpi;
import com.spectrum.PerformanceManagement.Feedback.service.FeedbackForKpiService;
import com.spectrum.PerformanceManagement.KpiManagement.model.EmployeeKpiSetting;
import com.spectrum.PerformanceManagement.KpiManagement.repository.EmployeeKpiSettingRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feedbackkpi")
public class FeedbackForKpiController {

    @Autowired
    private  FeedbackForKpiService feedbackForKpiService;
    @Autowired
    private  EmployeeRepository employeeRepository;
    @Autowired
    private  EmployeeKpiSettingRepository employeeKpiSettingRepository;

    @PostMapping("/save/{reportingManagerId}/{employeeId}/{employeeKpiSettingId}")
    public ResponseEntity<?> saveFeedbackForKpi(
            @PathVariable Long reportingManagerId,
            @PathVariable Long employeeId,
            @PathVariable Long employeeKpiSettingId,
            @RequestBody FeedbackForKpi feedbackForKpi) {

        Optional<Employee> reportingManager = employeeRepository.findById(reportingManagerId);
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        Optional<EmployeeKpiSetting> employeeKpiSetting = employeeKpiSettingRepository.findById(employeeKpiSettingId);

        if (reportingManager.isEmpty() || employee.isEmpty() || employeeKpiSetting.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid IDs provided");
        }

        feedbackForKpi.setEmployees(reportingManager.get());
        feedbackForKpi.setEmployee(employee.get());
        feedbackForKpi.setEmployeeKpiSetting(employeeKpiSetting.get());

        FeedbackForKpi savedFeedback = feedbackForKpiService.saveFeedbackForKpi(feedbackForKpi);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFeedback);
    }




  

    // @PostMapping("/save-multiple/{reportingManagerId}/{employeeId}")
    // public ResponseEntity<?> saveMultipleFeedbackForKpi(
    //         @PathVariable Long reportingManagerId,
    //         @PathVariable Long employeeId,
    //         @RequestBody List<FeedbackForKpi> feedbackList) {

    //     Optional<Employee> reportingManager = employeeRepository.findById(reportingManagerId);
    //     Optional<Employee> employee = employeeRepository.findById(employeeId);

    //     if (reportingManager.isEmpty() || employee.isEmpty()) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid reportingManagerId or employeeId");
    //     }

    //     for (FeedbackForKpi feedback : feedbackList) {
    //         Optional<EmployeeKpiSetting> employeeKpiSetting = employeeKpiSettingRepository.findById(feedback.getEmployeeKpiSetting().getId());

    //         if (employeeKpiSetting.isEmpty()) {
    //             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid employeeKpiSettingId: " + feedback.getEmployeeKpiSetting().getId());
    //         }

    //         feedback.setEmployees(reportingManager.get());
    //         feedback.setEmployee(employee.get());
    //         feedback.setEmployeeKpiSetting(employeeKpiSetting.get());
    //     }

    //     List<FeedbackForKpi> savedFeedbackList = feedbackForKpiService.saveMultipleFeedbackForKpi(feedbackList);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(savedFeedbackList);
    // }



    @PostMapping("/save-multiple/{reportingManagerId}/{employeeId}")
    public ResponseEntity<?> saveMultipleFeedbackForKpi(
            @PathVariable Long reportingManagerId,
            @PathVariable Long employeeId,
            @RequestBody List<FeedbackForKpi> feedbackList) {
    
        Optional<Employee> reportingManager = employeeRepository.findById(reportingManagerId);
        Optional<Employee> employee = employeeRepository.findById(employeeId);
    
        if (reportingManager.isEmpty() || employee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid reportingManagerId or employeeId");
        }
    
        for (FeedbackForKpi feedback : feedbackList) {
            Optional<EmployeeKpiSetting> employeeKpiSettingOpt = employeeKpiSettingRepository.findById(feedback.getEmployeeKpiSetting().getId());
    
            if (employeeKpiSettingOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid employeeKpiSettingId: " + feedback.getEmployeeKpiSetting().getId());
            }
    
            EmployeeKpiSetting employeeKpiSetting = employeeKpiSettingOpt.get();
    
            // Set necessary fields in feedback
            feedback.setEmployees(reportingManager.get());
            feedback.setEmployee(employee.get());
            feedback.setEmployeeKpiSetting(employeeKpiSetting);
    
            // Update reviewStatus to false
            employeeKpiSetting.setReviewStatus(false);
            employeeKpiSettingRepository.save(employeeKpiSetting);
        }
    
        List<FeedbackForKpi> savedFeedbackList = feedbackForKpiService.saveMultipleFeedbackForKpi(feedbackList);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFeedbackList);
    }
    
}
