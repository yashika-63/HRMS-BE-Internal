package com.spectrum.PerformanceManagement.KpiManagement.controller;

import com.spectrum.PerformanceManagement.KpiManagement.model.EmployeeKpiSetting;
import com.spectrum.PerformanceManagement.KpiManagement.repository.EmployeeKpiSettingRepository;
import com.spectrum.PerformanceManagement.KpiManagement.service.EmployeeKpiSettingService;
import com.spectrum.model.Employee;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kpi")
public class EmployeeKpiSettingController {

    private final EmployeeKpiSettingService employeeKpiSettingService;


    @Autowired
    private EmployeeKpiSettingRepository employeeKpiSettingRepository;

    public EmployeeKpiSettingController(EmployeeKpiSettingService employeeKpiSettingService) {
        this.employeeKpiSettingService = employeeKpiSettingService;
    }

    // @PostMapping("/save/{employeeId}/{reportingManagerId}")
    // public ResponseEntity<EmployeeKpiSetting> saveKpiSetting(
    //         @PathVariable Long employeeId,
    //         @PathVariable Long reportingManagerId,
    //         @RequestBody EmployeeKpiSetting kpiSetting) {

    //     EmployeeKpiSetting savedKpiSetting = employeeKpiSettingService.saveKpiSetting(employeeId, reportingManagerId,
    //             kpiSetting);
    //     return ResponseEntity.ok(savedKpiSetting);
    // }


    @PostMapping("/saveMultiple/{employeeId}/{reportingManagerId}")
public ResponseEntity<List<EmployeeKpiSetting>> saveMultipleKpiSettings(
        @PathVariable Long employeeId,
        @PathVariable Long reportingManagerId,
        @RequestBody List<EmployeeKpiSetting> kpiSettings) {

    List<EmployeeKpiSetting> savedKpiSettings = employeeKpiSettingService.saveMultipleKpiSettings(employeeId, reportingManagerId, kpiSettings);
    return ResponseEntity.ok(savedKpiSettings);
}


@GetMapping("/getByEmployeeAndManager")
    public ResponseEntity<List<EmployeeKpiSetting>> getKpiSettings(@RequestParam Long employeeId, 
                                                                   @RequestParam Long reportingManagerId, 
                                                                   @RequestParam int year) {
        List<EmployeeKpiSetting> kpiSettings = employeeKpiSettingService.getKpiSettingsByEmployeeIdAndReportingManagerIdAndYear(employeeId, reportingManagerId, year);
        return ResponseEntity.ok(kpiSettings);
    }

    @GetMapping("/getByEmployeeAndYear")
    public ResponseEntity<List<EmployeeKpiSetting>> getKpiSettings(@RequestParam Long employeeId, 
                                                                   @RequestParam int year) {
        List<EmployeeKpiSetting> kpiSettings = employeeKpiSettingService.getKpiSettingsByEmployeeIdAndYear(employeeId, year);
        return ResponseEntity.ok(kpiSettings);
    }



    @GetMapping("/getByEmployeeAndYearDetailForReview")
    public ResponseEntity<List<Map<String, Object>>> getKpiSettingsByEmployeeAndYear(
            @RequestParam Long employeeId, @RequestParam int year) {
        List<Map<String, Object>> response = employeeKpiSettingService.getKpiSettingsWithRatings(employeeId, year);
        return ResponseEntity.ok(response);
    }




   @GetMapping("/active-reviewed/{employeeId}")
    public ResponseEntity<List<EmployeeKpiSetting>> getActiveReviewedKpi(@PathVariable Long employeeId) {
        List<EmployeeKpiSetting> kpis = employeeKpiSettingService.getActiveReviewedKpiByEmployeeId(employeeId);

        if (kpis == null || kpis.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(kpis);  // 204 No Content
        }

        return ResponseEntity.ok(kpis);
    }



    ////////////////
    


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployeeKpiSetting(@PathVariable Long id, @RequestBody EmployeeKpiSetting updatedKpi) {
        Optional<EmployeeKpiSetting> optionalKpi = employeeKpiSettingRepository.findById(id);
    
        if (optionalKpi.isPresent()) {
            EmployeeKpiSetting existingKpi = optionalKpi.get();
    
            // Prevent update if employeeApproval is true
            if (existingKpi.isEmployeeApproval()) {
                return ResponseEntity.badRequest().body("Update not allowed: KPI has been approved and cannot be changed.");
            }
    
            // Update only fields that are provided in the request, keeping old values for missing fields
            if (updatedKpi.getKpi() != null) existingKpi.setKpi(updatedKpi.getKpi());
            if (updatedKpi.getDate() != null) existingKpi.setDate(updatedKpi.getDate());
          
    
            // Update boolean fields only if they are different from the existing values
            if (updatedKpi.isEmployeeApproval() != existingKpi.isEmployeeApproval()) existingKpi.setEmployeeApproval(updatedKpi.isEmployeeApproval());
            if (updatedKpi.isStatus()        != existingKpi.isStatus())           existingKpi.setStatus(updatedKpi.isStatus());
            if (updatedKpi.isReviewStatus() != existingKpi.isReviewStatus()) { existingKpi.setReviewStatus(updatedKpi.isReviewStatus());
            }
            
            // Save updated KPI
            employeeKpiSettingRepository.save(existingKpi);
            return ResponseEntity.ok("KPI updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("KPI not found.");
        }
    }


    @PutMapping("/updateKpiApprovalStatus")
    public String updateKpiApprovalStatus(@RequestParam Long employeeId) {
        return employeeKpiSettingService.updateEmployeeKpiApprovalStatus(employeeId);
    }


    @GetMapping("/getKpiByYear")
public List<EmployeeKpiSetting> getKpiByYearAndEmployee(@RequestParam int year, @RequestParam Long employeeId) {
    return employeeKpiSettingService.getKpiByYearAndEmployeeId(year, employeeId);
}

    










}