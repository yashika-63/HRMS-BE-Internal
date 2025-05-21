package com.spectrum.Payroll.Payslip.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.Payroll.Payslip.model.PaySlip;
import com.spectrum.Payroll.Payslip.service.PaySlipService;

@RestController
@RequestMapping("/api/PaySlip")

public class PaySlipController {


     @Autowired
    private PaySlipService paySlipService;

      @PostMapping("/generate/{payrollRecordId}")
    public ResponseEntity<String> generatePaySlipsByPayrollRecord(@PathVariable Long payrollRecordId) {
        paySlipService.generatePaySlipsByPayrollRecord(payrollRecordId);
        return ResponseEntity.ok("PaySlips generated successfully for Payroll Record ID: " + payrollRecordId);
    }

        @GetMapping("/employee")                                                                                       
        public ResponseEntity<List<PaySlip>> getPaySlipsByEmployeeIdAndDate(
            @RequestParam Long employeeId,
             @RequestParam int year,
            @RequestParam int month) {
        List<PaySlip> paySlips = paySlipService.getPaySlipsByEmployeeIdAndDate(employeeId, year, month);
        return ResponseEntity.ok(paySlips);
    }

  

}
