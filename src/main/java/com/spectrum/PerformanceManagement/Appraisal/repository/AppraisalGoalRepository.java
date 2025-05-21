package com.spectrum.PerformanceManagement.Appraisal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalGoal;
import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalKpi;

public interface AppraisalGoalRepository extends JpaRepository<AppraisalGoal, Long> {

    List<AppraisalGoal> findByEmployeeIdAndAppraisalYear(Long employeeId, int appraisalYear);
    List<AppraisalGoal> findByEmployeeIdAndAppraisalYearAndEmployeesId(Long employeeId, int appraisalYear, Long employeesId);

    List<AppraisalGoal> findByEmployeeIdAndStatusTrue(Long employeeId);
       // Fetch records where status = true and employeeRatingStatus = false
       List<AppraisalGoal> findByEmployeeIdAndStatusAndEmployeeRatingStatus(Long employeeId, boolean status, boolean employeeRatingStatus);


    @Query("SELECT a FROM AppraisalGoal a " +
           "WHERE a.employees.id = :reportingManagerId " +
           "AND a.status = true " +
           "AND a.reportingRatingStatus = false " +
           "AND a.employeeRatingStatus = true")
    List<AppraisalGoal> findByReportingManagerIdAndStatus(
            @Param("reportingManagerId") Long reportingManagerId);


            List<AppraisalGoal> findByEmployeeIdAndStatus(Long employeeId, boolean status);

            List<AppraisalGoal> findByEmployeeIdAndEmployeesIdAndStatusTrue(Long employeeId, Long reportingManagerId);

}