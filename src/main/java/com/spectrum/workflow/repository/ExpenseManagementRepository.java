package com.spectrum.workflow.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.workflow.model.ExpenseManagement;

public interface ExpenseManagementRepository extends JpaRepository<ExpenseManagement, Long> {

    // Method to count requests by companyId, employeeId, and requestStatus
    long countByCompanyRegistrationIdAndEmployeeIdAndRequestStatus(long companyId, long employeeId,
            String requestStatus);

    // Method to find requests based on companyId, division, department, and role
    List<ExpenseManagement> findByCompanyRegistrationIdAndWorkflowDivisionAndWorkflowDepartmentAndWorkflowRole(
            long companyId, String workflowDivision, String workflowDepartment, String workflowRole);

    // Method to find a specific request by ID and companyId
    Optional<ExpenseManagement> findByIdAndCompanyRegistrationId(long id, long companyRegistrationId);

    // Method to find a specific request by ID, companyId, and employeeId
    Optional<ExpenseManagement> findByIdAndCompanyRegistrationIdAndEmployeeId(long id, long companyId,
            long employeeId);

    // Method to find requests by employeeId (simplified)
    @Query("SELECT e FROM ExpenseManagement e WHERE e.employee.id = :employeeId")
    List<ExpenseManagement> findByEmployeeId(@Param("employeeId") Long employeeId);

    // Method to find the latest 10 requests for an employee, ordered by ID (desc)
    List<ExpenseManagement> findTop10ByEmployeeIdOrderByIdDesc(Long employeeId);

    @Query("SELECT COUNT(e) FROM ExpenseManagement e WHERE e.companyRegistration.id = :companyRegistrationId " +
            "AND e.workflowDivision = :workflowDivision " +
            "AND e.workflowDepartment = :workflowDepartment " +
            "AND e.workflowRole = :workflowRole " +
            "AND e.requestStatus = 'PENDING'")
    long countPendingRequests(@Param("companyRegistrationId") long companyId,
            @Param("workflowDivision") String workflowDivision,
            @Param("workflowDepartment") String workflowDepartment,
            @Param("workflowRole") String workflowRole);

    @Query("SELECT e FROM ExpenseManagement e WHERE e.companyRegistration.id = :companyId " +
            "AND e.employee.id = :employeeId " +
            "AND e.expenseFromDate >= :startDate AND e.expenseTillDate <= :endDate")
    List<ExpenseManagement> findByDateRangeAndEmployeeId(@Param("companyId") long companyId,
            @Param("employeeId") long employeeId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);




//  @Query("SELECT la FROM ExpenseManagement la " +
//      "JOIN la.employee e " +
//      "JOIN e.GenerateProjects gp " +
//      "WHERE la.companyRegistration.id = :companyId " +
//      "AND la.workflowDivision = :workflowDivision " +
//      "AND la.workflowDepartment = :workflowDepartment " +
//      "AND la.workflowRole = :workflowRole " +
//      "AND gp.ProjectId IN :projectIds " + // Check if employee's project is in provided list
//      "ORDER BY la.fromDate DESC")
// List<ExpenseManagement> findByCompanyAndProjects(
//       @Param("companyId") long companyId,
//       @Param("projectIds") List<Long> projectIds,
//       @Param("workflowDivision") String workflowDivision,
//       @Param("workflowDepartment") String workflowDepartment,
//       @Param("workflowRole") String workflowRole);

// @Query("SELECT la FROM ExpenseManagement la "
//      + "JOIN la.employee e "
//      + "JOIN e.GenerateProjects gp " // Use corrected field name
//      + "WHERE la.companyRegistration.id = :companyId "
//      + "AND la.workflowDivision = :workflowDivision "
//      + "AND la.workflowDepartment = :workflowDepartment "
//      + "AND la.workflowRole = :workflowRole "
//      + "AND gp.ProjectId IN :projectIds "
//      + "ORDER BY la.expenseFromDate DESC")
// List<ExpenseManagement> findByCompanyAndProjects (
//      @Param("companyId") Long companyId,
//      @Param("projectIds") List<String> projectIds,
//      @Param("workflowDivision") String workflowDivision,
//      @Param("workflowDepartment") String workflowDepartment,
//      @Param("workflowRole") String workflowRole
// );


@Query("SELECT la FROM ExpenseManagement la " +
     "JOIN la.employee e " +
     "JOIN e.GenerateProjects gp " +
     "WHERE la.companyRegistration.id = :companyId " +
     "AND la.workflowDivision = :workflowDivision " +
     "AND la.workflowDepartment = :workflowDepartment " +
     "AND la.workflowRole = :workflowRole " +
     "AND gp.ProjectId IN :projectIds " + // Check if employee's project is in provided list
     "ORDER BY la.expenseFromDate DESC")
List<ExpenseManagement> findByCompanyAndProjects(
      @Param("companyId") long companyId,
      @Param("projectIds") List<Long> projectIds,
      @Param("workflowDivision") String workflowDivision,
      @Param("workflowDepartment") String workflowDepartment,
      @Param("workflowRole") String workflowRole);






         
    List<ExpenseManagement> findByEmployeeIdAndRequestStatusAndExpenseFromDateBetween(
        Long employeeId,
        String requestStatus,
        Date startDate,
        Date endDate
    );



}