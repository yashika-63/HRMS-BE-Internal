package com.spectrum.Payroll.PayrollRecord.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.Payroll.PayrollRecord.model.PayrollRecord;

@Repository
public interface PayrollRecordRepository extends JpaRepository<PayrollRecord, Long> {


    List<PayrollRecord> findByCompanyIdAndStatusFalseAndProceedForPayrollStatusFalse(Long companyId);


    List<PayrollRecord> findByProceedForPayrollStatusTrue();
    
    List<PayrollRecord> findByCompanyIdAndStatusTrueAndProceedForPayrollStatusFalse(Long companyId);
    List<PayrollRecord> findByCompanyIdAndStatusTrueAndProceedForPayrollStatusTrue(Long companyId);
    List<PayrollRecord> findByCompanyIdAndProceedForPayrollStatusTrue(Long companyId);



    @Modifying
    @Query("UPDATE PayrollRecord p SET p.proceedForPayrollStatus = true WHERE p.id = :id")
    int updateProceedForPayrollStatusById(@Param("id") Long id);


    List<PayrollRecord> findByCompanyIdAndProceedForPayrollStatusTrueAndProceedForPaymentFalse(Long companyId);


    @Query("SELECT p FROM PayrollRecord p WHERE p.company.id = :companyId AND p.status = true AND p.proceedForPayrollStatus = true AND YEAR(p.date) = :year AND MONTH(p.date) = :month")
    List<PayrollRecord> findByCompanyIdAndStatusTrueAndProceedForPayrollStatusTrueAndYearAndMonth(
            @Param("companyId") Long companyId,
            @Param("year") int year,
            @Param("month") int month);

            @Query("SELECT p FROM PayrollRecord p WHERE p.company.id = :companyId AND p.status = true AND p.proceedForPayrollStatus = false AND YEAR(p.date) = :year AND MONTH(p.date) = :month")
    List<PayrollRecord> findByCompanyIdAndStatusTrueAndProceedForPayrollStatusFalseAndYearAndMonth(
            @Param("companyId") Long companyId,
            @Param("year") int year,
            @Param("month") int month);
    


            //

            @Query("SELECT p FROM PayrollRecord p WHERE p.company.id = :companyId " +
            "AND p.proceedForPayrollStatus = true " +
            "AND p.status = true " +
            "AND p.proceedForPayment = :proceedForPayment")
    List<PayrollRecord> findByCompanyAndPayrollStatusAndPaymentStatus(
            @Param("companyId") Long companyId,
            @Param("proceedForPayment") boolean proceedForPayment);





            @Query("SELECT p FROM PayrollRecord p WHERE p.company.id = :companyId " +
            "AND p.proceedForPayrollStatus = true " +
            "AND p.status = true " +
            "AND p.proceedForPayment = :proceedForPayment " +
            "AND YEAR(p.date) = :year AND MONTH(p.date) = :month")
     List<PayrollRecord> findByCompanyAndStatusAndDate(
             @Param("companyId") Long companyId,
             @Param("proceedForPayment") boolean proceedForPayment,
             @Param("year") int year,
             @Param("month") int month);
}
