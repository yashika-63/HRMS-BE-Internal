package com.spectrum.PerformanceManagement.GoalSetting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import com.spectrum.PerformanceManagement.GoalSetting.model.EmployeeGoalSetting;
import com.spectrum.PerformanceManagement.KpiManagement.model.EmployeeKpiSetting;
import com.spectrum.model.Employee;

import jakarta.transaction.Transactional;

@Repository
public interface EmployeeGoalSettingRepository extends JpaRepository<EmployeeGoalSetting, Long> {


     @Query("SELECT e FROM EmployeeGoalSetting e WHERE e.employee.id = :employeeId")
    List<EmployeeGoalSetting> findByEmployeeId(@Param("employeeId") Long employeeId);

//  @Transactional
//     @Modifying
//     @Query("UPDATE EmployeeGoalSetting egs SET egs.employeeApproval = true WHERE egs.employee.id = :employeeId")
//     int updateEmployeeApprovalByEmployeeId(@Param("employeeId") Long employeeId);



  
    @Query("SELECT e FROM EmployeeGoalSetting e " +
    "WHERE e.regionId = :regionId " +
    "AND e.employee.id = :employeeId " +
    "AND e.goalType = :goalType " +
    "AND (e.departmentId = :departmentId OR e.departmentId = 0)")
List<EmployeeGoalSetting> findGoalsWithDepartmentOrZero(
     @Param("regionId") int regionId,
     @Param("employeeId") Long employeeId,
     @Param("goalType") boolean goalType,
     @Param("departmentId") int departmentId
);



    @Query("SELECT g FROM EmployeeGoalSetting g WHERE g.employee.id = :employeeId AND YEAR(g.date) = :year")
    List<EmployeeGoalSetting> findByEmployeeIdAndYear(@Param("employeeId") Long employeeId, @Param("year") int year);

    List<EmployeeGoalSetting> findByEmployeeIdAndStatusTrueAndReviewStatusTrue(Long employeeId);


    @Query("SELECT e FROM EmployeeGoalSetting e WHERE e.reviewStatus = false AND e.employee.companyRegistration.id = :companyId")
    List<EmployeeGoalSetting> findByReviewStatusFalseAndCompany(Long companyId);

    /////
    /// 
    
  
    @Query("SELECT e FROM EmployeeGoalSetting e " +
    "WHERE (e.regionId = :regionId OR e.regionId = 0) " +
    "AND e.employee.id = :employeeId " +
    "AND e.goalType = :goalType " +
    "AND (e.departmentId = :departmentId OR e.departmentId = 0) " +
    "AND (e.typeId = :typeId OR e.typeId = 0) " +
    "AND e.status = :status")
List<EmployeeGoalSetting> findGoalsWithDepartmentOrZeroAndRegionIdOrZeroAndTypeIdOrZero(
 @Param("regionId") int regionId,
 @Param("employeeId") Long employeeId,
 @Param("goalType") boolean goalType,
 @Param("departmentId") int departmentId,
 @Param("typeId") int typeId,
 @Param("status") boolean status
);


List<EmployeeGoalSetting> findByEmployeeApprovalFalse();




@Transactional
@Modifying
@Query("UPDATE EmployeeGoalSetting egs SET egs.employeeApproval = true WHERE egs.employee.id = :employeeId AND egs.employeeApproval = false")
int updateUnapprovedGoalsByEmployeeId(@Param("employeeId") Long employeeId);

@Query("SELECT egs FROM EmployeeGoalSetting egs WHERE egs.employee.id = :employeeId AND egs.employeeApproval = false")
List<EmployeeGoalSetting> findUnapprovedGoalsByEmployeeId(@Param("employeeId") Long employeeId);


    List<EmployeeGoalSetting> findByEmployeeAndStatus(Employee employee, boolean status);

}