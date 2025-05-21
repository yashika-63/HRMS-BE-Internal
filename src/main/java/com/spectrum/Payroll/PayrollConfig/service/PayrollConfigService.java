package com.spectrum.Payroll.PayrollConfig.service;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.Payroll.PayrollConfig.model.PayrollConfig;
import com.spectrum.Payroll.PayrollConfig.repository.PayrollConfigRepository;

@Service
public class PayrollConfigService {
    @Autowired
    private PayrollConfigRepository payrollConfigRepository;

    public List<PayrollConfig> saveAllPayrollConfig(List<PayrollConfig> payrollConfig) {
        return payrollConfigRepository.saveAll(payrollConfig);
    }

    public List<PayrollConfig> getPayrollConfigsByCompanyId(Long companyId) {
        return payrollConfigRepository.findByCompanyId(companyId);
    }

    public boolean deletePayrollConfigById(Long id) {
        if (payrollConfigRepository.existsById(id)) {
            payrollConfigRepository.deleteById(id);
            return true;
        }
        return false;
    }
   

    public List<PayrollConfig> getPayrollConfigsByPayrollDate(LocalDate payrollDate) {
    return payrollConfigRepository.findByPayrollDate(payrollDate);
}

 // Method to get all PayrollConfig records
 public List<PayrollConfig> getAllPayrollConfigs() {
    return payrollConfigRepository.findAll();
}

}
