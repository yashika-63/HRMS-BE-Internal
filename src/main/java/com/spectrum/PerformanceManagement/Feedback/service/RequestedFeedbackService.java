package com.spectrum.PerformanceManagement.Feedback.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spectrum.PerformanceManagement.Feedback.model.RequestedFeedback;
import com.spectrum.PerformanceManagement.Feedback.model.RequestedFeedbackDetails;
import com.spectrum.PerformanceManagement.Feedback.repository.RequestedFeedbackDetailsRepository;
import com.spectrum.PerformanceManagement.Feedback.repository.RequestedFeedbackRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class RequestedFeedbackService {

    @Autowired
    private RequestedFeedbackRepository requestedFeedbackRepository;

    @Autowired
    private RequestedFeedbackDetailsRepository requestedFeedbackDetailsRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmailService emailService;

     @Autowired
    private JavaMailSender mailSender; // Inject JavaMailSender

    public RequestedFeedback saveFeedback(Long employeeId, RequestedFeedback requestedFeedback) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Set default values for RequestedFeedback
        requestedFeedback.setEmployee(employee);
        requestedFeedback.setStatus(true);
        requestedFeedback.setApproval(false);
        requestedFeedback.setDate(LocalDate.now());
        requestedFeedback.setEndDate(LocalDate.now().plusDays(30));

        // **Ensure feedbackDetails list is not null**
        if (requestedFeedback.getFeedbackDetails() != null) {
            for (RequestedFeedbackDetails details : requestedFeedback.getFeedbackDetails()) {
                details.setRequestedFeedback(requestedFeedback); // Set before saving
            }
        }

        // Save the RequestedFeedback entity
        RequestedFeedback savedFeedback = requestedFeedbackRepository.save(requestedFeedback);

        // **Fetch requestedToEmployee using requestedToEmployeeId**
        Employee requestedToEmployee = employeeRepository.findById(requestedFeedback.getRequestedToEmployeeId())
                .orElseThrow(() -> new RuntimeException("Requested-To Employee not found"));

        // **Send Email Notification**
        sendFeedbackNotification(requestedToEmployee.getEmail(), requestedFeedback);

        return savedFeedback;
    }

    private void sendFeedbackNotification(String recipientEmail, RequestedFeedback feedback) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(recipientEmail);
            helper.setSubject("New Feedback Request");
            helper.setText(
                "Dear Employee,\n\n" +
                "You have received a new feedback request.\n\n" +
                "Feedback Description: " + feedback.getFeedbackDescription() + "\n" +
                "Overall Rating: Remaning \n" +
                "Please provide your feedback before: " + feedback.getEndDate() + "\n\n" +
                "Best regards,\nHR Team",
                false
            );

            mailSender.send(message);
            System.out.println("📧 Email Sent Successfully to " + recipientEmail);
        } catch (MessagingException e) {
            System.err.println("❌ Error Sending Email: " + e.getMessage());
        }
    }
    

    // Runs every day at 8 AM to check endDate
    @Scheduled(cron = "0 22 17 * * ?") // Runs every day at 8 AM
public void checkEndDateAndSendWarnings() {
    LocalDate threeDaysBefore = LocalDate.now().plusDays(3);

    // Fetch feedback records where endDate is 3 days away and status is true
    List<RequestedFeedback> feedbackList = requestedFeedbackRepository.findByEndDateAndStatus(threeDaysBefore, true);
    
    for (RequestedFeedback feedback : feedbackList) {
        // Fetch the employee using requestedToEmployeeId
        Employee requestedToEmployee = employeeRepository.findById(feedback.getRequestedToEmployeeId())
                .orElse(null);

        if (requestedToEmployee != null) {
            String employeeEmail = requestedToEmployee.getEmail();
            String subject = "⏳ Reminder: Feedback Deadline Approaching!";
            String body = "Dear " + requestedToEmployee.getFirstName() + ",\n\n"
                        + "This is a reminder that your feedback request will expire on " + feedback.getEndDate() + ".\n"
                        + "Please submit your feedback before the deadline.\n\n"
                        + "Best regards,\n" + feedback.getEmployee().getCompanyRegistration().getCompanyName();

            emailService.sendEmail(employeeEmail, subject, body);
        }
    }
}



