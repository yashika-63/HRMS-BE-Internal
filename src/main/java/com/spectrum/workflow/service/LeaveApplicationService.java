package com.spectrum.workflow.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;

import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.workflow.model.LeaveApplication;
import com.spectrum.workflow.model.WorkflowDetail;
import com.spectrum.workflow.repository.LeaveApplicationRepository;
import com.spectrum.workflow.repository.WorkflowDetailRepository;

@Service
public class LeaveApplicationService {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private WorkflowDetailRepository WorkflowDetailRepository;

    public LeaveApplication saveLeaveApplicationWithCompanyAndEmployee(long companyId, long employeeId,
            LeaveApplication leaveApplication) {
        // Retrieve the employee and company registration entities
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        CompanyRegistration companyRegistration = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("CompanyRegistration not found"));

        // Set the employee and company registration in the leave application
        leaveApplication.setEmployee(employee);
        leaveApplication.setCompanyRegistration(companyRegistration);

        // Save and return the leave application
        return leaveApplicationRepository.save(leaveApplication);
    }

    public Optional<LeaveApplication> getLeaveApplication(long id, long employeeId, long companyId) {
        return leaveApplicationRepository.findById(id)
                .filter(la -> la.getEmployee().getId() == employeeId
                        && la.getCompanyRegistration().getId() == companyId);
    }

    public List<LeaveApplication> getAllLeaveApplications(long employeeId, long companyId) {
        return leaveApplicationRepository.findByEmployeeIdAndCompanyRegistrationId(employeeId, companyId);
    }

    public LeaveApplication updateLeaveApplication(long id, long employeeId, long companyId,
            LeaveApplication leaveApplicationDetails) {
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(id)
                .filter(la -> la.getEmployee().getId() == employeeId
                        && la.getCompanyRegistration().getId() == companyId)
                .orElseThrow(() -> new RuntimeException("LeaveApplication not found"));

        leaveApplication.setIdentifier(leaveApplicationDetails.getIdentifier());
        leaveApplication.setName(leaveApplicationDetails.getName());
        leaveApplication.setReason(leaveApplicationDetails.getReason());
        leaveApplication.setFromDate(leaveApplicationDetails.getFromDate());
        leaveApplication.setToDate(leaveApplicationDetails.getToDate());
        leaveApplication.setWorkflowDivision(leaveApplicationDetails.getWorkflowDivision());
        leaveApplication.setWorkflowDepartment(leaveApplicationDetails.getWorkflowDepartment());
        leaveApplication.setWorkflowRole(leaveApplicationDetails.getWorkflowRole());
        leaveApplication.setRequestStatus(leaveApplicationDetails.getRequestStatus());

        return leaveApplicationRepository.save(leaveApplication);
    }

    public void deleteLeaveApplication(long id, long employeeId, long companyId) {
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(id)
                .filter(la -> la.getEmployee().getId() == employeeId
                        && la.getCompanyRegistration().getId() == companyId)
                .orElseThrow(() -> new RuntimeException("LeaveApplication not found"));
        leaveApplicationRepository.delete(leaveApplication);
    }

    // Method for workflow request
    public List<LeaveApplication> findByCriteria(long companyId, String workflowDivision, String workflowDepartment,
            String workflowRole) {

        return leaveApplicationRepository
                .findByCompanyRegistrationIdAndWorkflowDivisionAndWorkflowDepartmentAndWorkflowRoleOrderByFromDateDesc(
                        companyId, workflowDivision, workflowDepartment, workflowRole);
    }

    // public LeaveApplication saveLeaveRequestForWorkflowMain(long employeeId, long
    // companyId, long workflowMainId, LeaveApplication leaveApplication) {
    // Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
    // new RuntimeException("Employee not found"));
    // CompanyRegistration companyRegistration =
    // companyRegistrationRepository.findById(companyId).orElseThrow(() -> new
    // RuntimeException("CompanyRegistration not found"));
    // WorkflowDetail firstDetail =
    // WorkflowDetailRepository.findFirstByWorkflowMainIdOrderByIdAsc(workflowMainId);

    // if (firstDetail != null) {
    // leaveApplication.setEmployee(employee);
    // leaveApplication.setCompanyRegistration(companyRegistration);
    // leaveApplication.setWorkflowDivision(firstDetail.getWorkflowFromDivision());
    // leaveApplication.setWorkflowDepartment(firstDetail.getWorkflowFromDepartment());
    // leaveApplication.setWorkflowRole(firstDetail.getWorkflowRole());
    // leaveApplication.setWorkflowMain(firstDetail.getWorkflowMain());

    // return leaveApplicationRepository.save(leaveApplication);
    // } else {
    // throw new RuntimeException("No workflow details found for the given
    // workflowMainId.");
    // }

    // }

    // public LeaveApplication saveLeaveRequestForWorkflowMain(long employeeId, long
    // companyId, long workflowMainId,
    // LeaveApplication leaveApplication) {
    // Employee employee = employeeRepository.findById(employeeId)
    // .orElseThrow(() -> new RuntimeException("Employee not found"));
    // CompanyRegistration companyRegistration =
    // companyRegistrationRepository.findById(companyId)
    // .orElseThrow(() -> new RuntimeException("CompanyRegistration not found"));
    // WorkflowDetail firstDetail =
    // WorkflowDetailRepository.findFirstByWorkflowMainIdOrderByIdAsc(workflowMainId);

    // if (firstDetail != null) {
    // leaveApplication.setEmployee(employee);
    // leaveApplication.setCompanyRegistration(companyRegistration);
    // leaveApplication.setWorkflowDivision(firstDetail.getWorkflowToDivision());
    // leaveApplication.setWorkflowDepartment(firstDetail.getWorkflowToDepartment());
    // leaveApplication.setWorkflowRole(firstDetail.getWorkflowNextRole());
    // leaveApplication.setWorkflowMain(firstDetail.getWorkflowMain());

    // return leaveApplicationRepository.save(leaveApplication);
    // } else {
    // throw new RuntimeException("No workflow details found for the given
    // workflowMainId.");
    // }

    // }

    // public LeaveApplication updateWorkflowDetails(long leaveApplicationId, long
    // companyId, long employeeId,
    // String workflowDivision, String workflowDepartment, String workflowRole) {
    // Optional<LeaveApplication> leaveApplicationOpt = leaveApplicationRepository
    // .findByIdAndCompanyRegistrationIdAndEmployeeId(leaveApplicationId, companyId,
    // employeeId);
    // if (!leaveApplicationOpt.isPresent()) {
    // throw new RuntimeException("LeaveApplication not found");
    // }

    // LeaveApplication leaveApplication = leaveApplicationOpt.get();

    // // Find the matching WorkflowDetail
    // Optional<WorkflowDetail> workflowDetailOpt = WorkflowDetailRepository
    // .findByWorkflowFromDivisionAndWorkflowFromDepartmentAndWorkflowPreviousRole(
    // workflowDivision, workflowDepartment, workflowRole);

    // if (!workflowDetailOpt.isPresent()) {
    // throw new RuntimeException("Matching WorkflowDetail not found");
    // }

    // WorkflowDetail workflowDetail = workflowDetailOpt.get();

    // // Update the LeaveApplication with the details from WorkflowDetail
    // leaveApplication.setWorkflowDivision(workflowDetail.getWorkflowToDivision());
    // leaveApplication.setWorkflowDepartment(workflowDetail.getWorkflowToDepartment());
    // leaveApplication.setWorkflowRole(workflowDetail.getWorkflowNextRole());

    // return leaveApplicationRepository.save(leaveApplication);
    // }

    // public LeaveApplication updatedWorkflowDetailss(long leaveApplicationId, long
    // companyId, long employeeId,
    // String workflowDivision, String workflowDepartment, String workflowRole) {
    // Optional<LeaveApplication> leaveApplicationOpt = leaveApplicationRepository
    // .findByIdAndCompanyRegistrationIdAndEmployeeId(leaveApplicationId, companyId,
    // employeeId);
    // if (!leaveApplicationOpt.isPresent()) {
    // throw new RuntimeException("LeaveApplication not found");
    // }

    // LeaveApplication leaveApplication = leaveApplicationOpt.get();

    // // Find the matching WorkflowDetail
    // Optional<WorkflowDetail> workflowDetailOpt = WorkflowDetailRepository
    // .findByWorkflowFromDivisionAndWorkflowFromDepartmentAndWorkflowPreviousRole(
    // workflowDivision, workflowDepartment, workflowRole);

    // if (!workflowDetailOpt.isPresent()) {
    // // No further WorkflowDetail, mark requestStatus as APPROVED
    // leaveApplication.setRequestStatus("APPROVED");

    // // Set division, department, and role to employee's details
    // Optional<Employee> employeeOpt =
    // employeeRepository.findByIdAndCompanyRegistration_Id(employeeId,
    // companyId);
    // if (employeeOpt.isPresent()) {
    // Employee employee = employeeOpt.get();
    // leaveApplication.setWorkflowDivision(employee.getDivision());
    // leaveApplication.setWorkflowDepartment(employee.getDepartment());
    // leaveApplication.setWorkflowRole(employee.getRole());
    // } else {
    // throw new RuntimeException("Employee not found");
    // }
    // } else {
    // WorkflowDetail workflowDetail = workflowDetailOpt.get();

    // // Update the LeaveApplication with the details from WorkflowDetail
    // leaveApplication.setWorkflowDivision(workflowDetail.getWorkflowToDivision());
    // leaveApplication.setWorkflowDepartment(workflowDetail.getWorkflowToDepartment());
    // leaveApplication.setWorkflowRole(workflowDetail.getWorkflowNextRole());
    // }

    // return leaveApplicationRepository.save(leaveApplication);
    // }

    // public LeaveApplication declineWorkflowDetails(long leaveApplicationId, long
    // companyId, long employeeId) {
    // // Find the LeaveApplication by its ID, company ID, and employee ID
    // Optional<LeaveApplication> leaveApplicationOpt = leaveApplicationRepository
    // .findByIdAndCompanyRegistrationIdAndEmployeeId(leaveApplicationId, companyId,
    // employeeId);
    // if (!leaveApplicationOpt.isPresent()) {
    // throw new RuntimeException("LeaveApplication not found");
    // }

    // LeaveApplication leaveApplication = leaveApplicationOpt.get();

    // // Set the request status to REJECTED
    // leaveApplication.setRequestStatus("REJECTED");

    // // Set division, department, and role to employee's details
    // Optional<Employee> employeeOpt =
    // employeeRepository.findByIdAndCompanyRegistration_Id(employeeId, companyId);
    // if (employeeOpt.isPresent()) {
    // Employee employee = employeeOpt.get();
    // leaveApplication.setWorkflowDivision(employee.getDivision());
    // leaveApplication.setWorkflowDepartment(employee.getDepartment()); // Fixed
    // typo here
    // leaveApplication.setWorkflowRole(employee.getRole());
    // } else {
    // throw new RuntimeException("Employee not found");
    // }

    // // Save the updated LeaveApplication
    // return leaveApplicationRepository.save(leaveApplication);
    // }

    // public LeaveApplication saveLeaveRequestForWorkflowMain(long employeeId, long
    // companyId, long workflowMainId,
    // LeaveApplication leaveApplication) {
    // Employee employee = employeeRepository.findById(employeeId)
    // .orElseThrow(() -> new RuntimeException("Employee not found"));
    // CompanyRegistration companyRegistration =
    // companyRegistrationRepository.findById(companyId)
    // .orElseThrow(() -> new RuntimeException("CompanyRegistration not found"));
    // WorkflowDetail firstDetail =
    // WorkflowDetailRepository.findFirstByWorkflowMainIdOrderByIdAsc(workflowMainId);

    // if (firstDetail != null) {
    // leaveApplication.setEmployee(employee);
    // leaveApplication.setCompanyRegistration(companyRegistration);
    // leaveApplication.setWorkflowDivision(firstDetail.getWorkflowToDivision());
    // leaveApplication.setWorkflowDepartment(firstDetail.getWorkflowToDepartment());
    // leaveApplication.setWorkflowRole(firstDetail.getWorkflowNextRole());
    // leaveApplication.setWorkflowMain(firstDetail.getWorkflowMain());

    // return leaveApplicationRepository.save(leaveApplication);
    // } else {
    // throw new RuntimeException("No workflow details found for the given
    // workflowMainId.");
    // }
    // }

    public LeaveApplication saveLeaveRequestForWorkflowMain(long employeeId, long companyId, long workflowMainId,
            LeaveApplication leaveApplication) {
        try {
            // Check if the employee exists
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

            // Check if the company registration exists
            CompanyRegistration companyRegistration = companyRegistrationRepository.findById(companyId)
                    .orElseThrow(() -> new RuntimeException("CompanyRegistration not found with ID: " + companyId));

            // Fetch the first workflow detail for the given workflowMainId
            WorkflowDetail firstDetail = WorkflowDetailRepository.findFirstByWorkflowMainIdOrderByIdAsc(workflowMainId);
            if (firstDetail == null) {
                throw new RuntimeException("No workflow details found for the given workflowMainId: " + workflowMainId);
            }

            // Set the related entities and workflow details to the leave application
            leaveApplication.setEmployee(employee);
            leaveApplication.setCompanyRegistration(companyRegistration);
            leaveApplication.setWorkflowDivision(firstDetail.getWorkflowToDivision());
            leaveApplication.setWorkflowDepartment(firstDetail.getWorkflowToDepartment());
            leaveApplication.setWorkflowRole(firstDetail.getWorkflowNextRole());
            leaveApplication.setWorkflowMain(firstDetail.getWorkflowMain());
            leaveApplication.setRequestStatus("PENDING");

            // Save the leave application and return the saved entity
    return leaveApplicationRepository.save(leaveApplication);
} catch (RuntimeException e) {
    // Rethrow runtime exceptions with enhanced details
    throw new RuntimeException("Error in processing leave request: " + e.getMessage());
} catch (Exception e) {
    // Log any other exceptions for debugging and throw a custom exception
    e.printStackTrace();
    throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
}
}

    public LeaveApplication updatedWorkflowDetailss(long leaveApplicationId, long companyId, long employeeId,

            long workflowMainId, String workflowDivision, String workflowDepartment, String workflowRole) {
        Optional<LeaveApplication> leaveApplicationOpt = leaveApplicationRepository
                .findByIdAndCompanyRegistrationIdAndEmployeeId(leaveApplicationId, companyId, employeeId);
        if (!leaveApplicationOpt.isPresent()) {
            throw new RuntimeException("LeaveApplication not found");
        }

        LeaveApplication leaveApplication = leaveApplicationOpt.get();
        Optional<WorkflowDetail> workflowDetailOpt = WorkflowDetailRepository
                .findByWorkflowMainIdAndWorkflowFromDivisionAndWorkflowFromDepartmentAndWorkflowPreviousRole(
                        workflowMainId, workflowDivision,
                        workflowDepartment, workflowRole);

        if (!workflowDetailOpt.isPresent()) {
            leaveApplication.setRequestStatus("APPROVED");
            Optional<Employee> employeeOpt = employeeRepository.findByIdAndCompanyRegistration_Id(employeeId,
                    companyId);
            if (employeeOpt.isPresent()) {
                Employee employee = employeeOpt.get();
                leaveApplication.setWorkflowDivision(employee.getDivision());
                leaveApplication.setWorkflowDepartment(employee.getDepartment());
                leaveApplication.setWorkflowRole(employee.getRole());
            } else {
                throw new RuntimeException("Employee not found");
            }
        } else {
            WorkflowDetail workflowDetail = workflowDetailOpt.get();
            leaveApplication.setWorkflowDivision(workflowDetail.getWorkflowToDivision());
            leaveApplication.setWorkflowDepartment(workflowDetail.getWorkflowToDepartment());
            leaveApplication.setWorkflowRole(workflowDetail.getWorkflowNextRole());
        }

        return leaveApplicationRepository.save(leaveApplication);
    }

    public LeaveApplication declineWorkflowDetails(long leaveApplicationId, long companyId, long employeeId) {
        Optional<LeaveApplication> leaveApplicationOpt = leaveApplicationRepository
                .findByIdAndCompanyRegistrationIdAndEmployeeId(leaveApplicationId, companyId, employeeId);
        if (!leaveApplicationOpt.isPresent()) {
            throw new RuntimeException("LeaveApplication not found");
        }

        LeaveApplication leaveApplication = leaveApplicationOpt.get();
        leaveApplication.setRequestStatus("REJECTED");
        Optional<Employee> employeeOpt = employeeRepository.findByIdAndCompanyRegistration_Id(employeeId, companyId);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            leaveApplication.setWorkflowDivision(employee.getDivision());
            leaveApplication.setWorkflowDepartment(employee.getDepartment());
            leaveApplication.setWorkflowRole(employee.getRole());
        } else {
            throw new RuntimeException("Employee not found");
        }

        return leaveApplicationRepository.save(leaveApplication);
    }

    public int getTotalLeaveDaysForCurrentYear(long companyId, long employeeId) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<LeaveApplication> leaveApplications = leaveApplicationRepository.findLeaveApplicationsForYear(companyId, employeeId, currentYear);
        return leaveApplications.stream()
                .mapToInt(la -> (int) ((la.getToDate().getTime() - la.getFromDate().getTime()) / (1000 * 60 * 60 * 24)) + 1)
                .sum();

    }

    public int getTotalApprovedLeaveDays(long companyId, long employeeId) {
        return leaveApplicationRepository
                .findTotalApprovedLeaveDays(companyId, employeeId)
                .orElse(0);
    }

    public Map<String, Integer> getLeaveCountsByStatus(long companyId, long employeeId) {
        int approvedCount = leaveApplicationRepository.countByCompanyIdAndEmployeeIdAndStatus(companyId,
                employeeId, "APPROVED");
        int rejectedCount = leaveApplicationRepository.countByCompanyIdAndEmployeeIdAndStatus(companyId,
                employeeId, "REJECTED");

        Map<String, Integer> leaveCounts = new HashMap<>();
        leaveCounts.put("approved", approvedCount);
        leaveCounts.put("rejected", rejectedCount);

        return leaveCounts;
    }

    public Map<String, Integer> getLeaveCountsByStatusAndType(long companyId, long employeeId) {
        int approvedPaidCount = leaveApplicationRepository
                .countByCompanyIdAndEmployeeIdAndStatusAndType(companyId, employeeId, "APPROVED", "Paid");
        int approvedUnpaidCount = leaveApplicationRepository
                .countByCompanyIdAndEmployeeIdAndStatusAndType(companyId, employeeId, "APPROVED", "Unpaid");

        Map<String, Integer> leaveCounts = new HashMap<>();
        leaveCounts.put("approvedPaid", approvedPaidCount);
        leaveCounts.put("approvedUnpaid", approvedUnpaidCount);

        return leaveCounts;
            }

         // Method to get the latest 10 leave applications by employee ID
    public List<LeaveApplication> getLatestLeaveApplicationsByEmployeeId(long employeeId) {
        return leaveApplicationRepository.findTop10ByEmployeeIdOrderByIdDesc(employeeId);
    }

    public LeaveApplicationService(LeaveApplicationRepository leaveApplicationRepository) {
        this.leaveApplicationRepository = leaveApplicationRepository;
    }

    public Map<String, Integer> getLeaveDaysByStatus(long companyId, long employeeId) {
        int approvedDays = leaveApplicationRepository.sumLeaveDaysByStatus(companyId, employeeId, "APPROVED");
        int rejectedDays = leaveApplicationRepository.sumLeaveDaysByStatus(companyId, employeeId, "REJECTED");
        int pendingDays = leaveApplicationRepository.sumLeaveDaysByStatus(companyId, employeeId, "PENDING");

        Map<String, Integer> leaveDayCounts = new HashMap<>();
        leaveDayCounts.put("APPROVED", approvedDays);
        leaveDayCounts.put("REJECTED", rejectedDays);
        leaveDayCounts.put("PENDING", pendingDays);

        return leaveDayCounts;
    }

    public LeaveApplication updateLeaveApplicationById(long id, LeaveApplication leaveApplicationDetails) {
        Optional<LeaveApplication> existingLeaveApplicationOpt = leaveApplicationRepository.findById(id);

        if (existingLeaveApplicationOpt.isPresent()) {
            LeaveApplication existingLeaveApplication = existingLeaveApplicationOpt.get();

            // Update the fields with the new details
            existingLeaveApplication.setName(leaveApplicationDetails.getName());
            existingLeaveApplication.setReason(leaveApplicationDetails.getReason());
            existingLeaveApplication.setFromDate(leaveApplicationDetails.getFromDate());
            existingLeaveApplication.setToDate(leaveApplicationDetails.getToDate());
            existingLeaveApplication.setWorkflowDivision(leaveApplicationDetails.getWorkflowDivision());
            existingLeaveApplication.setWorkflowDepartment(leaveApplicationDetails.getWorkflowDepartment());
            existingLeaveApplication.setWorkflowRole(leaveApplicationDetails.getWorkflowRole());
            existingLeaveApplication.setRequestStatus(leaveApplicationDetails.getRequestStatus());
            existingLeaveApplication.setDesignation(leaveApplicationDetails.getDesignation());
            existingLeaveApplication.setManagerName(leaveApplicationDetails.getManagerName());
            existingLeaveApplication.setLeaveType(leaveApplicationDetails.getLeaveType());
            existingLeaveApplication.setLeaveCategory(leaveApplicationDetails.getLeaveCategory());
            existingLeaveApplication.setOtherReason(leaveApplicationDetails.getOtherReason());
            existingLeaveApplication.setTotalNoOfDays(leaveApplicationDetails.getTotalNoOfDays());

            // Add more fields as needed

            return leaveApplicationRepository.save(existingLeaveApplication);
        } else {
            throw new RuntimeException("Leave Application not found with id " + id);
        }
    }
  // New service method to get count of pending leave applications
  public long getPendingLeaveCount(long companyId, long employeeId) {
    return leaveApplicationRepository.countByCompanyRegistrationIdAndEmployeeIdAndRequestStatus(
            companyId, employeeId, "PENDING");
}
public long countPendingRequestsByCriteria(long companyId, String workflowDivision,
String workflowDepartment, String workflowRole) {
// Call the repository method to get the count
return leaveApplicationRepository.countPendingRequests(companyId, workflowDivision, workflowDepartment, workflowRole);
}
public List<LeaveApplication> getLeaveApplicationsBetweenDates(long companyId, long employeeId, Date fromDate, Date toDate) {
return leaveApplicationRepository.findLeaveApplicationsBetweenDates(companyId, employeeId, fromDate, toDate);
}











/////////////////////////////////////
/// 
/// 
/// 
/// 
public List<LeaveApplication> findByCriteriaNew(long companyId, String workflowDivision, String workflowDepartment,
        String workflowRole, long projectId) {
    
    List<LeaveApplication> leaveApplications = leaveApplicationRepository
            .findByCompanyRegistrationIdAndWorkflowDivisionAndWorkflowDepartmentAndWorkflowRoleOrderByFromDateDesc(
                    companyId, workflowDivision, workflowDepartment, workflowRole);

    // Filter based on projectId in employee.generateProjects
    return leaveApplications.stream()
            .filter(leave -> leave.getEmployee() != null &&
                    leave.getEmployee().getGenerateProjects().stream()
                            .anyMatch(project -> project.getProjectId() == projectId))
            .collect(Collectors.toList());
}

public List<LeaveApplication> findByCriteriaList(long companyId, List<Long> projectIds, String workflowDivision, String workflowDepartment, String workflowRole) {
    return leaveApplicationRepository.findByCompanyAndProjects(
            companyId, projectIds, workflowDivision, workflowDepartment, workflowRole);
}


}