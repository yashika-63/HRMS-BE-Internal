package com.spectrum.Payroll.PayrollProcessed.repository;

import com.spectrum.Payroll.PayrollProcessed.model.PayrollProcessed;
import com.spectrum.Payroll.PayrollRecord.model.PayrollRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface payrollProcessedRepository extends JpaRepository<PayrollProcessed, Long> {

    @Query("SELECT p FROM PayrollProcessed p WHERE FUNCTION('MONTH', p.date) = :month AND FUNCTION('YEAR', p.date) = :year AND p.employee.companyRegistration.id = :companyId")
    List<PayrollProcessed> findByMonthYearAndCompanyId(int month, int year, Long companyId);

    @Query("SELECT p FROM PayrollProcessed p WHERE FUNCTION('MONTH', p.date) = :month AND FUNCTION('YEAR', p.date) = :year AND p.employee.companyRegistration.id = :companyId AND p.paymentPaidStatus = :status")
    List<PayrollProcessed> findByMonthYearAndCompanyIdAndPaymentStatus(int month, int year, Long companyId, boolean status);


}
