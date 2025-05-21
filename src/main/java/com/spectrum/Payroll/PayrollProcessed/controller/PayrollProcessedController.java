package com.spectrum.Payroll.PayrollProcessed.controller;

import com.spectrum.Payroll.PayrollProcessed.model.PayrollProcessed;
import com.spectrum.Payroll.PayrollProcessed.service.PayrollProcessedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payrollProcessed")
public class PayrollProcessedController {

    private final PayrollProcessedService payrollProcessedService;

    @Autowired
    public PayrollProcessedController(PayrollProcessedService payrollProcessedService) {
        this.payrollProcessedService = payrollProcessedService;
    }

    @GetMapping("/byMonthYearCompanyAndStatus")
    public ResponseEntity<List<PayrollProcessed>> getByMonthYearCompanyIdAndStatus(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam Long companyId,
            @RequestParam boolean status) {
        List<PayrollProcessed> payrollProcessed =
                payrollProcessedService.getByMonthYearCompanyIdAndStatus(month, year, companyId, status);
        return ResponseEntity.ok(payrollProcessed);
    }

    @GetMapping("/byCurrentMonthYearAndCompanyPaid")
    public ResponseEntity<List<PayrollProcessed>> getPaidPayrollsForCurrentMonth(
            @RequestParam int year,
            @RequestParam Long companyId) {
        int currentMonth = LocalDate.now().getMonthValue(); // Get the current month
        List<PayrollProcessed> payrollProcessed =
                payrollProcessedService.getByMonthYearCompanyIdAndStatus(currentMonth, year, companyId, true);
        return ResponseEntity.ok(payrollProcessed);
    }

    @GetMapping("/byCurrentMonthYearAndCompanyUnpaid")
    public ResponseEntity<List<PayrollProcessed>> getUnpaidPayrollsForCurrentMonth(
            @RequestParam int year,
            @RequestParam Long companyId) {
        int currentMonth = LocalDate.now().getMonthValue(); // Get the current month
        List<PayrollProcessed> payrollProcessed =
                payrollProcessedService.getByMonthYearCompanyIdAndStatus(currentMonth, year, companyId, false);
        return ResponseEntity.ok(payrollProcessed);
    }

    @PutMapping("/updatePaymentStatus/{id}")
    public ResponseEntity<String> updatePaymentStatusToPaid(@PathVariable Long id) {
        boolean isUpdated = payrollProcessedService.updatePaymentStatusToPaid(id);
        if (isUpdated) {
            return ResponseEntity.ok("Payment status updated to 'paid' successfully.");
        } else {
            return ResponseEntity.status(404).body("PayrollProcessed with the specified ID not found.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePayrollProcessedIfUnpaid(@PathVariable Long id) {
        boolean isDeleted = payrollProcessedService.deletePayrollProcessedIfUnpaid(id);
        if (isDeleted) {
            return ResponseEntity.ok("PayrollProcessed record deleted successfully.");
        } else {
            return ResponseEntity.status(400).body("PayrollProcessed record not deleted. Either it doesn't exist or the paymentPaidStatus is true.");
        }
    }
}
