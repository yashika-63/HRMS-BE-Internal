package com.spectrum.Confirmation.ConfirmationRecord.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.EmployeeConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.EmployeeConfigRepository;
import com.spectrum.Confirmation.ConfirmationRecord.model.ConfirmationAction;
import com.spectrum.Confirmation.ConfirmationRecord.model.ConfirmationRecord;
import com.spectrum.Confirmation.ConfirmationRecord.repository.ConfirmationActionRepository;
import com.spectrum.Confirmation.ConfirmationRecord.repository.ConfirmationRecordRepository;
import com.spectrum.model.Employee;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfirmationActionService {

    @Autowired
    private  ConfirmationActionRepository confirmationActionRepository;
    @Autowired
    private  ConfirmationRecordRepository confirmationRecordRepository;
    @Autowired
    private  JavaMailSender mailSender;
    @Autowired
    private EmployeeConfigRepository employeeConfigRepository;

    public ConfirmationAction saveConfirmationAction(Long confirmationRecordId, ConfirmationAction inputAction) {
        ConfirmationRecord record = confirmationRecordRepository.findById(confirmationRecordId)
                .orElseThrow(() -> new RuntimeException("Confirmation record not found"));
    
        // Set flag in ConfirmationRecord
        record.setResponsiblePersonAction(true);
        confirmationRecordRepository.save(record);
    
        // Create and populate ConfirmationAction
        ConfirmationAction action = new ConfirmationAction();
        action.setConfirmationRecord(record);
        action.setNote(inputAction.getNote());
        action.setActionTakenBy(inputAction.getActionTakenBy());
        action.setConfirm(true);
        action.setExtend(false);
        action.setTerminate(false);
    
        // Save action
        ConfirmationAction saved = confirmationActionRepository.save(action);
    
        // ✅ Update EmployeeConfig to mark confirmation status = true
        Employee employee = record.getEmployee();
        EmployeeConfig config = employeeConfigRepository.findByEmployee(employee)
                .orElseThrow(() -> new RuntimeException("EmployeeConfig not found"));
    
        config.setConfirmationStatus(true);
        employeeConfigRepository.save(config);
    
        // Send emails
        sendConfirmationEmails(record);
    
        return saved;
    }
    
    
    
   private void sendConfirmationEmails(ConfirmationRecord record) {
    Employee emp = record.getEmployee();
    Employee hr = record.getEmployeehr();
    Employee responsible = record.getEmployees(); // assuming this is the responsible person

    String subject = "🎉 Confirmation Completed for Employee: " + emp.getFirstName() + " " + emp.getLastName();

    String message = """
        Dear Team,

        The confirmation process for the following employee has been successfully completed:

        👤 Employee Name     : %s %s
        🆔 Employee ID       : %s
        🧑‍💼 Designation       : %s
        🏢 Department         : %s
        📅 Joining Date       : %s
        📍 Division           : %s
        🎯 Role               : %s

        ✅ Action taken by    : %s (%s)
        🕒 Confirmation Date  : %s

        Please proceed with the next steps accordingly.

        Regards,  
        HRMS System
        """.formatted(
            emp.getFirstName(), emp.getLastName(),
            emp.getEmployeeId(),
            emp.getDesignation(),
            emp.getDepartment(),
            emp.getJoiningDate() != null ? new java.text.SimpleDateFormat("dd-MM-yyyy").format(emp.getJoiningDate()) : "N/A",
            emp.getDivision(),
            emp.getRole(),
            responsible.getFirstName() + " " + responsible.getLastName(),
            responsible.getEmail(),
            java.time.LocalDate.now()
        );

    sendEmail(emp.getEmail(), subject, message);
    sendEmail(hr.getEmail(), subject, message);
    sendEmail(responsible.getEmail(), subject, message);
}


    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailSender.send(mailMessage);
    }




    private void sendProbationExtensionEmail(ConfirmationRecord record, int newMonths) {
        Employee emp = record.getEmployee();
        Employee hr = record.getEmployeehr();
        Employee responsible = record.getEmployees();
    
        String subject = "📢 Probation Extended for: " + emp.getFirstName() + " " + emp.getLastName();
        String message = """
            Dear Team,
    
            The probation period for the following employee has been extended:
    
            👤 Employee Name   : %s %s
            🆔 Employee ID     : %s
            🏢 Department       : %s
            📅 Joining Date     : %s
            🕒 Extended to      : %d month(s)
    
            🧑‍💼 Action taken by: %s (%s)
    
            Please take necessary actions.
    
            Regards,  
            HRMS System
            """.formatted(
                emp.getFirstName(), emp.getLastName(),
                emp.getEmployeeId(),
                emp.getDepartment(),
                emp.getJoiningDate() != null ? new java.text.SimpleDateFormat("dd-MM-yyyy").format(emp.getJoiningDate()) : "N/A",
                newMonths,
                responsible.getFirstName() + " " + responsible.getLastName(),
                responsible.getEmail()
            );
    
        sendEmail(emp.getEmail(), subject, message);
        sendEmail(hr.getEmail(), subject, message);
        sendEmail(responsible.getEmail(), subject, message);
    }
    












    public Map<String, Object> extendProbation(Long confirmationRecordId, int newTotalMonths, String actionTakenBy, String note) {
    ConfirmationRecord record = confirmationRecordRepository.findById(confirmationRecordId)
            .orElseThrow(() -> new RuntimeException("Confirmation record not found"));

    // Update confirmation record status
    record.setResponsiblePersonAction(true);
    confirmationRecordRepository.save(record);

    // Create ConfirmationAction
    ConfirmationAction action = new ConfirmationAction();
    action.setConfirmationRecord(record);
    action.setExtend(true);
    action.setConfirm(false);
    action.setTerminate(false);
    action.setNote(note);
    action.setActionTakenBy(actionTakenBy);
    confirmationActionRepository.save(action);

    // Update probationMonth and status in EmployeeConfig
    Employee employee = record.getEmployee();
    EmployeeConfig config = employeeConfigRepository.findByEmployee(employee)
            .orElseThrow(() -> new RuntimeException("Employee config not found"));

    config.setProbationMonth(newTotalMonths);
    config.setConfirmationStatus(false);
    employeeConfigRepository.save(config);

    // Send email
    sendProbationExtensionEmail(record, newTotalMonths);

    // Response
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Probation extended successfully");
    response.put("employeeId", employee.getEmployeeId());
    response.put("newProbationMonths", newTotalMonths);
    return response;
}




