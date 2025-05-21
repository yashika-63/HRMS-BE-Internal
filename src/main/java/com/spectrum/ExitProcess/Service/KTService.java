package com.spectrum.ExitProcess.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spectrum.ExitProcess.Model.KnowledgeTransfer;
import com.spectrum.ExitProcess.Model.OffBoarding;
import com.spectrum.ExitProcess.Repo.KTRepo;
import com.spectrum.ExitProcess.Repo.OffBoardingRepo;
import com.spectrum.model.Employee;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.transaction.Transactional;

@Service
public class KTService {

    @Autowired
    private KTRepo ktRepo;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OffBoardingRepo offBoardingRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CompanyRegistrationRepository compayRepo;

    public List<KnowledgeTransfer> getAllKnowledgeTransfers() {
        return ktRepo.findAll();
    }

    public KnowledgeTransfer getKnowledgeTransferById(Long id) {
        return ktRepo.findById(id).orElseThrow(() -> new RuntimeException("Id not Found"));
    }

    public KnowledgeTransfer saveKnowledgeTransfer(KnowledgeTransfer knowledgeTransfer) {
        return ktRepo.save(knowledgeTransfer);
    }

    public void deleteKnowledgeTransfer(Long id) {
        ktRepo.deleteById(id);
    }

    ////////////////////////////////////////////////////////////////////////////////

    // @Transactional
    // public List<KnowledgeTransfer> saveKnowledgeTransfer(Long offBoardingId,
    // Long employeeById,
    // List<Long> employeeToIds,
    // KnowledgeTransfer knowledgeTransferTemplate) {
    // // Validate if employees and offBoarding exist
    // Employee employeeBy = employeeRepository.findById(employeeById)
    // .orElseThrow(() -> new IllegalArgumentException("Employee_by not present"));

    // OffBoarding offBoarding = offBoardingRepo.findById(offBoardingId)
    // .orElseThrow(() -> new IllegalArgumentException("OffBoarding not present"));

    // List<KnowledgeTransfer> savedTransfers = new ArrayList<>();

    // // Process each employee_to
    // for (Long employeeToId : employeeToIds) {
    // Employee employeeTo = employeeRepository.findById(employeeToId)
    // .orElseThrow(() -> new IllegalArgumentException("Employee_to with ID " +
    // employeeToId + " not present"));

    // // Create a new KnowledgeTransfer for each employee_to
    // KnowledgeTransfer knowledgeTransfer = new KnowledgeTransfer();

    // // Copy properties from the template
    // BeanUtils.copyProperties(knowledgeTransferTemplate, knowledgeTransfer);

    // // Set specific relationships
    // knowledgeTransfer.setOffBoarding(offBoarding);
    // knowledgeTransfer.setEmployee(employeeTo); // employee_to (receiving
    // knowledge)
    // knowledgeTransfer.setEmployees(employeeBy); // employee_by (giving knowledge)
    // knowledgeTransfer.setCompletionStatus(false);

    // // Save each knowledge transfer
    // KnowledgeTransfer savedTransfer = ktRepo.save(knowledgeTransfer);
    // savedTransfers.add(savedTransfer);

    // // Send email to employee_to
    // String emailToContent = String.format(
    // "You have received a knowledge transfer from %s %s.",
    // employeeBy.getFirstName(),
    // employeeBy.getLastName());

    // emailService.sendEmail(
    // employeeTo.getEmail(),
    // "Knowledge Transfer Received",
    // emailToContent);
    // }

    // // Send summary email to employee_by
    // String emailByContent = String.format(
    // "You have assigned knowledge transfers to %d employees: %s",
    // employeeToIds.size(),
    // employeeToIds.stream()
    // .map(id -> employeeRepository.findById(id).get().getFirstName())
    // .collect(Collectors.joining(", ")));

    // emailService.sendEmail(
    // employeeBy.getEmail(),
    // "Knowledge Transfers Assigned",
    // emailByContent);

    // return savedTransfers;
    // }

    // @Transactional
    // public List<KnowledgeTransfer> saveKnowledgeTransfer(Long offBoardingId,
    // Long employeeById,
    // List<Long> employeeToIds,
    // List<KnowledgeTransfer> knowledgeTransferTemplates) {
    // // Validate if employees and offBoarding exist
    // Employee employeeBy = employeeRepository.findById(employeeById)
    // .orElseThrow(() -> new IllegalArgumentException("Employee_by not present"));

