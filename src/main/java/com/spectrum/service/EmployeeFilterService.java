package com.spectrum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.EmployeeConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.EmployeeConfigRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;

@Service
public class EmployeeFilterService {
    @Autowired
    private EmployeeConfigRepository empRepo;

    //  public List<Employee> filterEmployees(String designation, String region, String department) {
    //     return empRepo.findFilteredEmployees(designation, region, department);
    // }
    public List<EmployeeConfig> getFilteredConfigs(Long companyId, Integer designationId, Integer departmentId, Integer regionId) {
        return empRepo.findFilteredEmployeeConfigs(companyId, designationId, departmentId, regionId);
    }


    
}
