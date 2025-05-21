package com.spectrum.PerformanceManagement.KpiManagement.repository;

import com.spectrum.PerformanceManagement.KpiManagement.model.EmployeeKpiSetting;
import com.spectrum.model.Employee;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeKpiSettingRepository extends JpaRepository<EmployeeKpiSetting, Long> {


    @Query("SELECT k FROM EmployeeKpiSetting k WHERE k.employee.id = :employeeId AND k.employees.id = :reportingManagerId AND YEAR(k.date) = :year")
    List<EmployeeKpiSetting> findByEmployeeIdAndReportingManagerIdAndYear(@Param("employeeId") Long employeeId, 
                                                                           @Param("reportingManagerId") Long reportingManagerId, 
                                                                           @Param("year") int year);

     @Query("SELECT k FROM EmployeeKpiSetting k WHERE k.employee.id = :employeeId AND YEAR(k.date) = :year")
      List<EmployeeKpiSetting> findByEmployeeIdAndYear(@Param("employeeId") Long employeeId, 
        
      @Param("year") int year);       
      
      


      @Query("SELECT e FROM EmployeeKpiSetting e WHERE e.employee.id = :employeeId AND e.status = true AND e.reviewStatus = true")
    List<EmployeeKpiSetting> findByEmployeeIdAndStatusTrueAndReviewStatusTrue(@Param("employeeId") Long employeeId);



    // Fetch active KPIs where reviewStatus is false and status is true for a specific company
    @Query("SELECT k FROM EmployeeKpiSetting k WHERE k.reviewStatus = false AND k.status = true AND k.employee.companyRegistration.id = :companyId")
    List<EmployeeKpiSetting> findByReviewStatusFalseAndStatusTrueAndCompany(@Param("companyId") Long companyId);


    List<EmployeeKpiSetting> findByEmployeeApprovalFalse();


    @Transactional
@Modifying
@Query("UPDATE EmployeeKpiSetting egs SET egs.employeeApproval = true WHERE egs.employee.id = :employeeId AND egs.employeeApproval = false")
int updateUnapprovedKpisByEmployeeId(@Param("employeeId") Long employeeId);

@Query("SELECT egs FROM EmployeeKpiSetting egs WHERE egs.employee.id = :employeeId AND egs.employeeApproval = false")
List<EmployeeKpiSetting> findUnapprovedKpisByEmployeeId(@Param("employeeId") Long employeeId);


@Query("SELECT e FROM EmployeeKpiSetting e WHERE e.employee.id = :employeeId AND YEAR(e.date) = :year")
List<EmployeeKpiSetting> findByYearAndEmployeeId(@Param("year") int year, @Param("employeeId") Long employeeId);



    List<EmployeeKpiSetting> findByEmployeeAndStatus(Employee employee, boolean status);

}
