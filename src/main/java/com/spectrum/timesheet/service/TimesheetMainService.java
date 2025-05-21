package com.spectrum.timesheet.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;
import com.spectrum.timesheet.modal.TimesheetDay;
import com.spectrum.timesheet.modal.TimesheetMain;
import com.spectrum.timesheet.repository.TimesheetDayRepository;
import com.spectrum.timesheet.repository.TimesheetMainRepository;
import com.spectrum.workflow.model.WorkflowDetail;
import com.spectrum.workflow.repository.WorkflowDetailRepository;

import jakarta.transaction.Transactional;

@Service
public class TimesheetMainService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private WorkflowDetailRepository workflowDetailRepository;

    @Autowired
    private TimesheetMainRepository timesheetMainRepository;

    @Autowired
    private TimesheetDayRepository timesheetDayRepository;

    @Autowired
    private  EmailService emailService;

    // public TimesheetMain saveTimesheetMain(long employeeId, long companyId,
    // TimesheetMain timesheetMain) {
    // // Retrieve the Employee and Company entities using their respective IDs
    // Employee employee = employeeRepository.findById(employeeId)
    // .orElseThrow(() -> new RuntimeException("Employee not found"));
    // CompanyRegistration company =
    // companyRegistrationRepository.findById(companyId)
    // .orElseThrow(() -> new RuntimeException("Company not found"));

    // // Set the employee and company details in the TimesheetMain object
    // timesheetMain.setEmployee(employee);
    // timesheetMain.setCompanyRegistration(company);

    // // Save and return the TimesheetMain entity
    // return timesheetMainRepository.save(timesheetMain);
    // }

    public TimesheetMain saveTimesheetMain(long employeeId, long companyId, TimesheetMain timesheetMain) {
        // Check for date range conflicts
        if (isDateRangeConflict(employeeId, companyId, timesheetMain.getFromDate(), timesheetMain.getToDate())) {
            throw new IllegalArgumentException("Timesheet date range conflicts with existing records. Please check the timesheet.");
        }
    
        // Retrieve Employee and CompanyRegistration based on provided IDs
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
        CompanyRegistration companyRegistration = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with ID: " + companyId));
    
        // Set the Employee and CompanyRegistration on the TimesheetMain object
        timesheetMain.setEmployee(employee);
        timesheetMain.setCompanyRegistration(companyRegistration);
    
        // Save and return the TimesheetMain
        return timesheetMainRepository.save(timesheetMain);
    }
    

    // Method to check if date range conflicts with any existing records
    public boolean isDateRangeConflict(long employeeId, long companyId, Date fromDate, Date toDate) {
        List<TimesheetMain> conflictingTimesheets = timesheetMainRepository.findConflictingTimesheets(employeeId,
                companyId, fromDate, toDate);
        return !conflictingTimesheets.isEmpty();
    }

    public TimesheetMain saveTimesheetMain(long employeeId, long companyId, long workflowMainId,
            TimesheetMain timesheetMain) {
        // Fetch Employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

        // Fetch CompanyRegistration
        CompanyRegistration companyRegistration = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("CompanyRegistration not found with id: " + companyId));

        // Fetch WorkflowDetail (assuming you need the first one for the workflowMain)
        WorkflowDetail firstDetail = workflowDetailRepository.findFirstByWorkflowMainIdOrderByIdAsc(workflowMainId);

        if (firstDetail != null) {
            // Set associations and other fields
            timesheetMain.setEmployee(employee);
            timesheetMain.setCompanyRegistration(companyRegistration);
            timesheetMain.setWorkflowDivision(firstDetail.getWorkflowToDivision());
            timesheetMain.setWorkflowDepartment(firstDetail.getWorkflowToDepartment());
            timesheetMain.setWorkflowRole(firstDetail.getWorkflowNextRole());
            timesheetMain.setWorkflowMain(firstDetail.getWorkflowMain());
            timesheetMain.setRequestStatus("PENDING");

            // Save TimesheetMain
            TimesheetMain savedTimesheetMain = timesheetMainRepository.save(timesheetMain);

            // Save TimesheetDay for each day
            if (timesheetMain.getTimesheetDays() != null) {
                for (TimesheetDay day : timesheetMain.getTimesheetDays()) {
                    day.setTimesheetMain(savedTimesheetMain); // Associate with TimesheetMain
                    timesheetDayRepository.save(day);
                }
            }

            return savedTimesheetMain;
        } else {
            throw new RuntimeException("No workflow details found for the given workflowMainId: " + workflowMainId);
        }
    }

    // public List<TimesheetMain> getTimesheetsBetweenDates(Date fromDate, Date
    // toDate) {
    // return timesheetMainRepository.findByFromDateBetween(fromDate, toDate);
    // }

    // public List<TimesheetMain> getTimesheetsBetweenDates(long employeeId, long companyId, Date fromDate, Date toDate) {
    //     System.out.println("Fetching timesheets for employeeId: " + employeeId + ", companyId: " + companyId +
    //             ", fromDate: " + fromDate + ", toDate: " + toDate);

    //     List<TimesheetMain> result = timesheetMainRepository.findTimesheetsBetweenDates(employeeId, companyId, fromDate,
    //             toDate);

    //     if (result.isEmpty()) {
    //         System.out.println("No timesheets found for the given criteria.");
    //     }

    //     return result;
    // }
    public List<TimesheetMain> getTimesheetsBetweenDates(Date fromDate, Date toDate) {
        return timesheetMainRepository.findByFromDateBetween(fromDate, toDate);
    }



    public List<TimesheetMain> getTimesheetsBetweenDates(long employeeId, long companyId, Date fromDate, Date toDate) {
        // Assuming you update the repository to handle employeeId and companyId
        return timesheetMainRepository.findTimesheetsBetweenDates(employeeId, companyId, fromDate, toDate);
    }

    public List<TimesheetMain> getTimesheetsByDate(Date date) {
        return timesheetMainRepository.findByDateBetween(date);
    }

    public List<TimesheetMain> getTimesheetsByDateAndEmployeeId(Date date, long employeeId) {
        return timesheetMainRepository.findByDateAndEmployeeId(date, employeeId);
    }


































    ///////////////////////////////////////////////////////////////////////////
    /// 
    // This is the section newely developed at (27-02-2025) for timesheet approval

    public TimesheetMain updateTimesheetStatus(Long id) {
        Optional<TimesheetMain> optionalTimesheet = timesheetMainRepository.findById(id);
    
        if (optionalTimesheet.isPresent()) {
            TimesheetMain timesheet = optionalTimesheet.get();
    
            // ✅ Set requestStatus to true
            timesheet.setSendForApproval(true);
    
            // ✅ Set dateSendForApproval to the current date
            timesheet.setDateSendForApproval(LocalDate.now());
    
            // ✅ Get Employee Name (Handle potential null values)
            Employee employee = timesheet.getEmployee();
            String employeeName = (employee != null) 
                ? (employee.getFirstName() != null ? employee.getFirstName() : "") + " " + 
                  (employee.getLastName() != null ? employee.getLastName() : "")
                : "Unknown Employee";
    
            // ✅ Get Reporting Manager
            Optional<Employee> managerOpt = employeeRepository.findById((long) timesheet.getReportingManagerId());
    
            managerOpt.ifPresent(manager -> {
                String managerEmail = manager.getEmail();
    
                // ✅ Improved Email Message
                String subject = "Timesheet Approval Request for " + employeeName;
                String message = "Dear " + manager.getFirstName() + ",\n\n"
                        + "You have received a new timesheet approval request from " + employeeName + ".\n"
                        + "Please review and approve the request within 2 days; otherwise, it will be auto-approved.\n\n"
                        + "Best regards,\nYour Timesheet Management System";
    
                emailService.sendEmail(managerEmail, subject, message);
            });
    
            // ✅ Save updated timesheet
            return timesheetMainRepository.save(timesheet);
        } else {
            throw new RuntimeException("Timesheet not found with id: " + id);
        }
    }
    


    public List<TimesheetMain> getTimesheetsForApproval(int managerId) {
        return timesheetMainRepository.findByReportingManagerIdAndSendForApprovalTrue(managerId);
    }








    @Transactional
