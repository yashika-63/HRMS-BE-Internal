package com.spectrum.Payroll.IncomeTax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.CTCModuleMain.model.CTCBreakdownHeader;
import com.spectrum.CTCModuleMain.repository.CTCBreakdownHeaderRepository;
import com.spectrum.Payroll.IncomeTax.model.EmployeeIncomeTax;
import com.spectrum.Payroll.IncomeTax.repository.EmployeeIncomeTaxRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;

@Service
public class EmployeeIncomeTaxService {


    @Autowired
    private EmployeeIncomeTaxRepository incomeTaxRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private CTCBreakdownHeaderRepository ctcBreakdownRepo;


      public EmployeeIncomeTax saveIncomeTax(Long employeeId, Long ctcBreakdownId, double incomeTaxAmount) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        CTCBreakdownHeader ctc = ctcBreakdownRepo.findById(ctcBreakdownId)
                .orElseThrow(() -> new RuntimeException("CTC Breakdown not found"));

        // Make previous records false
        List<EmployeeIncomeTax> oldRecords = incomeTaxRepo.findByEmployeeIdAndStatusTrue(employeeId);
        for (EmployeeIncomeTax record : oldRecords) {
            record.setStatus(false);
        }
        incomeTaxRepo.saveAll(oldRecords);

        // Save new record
        EmployeeIncomeTax newTax = new EmployeeIncomeTax();
        newTax.setEmployee(employee);
        newTax.setCtcBreakdownHeader(ctc);
        newTax.setIncomeTaxDeduction(incomeTaxAmount);
        newTax.setStatus(true);

        return incomeTaxRepo.save(newTax);
    }
}
