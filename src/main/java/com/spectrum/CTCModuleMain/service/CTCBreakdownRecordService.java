package com.spectrum.CTCModuleMain.service;

import com.spectrum.CTCModuleMain.model.CTCBreakdownRecord;
import com.spectrum.CTCModuleMain.repository.CTCBreakdownRecordRepository;
import com.spectrum.Payroll.PayrollRecord.model.PayrollRecord;
import com.spectrum.Payroll.PayrollRecord.repository.PayrollRecordRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class CTCBreakdownRecordService {

    private final CTCBreakdownRecordRepository ctcBreakdownRecordRepository;
    private final PayrollRecordRepository payrollRecordRepository;

    @Autowired
    
    public CTCBreakdownRecordService(CTCBreakdownRecordRepository ctcBreakdownRecordRepository,
            PayrollRecordRepository payrollRecordRepository) {
        this.ctcBreakdownRecordRepository = ctcBreakdownRecordRepository;
        this.payrollRecordRepository = payrollRecordRepository;
    }

    // Method to store Payroll Records for a company
    public void storePayrollRecordsForCompany(Long companyId) {
        List<CTCBreakdownRecord> activeRecords = ctcBreakdownRecordRepository.findActiveRecordsByCompanyId(companyId);

        for (CTCBreakdownRecord ctcRecord : activeRecords) {
            PayrollRecord payrollRecord = new PayrollRecord();
            payrollRecord.setDate(LocalDate.now());
            payrollRecord.setStaticAmount(ctcRecord.getStaticAmount());
            payrollRecord.setVariableAmount(ctcRecord.getVariableAmount());
            payrollRecord.setVariableAmountPerHour(ctcRecord.getVariableAmountPerHour());
            payrollRecord.setStatus(ctcRecord.isStatus());
            payrollRecord.setEmployee(ctcRecord.getEmployee());
            payrollRecord.setCompany(ctcRecord.getEmployee().getCompanyRegistration());

            payrollRecordRepository.save(payrollRecord);
        }










        /////////////////
        



        
    }









}
