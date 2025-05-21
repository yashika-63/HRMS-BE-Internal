package com.spectrum.Training.service;

import com.spectrum.Training.model.EmployeeTrainingHeader;
import com.spectrum.Training.model.TrainingApprovalRequest;
import com.spectrum.Training.repository.EmployeeTrainingHeaderRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class EmployeeTrainingService {

    @Autowired
    private EmployeeTrainingHeaderRepository headerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

 
    @Autowired
    private JavaMailSender mailSender; // For sending email


    @Autowired
    private EmailService emailService;


    @Transactional
    public void saveEmployeeTraining(List<EmployeeTrainingHeader> trainingHeaders, Long employeeId) {
        // Fetch employee (who is requesting training)
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

        // Collect training names and reporting manager email
        StringBuilder trainingList = new StringBuilder();
        String reportingManagerEmail = null;

        for (EmployeeTrainingHeader header : trainingHeaders) {
            // Fetch the reporting manager using reportingmanagerId
            Employee reportingManager = employeeRepository.findById((long) header.getReportingmanagerId())
                    .orElseThrow(() -> new IllegalArgumentException("Reporting Manager not found with ID: " + header.getReportingmanagerId()));

            // Set reporting manager email (use only one email for all records)
            if (reportingManagerEmail == null) {
                reportingManagerEmail = reportingManager.getEmail();
            }

            // Append training name to the list
            trainingList.append("- ").append(header.getTraining()).append("\n");

            // Set training details
            header.setEmployee(employee); // Assign employee
            header.setStatus(true); // New request, set status as true
            header.setManagerApprovalStatus(false); // Not yet approved
            header.setPercentageComplete(0); // not started
            // Save training request
            headerRepository.save(header);
        }

        // Send a single email notification to the reporting manager
        if (reportingManagerEmail != null && trainingList.length() > 0) {
            sendEmailNotification(reportingManagerEmail, employee.getFirstName(), trainingList.toString());
        }
    }

    private void sendEmailNotification(String managerEmail, String employeeName, String trainingDetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(managerEmail);
            message.setSubject("New Training Requests Approval Required");
            message.setText("Dear Manager,\n\n" +
                    employeeName + " has requested the following training(s):\n" +
                    trainingDetails +
                    "\nPlease review and approve these requests within 7 days.\n\n" +
                    "Best Regards,\nHR Team");

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }






    public List<EmployeeTrainingHeader> getPendingApprovalsByManager(Long reportingManagerId) {
        return headerRepository.findByReportingmanagerIdAndManagerApprovalStatusFalse(reportingManagerId);
    }



    public List<EmployeeTrainingHeader> getTrainingByEmployeeAndYear(Long employeeId, int year) {
        return headerRepository.findByEmployeeIdAndYear(employeeId, year);
    }



    @Transactional
    public void updateManagerApproval(int reportingManagerId, List<TrainingApprovalRequest> approvals) {
        if (approvals.isEmpty()) {
            throw new RuntimeException("No training records provided for update.");
        }

        // Fetch training records based on provided IDs and reportingManagerId
        List<Long> trainingIds = approvals.stream().map(TrainingApprovalRequest::getId).toList();
        List<EmployeeTrainingHeader> trainingRecords = headerRepository.findByIdInAndReportingmanagerId(trainingIds, reportingManagerId);

        if (trainingRecords.isEmpty()) {
            throw new RuntimeException("No matching training records found for the given manager.");
        }

        // Map of employeeId -> List of training details for bulk email notifications
        Map<Long, List<String>> employeeTrainingMap = new HashMap<>();

        for (EmployeeTrainingHeader training : trainingRecords) {
            for (TrainingApprovalRequest approval : approvals) {
                if (training.getId().equals(approval.getId())) {
                    training.setManagerApprovalStatus(true);
                    training.setManagerApproval(approval.getManagerApproval());
                    headerRepository.save(training);

                    // Collect training details for each employee
                    employeeTrainingMap
                            .computeIfAbsent(training.getEmployee().getId(), k -> new ArrayList<>())
                            .add(training.getTraining() + " (" + (approval.getManagerApproval() ? "Approved" : "Rejected") + ")");
                }
            }
        }

        // Send consolidated email notifications to employees
        for (Map.Entry<Long, List<String>> entry : employeeTrainingMap.entrySet()) {
            Long employeeId = entry.getKey();
            List<String> trainingDetails = entry.getValue();

            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

            String subject = "Training Approval Update";
            String message = "Your training requests for: \n" + String.join(", ", trainingDetails) +
                    " have been reviewed by your manager.";

            emailService.sendEmail(employee.getEmail(), subject, message);
        }
    }


    public EmployeeTrainingHeader updateTrainingHeader(Long id, EmployeeTrainingHeader updatedTrainingHeader) {
        Optional<EmployeeTrainingHeader> existingTrainingHeaderOpt = headerRepository.findById(id);
    
        if (existingTrainingHeaderOpt.isPresent()) {
            EmployeeTrainingHeader existingTrainingHeader = existingTrainingHeaderOpt.get();
    
            // Update only if the field is provided (not null)
            if (updatedTrainingHeader.getTraining() != null) {
                existingTrainingHeader.setTraining(updatedTrainingHeader.getTraining());
            }
            if (updatedTrainingHeader.getEmployee() != null) {
                existingTrainingHeader.setEmployee(updatedTrainingHeader.getEmployee());
            }
    
            // Only update boolean fields if they are explicitly set
            if (updatedTrainingHeader.getStatus() != null) {
                existingTrainingHeader.setStatus(updatedTrainingHeader.getStatus());
            }
            if (updatedTrainingHeader.getManagerApprovalStatus() != null) {
                existingTrainingHeader.setManagerApprovalStatus(updatedTrainingHeader.getManagerApprovalStatus());
            }
            if (updatedTrainingHeader.getManagerApproval() != null) {
                existingTrainingHeader.setManagerApproval(updatedTrainingHeader.getManagerApproval());
            }
    
            if (updatedTrainingHeader.getReportingmanagerId() != 0) {
                existingTrainingHeader.setReportingmanagerId(updatedTrainingHeader.getReportingmanagerId());
            }
            if (updatedTrainingHeader.getPercentageComplete() != 0) {
                existingTrainingHeader.setPercentageComplete(updatedTrainingHeader.getPercentageComplete());
            }
    
            // Save and return updated entity
            return headerRepository.save(existingTrainingHeader);
        } else {
            throw new RuntimeException("Training Header not found with id: " + id);
        }
    }
    
}

