package com.spectrum.Training.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spectrum.Training.model.AssignTraining;
import com.spectrum.Training.model.TrainingHRMS;
import com.spectrum.Training.repository.AssignTrainingRepo;
import com.spectrum.Training.repository.TrainingRepositoary;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.transaction.Transactional;
@Service
public class AssignTrainingService {
 @Autowired
    private AssignTrainingRepo assignTrainingRepo;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired 
    private TrainingRepositoary trainingRepository;

   @Autowired
    private EmailService emailService;   
    
    @Autowired
    private CompanyRegistrationRepository companyRepo;

    // Get all assigned trainings
    public List<AssignTraining> getAllAssignedTrainings() {
        return assignTrainingRepo.findAll();
    }

    // Get assigned training by ID
    public AssignTraining getAssignTrainingById(Long id) {
        return assignTrainingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("AssignTraining with id " + id + " not found"));
    }

    // Save a new assigned training
    public AssignTraining saveAssignTraining(AssignTraining assignTraining) {
        return assignTrainingRepo.save(assignTraining);
    }

    // Delete assigned training by ID
    public void deleteAssignTraining(Long id) {
        assignTrainingRepo.deleteById(id);
    }

    // Update assigned training
    public AssignTraining updateAssignTraining(Long id, AssignTraining updatedTraining) {
        AssignTraining existing = assignTrainingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("AssignTraining with id " + id + " not found"));

        existing.setAssignedBy(updatedTraining.getAssignedBy());
        existing.setAssignDate(updatedTraining.getAssignDate());
        existing.setExpiryStatus(updatedTraining.getExpiryStatus());
        existing.setCompletionStatus(updatedTraining.getCompletionStatus());
        existing.setCompanyId(updatedTraining.getCompanyId());

        return assignTrainingRepo.save(existing);
    } 

    //////////////////////////////////////////////////////////////////////////////////////////
//     public List<AssignTraining> saveAllWithNotification(
//         List<AssignTraining> assignList, Long employeeId, Long trainingId, Long assignedById, Long compId) {

//     Employee employee = employeeRepository.findById(employeeId)
//             .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

//             Employee employee1 = employeeRepository.findById(assignedById)
//             .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

//     TrainingHRMS trainingHRMS = trainingRepository.findById(trainingId)
//             .orElseThrow(() -> new RuntimeException("Training not found with ID: " + trainingId));

//     assignList.forEach(assign -> {
//         assign.setCompletionStatus(false);
//         assign.setExpiryStatus(false);
//         assign.setEmployee(employee);
//         assign.setTrainingHRMS(trainingHRMS);
//         assign.setAssignedBy(assignedById);
//         assign.setCompanyId(compId);
//         assign.setAssignDate(LocalDate.now());

//         // Prepare email content
//         String employeeEmail = employee.getEmail();
//         String assignedByEmail = employee1.getEmail();

//         String subject = "Training Assigned - Complete within 10 days";
//         String message = "You have been assigned a training on " + assign.getAssignDate()
//                 + ". Please complete it within 10 days.";

//         if (employeeEmail != null) {
//             emailService.sendEmail(employeeEmail, subject, message);
//         }

//         if (assignedByEmail != null) {
//             emailService.sendEmail(assignedByEmail, 
//                 "You assigned a training", 
//                 "You assigned training to employee on " + assign.getAssignDate());
//         }
//     });

//     return assignTrainingRepo.saveAll(assignList);
// }

    public List<AssignTraining> saveAllWithNotification(List<Long> employeeIds, Long trainingId, Long assignedById, Long compId) {

        // Fetch the list of employees based on the provided employee IDs
        List<Employee> employees = employeeRepository.findAllById(employeeIds);

        // Validate if the provided employee IDs are valid
        if (employees.size() != employeeIds.size()) {
            throw new RuntimeException("One or more employees not found");
        }

        // Fetch the "assignedBy" employee details
        Employee assignedBy = employeeRepository.findById(assignedById)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + assignedById));

        // Fetch the TrainingHRMS
        TrainingHRMS trainingHRMS = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found with ID: " + trainingId));

        List<AssignTraining> assignList = new ArrayList<>();

        // Loop through each employee to create AssignTraining objects
        for (Employee employee : employees) {

            AssignTraining assign = new AssignTraining();
            assign.setCompletionStatus(false);
            assign.setExpiryStatus(false);
            assign.setTrainingHRMS(trainingHRMS);
            assign.setAssignedBy(assign.getAssignedBy());
            assign.setCompanyId(compId);
            assign.setAssignDate(LocalDate.now());
            assign.setEmployee(employee);

            // Send email to the employee
            if (employee.getEmail() != null) {
                String subject = "Training Assigned - Complete within 10 days";
                String message = "You have been assigned a training on " + assign.getAssignDate()
                        + ". Please complete it within 10 days.";
                emailService.sendEmail(employee.getEmail(), subject, message);
            }

            // Notify the person who assigned the training
            if (assignedBy.getEmail() != null) {
                emailService.sendEmail(
                        assignedBy.getEmail(),
                        "You assigned a training",
                        "You assigned training to employee: " + " on " + assign.getAssignDate()
                );
            }

            assignList.add(assign);
        }

        // Save all assigned training records to the database
        return assignTrainingRepo.saveAll(assignList);
    }

 @Scheduled(cron = "0 0 9 * * ?")  // runs daily at 9 AM
