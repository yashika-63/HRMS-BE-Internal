package com.spectrum.Payroll.Payslip.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.CTCModuleMain.model.CTCBreakdownHeader;
import com.spectrum.CTCModuleMain.model.StaticCTCBreakdown;
import com.spectrum.CTCModuleMain.model.VariableCTCBreakdown;
import com.spectrum.CTCModuleMain.repository.CTCBreakdownHeaderRepository;
import com.spectrum.Payroll.PayrollRecord.model.PayrollRecord;
import com.spectrum.Payroll.PayrollRecord.repository.PayrollRecordRepository;
import com.spectrum.Payroll.Payslip.model.PaySlip;
import com.spectrum.Payroll.Payslip.repository.PaySlipRepository;
import com.spectrum.model.Employee;
@Service
public class PaySlipService {

    @Autowired
    private CTCBreakdownHeaderRepository ctcBreakdownHeaderRepository;

    @Autowired
    private PayrollRecordRepository payrollRecordRepository;

    @Autowired
    private PaySlipRepository paySlipRepository;

    public void generatePaySlipsByPayrollRecord(Long payrollRecordId) {
        // Fetch the payroll record
        PayrollRecord payrollRecord = payrollRecordRepository.findById(payrollRecordId)
                .orElseThrow(() -> new RuntimeException("Payroll Record not found."));

        // Extract required fields from PayrollRecord
        int totalWorkingHours = payrollRecord.getTotalWorkingHours();
        // int actualHours = payrollRecord.getActualHours();
        Employee employee = payrollRecord.getEmployee();
        double overtimeRate = payrollRecord.getOvertimeRate();
        double overtimeHours = payrollRecord.getOvertimeHours();
        int dailyWorkingHours = payrollRecord.getDailyWorkingHours();
        int actualHours = payrollRecord.getActualHours() != null ? payrollRecord.getActualHours() : 0;
        // int totalWorkingHours = payrollRecord.getTotalWorkingHours();
        double variableAmount = payrollRecord.getVariableAmount();
        // double variableAmountPerHour = payrollRecord.getVariableAmountPerHour();
        double staticAmount = payrollRecord.getStaticAmount();
        // double overtimeHours = payrollRecord.getOvertimeHours(); // Retrieve the total overtime hours
        // double overtimeRate = payrollRecord.getOvertimeRate(); // Retrieve the rate for overtime hours
        double basicAmount = payrollRecord.getBasicAmount(); 
        double incomeTaxDeduction = payrollRecord.getIncomeTaxDeduction();

        // Fetch the active CTC Breakdown Header for the employee
        Optional<CTCBreakdownHeader> ctcHeaderOpt = ctcBreakdownHeaderRepository
                .findByEmployeeIdAndCtcStatusTrue(employee.getId());

        if (ctcHeaderOpt.isEmpty()) {
            throw new RuntimeException("No active CTC Breakdown found for the employee.");
        }

        CTCBreakdownHeader ctcHeader = ctcHeaderOpt.get();

        // Process Static CTC Breakdown
        List<StaticCTCBreakdown> staticBreakdowns = ctcHeader.getStaticCTCBreakdowns();
        for (StaticCTCBreakdown staticBreakdown : staticBreakdowns) {
            PaySlip paySlip = new PaySlip();
            paySlip.setActualWorkingHours(actualHours);
            paySlip.setTotalWorkingHours(totalWorkingHours);
            paySlip.setLable(staticBreakdown.getLabel());
            paySlip.setAmount(staticBreakdown.getAmount() / 12.0); // Monthly amount
            paySlip.setType(false); // Static breakdown
            // paySlip.setNetSalary(0); // Update logic if required
            paySlip.setOvertimeAmount(0); // Update logic if required
            paySlip.setPayrollRecord(payrollRecord);
            paySlip.setNetSalary((actualHours / dailyWorkingHours * variableAmount) + staticAmount +(basicAmount*overtimeRate*overtimeHours)); // Update logic if required
            paySlip.setIncomeTaxDeduction(incomeTaxDeduction);
            paySlip.setEmployee(employee);
            

            paySlipRepository.save(paySlip);
        }

        // Process Variable CTC Breakdown
        List<VariableCTCBreakdown> variableBreakdowns = ctcHeader.getVariableCTCBreakdowns();
        for (VariableCTCBreakdown variableBreakdown : variableBreakdowns) {
            PaySlip paySlip = new PaySlip();
            paySlip.setActualWorkingHours(actualHours);
            paySlip.setTotalWorkingHours(totalWorkingHours);
            paySlip.setLable(variableBreakdown.getLabel());
            paySlip.setAmount((variableBreakdown.getAmount() / (12.0 * totalWorkingHours))*actualHours); // Monthly variable amount
            paySlip.setType(true); // Variable breakdown
            paySlip.setNetSalary((actualHours / dailyWorkingHours * variableAmount) + staticAmount +(basicAmount*overtimeRate*overtimeHours)); // Update logic if required
            paySlip.setOvertimeAmount((overtimeHours*overtimeRate)); // Update logic if required
            paySlip.setIncomeTaxDeduction(incomeTaxDeduction);
            paySlip.setPayrollRecord(payrollRecord);
            paySlip.setEmployee(employee);

            paySlipRepository.save(paySlip);
        }
    }

    public List<PaySlip> getPaySlipsByEmployeeIdAndDate(Long employeeId, int year, int month) {
        return paySlipRepository.findByEmployeeIdAndYearAndMonth(employeeId, year, month);
    }
    
}
