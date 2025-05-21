package com.spectrum.ExitProcess.Service;


import com.spectrum.ExitProcess.Model.ExitInterview;
import com.spectrum.ExitProcess.Model.OffBoarding;
import com.spectrum.ExitProcess.Repo.ExitIntervieRepo;
import com.spectrum.ExitProcess.Repo.OffBoardingRepo;
import com.spectrum.model.Employee;
import com.spectrum.repository.EducationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExitInterviewService {
    @Autowired
    private  ExitIntervieRepo exitInterviewRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private OffBoardingRepo  offBoardingRepo;


    @Autowired
    private EmailService emailService;

    
    // Create a new ExitInterview
    public ExitInterview createExitInterview(ExitInterview exitInterview) {
        exitInterview.setDate(LocalDate.now()); // Auto-set current date if not provided
        return exitInterviewRepo.save(exitInterview);
    }

    // Get all ExitInterviews
    public List<ExitInterview> getAllExitInterviews() {
        return exitInterviewRepo.findAll();
    }

    // Get ExitInterview by ID
    public ExitInterview getExitInterviewById(Long id) {
        return exitInterviewRepo.findById(id).orElseThrow(()-> new RuntimeException("Id Not found"));
    }

    // Update an ExitInterview
    public ExitInterview updateExitInterview(Long id, ExitInterview updatedInterview) {
        return exitInterviewRepo.findById(id)
                .map(interview -> {
                    interview.setAssigned(updatedInterview.getAssigned());
                    interview.setStatus(updatedInterview.getStatus());
                    return exitInterviewRepo.save(interview);
                })
                .orElseThrow(() -> new RuntimeException("ExitInterview not found with id: " + id));
    }

    // Delete an ExitInterview
    public void deleteExitInterview(Long id) {
        exitInterviewRepo.deleteById(id);
    }

     @Transactional
    public ExitInterview createExitInterview(Long employeeId, Long offBoardingId, ExitInterview exitInterview) {
        // Verify entities exist
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        OffBoarding offBoarding = offBoardingRepo.findById(offBoardingId)
                .orElseThrow(() -> new RuntimeException("OffBoarding not found with id: " + offBoardingId));

        // Set relationships
        exitInterview.setEmployee(employee);
        exitInterview.setOffBoarding(offBoarding);

        // Set current date if not provided
        if (exitInterview.getDate() == null) {
            exitInterview.setDate(LocalDate.now());
        }

        // Save the exit interview
        ExitInterview savedInterview = exitInterviewRepo.save(exitInterview);

        // Send email notification to the employee
        sendExitInterviewEmail(savedInterview, employee);

        return savedInterview;
    }

    @Transactional
    public ExitInterview createExitInterviews(Long employeeId, Long offBoardingId, ExitInterview exitInterview) {
        // Verify entities exist
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        OffBoarding offBoarding = offBoardingRepo.findById(offBoardingId)
                .orElseThrow(() -> new RuntimeException("OffBoarding not found with id: " + offBoardingId));

        // Set relationships
        exitInterview.setEmployee(employee);
        exitInterview.setOffBoarding(offBoarding);
        offBoarding.setStatus(true);

        // Set current date if not provided
        if (exitInterview.getDate() == null) {
            exitInterview.setDate(LocalDate.now());
        }

        // Save the exit interview
        ExitInterview savedInterview = exitInterviewRepo.save(exitInterview);

        // Send email notification to the employee
        sendExitInterviewEmail(savedInterview, employee);

        return savedInterview;
    }

    private void sendExitInterviewEmail(ExitInterview exitInterview, Employee employee) {
        if (employee.getEmail() != null) {
            String fullName = employee.getFirstName() + 
                            (employee.getMiddleName() != null ? " " + employee.getMiddleName() : "") + 
                            " " + employee.getLastName();
            
            String subject = "Exit Interview Scheduled";
            String message = "Dear " + fullName + ",\n\n" +
                           "Your exit interview has been scheduled on " + exitInterview.getDate() + ".\n" +
                           "Assigned to: " + exitInterview.getAssigned() + "\n" +
                           "Status: " + (exitInterview.getStatus() ? "Confirmed" : "Pending") + "\n\n" +
                           "Best regards,\n" +
                           "HR Team";

            emailService.sendEmail(employee.getEmail(), subject, message);
        }
    }

    public List<ExitInterview> getInterviewsByOffBoardingId(Long offBoardingId) {
        return exitInterviewRepo.findByOffBoardingId(offBoardingId);
    }

   
}