    // OffBoarding offBoarding = offBoardingRepo.findById(offBoardingId)
    // .orElseThrow(() -> new IllegalArgumentException("OffBoarding not present"));

    // List<KnowledgeTransfer> savedTransfers = new ArrayList<>();
    // Map<Long, List<String>> employeeTransferMap = new HashMap<>(); // To track
    // transfers per employee

    // // Process each employee_to
    // for (Long employeeToId : employeeToIds) {
    // Employee employeeTo = employeeRepository.findById(employeeToId)
    // .orElseThrow(() -> new IllegalArgumentException("Employee_to with ID " +
    // employeeToId + " not present"));

    // List<String> transferTitles = new ArrayList<>();

    // // Process each template
    // for (KnowledgeTransfer template : knowledgeTransferTemplates) {
    // // Create a new KnowledgeTransfer
    // KnowledgeTransfer knowledgeTransfer = new KnowledgeTransfer();
    // BeanUtils.copyProperties(template, knowledgeTransfer);

    // // Set relationships
    // knowledgeTransfer.setOffBoarding(offBoarding);
    // knowledgeTransfer.setEmployee(employeeTo);
    // knowledgeTransfer.setEmployees(employeeBy);
    // knowledgeTransfer.setCompletionStatus(false);

    // // Save
    // KnowledgeTransfer savedTransfer = ktRepo.save(knowledgeTransfer);
    // savedTransfers.add(savedTransfer);
    // transferTitles.add(template.getTitle());
    // }

    // // Store transfers for this employee for email
    // employeeTransferMap.put(employeeToId, transferTitles);

    // // Send detailed email to this employee_to
    // sendEmployeeToEmail(employeeBy, employeeTo, transferTitles);
    // }

    // // Send summary email to employee_by
    // sendEmployeeBySummaryEmail(employeeBy, employeeToIds, employeeTransferMap);

    // return savedTransfers;
    // }

    // private void sendEmployeeToEmail(Employee employeeBy, Employee employeeTo,
    // List<String> transferTitles) {
    // String emailContent = String.format(
    // "Dear %s %s,\n\n" +
    // "You have been assigned the following knowledge transfers from %s %s:\n\n" +
    // "%s\n\n" +
    // "Please coordinate with them to complete these transfers.\n\n" +
    // "Regards,\nKnowledge Transfer System",
    // employeeTo.getFirstName(),
    // employeeTo.getLastName(),
    // employeeBy.getFirstName(),
    // employeeBy.getLastName(),
    // transferTitles.stream()
    // .map(title -> "• " + title)
    // .collect(Collectors.joining("\n"))
    // );

    // emailService.sendEmail(
    // employeeTo.getEmail(),
    // "New Knowledge Transfers Assigned to You",
    // emailContent
    // );
    // }

    // private void sendEmployeeBySummaryEmail(Employee employeeBy, List<Long>
    // employeeToIds,
    // Map<Long, List<String>> employeeTransferMap) {
    // StringBuilder emailContent = new StringBuilder();
    // emailContent.append(String.format(
    // "Dear %s %s,\n\n" +
    // "You have successfully assigned knowledge transfers to %d employees:\n\n",
    // employeeBy.getFirstName(),
    // employeeBy.getLastName(),
    // employeeToIds.size()
    // ));

    // for (Long employeeToId : employeeToIds) {
    // Employee employeeTo = employeeRepository.findById(employeeToId).orElse(null);
    // if (employeeTo != null) {
    // emailContent.append(String.format(
    // "• %s %s received %d transfers: %s\n",
    // employeeTo.getFirstName(),
    // employeeTo.getLastName(),
    // employeeTransferMap.get(employeeToId).size(),
    // String.join(", ", employeeTransferMap.get(employeeToId))
    // ));
    // }
    // }

    // emailContent.append("\nYou will be notified when they complete these
    // transfers.\n\n");
    // emailContent.append("Regards,\nKnowledge Transfer System");

