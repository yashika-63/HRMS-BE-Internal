package com.spectrum.workflow.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.service.EmailService;
import com.spectrum.workflow.model.LeaveApplication;
import com.spectrum.workflow.model.LeaveApproval;
import com.spectrum.workflow.repository.*;
@Service
public class LeaveApprovalService {

    @Autowired
    private leaveApprovalRepository leaveApprovalRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private EmailService emailService;

    // public LeaveApproval saveAndNotify(Long leaveApplicationId, LeaveApproval leaveApproval) throws Exception {
    //     // Retrieve the LeaveApplication using the leaveApplicationId
    //     LeaveApplication leaveApplication = leaveApplicationRepository.findById(leaveApplicationId)
    //             .orElseThrow(() -> new Exception("Leave Application not found with id: " + leaveApplicationId));

    //     // Set the LeaveApplication for the LeaveApproval
    //     leaveApproval.setLeaveApplication(leaveApplication);

    //     // Save the LeaveApproval object
    //     LeaveApproval savedLeaveApproval = leaveApprovalRepository.save(leaveApproval);

    //     // Send email notification
    //     String toEmail = leaveApproval.getMail();
    //     String subject = "Leave Approval Notification";
    //     String message = "Dear " + leaveApproval.getName() + ",\n\n" +
    //                      "Your leave request (ID: " + leaveApplicationId + ") has been " +
    //                      (leaveApproval.isAction() ? "approved" : "rejected") + ".\n" +
    //                      "Note: " + leaveApproval.getNote() + "\n\n" +
    //                      "Best regards,\nYour Company";

    //     emailService.sendEmail(toEmail, subject, message);

    //     return savedLeaveApproval;
    // }

    


















    public LeaveApproval saveAndNotify(Long leaveApplicationId, LeaveApproval leaveApproval) throws Exception {
        // Retrieve the LeaveApplication using the leaveApplicationId
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(leaveApplicationId)
                .orElseThrow(() -> new Exception("Leave Application not found with id: " + leaveApplicationId));

        // Retrieve the associated Employee and CompanyRegistration
        Employee employee = leaveApplication.getEmployee();
        CompanyRegistration company = leaveApplication.getCompanyRegistration();

        // Set the LeaveApplication for the LeaveApproval
        leaveApproval.setLeaveApplication(leaveApplication);

        // Save the LeaveApproval object
        LeaveApproval savedLeaveApproval = leaveApprovalRepository.save(leaveApproval);

        // Send email notification
        String toEmail = leaveApproval.getMail();
        String subject = "Leave Approval Notification";
        String message = String.format(
            "Dear %s\t%s\t%s ,\n\nYour leave request (ID: %d) has been %s.\n" +
            "Note: %s\n\nCompany: %s\nEmployee: %s\nDesignation: %s\n\nBest regards,\n%s",
            employee != null ? employee.getFirstName() : "N/A",
            employee != null ? employee.getMiddleName() : "N/A",
            employee != null ? employee.getLastName() : "N/A",
            leaveApplicationId,
            leaveApproval.isAction() ? "approved" : "rejected",
            leaveApproval.getNote(),
            company != null ? company.getCompanyName() : "N/A",
            employee != null ? employee.getFirstName() : "N/A",
            employee != null ? employee.getDesignation() : "N/A",
            company != null ? company.getCompanyName() : "N/A"
        );

        emailService.sendEmail(toEmail, subject, message);

        return savedLeaveApproval;
    }

    public List<LeaveApproval> getAllLeaveApprovalsByLeaveApplicationId(Long leaveApplicationId) {
        return leaveApprovalRepository.findByLeaveApplicationId(leaveApplicationId);
    }
}
