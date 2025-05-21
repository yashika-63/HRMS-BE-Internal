package com.spectrum.Induction.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import com.spectrum.Induction.Model.AssignInduction;
import com.spectrum.Induction.Model.Inductions;
import com.spectrum.Induction.Repo.AssignInductionRepo;
import com.spectrum.Induction.Repo.InductionsRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EducationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;
import org.springframework.data.domain.Pageable;

import jakarta.transaction.Transactional;

@Service
public class AssignInductionService {
  @Autowired
    private AssignInductionRepo assignInductionRepo;
  @Autowired
private EmployeeRepository employeeRepository;
    @Autowired
    private EmailService emailService;
     @Autowired
    private EmployeeRepository eRepo;

    @Autowired
    private CompanyRegistrationRepository companyRepo;

    @Autowired
    private AssignInductionRepo assignInduction;
 

    @Autowired
    private InductionsRepository inductionRepository;
    public AssignInduction save(AssignInduction assignInduction) {
        return assignInductionRepo.save(assignInduction);
    }

    public List<AssignInduction> getAll() {
        return assignInductionRepo.findAll();
    }

    public AssignInduction getById(Long id) {
        return assignInductionRepo.findById(id).orElseThrow(()-> new RuntimeException("Not Found"));
    }

    public AssignInduction update(Long id, AssignInduction updatedInduction) {
        return assignInductionRepo.findById(id).map(existing -> {
            // existing.setAssignedById(updatedInduction.getAssignedById());
            existing.setAssignDate(updatedInduction.getAssignDate());
            existing.setExpiryStatus(updatedInduction.getExpiryStatus());
            existing.setCompletionStatus(updatedInduction.getCompletionStatus());
            return assignInductionRepo.save(existing);
        }).orElseThrow(() -> new RuntimeException("AssignInduction not found with id: " + id));
    }

    // public void delete(Long id) {
    //     assignInductionRepo.deleteById(id);
    // }
    //
  
   

