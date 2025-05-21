package com.spectrum.Payroll.IncomeTax.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.Payroll.IncomeTax.model.EmployeeIncomeTax;
import com.spectrum.Payroll.IncomeTax.service.EmployeeIncomeTaxService;

@RestController
@RequestMapping("/api/incomeTax")
public class EmployeeIncomeTaxController {

    @Autowired
    private EmployeeIncomeTaxService employeeIncomeTaxService ;


    @Autowired
    private EmployeeIncomeTaxService employeeIncomeTaxService2;

    @PostMapping("/save")
    public ResponseEntity<EmployeeIncomeTax> saveIncomeTax(
            @RequestParam Long employeeId,
            @RequestParam Long ctcBreakdownId,
            @RequestParam double incomeTaxAmount) {

        EmployeeIncomeTax saved = employeeIncomeTaxService2.saveIncomeTax(employeeId, ctcBreakdownId, incomeTaxAmount);
        return ResponseEntity.ok(saved);
    }




}
