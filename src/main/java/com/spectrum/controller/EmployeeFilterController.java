package com.spectrum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.EmployeeConfig;
import com.spectrum.model.Employee;
import com.spectrum.service.EmployeeFilterService;
@RestController
@RequestMapping("/employees")

public class EmployeeFilterController {
    @Autowired
    private EmployeeFilterService empservice;


    // @GetMapping("/filter")   
    // public ResponseEntity<List<Employee>> filterEmployees(
    //         @RequestParam(required = false) String designation,
    //         @RequestParam(required = false) String region,
    //         @RequestParam(required = false) String department) {
    //     List<Employee> employees = empservice.filterEmployees(designation, region, department);
    //     return ResponseEntity.ok(employees);
    // }

    @GetMapping("/filter")
    public ResponseEntity<List<EmployeeConfig>> filterEmployeeConfigs(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Integer designationId,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer regionId
    ) {
        List<EmployeeConfig> filteredConfigs = empservice.getFilteredConfigs(companyId, designationId, departmentId, regionId);
        return ResponseEntity.ok(filteredConfigs);
    }
}