public void updateApprovalStatus(Long feedbackId, boolean approvalStatus) {
    // Fetch the feedback using the provided feedbackId
    RequestedFeedback feedback = requestedFeedbackRepository.findById(feedbackId)
            .orElseThrow(() -> new RuntimeException("Requested Feedback not found"));

    // Set the approval status
    feedback.setApproval(approvalStatus);

    // Fetch the employee to whom the feedback is requested
    Employee requestedToEmployee = employeeRepository.findById(feedback.getRequestedToEmployeeId())
            .orElseThrow(() -> new RuntimeException("Employee not found"));

    // Send the appropriate email based on approval status
    String subject;
    String body;
    String employeeEmail = requestedToEmployee.getEmail();

    if (approvalStatus) {
        // If feedback is approved
        subject = "✅ Feedback Request Approved!";
        body = "Dear " + requestedToEmployee.getFirstName() + ",\n\n"
                + "Your feedback request has been approved. Please proceed with your feedback.\n"
                + "Deadline: " + feedback.getEndDate() + "\n\n"
                + "Best regards,\n" + feedback.getEmployee().getCompanyRegistration().getCompanyName();
    } else {
        // If feedback is rejected
        subject = "❌ Feedback Request Rejected";
        body = "Dear " + requestedToEmployee.getFirstName() + ",\n\n"
                + "Your feedback request has been rejected. Please reach out for clarification.\n"
                + "Deadline: " + feedback.getEndDate() + "\n\n"
                + "Best regards,\n" + feedback.getEmployee().getCompanyRegistration().getCompanyName();
    }

    // Save the updated feedback with the new approval status
    requestedFeedbackRepository.save(feedback);

    // Send email notification
    emailService.sendEmail(employeeEmail, subject, body);
}




public List<RequestedFeedback> getFeedbackByStatusAndEmployeeId(boolean status, int requestedToEmployeeId) {
    return requestedFeedbackRepository.findByStatusAndRequestedToEmployeeId(status, requestedToEmployeeId);
}


public RequestedFeedback updateFeedbackById(
        Long feedbackId, String notes, int overallRating, 
        List<RequestedFeedbackDetails> feedbackDetails, int requestedToEmployeeId) {

    // Fetch RequestedFeedback by ID
    RequestedFeedback requestedFeedback = requestedFeedbackRepository.findById(feedbackId)
            .orElseThrow(() -> new RuntimeException("Requested Feedback not found"));

    // Update fields
    requestedFeedback.setNotes(notes);
    requestedFeedback.setOverallRating(overallRating);
    requestedFeedback.setStatus(false);  // Set status to false after update

    // Fetch Employee by ID
    Employee requestedToEmployee = employeeRepository.findById(requestedToEmployeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

    // Update feedbackDetails (ratings) for each item in the list
    for (RequestedFeedbackDetails detail : feedbackDetails) {
        detail.setRequestedFeedback(requestedFeedback);
    }
    requestedFeedback.setFeedbackDetails(feedbackDetails); // Ensure feedback details are updated
    requestedFeedbackDetailsRepository.saveAll(feedbackDetails); // Save all details at once

    // Send email notification
    String employeeEmail = requestedToEmployee.getEmail();
    String subject = "Feedback Request Status Update";
    String body = "Dear " + requestedToEmployee.getFirstName() + ",\n\n"
            + "Your feedback request has been updated. The status has been changed to 'Completed'.\n"
            + "Notes: " + notes + "\n"
            + "Rating: " + overallRating + "\n\n"
            + "Best regards,\nYour Company";

    emailService.sendEmail(employeeEmail, subject, body);

    // Save the updated RequestedFeedback entity
    return requestedFeedbackRepository.save(requestedFeedback);
}




public List<RequestedFeedback> getFeedbackByEmployeeIdAndYear(Long employeeId, int year) {
    return requestedFeedbackRepository.findByEmployeeIdAndYear(employeeId, year);
}
}