public TimesheetMain updateManagerApproval(Long id, Boolean approved, Boolean rejected) {
    Optional<TimesheetMain> optionalTimesheet = timesheetMainRepository.findById(id);

    if (optionalTimesheet.isEmpty()) {
        throw new RuntimeException("Timesheet not found with id: " + id);
    }

    TimesheetMain timesheet = optionalTimesheet.get();

    // ✅ Ensure mutual exclusivity: If approved, rejected should be false, and vice versa
    if (Boolean.TRUE.equals(approved)) {
        rejected = false;
    } else if (Boolean.TRUE.equals(rejected)) {
        approved = false;
    } else {
        throw new RuntimeException("Either approved or rejected must be true.");
    }

    // ✅ Update approval/rejection status
    timesheet.setManagerApproved(approved);
    timesheet.setManagerRejected(rejected);
    
    // ✅ Set requestStatus to false
    timesheet.setSendForApproval(false);

    // ✅ Get Employee Details
    Employee employee = timesheet.getEmployee();
    if (employee == null) {
        throw new RuntimeException("Employee details not found for this timesheet.");
    }

    String employeeEmail = employee.getEmail();
    if (employeeEmail == null || employeeEmail.isEmpty()) {
        throw new RuntimeException("Employee email not available.");
    }

    String employeeName = employee.getFirstName() + " " + employee.getLastName();

    // ✅ Send email notification based on approval/rejection
    String subject = approved ? "Timesheet Approved" : "Timesheet Rejected";
    String message = "Dear " + employeeName + ",\n\n"
            + (approved 
                ? "Your timesheet has been approved by your reporting manager."
                : "Your timesheet has been rejected by your reporting manager. Please make the necessary corrections and resubmit it.") 
            + "\n\nBest regards,\nTimesheet Management System";

    // ✅ Send email
    emailService.sendEmail(employeeEmail, subject, message);

    // ✅ Save updated timesheet
    return timesheetMainRepository.save(timesheet);
}

public List<TimesheetMain> getRejectedTimesheetsByEmployeeId(Long employeeId) {
    return timesheetMainRepository.findByEmployeeIdAndManagerRejectedTrue(employeeId);
}

}
