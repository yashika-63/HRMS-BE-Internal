package com.spectrum.Payroll.PayrollHours.controller;

import com.spectrum.Payroll.PayrollHours.model.PayrollHours;
import com.spectrum.Payroll.PayrollHours.repository.PayrollHoursRepository;
import com.spectrum.Payroll.PayrollHours.service.PayrollHoursService;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payroll-hours")
public class PayrollHoursController {

    @Autowired
    private PayrollHoursService payrollHoursService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PayrollHoursRepository payrollHoursRepository;

    // Save multiple PayrollHours records
    @PostMapping("/save")
    public ResponseEntity<?> savePayrollHours(@RequestBody List<PayrollHours> payrollHoursList) {
        for (PayrollHours payrollHours : payrollHoursList) {
            Long employeeId = payrollHours.getEmployee().getId();

            // Check if the employee exists
            Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
            if (employeeOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Employee with ID " + employeeId + " does not exist.");
            }

            Employee employee = employeeOpt.get();
            payrollHours.setEmployee(employee);

            // Update the status of existing records for this employee to false
            List<PayrollHours> existingRecords = payrollHoursRepository.findByEmployeeId(employeeId);
            for (PayrollHours existingRecord : existingRecords) {
                if (existingRecord.getStatus()) {
                    existingRecord.setStatus(false);
                    payrollHoursRepository.save(existingRecord);
                }
            }

            // Optional: Uncomment if you want to prevent duplicate records for the same month
            // LocalDateTime now = LocalDateTime.now();
            // boolean recordExists = payrollHoursRepository.existsByEmployeeIdAndMonth(employeeId, now.getMonthValue(), now.getYear());
            // if (recordExists) {
            //     return ResponseEntity.status(HttpStatus.CONFLICT)
            //             .body("A PayrollHours record for employee ID " + employeeId + " already exists for this month.");
            // }
        }

        // Save all payroll records
        List<PayrollHours> savedRecords = payrollHoursService.savePayrollHoursList(payrollHoursList);
        return ResponseEntity.ok(savedRecords);
    }

    // Get PayrollHours data by employeeId
    @GetMapping("/employee/{employeeId}")
    public List<PayrollHours> getPayrollHoursByEmployeeId(@PathVariable Long employeeId) {
        return payrollHoursService.getPayrollHoursByEmployeeId(employeeId);
    }

    // Update PayrollHours data by id
    @PutMapping("/update/{id}")
    public PayrollHours updatePayrollHours(@PathVariable Long id, @RequestBody PayrollHours payrollHours) {
        return payrollHoursService.updatePayrollHours(id, payrollHours);
    }

    // Delete PayrollHours data by id
    @DeleteMapping("/delete/{id}")
    public String deletePayrollHours(@PathVariable Long id) {
        payrollHoursService.deletePayrollHours(id);
        return "PayrollHours record deleted successfully!";
    }

    // Store PayrollHours records for a specific company
    @PostMapping("/store/{companyId}")
    public ResponseEntity<String> storePayrollHours(@PathVariable Long companyId) {
        try {
            payrollHoursService.storePayrollHoursForCompany(companyId);
            return ResponseEntity.ok("Payroll hours records stored successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error storing payroll hours records.");
        }
    }

    // Get PayrollHours records by companyId and month
    @GetMapping("/byCompanyAndMonth")
    public List<PayrollHours> getPayrollHoursByCompanyAndMonth(
            @RequestParam Long companyId, @RequestParam int month) {
        return payrollHoursService.getPayrollHoursByCompanyAndMonth(companyId, month);
    }


    // Get PayrollHours records by companyId, month, and year
    @GetMapping("/byCompanyAndMonthAndYear")
    public List<PayrollHours> getPayrollHoursByCompanyAndMonthAndYear(
            @RequestParam Long companyId,
            @RequestParam int month,
            @RequestParam int year) {
        return payrollHoursService.getPayrollHoursByCompanyAndMonthAndYear(companyId, month, year);
    }




    @PostMapping("/storeMain/{companyId}")
    public ResponseEntity<String> storePayrollHours(
            @PathVariable Long companyId,
            @RequestBody List<PayrollHours> payrollHoursList) {
        try {
            // Validate input
            if (companyId == null || payrollHoursList == null || payrollHoursList.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid input data.");
            }
    
            // Call the service method
            payrollHoursService.storePayrollHoursForCompany(companyId, payrollHoursList);
            return ResponseEntity.ok("Payroll hours records stored successfully.");
        } catch (Exception e) {
            // Log the error for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error storing payroll hours records.");
        }
    }
    
}