public Map<String, Object> terminateEmployee(Long confirmationRecordId, String actionTakenBy, String note) {
    ConfirmationRecord record = confirmationRecordRepository.findById(confirmationRecordId)
            .orElseThrow(() -> new RuntimeException("Confirmation record not found"));

    // Update flag in ConfirmationRecord
    record.setResponsiblePersonAction(true);
    confirmationRecordRepository.save(record);

    // Create and save new ConfirmationAction
    ConfirmationAction action = new ConfirmationAction();
    action.setConfirmationRecord(record);
    action.setNote(note);
    action.setActionTakenBy(actionTakenBy);
    action.setConfirm(false);
    action.setExtend(false);
    action.setTerminate(true);
    confirmationActionRepository.save(action);

    // Send termination emails
    sendTerminationEmails(record, action);

    // Return a response map
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Employee termination recorded successfully.");
    response.put("confirmationAction", action);
    return response;
}




private void sendTerminationEmails(ConfirmationRecord record, ConfirmationAction action) {
    Employee emp = record.getEmployee();
    Employee hr = record.getEmployeehr();
    Employee responsible = record.getEmployees(); // the responsible person

    String subject = "⚠️ Termination Notice for Employee: " + emp.getFirstName() + " " + emp.getLastName();

    String message = """
        Dear Team,

        The termination process for the following employee has been executed:

        👤 Employee Name     : %s %s
        🆔 Employee ID       : %s
        🧑‍💼 Designation       : %s
        🏢 Department         : %s
        📅 Joining Date       : %s
        📍 Division           : %s
        🎯 Role               : %s

        ❌ Terminated By      : %s
        📝 Note               : %s
        📅 Date               : %s

        Please take the necessary actions.

        Regards,  
        HRMS System
        """.formatted(
            emp.getFirstName(), emp.getLastName(),
            emp.getEmployeeId(),
            emp.getDesignation(),
            emp.getDepartment(),
            emp.getJoiningDate() != null ? new java.text.SimpleDateFormat("dd-MM-yyyy").format(emp.getJoiningDate()) : "N/A",
            emp.getDivision(),
            emp.getRole(),
            action.getActionTakenBy(),
            action.getNote(),
            java.time.LocalDate.now()
        );

    sendEmail(emp.getEmail(), subject, message);
    sendEmail(hr.getEmail(), subject, message);
    sendEmail(responsible.getEmail(), subject, message);
}

}
