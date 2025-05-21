package com.spectrum.Payroll.PayrollConfig.repository;

import com.spectrum.Payroll.PayrollConfig.model.PayrollConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PayrollConfigRepository extends JpaRepository<PayrollConfig, Long> {
    List<PayrollConfig> findByCompanyId(Long companyId);

    List<PayrollConfig> findByPayrollDate(LocalDate payrollDate);

}
