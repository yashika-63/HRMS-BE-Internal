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

import com.spectrum.model.BankDetails;
import com.spectrum.model.Employee;
import com.spectrum.service.BankDetailsService;
import com.spectrum.service.EmployeeService;

@RestController
// @CrossOrigin("http://localhost:3000")
@RequestMapping("/BankDetails")

public class BankDetailsController {

    @Autowired
    private BankDetailsService bankDetailsService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/saveForEmployee")
    public ResponseEntity<String> saveBankDetailsForEmployee(@RequestBody List<BankDetails> bankDetails,
                                                             @RequestParam Long employeeId) {
        try {
            // Check if the employee exists
            Employee employee = employeeService.getById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

            for (BankDetails bankDetail : bankDetails) {
                // Associate the bank details with the employee
                bankDetail.setEmployee(employee);

                // Save the bank details
                bankDetailsService.saveBankDetails(bankDetail);
            }

            return ResponseEntity.ok("Bank details saved successfully for employee with ID: " + employeeId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save bank details for employee with ID: " + employeeId);
        }
    }

    // Get BankDetails by ID
    @GetMapping("/{id}")
    public ResponseEntity<BankDetails> getBankDetailsById(@PathVariable Long id) {
        BankDetails bankDetails = bankDetailsService.getBankDetailsById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bank details not found with ID: " + id));
        return ResponseEntity.ok(bankDetails);
    }

    // Update BankDetails by ID
    @PutMapping("/{id}")
    public ResponseEntity<BankDetails> updateBankDetailsById(@PathVariable Long id,
            @RequestBody BankDetails updatedBankDetails) {
        BankDetails bankDetails = bankDetailsService.updateBankDetails(id, updatedBankDetails);
        return ResponseEntity.ok(bankDetails);
    }

    // Delete BankDetails by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBankDetailsById(@PathVariable Long id) {
        bankDetailsService.deleteBankDetails(id);
        return ResponseEntity.ok("Bank details deleted successfully with ID: " + id);
    }

    @GetMapping("/getByEmployeeId")
    public ResponseEntity<List<BankDetails>> getBankDetailsByEmployeeId(@RequestParam Long employeeId) {
        List<BankDetails> bankDetails = bankDetailsService.getBankDetailsByEmployeeId(employeeId);
        if (bankDetails.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bankDetails);
    }

}