//  @Scheduled(cron = "*/10 * * * * ?")
@Transactional
public void checkTrainingCompletionAndSendNotifications() {

    List<AssignTraining> assignList = assignTrainingRepo.findByCompletionStatusFalseAndExpiryStatusFalse();
    LocalDate today = LocalDate.now();
  

    for (AssignTraining assign : assignList) {

        LocalDate assignDate = assign.getAssignDate();
        long daysPassed = ChronoUnit.DAYS.between(assignDate, today);
        System.out.println("Days Passed:"+daysPassed);

        // Fetch employee and assignedBy emails
        String employeeEmail = null;
        String assignedByEmail = null;

        if (assign.getEmployee() != null) {
            employeeEmail = assign.getEmployee().getEmail();
        }

        // if (assign.getAssignedBy() != null) {
        //     Optional<Employee> assignedBy = employeeRepository.findById(assign.getAssignedBy());
        //     if (assignedBy.isPresent()) {
        //         assignedByEmail = assignedBy.get().getEmail();
        //     }
        // }

        System.out.println("Assign ID: " + assign.getId() + 
                   " | Assigned Date: " + assignDate + 
                   " | Days Passed: " + daysPassed + 
                   " | Completion Status: " + assign.getCompletionStatus() + 
                   " | Expiry Status: " + assign.getExpiryStatus());

                   System.out.println(" Iam outside if :"+!assign.getCompletionStatus());
        // Proceed only if training is still incomplete
        if (!assign.getCompletionStatus()) {

            if (daysPassed >= 5 && daysPassed < 10) {
                // Warning Mail for Incomplete Training
                if (employeeEmail != null) {
                    System.out.println(" Iam inside if :"+!assign.getCompletionStatus());
                    System.out.println("Reminder: Training Incomplete");
                    String subject = "Reminder: Training Incomplete!";
                    String message = "You were assigned a training on " + assignDate +
                                     ".\nPlease complete it within 10 days to avoid expiry.";
                    emailService.sendEmail(employeeEmail, subject, message);
                }

            } else if (daysPassed >= 10) {
                // Expiration Handling
                if (!assign.getExpiryStatus()) {

                    assign.setExpiryStatus(true);  // Mark as expired in DB
                    assignTrainingRepo.save(assign);

                    // Notify employee about expiration
                    if (employeeEmail != null) {
                        String subject = "Training Expired: Action Required!";
                        String message = "Your training assigned on " + assignDate +
                                         " has expired because it was not completed within 10 days.";
                        emailService.sendEmail(employeeEmail, subject, message);
                    }

                    // Notify assigner about employee failure
                    if (assignedByEmail != null) {
                        String subject = "Assigned Training Not Completed!";
                        String message = "The training you assigned on " + assignDate +
                                         " has not been completed by the employee and is now marked as expired.";
                        emailService.sendEmail(assignedByEmail, subject, message);
                    }
                }
            }
        }
    }
}


// public List<AssignTraining> getAssignedTrainingsByEmployeeId(Long employeeId) {
   

//     return assignTrainingRepo.findByEmployeeId(employeeId);
// }

public Page<AssignTraining> getAssignedTrainingsByEmployeeId(Long employeeId, Pageable pageable) {
    Employee employee = employeeRepository.findById(employeeId)
    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
    return assignTrainingRepo.findByEmployeeId(employeeId, pageable);
}

public Page<AssignTraining> getAssignedTrainingsByCompanyAndYear(Long companyId, int year, Pageable pageable) {
        CompanyRegistration var1=companyRepo.findById(companyId).orElseThrow(()-> new RuntimeException("Company NotFound:"+companyId));

    return assignTrainingRepo.findByCompanyIdAndYear(companyId, year, pageable);
}