    // emailService.sendEmail(
    // employeeBy.getEmail(),
    // "Summary of Your Assigned Knowledge Transfers",
    // emailContent.toString()
    // );
    // }

    @Transactional
    public KnowledgeTransfer saveKnowledgeTransfer(Long offBoardingId,
            Long employeeById,
            Long employeeToId,
            KnowledgeTransfer knowledgeTransfer) {

        // Validate employees and offBoarding exist
        Employee employeeBy = employeeRepository.findById(employeeById)
                .orElseThrow(() -> new IllegalArgumentException("Employee_by not present"));

        Employee employeeTo = employeeRepository.findById(employeeToId)
                .orElseThrow(() -> new IllegalArgumentException("Employee_to not present"));

        OffBoarding offBoarding = offBoardingRepo.findById(offBoardingId)
                .orElseThrow(() -> new IllegalArgumentException("OffBoarding not present"));

        // Create new KnowledgeTransfer
        KnowledgeTransfer newTransfer = new KnowledgeTransfer();
        BeanUtils.copyProperties(knowledgeTransfer, newTransfer);

        // Set relationships
        newTransfer.setOffBoarding(offBoarding);
        newTransfer.setEmployee(employeeTo);
        newTransfer.setEmployees(employeeBy);
        newTransfer.setCompletionStatus(false);

        // Save and return
        KnowledgeTransfer savedTransfer = ktRepo.save(newTransfer);

        // Send email
        String emailContent = String.format(
                "You have received a knowledge transfer from %s %s: %s - %s",
                employeeBy.getFirstName(),
                employeeBy.getLastName(),
                savedTransfer.getTitle(),
                savedTransfer.getDescription());

        emailService.sendEmail(
                employeeTo.getEmail(),
                "Knowledge Transfer Received",
                emailContent);

        return savedTransfer;
    }

