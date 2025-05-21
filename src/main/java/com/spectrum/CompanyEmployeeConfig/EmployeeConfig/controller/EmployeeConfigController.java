package com.spectrum.CompanyEmployeeConfig.EmployeeConfig.controller;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.EmployeeConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.service.EmployeeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee-config")
public class EmployeeConfigController {

    @Autowired
    private EmployeeConfigService employeeConfigService;

    // Save EmployeeConfig mapped to an employee ID (pass employeeId in URL)
    @PostMapping("/{employeeId}")
    public ResponseEntity<Map<String, Object>> saveEmployeeConfig(
            @PathVariable Long employeeId,
            @RequestBody EmployeeConfig employeeConfig) {
    
        Map<String, Object> response = employeeConfigService.saveEmployeeConfig(employeeId, employeeConfig);
        return ResponseEntity.ok(response);
    }
    

    // Get EmployeeConfig records by employee ID
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeConfig>> getByEmployeeId(@PathVariable Long employeeId) {
        List<EmployeeConfig> configs = employeeConfigService.getByEmployeeId(employeeId);
        if (configs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(configs);
    }

    // Delete EmployeeConfig by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeConfig(@PathVariable Long id) {
        employeeConfigService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
public ResponseEntity<EmployeeConfig> updateEmployeeConfig(
        @PathVariable Long id,
        @RequestBody EmployeeConfig updatedConfig) {
    EmployeeConfig savedConfig = employeeConfigService.updateEmployeeConfig(id, updatedConfig);
    return ResponseEntity.ok(savedConfig);
}




// @GetMapping("/by-reporting-manager/{reportingManagerId}")
// public ResponseEntity<EmployeeConfig> getByReportingManager(@PathVariable Long reportingManagerId) {
//     Optional<EmployeeConfig> employeeConfig = employeeConfigService.getEmployeeConfigByReportingManager(reportingManagerId);
//     return employeeConfig.map(ResponseEntity::ok)
//             .orElseGet(() -> ResponseEntity.notFound().build());
// }

@GetMapping("/by-reporting-manager/{reportingManagerId}")
public ResponseEntity<List<EmployeeConfig>> getByReportingManager(@PathVariable Long reportingManagerId) {
    List<EmployeeConfig> employeeConfigs = employeeConfigService.getEmployeeConfigByReportingManager(reportingManagerId);
    
    if (employeeConfigs.isEmpty()) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(employeeConfigs);
}




    @PostMapping("/save/{employeeId}/{reportingManagerId}")
    public ResponseEntity<EmployeeConfig> saveEmployeeConfig(
            @PathVariable Long employeeId,
            @PathVariable Long reportingManagerId,
            @RequestBody EmployeeConfig employeeConfig) {

        EmployeeConfig savedConfig = employeeConfigService.saveEmployeeConfig(employeeId, reportingManagerId, employeeConfig);
        return ResponseEntity.ok(savedConfig);
    }









    /////
    /// 
    /// 
    /// Confirmation Api's 
    /// 
    /// 
    


    @GetMapping("/check-probation")
    public ResponseEntity<String> checkProbation() {
        employeeConfigService.checkProbationEndApproaching();

        return ResponseEntity.ok("Probation check executed successfully.");
    }









    

}