public Page<AssignTraining> getAssignedTrainingsByCompanyAndDateRange(Long companyId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
    CompanyRegistration var1=companyRepo.findById(companyId).orElseThrow(()-> new RuntimeException("Company NotFound:"+companyId));

    return assignTrainingRepo.findByCompanyIdAndAssignDateBetween(companyId, startDate, endDate, pageable);
}

public Page<AssignTraining> getAssignedTrainingsByEmployeeAndStatus(
    Long employeeId,
    Boolean completionStatus,
    Boolean expiryStatus,
    Pageable pageable) {
        Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
return assignTrainingRepo.findByEmployeeAndStatus(employeeId, completionStatus, expiryStatus, pageable);
}



// // @Scheduled(cron = "*/10 * * * * ?")
//  @Scheduled(cron = "0 0 9 * * ?")
// @Transactional
// public void checkTrainingCompletionAndSendNotifications() {
//     List<AssignTraining> assignList = assignTrainingRepo.findByCompletionStatusFalseAndExpiryStatusFalse();

//     LocalDate today = LocalDate.now();

//     for (AssignTraining assign : assignList) {
//         System.out.println(assign);
//         LocalDate assignDate = assign.getAssignDate();
//         long daysPassed = ChronoUnit.DAYS.between(assignDate, today);
//         int expiryDays = assign.getExpiryDays();
        
//         // Calculate warning periods (50% and 90% of expiry time)
//         int firstWarningDay = expiryDays / 2;
//         int finalWarningDay = (int) (expiryDays * 0.9); // 90% of expiry time

//         // Get emails
//         String employeeEmail = getEmployeeEmail(assign);
//         String assignedByEmail = getAssignedByEmail(assign);

//         // First warning (at 50% of expiry period)
//         if (daysPassed == firstWarningDay) {
//             sendWarningEmail(employeeEmail, assignDate, 
//                 "First Reminder: Training Incomplete!", 
//                 expiryDays - daysPassed);
//         }
//         // Final warning (at 90% of expiry period)
//         else if (daysPassed == finalWarningDay) {
//             sendWarningEmail(employeeEmail, assignDate,
//                 "Final Reminder: Training About to Expire!", 
//                 expiryDays - daysPassed);
//         }
//         // Expiry day
//         else if (daysPassed >= expiryDays) {
//             handleExpiry(assign, employeeEmail, assignedByEmail, assignDate, expiryDays);
//         }
//     }
// }

// private String getEmployeeEmail(AssignTraining assign) {
//     return assign.getEmployee() != null ? assign.getEmployee().getEmail() : null;
// }

// private String getAssignedByEmail(AssignTraining assign) {
//     if (assign.getAssignedBy() == null) return null;
//     return employeeRepository.findById(assign.getAssignedBy())
//             .map(Employee::getEmail)
//             .orElse(null);
// }

// private void sendWarningEmail(String email, LocalDate assignDate, String subject, long daysRemaining) {
//     if (email != null) {
//         String message = String.format(
//             "You were assigned a training on %s.%n" +
//             "Please complete it within %d days to avoid expiry.",
//             assignDate, daysRemaining);
//         emailService.sendEmail(email, subject, message);
//     }
// }

// private void handleExpiry(AssignTraining assign, String employeeEmail, 
//                          String assignedByEmail, LocalDate assignDate, int expiryDays) {
//     assign.setExpiryStatus(true);
//     assignTrainingRepo.save(assign);

//     // Notify employee
//     if (employeeEmail != null) {
//         String subject = "Training Expired: Action Required!";
//         String message = String.format(
//             "Your training assigned on %s has expired " +
//             "because it was not completed within %d days.",
//             assignDate, expiryDays);
//         emailService.sendEmail(employeeEmail, subject, message);
//     }

//     // Notify assigner
//     if (assignedByEmail != null) {
//         String subject = "Assigned Training Not Completed!";
//         String message = String.format(
//             "The training you assigned on %s has not been completed " +
//             "by the employee and is now marked as expired.",
//             assignDate);
//         emailService.sendEmail(assignedByEmail, subject, message);
//     }
// }
public Page<AssignTraining> getAssignedTrainingsByCompany(Long companyId, Pageable pageable) {
    CompanyRegistration var1=companyRepo.findById(companyId).orElseThrow(()-> new RuntimeException("Company NotFound:"+companyId));

return assignTrainingRepo.findByCompanyId(companyId, pageable);
}

}
