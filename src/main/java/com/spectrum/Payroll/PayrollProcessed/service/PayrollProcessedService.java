package com.spectrum.Payroll.PayrollProcessed.service;

import com.spectrum.Payroll.PayrollProcessed.model.PayrollProcessed;
import com.spectrum.Payroll.PayrollProcessed.repository.payrollProcessedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayrollProcessedService {

    private final payrollProcessedRepository payrollProcessedRepository;

    @Autowired
    public PayrollProcessedService(payrollProcessedRepository payrollProcessedRepository) {
        this.payrollProcessedRepository = payrollProcessedRepository;
    }

    public List<PayrollProcessed> getByMonthYearAndCompanyId(int month, int year, Long companyId) {
        return payrollProcessedRepository.findByMonthYearAndCompanyId(month, year, companyId);
    }

    public List<PayrollProcessed> getByMonthYearCompanyIdAndStatus(int month, int year, Long companyId, boolean status) {
        return payrollProcessedRepository.findByMonthYearAndCompanyIdAndPaymentStatus(month, year, companyId, status);
    }

    public boolean updatePaymentStatusToPaid(Long id) {
        return payrollProcessedRepository.findById(id).map(payrollProcessed -> {
            payrollProcessed.setPaymentPaidStatus(true); // Always set to 'true'
            payrollProcessedRepository.save(payrollProcessed);
            return true;
        }).orElse(false);
    }

    public boolean deletePayrollProcessedIfUnpaid(Long id) {
        return payrollProcessedRepository.findById(id).map(payrollProcessed -> {
            if (!payrollProcessed.isPaymentPaidStatus()) {
                payrollProcessedRepository.deleteById(id);
                return true;
            }
            return false;
        }).orElse(false);
    }
}
