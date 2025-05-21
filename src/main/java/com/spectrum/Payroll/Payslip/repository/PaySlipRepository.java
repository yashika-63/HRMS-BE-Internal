package com.spectrum.Payroll.Payslip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spectrum.Payroll.Payslip.model.PaySlip;

@Repository
public interface PaySlipRepository  extends JpaRepository<PaySlip, Long> {
    @Query("SELECT p FROM PaySlip p WHERE p.employee.id = :employeeId AND YEAR(p.payrollRecord.date) = :year AND MONTH(p.payrollRecord.date) = :month")
    List<PaySlip> findByEmployeeIdAndYearAndMonth(Long employeeId, int year, int month);
}