    public List<AssignInduction> saveAllWithNotification(
        List<AssignInduction> assignList, Long employeeId, Long inductionId, Long assignedById,Long compId) {
            if(employeeId==null||inductionId==null||assignedById==null||compId==null){
                throw new RuntimeException("Some Parameter is null");
            }

    Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
    Inductions induction = inductionRepository.findById(inductionId).orElseThrow(() -> new RuntimeException("Induction not found with ID: " + inductionId));
    CompanyRegistration var1 = companyRepo.findById(compId).orElseThrow(() -> new RuntimeException("Company not found with ID: " + compId));
    Employee var2 = employeeRepository.findById(assignedById).orElseThrow(() -> new RuntimeException("Assign not found with ID: " + assignedById));

    

    if (employee == null || induction == null) {
        throw new RuntimeException("Employee or Induction not found!");
    }

    assignList.forEach(assign -> {
       assign.setCompletionStatus(false);
        assign.setExpiryStatus(false);
        // assign.setStatus(true);
        assign.setEmployee(employee);
        assign.setInduction(induction);
        assign.setAssignedBy(assignedById);
        assign.setCompanyId(compId); // ✅ Set assignedById

        // Emails
        String employeeEmail = employee.getEmail();
        String assignedByEmail = getEmailByUserId(assignedById); // ✅ Use passed assignedById

        String subject = "Induction Assigned - Complete within 10 days";
        String message = "You have been assigned an induction task on " + assign.getAssignDate()
                + ". Please complete it within 10 days.";

        if (employeeEmail != null) {
            emailService.sendEmail(employeeEmail, subject, message);
        }

        if (assignedByEmail != null) {
            emailService.sendEmail(assignedByEmail, "You assigned an induction", 
                "You assigned an induction to employee on " + assign.getAssignDate());
        }
    });

    return assignInductionRepo.saveAll(assignList);
}

public String getEmailByUserId(long userId) {
    return employeeRepository.findById(userId)
            .map(Employee::getEmail)
            .orElse(null); // or throw new RuntimeException("User not found!");
}
public List<AssignInduction> getAssignmentsByEmployeeId(Long employeeId) {
    return assignInductionRepo.findByEmployeeId(employeeId);
}

public List<AssignInduction> getByCompanyIdAndYear(Long companyId, int year) {
    return assignInductionRepo.findAllByCompanyIdAndYear(companyId, year);
}
///////////////////////////////////// scheduler 
  // Runs every day at 9 AM

// @Transactional
// // @Scheduled(cron = "*/10 * * * * ?")
// @Scheduled(cron = "0 0 9 * * ?")
// public void sendInductionReminderEmails() {
//       List<AssignInduction> assignments = assignInductionRepo.findAll();

//       for (AssignInduction assign : assignments) {

//           if (assign.getAssignDate() == null || Boolean.TRUE.equals(assign.getStatus())) {
//               continue; // Skip if no assignDate or already completed
//           }

//           LocalDate assignDate = assign.getAssignDate();
//           long daysSinceAssigned = ChronoUnit.DAYS.between(assignDate, LocalDate.now());

//           if (daysSinceAssigned == 5 || daysSinceAssigned == 9) {
//               Employee employee = assign.getEmployee();

//               if (employee != null && employee.getEmail() != null) {
//                   String email = employee.getEmail();
//                   String subject = "Reminder: Induction Incomplete - Day " + daysSinceAssigned;
//                   String message = "Dear " + employee.getFirstName() + ",\n\n" +
//                           "You were assigned an induction on " + assignDate + ".\n" +
//                           "This is a reminder to complete your induction. It has been " + daysSinceAssigned +
//                           " days.\n\nPlease complete it at the earliest.\n\nRegards,\nHR Team";

//                   emailService.sendEmail(email, subject, message);
//               }
//           }

          
//       }
//   }




// Every day at 9:00 AM
// @Scheduled(cron = "*/10 * * * * ?")
// @Transactional // Important to prevent LazyInitializationException
// @Scheduled(cron = "0 0 9 * * ?") 
// public void sendInductionReminderEmails() {
//     List<AssignInduction> assignments = assignInductionRepo.findAll();

//     for (AssignInduction assign : assignments) {

//         if (assign.getAssignDate() == null || Boolean.TRUE.equals(assign.getCompletionStatus())) {
//             continue; // Skip if no assignDate or already completed
//         }

//         LocalDate assignDate = assign.getAssignDate();
//         long daysSinceAssigned = ChronoUnit.DAYS.between(assignDate, LocalDate.now());

//         Employee employee = assign.getEmployee();

//         if (employee != null && employee.getEmail() != null) {
//             String email = employee.getEmail();
//             String firstName = employee.getFirstName();

//             if (daysSinceAssigned == 5 || daysSinceAssigned == 9 && !assign.getCompletionStatus()) {
//                 String subject = "Reminder: Induction Incomplete - Day " + daysSinceAssigned;
//                 String message = "Dear " + firstName + ",\n\n" +
//                         "You were assigned an induction on " + assignDate + ".\n" +
//                         "This is a reminder to complete your induction. It has been " + daysSinceAssigned +
//                         " days.\n\nPlease complete it at the earliest.\n\nRegards,\nHR Team";

//                 emailService.sendEmail(email, subject, message);
//             }

//             if (daysSinceAssigned >= 10 && !assign.getCompletionStatus()) {
//                 // Update expiry status
//                 assign.setExpiryStatus(true);
//                 assignInductionRepo.save(assign); // Persist change

//                 String subject = "Final Reminder: Induction Expired";
//                 String message = "Dear " + firstName + ",\n\n" +
//                         "Your induction assigned on " + assignDate + " has not been completed even after 10 days.\n" +
//                         "It is now marked as expired.\n\nPlease report to your manager immediately.\n\nRegards,\nHR Team";

//                 emailService.sendEmail(email, subject, message);
//             }
//         }
//     }
// }


// @Transactional // Prevent LazyInitializationException
//  @Scheduled(cron = "0 0 9 * * ?")  // Runs daily at 9 AM
// public void sendInductionReminderEmails() {
//     List<AssignInduction> assignments = assignInductionRepo.findAll();

//     for (AssignInduction assign : assignments) {

//         if (assign.getAssignDate() == null || Boolean.TRUE.equals(assign.getCompletionStatus())) {
//             continue; // Skip if no assignDate or already completed
//         }

//         LocalDate assignDate = assign.getAssignDate();
//         long daysSinceAssigned = ChronoUnit.DAYS.between(assignDate, LocalDate.now());

//         Employee employee = assign.getEmployee();
//         String employeeEmail = (employee != null) ? employee.getEmail() : null;
//         System.out.println("employeeEmail"+employeeEmail);
//         String firstName = (employee != null) ? employee.getFirstName() : "Employee";

//         // Fetch the assigner's email (if assignedBy exists)
//         String assignedByEmail = null;
//         if (assign.getAssignedBy() != null) {
//             Optional<Employee> assignedBy = employeeRepository.findById(assign.getAssignedBy());
//             if (assignedBy.isPresent()) {
//                 assignedByEmail = assignedBy.get().getEmail();
//                 System.out.println("assignedByEmail"+assignedByEmail );
//             }
//         }

//         if (employeeEmail != null) {

//             if ((daysSinceAssigned == 5 || daysSinceAssigned == 9) && !assign.getCompletionStatus()) {

//                 String subject = "Reminder: Induction Incomplete - Day " + daysSinceAssigned;
//                 String message = "Dear " + firstName + ",\n\n" +
//                         "You were assigned an induction on " + assignDate + ".\n" +
//                         "This is a reminder to complete your induction. It has been " + daysSinceAssigned +
//                         " days.\n\nPlease complete it at the earliest.\n\nRegards,\nHR Team";

//                 emailService.sendEmail(employeeEmail, subject, message);
//             }

//             if (daysSinceAssigned >= 10 && !assign.getCompletionStatus() && !assign.getExpiryStatus()) {

//                 // Update expiry status
//                 assign.setExpiryStatus(true);
//                 assignInductionRepo.save(assign); // Persist change

//                 // Notify employee about expiry
//                 String subject = "Final Reminder: Induction Expired";
//                 String message = "Dear " + firstName + ",\n\n" +
//                         "Your induction assigned on " + assignDate + " has not been completed even after 10 days.\n" +
//                         "It is now marked as expired.\n\nPlease report to your manager immediately.\n\nRegards,\nHR Team";

//                 emailService.sendEmail(employeeEmail, subject, message);

//                 // Notify assigner about employee failure
//                 if (assignedByEmail != null) {
//                     String assignerSubject = "Notification: Assigned Induction Expired";
//                     String assignerMessage = "The induction you assigned on " + assignDate +
//                             " was not completed by the employee (" + firstName + ") and is now marked as expired.\n\nRegards,\nHR Team";

//                     emailService.sendEmail(assignedByEmail, assignerSubject, assignerMessage);
//                 }
//             }
//         }
//     }
// }


public List<AssignInduction> getAssignedByCompanyAndDateRange(Long companyId, LocalDate startDate, LocalDate endDate) {
    CompanyRegistration var1 = companyRepo.findById(companyId).orElseThrow(() -> new RuntimeException("Induction not found with ID: " + companyId));
    // 2. Validate dates are not empty/null
    if (startDate.isAfter(endDate)) {
        throw new IllegalArgumentException("Start date must be before or equal to end date");
    }

     

    return assignInductionRepo.findAllByCompanyIdAndAssignDateBetween(companyId, startDate, endDate);
}
 // Service method to filter inductions based on employeeId, completionStatus, and expiryStatus
 public List<AssignInduction> filterInductions(Long employeeId, Boolean completionStatus, Boolean expiryStatus) {
    
    Employee var1 = eRepo.findById(employeeId)
    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

    return assignInductionRepo.filterInductions(employeeId, completionStatus, expiryStatus);
}

@Scheduled(cron = "0 0 9 * * ?") // Runs daily at 9 AM
// @Scheduled(cron = "*/10 * * * * ?") // For testing every 10 seconds
@Transactional
public void checkInductionCompletionAndSendNotifications() {
    List<AssignInduction> assignList = assignInductionRepo.findAll();
    LocalDate today = LocalDate.now();

   
        


    for (AssignInduction assign : assignList) {
        System.out.println(assign);
        LocalDate assignDate = assign.getAssignDate();
        LocalDate expiryDate = assign.getExpiryDate();
        
        if (isExpiryDateInPast(expiryDate, today)) {
            System.out.println("Invalid expiry date for assignment: " + assign);
            continue; // Skip this assignment
           }

        // Calculate total days between assign and expiry
        long totalDays = ChronoUnit.DAYS.between(assignDate, expiryDate);
        
        // Calculate warning periods (50% and 90% of total time)
        LocalDate firstWarningDate = assignDate.plusDays(totalDays / 2);
        LocalDate finalWarningDate = assignDate.plusDays((long)(totalDays * 0.9));
        
        // Get emails
        String employeeEmail = getEmployeeEmail(assign);
        String assignedByEmail = getAssignedByEmail(assign);

        // First warning (at 50% of period)
        if (today.equals(firstWarningDate)) {
            long daysRemaining = ChronoUnit.DAYS.between(today, expiryDate);
            sendWarningEmail(employeeEmail, assignDate, 
                "First Reminder: Induction Incomplete!", 
                daysRemaining);
        }
        // Final warning (at 90% of period)
        else if (today.equals(finalWarningDate)) {
            long daysRemaining = ChronoUnit.DAYS.between(today, expiryDate);
            sendWarningEmail(employeeEmail, assignDate,
                "Final Reminder: Induction About to Expire!", 
                daysRemaining);
        }
        // Expiry day or after
        else if (today.isAfter(expiryDate) || today.equals(expiryDate)) {
            handleExpiry(assign, employeeEmail, assignedByEmail, assignDate, expiryDate);
        }
    }
}

private String getEmployeeEmail(AssignInduction assign) {
    return assign.getEmployee() != null ? assign.getEmployee().getEmail() : null;
}

private String getAssignedByEmail(AssignInduction assign) {
    if (assign.getAssignedBy() == null) return null;
    return employeeRepository.findById(assign.getAssignedBy())
            .map(Employee::getEmail)
            .orElse(null);
}

private void sendWarningEmail(String email, LocalDate assignDate, String subject, long daysRemaining) {
    if (email != null) {
        String message = String.format(
            "You were assigned an induction on %s.%n" +
            "Please complete it within %d days to avoid expiry.",
            assignDate, daysRemaining);
        emailService.sendEmail(email, subject, message);
    }
}

private void handleExpiry(AssignInduction assign, String employeeEmail, 
                         String assignedByEmail, LocalDate assignDate, LocalDate expiryDate) {
    assign.setExpiryStatus(true);
    assignInductionRepo.save(assign);

    // Notify employee
    if (employeeEmail != null) {
        String subject = "Induction Expired: Action Required!";
        String message = String.format(
            "Your induction assigned on %s has expired " +
            "because it was not completed by the expiry date %s.",
            assignDate, expiryDate);
        emailService.sendEmail(employeeEmail, subject, message);
    }

    // Notify assigner
    if (assignedByEmail != null) {
        String subject = "Assigned Induction Not Completed!";
        String message = String.format(
            "The induction you assigned on %s has not been completed " +
            "by the employee and expired on %s.",
            assignDate, expiryDate);
        emailService.sendEmail(assignedByEmail, subject, message);
    }
}


private boolean isExpiryDateInPast(LocalDate expiryDate, LocalDate today) {
    
  return expiryDate.isBefore(today);
    }
    // public List<AssignInduction> getAssignedByCompany(Long companyId) {
    //     CompanyRegistration var1 = companyRepo.findById(companyId).orElseThrow(() -> new RuntimeException("Induction not found with ID: " + companyId));
    //     // 2. Validate dates are not empty/null
       
         
     
    //     return assignInductionRepo.findAllByCompanyId(companyId);
    // }
    
}
