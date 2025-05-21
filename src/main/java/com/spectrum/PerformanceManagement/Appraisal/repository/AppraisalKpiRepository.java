package com.spectrum.PerformanceManagement.Appraisal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalGoal;
import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalKpi;

public interface AppraisalKpiRepository extends JpaRepository<AppraisalKpi, Long> {

    List<AppraisalKpi> findByEmployeeIdAndAppraisalYear(Long employeeId, int appraisalYear);
    List<AppraisalKpi> findByEmployeeIdAndAppraisalYearAndEmployeesId(Long employeeId, int appraisalYear, Long employeesId);
    List<AppraisalKpi> findByEmployeeId(Long employeeId);
    List<AppraisalKpi> findByEmployeeIdAndStatusTrue(Long employeeId);


    List<AppraisalKpi> findByEmployeeIdAndStatusAndEmployeeRatingStatus(Long employeeId, boolean status, boolean employeeRatingStatus);



////////////////////////

    @Query("SELECT a FROM AppraisalKpi a " +
           "WHERE a.employees.id = :reportingManagerId " +
           "AND a.status = true " +
           "AND a.reportingRatingStatus = false " +
           "AND a.employeeRatingStatus = true")
    List<AppraisalKpi> findByReportingManagerIdAndStatus(
            @Param("reportingManagerId") Long reportingManagerId);

            List<AppraisalKpi> findByEmployeeIdAndStatus(Long employeeId, boolean status);



            List<AppraisalKpi> findByEmployeeIdAndEmployeesIdAndStatusTrue(Long employeeId, Long reportingManagerId);


}