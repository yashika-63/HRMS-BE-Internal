package com.spectrum.Payroll.PayrollConfig.controller;

import com.spectrum.Payroll.PayrollConfig.model.PayrollConfig;
import com.spectrum.Payroll.PayrollConfig.service.PayrollConfigService;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payrollConfig")
public class PayrollConfigController {

    @Autowired
    private PayrollConfigService payrollConfigService;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    // Save multiple PayrollConfig by companyId
    @PostMapping("/saveMultiple/{companyId}")
    public ResponseEntity<?> saveMultiplePayrollConfigs(
            @PathVariable Long companyId,
            @RequestBody List<PayrollConfig> payrollConfigList) {

        Optional<CompanyRegistration> company = companyRegistrationRepository.findById(companyId);

        if (!company.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company with id " + companyId + " not found.");
        }

        for (PayrollConfig config : payrollConfigList) {
            config.setCompany(company.get()); // Set the company for each PayrollConfig record
        }

        List<PayrollConfig> savedConfigs = payrollConfigService.saveAllPayrollConfig(payrollConfigList);
        return ResponseEntity.ok(savedConfigs);
    }

    // Get PayrollConfig by companyId
    @GetMapping("/getByCompanyId/{companyId}")
    public ResponseEntity<?> getPayrollConfigsByCompanyId(@PathVariable Long companyId) {
        List<PayrollConfig> payrollConfigs = payrollConfigService.getPayrollConfigsByCompanyId(companyId);
        if (payrollConfigs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No payroll configurations found for companyId: " + companyId);
        }
        return ResponseEntity.ok(payrollConfigs);
    }

    // Delete PayrollConfig by id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePayrollConfigById(@PathVariable Long id) {
        boolean isDeleted = payrollConfigService.deletePayrollConfigById(id);
        if (isDeleted) {
            return ResponseEntity.ok("PayrollConfig with id " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PayrollConfig with id " + id + " not found.");
        }
    }
}