    public List<KnowledgeTransfer> getKnowledgeTransfersByEmployeeBy(Long employeeToId) {
        if (employeeToId == null) {
            throw new RuntimeException("Employee ID is null");

        }
        Employee employeeTo = employeeRepository.findById(employeeToId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Employee_to with ID " + employeeToId + " not present"));
        return ktRepo.findByEmployeesId(employeeToId);

    }

    public List<KnowledgeTransfer> getFilteredResignations(Long employeeById, Long employeeToId, LocalDate startDate,
            LocalDate endDate, Boolean completionStatus) {
        if (employeeById != null) {
            return ktRepo.findByEmployeesId(employeeById);
        } else if (employeeToId != null) {
            return ktRepo.findByEmployeeId(employeeToId);
        } else if (startDate != null && endDate != null) {
            return ktRepo.findByDateBetween(startDate, endDate);
        } else if (completionStatus != null) {
            return ktRepo.findByCompletionStatus(completionStatus);
        } else {
            return ktRepo.findAll();
        }
    }

    public KnowledgeTransfer updateKnowledgeTransfer(
            Long id,
            Long employeeToId,
            Long employeeById,
            KnowledgeTransfer updatedKnowledgeTransfer) {

        // Find existing knowledge transfer by ID and employee IDs
        Optional<KnowledgeTransfer> existingTransfer = ktRepo
                .findByIdAndEmployeeIdAndEmployeesId(id, employeeToId, employeeById);

        if (!existingTransfer.isPresent()) {
            throw new RuntimeException("Wrong Id Misatched");
        }

        KnowledgeTransfer knowledgeTransfer = existingTransfer.get();
        if (Boolean.TRUE.equals(knowledgeTransfer.getCompletionStatus())) {
            throw new IllegalStateException("Cannot update - knowledge transfer already completed");
        }
        // Update fields
        if (updatedKnowledgeTransfer.getTitle() != null) {
            knowledgeTransfer.setTitle(updatedKnowledgeTransfer.getTitle());
        }

        if (updatedKnowledgeTransfer.getDate() != null) {
            knowledgeTransfer.setDate(updatedKnowledgeTransfer.getDate());
        }
        knowledgeTransfer.setCompletionStatus(true);

        if (updatedKnowledgeTransfer.getDescription() != null) {
            knowledgeTransfer.setDescription(updatedKnowledgeTransfer.getDescription());
        }

        return ktRepo.save(knowledgeTransfer);
    }

    public List<KnowledgeTransfer> getByCompletionStatusAndEmpToId(Boolean completionStatus, Long empToId) {
        if (empToId == null || completionStatus == null) {
            throw new RuntimeException("You are missing some paramter");
        }
        // if(){
        // throw new RuntimeException("Completion status is Null");
        // }

        Employee employeeTo = employeeRepository.findById(empToId)
                .orElseThrow(() -> new IllegalArgumentException("Employee_to with ID " + empToId + " not present"));
        return ktRepo.findByCompletionStatusAndEmployee_Id(completionStatus, empToId);
    }



    // public KnowledgeTransfer updateCompletionStatus(Long knowledgeTransferId, Long employeeToId) {
    //     KnowledgeTransfer kt = ktRepo.findByIdAndEmployeeToId(knowledgeTransferId, employeeToId)
    //         .orElseThrow(() -> new ResponseStatusException(
    //             HttpStatus.NOT_FOUND, 
    //             "Knowledge Transfer not found for given ID and Employee ID"
    //         ));
    //     kt.setCompletionStatus(true);
    //     return ktRepo.save(kt);
    // }
    public List<KnowledgeTransfer> getKnowledgeTransfersByOffBoardingId(Long offBoardingId) {
        if(offBoardingId==null){
            throw new RuntimeException("OffBoarding is Empty");
        }
        OffBoarding offBoarding = offBoardingRepo.findById(offBoardingId)
        .orElseThrow(() -> new IllegalArgumentException("OffBoarding not present"));
        return ktRepo.findByOffBoardingId(offBoardingId);
      }








      
      public String markKnowledgeTransferComplete(Long recordId, Long employeeToId) {
        if(recordId==null || employeeToId==null ){
            throw new RuntimeException("Some of the parammeter is missing");
        }
        Employee employeeTo = employeeRepository.findById(employeeToId)
        .orElseThrow(() -> new IllegalArgumentException("Employee_to not present"));


        KnowledgeTransfer var1=ktRepo.findById(recordId).orElseThrow(() -> new RuntimeException("Id not Found"));

          Optional<KnowledgeTransfer> optionalTransfer = ktRepo.findById(recordId);
      
          
          if (optionalTransfer.isEmpty()) {
              return "Knowledge Transfer record not found.";
          }
      
          KnowledgeTransfer transfer = optionalTransfer.get();
          if (transfer.getCompletionStatus()) {
            return "This knowledge transfer has already been marked as completed.";
        }
          if (!transfer.getEmployee().getId().equals(employeeToId)) {
              return "Employee ID does not match the assigned employee.";
          }
      
          transfer.setCompletionStatus(true);
          ktRepo.save(transfer);
      
          // Send email notifications
          String subject = "Knowledge Transfer Completed";
          String messageToBy = "KT '" + transfer.getTitle() + "' marked complete by " + transfer.getEmployee().getEmail();
          String messageToTo = "You have marked '" + transfer.getTitle() + "' as completed.";
      
          emailService.sendEmail(transfer.getEmployees().getEmail(), subject, messageToBy);
          emailService.sendEmail(transfer.getEmployee().getEmail(), subject, messageToTo);
      
          return "Knowledge transfer marked as completed and emails sent.";
      }
      public List<KnowledgeTransfer> getTransfersBySourceEmployeeId(Long employeeById) {
        if(employeeById==null){
            throw new RuntimeException("Parameter is Null");
        }
        List<KnowledgeTransfer> transfers = ktRepo.findByEmployeesId(employeeById);
 
        if (transfers == null || transfers.isEmpty()) {
            throw new IllegalArgumentException("No knowledge transfers found for employee with ID " + employeeById);
        }
        return transfers;
 
    }
 
    public List<KnowledgeTransfer> getTransfersByTargetEmployeeId(Long employeeToId) {
        if (employeeToId == null) {
            throw new RuntimeException("Parameter is null");
        }
   
        List<KnowledgeTransfer> transfers = ktRepo.findByEmployeeId(employeeToId);
   
        if (transfers == null || transfers.isEmpty()) {
            throw new IllegalArgumentException("No knowledge transfers found for employee with ID " + employeeToId);
        }
   
        return transfers;
    }
 
    
}
