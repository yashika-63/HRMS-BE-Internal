package com.spectrum.workflow.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.model.Employee;
import com.spectrum.workflow.model.LeaveApplication;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {

    List<LeaveApplication> findByEmployeeIdAndCompanyRegistrationId(long employeeId, long companyRegistrationId);

    // List<LeaveApplication> findByCompanyRegistrationIdAndWorkflowDivisionAndWorkflowDepartmentAndWorkflowRole(
    //         long companyId, String workflowDivision, String workflowDepartment, String workflowRole);
List<LeaveApplication> findByEmployeeAndRequestStatus(Employee employee, String requestStatus);

  // New method to count pending leave applications
  long countByCompanyRegistrationIdAndEmployeeIdAndRequestStatus(long companyId, long employeeId, String requestStatus);
    List<LeaveApplication> findByCompanyRegistrationIdAndWorkflowDivisionAndWorkflowDepartmentAndWorkflowRoleOrderByFromDateDesc(
    long companyId, 
    String workflowDivision, 
    String workflowDepartment, 
    String workflowRole
);


    List<LeaveApplication> findByWorkflowMainId(long workflowMainId);

    List<LeaveApplication> findByEmployeeIdAndCompanyRegistrationIdAndWorkflowMainId(long employeeId,
            long companyRegistrationId, long workflowMainId);

    Optional<LeaveApplication> findByIdAndCompanyRegistrationIdAndEmployeeId(long id, long companyId, long employeeId);

    Optional<LeaveApplication> findByIdAndCompanyRegistrationId(long id, long companyRegistrationId);




      @Query("SELECT la FROM LeaveApplication la WHERE la.companyRegistration.id = :companyId AND la.employee.id = :employeeId AND FUNCTION('YEAR', la.fromDate) = :year")
    List<LeaveApplication> findLeaveApplicationsForYear(@Param("companyId") long companyId,
                                                        @Param("employeeId") long employeeId,
                                                        @Param("year") int year);

    @Query("SELECT SUM(DATEDIFF(la.toDate, la.fromDate) + 1) " +
        "FROM LeaveApplication la " +
        "WHERE la.companyRegistration.id = :companyId " +
        "AND la.employee.id = :employeeId " +
        "AND la.requestStatus = 'APPROVED'")
    Optional<Integer> findTotalApprovedLeaveDays(@Param("companyId") long companyId,
        @Param("employeeId") long employeeId);

    @Query("SELECT COUNT(l) FROM LeaveApplication l WHERE l.companyRegistration.id = :companyId AND l.employee.id = :employeeId AND l.requestStatus = :status")
    int countByCompanyIdAndEmployeeIdAndStatus(@Param("companyId") long companyId, @Param("employeeId") long employeeId,
        @Param("status") String status);

    @Query("SELECT COUNT(l) FROM LeaveApplication l WHERE l.companyRegistration.id = :companyId AND l.employee.id = :employeeId AND l.requestStatus = :status AND l.leaveType = :type")
    int countByCompanyIdAndEmployeeIdAndStatusAndType(@Param("companyId") long companyId,
        @Param("employeeId") long employeeId, @Param("status") String status, @Param("type") String type);

        List<LeaveApplication> findTop10ByEmployeeIdOrderByIdDesc(long employeeId);



     @Modifying
    @Query("UPDATE LeaveApplication la SET la.workflowMain = null WHERE la.workflowMain.id = :workflowMainId")
    void removeWorkflowMainReferences(@Param("workflowMainId") long workflowMainId);

    

    @Query("SELECT COALESCE(SUM(DATEDIFF(l.toDate, l.fromDate) + 1), 0) FROM LeaveApplication l WHERE l.companyRegistration.id = :companyId AND l.employee.id = :employeeId AND l.requestStatus = :status")
    int sumLeaveDaysByStatus(long companyId, long employeeId, String status);



    @Query("SELECT COUNT(l) FROM LeaveApplication l " +
    "WHERE l.companyRegistration.id = :companyId " +
    "AND l.workflowDivision = :workflowDivision " +
    "AND l.workflowDepartment = :workflowDepartment " +
    "AND l.workflowRole = :workflowRole " +
    "AND l.requestStatus = 'PENDING'")
long countPendingRequests(@Param("companyId") long companyId,
                       @Param("workflowDivision") String workflowDivision,
                       @Param("workflowDepartment") String workflowDepartment,
                       @Param("workflowRole") String workflowRole);



                       @Query("SELECT la FROM LeaveApplication la " +
    "WHERE la.companyRegistration.id = :companyId " +
    "AND la.employee.id = :employeeId " +
    "AND la.fromDate >= :fromDate " +
    "AND la.toDate <= :toDate")
List<LeaveApplication> findLeaveApplicationsBetweenDates(
     @Param("companyId") long companyId,
     @Param("employeeId") long employeeId,
     @Param("fromDate") Date fromDate,
     @Param("toDate") Date toDate);



     @Query("SELECT la FROM LeaveApplication la " +
     "JOIN la.employee e " +
     "JOIN e.GenerateProjects gp " +
     "WHERE la.companyRegistration.id = :companyId " +
     "AND la.workflowDivision = :workflowDivision " +
     "AND la.workflowDepartment = :workflowDepartment " +
     "AND la.workflowRole = :workflowRole " +
     "AND gp.ProjectId IN :projectIds " + // Check if employee's project is in provided list
     "ORDER BY la.fromDate DESC")
List<LeaveApplication> findByCompanyAndProjects(
      @Param("companyId") long companyId,
      @Param("projectIds") List<Long> projectIds,
      @Param("workflowDivision") String workflowDivision,
      @Param("workflowDepartment") String workflowDepartment,
      @Param("workflowRole") String workflowRole);



  
}
