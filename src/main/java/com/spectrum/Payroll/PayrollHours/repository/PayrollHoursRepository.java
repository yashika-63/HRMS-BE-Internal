package com.spectrum.Payroll.PayrollHours.repository;

import com.spectrum.CTCModuleMain.model.CTCBreakdownRecord;
import com.spectrum.CTCModuleMain.model.CTCBreakdownRecord;
import com.spectrum.Payroll.PayrollHours.model.PayrollHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollHoursRepository extends JpaRepository<PayrollHours, Long> {
    List<PayrollHours> findByEmployeeId(Long employeeId); // Custom method to get data by employeeId

    @Query("SELECT CASE WHEN COUNT(ph) > 0 THEN TRUE ELSE FALSE END FROM PayrollHours ph " +
            "WHERE ph.employee.id = :employeeId " +
            "AND MONTH(ph.date) = :month " +
            "AND YEAR(ph.date) = :year")
    boolean existsByEmployeeIdAndMonth(@Param("employeeId") Long employeeId,
            @Param("month") int month,
            @Param("year") int year);


            
    @Query("SELECT c FROM PayrollHours c WHERE c.employee.companyRegistration.id = :companyId ")
    List<PayrollHours> findRecordsByCompanyId(@Param("companyId") Long companyId);



    @Query("SELECT p FROM PayrollHours p WHERE p.employee.companyRegistration.id = :companyId " +
       "AND p.Status = true " +
       "AND FUNCTION('MONTH', p.date) = :month")
List<PayrollHours> findByCompanyIdAndStatusAndMonth(@Param("companyId") Long companyId, @Param("month") int month);



@Query("SELECT ph FROM PayrollHours ph " +
       "WHERE ph.employee.companyRegistration.id = :companyId " +
       "AND FUNCTION('MONTH', ph.date) = :month " +
       "AND FUNCTION('YEAR', ph.date) = :year")
List<PayrollHours> findRecordsByCompanyIdAndMonthAndYear(@Param("companyId") Long companyId,
                                                         @Param("month") int month,
                                                         @Param("year") int year);

 // Find payroll records for a specific companyId, month, and year (alternative query)
 @Query("SELECT p FROM PayrollHours p WHERE p.employee.companyRegistration.id = :companyId " +
 "AND FUNCTION('MONTH', p.date) = :month " +
 "AND FUNCTION('YEAR', p.date) = :year")
List<PayrollHours> findByCompanyIdAndMonthAndYear(@Param("companyId") Long companyId, 
                                           @Param("month") int month, 
                                           @Param("year") int year);



        // Fetch payroll records by employeeId, month, and year
    @Query("SELECT ph FROM PayrollHours ph " +
    "WHERE ph.employee.id = :employeeId " +
    "AND FUNCTION('MONTH', ph.date) = :month " +
    "AND FUNCTION('YEAR', ph.date) = :year")
PayrollHours findByEmployeeAndMonthAndYear(@Param("employeeId") Long employeeId,
                                        @Param("month") int month,
                                        @Param("year") int year);
}
                                                         

