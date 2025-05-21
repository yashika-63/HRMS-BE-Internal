package com.spectrum.Confirmation.ConfirmationRecord.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.EmployeeConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.EmployeeConfigRepository;
import com.spectrum.Confirmation.ConfirmationRecord.model.ConfirmationRecord;
import com.spectrum.Confirmation.ConfirmationRecord.repository.ConfirmationRecordRepository;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;

@Service
public class ConfirmationRecordService {

    @Autowired
    private ConfirmationRecordRepository confirmationRecordRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

     @Autowired
    private EmailService emailService; // Make sure this is available and configured

    
    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository; // Make sure this is available and configured



    @Autowired
    private EmployeeConfigRepository employeeConfigRepository;


public ConfirmationRecord saveConfirmationRecord(Long employeeId, Long responsiblePersonId,
                                                 Long hrId, Long companyId, boolean status) {
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

    Employee responsiblePerson = employeeRepository.findById(responsiblePersonId)
            .orElseThrow(() -> new RuntimeException("Responsible person not found"));

    Employee hr = employeeRepository.findById(hrId)
            .orElseThrow(() -> new RuntimeException("HR not found"));

    CompanyRegistration company = companyRegistrationRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));

    // Check EmployeeConfig for confirmation status
    Optional<EmployeeConfig> employeeConfigOpt = employeeConfigRepository.findByEmployeeAndCompany(employee, company);
    if (employeeConfigOpt.isEmpty()) {
        throw new RuntimeException("EmployeeConfig not found for employee and company.");
    }

    EmployeeConfig employeeConfig = employeeConfigOpt.get();
    
    if (employeeConfig.isConfirmationStatus()) {
        throw new RuntimeException("Confirmation already completed for this employee.");
    }

    // Create confirmation record only if status is false
    ConfirmationRecord record = new ConfirmationRecord();
    record.setEmployee(employee);
    record.setEmployees(responsiblePerson);
    record.setEmployeehr(hr);
    record.setCompany(company);
    record.setResponsiblePersonAction(false);
    record.setStatus(status);

    ConfirmationRecord savedRecord = confirmationRecordRepository.save(record);

    // Compose and send email to responsible person
    String to = responsiblePerson.getEmail();
    if (to != null && !to.isEmpty()) {
        String subject = "Confirmation Process Pending";
        String body = String.format(
            "Dear %s,\n\nPlease complete the confirmation process for employee %s (%s).\n\nThank you,\nHR Team\n\nRegards,\n%s",
            responsiblePerson.getFirstName(),
            employee.getFirstName(),
            employee.getEmployeeId(),
            company.getCompanyName()
        );

        emailService.sendEmail(to, subject, body);
    }

    return savedRecord;
}




    public List<ConfirmationRecord> getActiveRecordsByCompanyId(Long companyId) {
        return confirmationRecordRepository.findByCompanyIdAndStatusTrue(companyId);
    }

    public List<ConfirmationRecord> getActiveRecordsByResponsiblePersionId(Long resposiblePersonId) {
        return confirmationRecordRepository.findByEmployeesIdAndStatusTrue(resposiblePersonId);
    }

    public List<ConfirmationRecord> getPendingConfirmationRecordsByResponsiblePerson(Long responsiblePersonId) {
        return confirmationRecordRepository
                .findByEmployeesIdAndResponsiblePersonActionFalseAndStatusTrue(responsiblePersonId);
    }
    

    public List<ConfirmationRecord> getConfirmedActionsByMonthAndYear(Long companyId, int year, int month) {
        return confirmationRecordRepository.findConfirmedActionsByCompanyMonthAndYear(companyId, year, month);
    }
    
    public List<ConfirmationRecord> getRejectedActionsByMonthAndYear(Long companyId, int year, int month) {
        return confirmationRecordRepository.findRejectedActionsByCompanyMonthAndYear(companyId, year, month);
    }

    

    public List<ConfirmationRecord> getExtendedActionsByMonthAndYear(Long companyId, int year, int month) {
        return confirmationRecordRepository.findExtendedActionsByCompanyMonthAndYear(companyId, year, month);
    }
    
 


    public List<Map<String, Object>> getExtendedActionsWithDetails(Long companyId, int year, int month) {
        List<Object[]> results = confirmationRecordRepository.findExtendedActionsWithEmployeeDetails(companyId, year, month);
        
        List<Map<String, Object>> response = new ArrayList<>();
    
        for (Object[] row : results) {
            ConfirmationRecord cr = (ConfirmationRecord) row[0];
            Employee emp = (Employee) row[1];
            Employee rp = (Employee) row[2];
    
            Map<String, Object> map = new HashMap<>();
            map.put("recordId", cr.getId());
            map.put("date", cr.getDate());
            map.put("status", cr.isStatus());
            map.put("responsiblePersonAction", cr.isResponsiblePersonAction());
    
            // Employee Details
            map.put("employeeId", emp.getId());
            map.put("employeeName", emp.getFirstName() + " " + emp.getLastName());
            map.put("employeeEmail", emp.getEmail());
    
            // Responsible Person Details
            map.put("responsiblePersonId", rp.getId());
            map.put("responsiblePersonName", rp.getFirstName() + " " + rp.getLastName());
            map.put("responsiblePersonEmail", rp.getEmail());
    
            response.add(map);
        }
    
        return response;
    }
    



    public List<Map<String, Object>> getConfirmedActionsWithDetails(Long companyId, int year, int month) {
        List<Object[]> results = confirmationRecordRepository.findConfirmedActionsWithEmployeeDetails(companyId, year, month);
    
        return buildResponseList(results);
    }
    
    public List<Map<String, Object>> getTerminatedActionsWithDetails(Long companyId, int year, int month) {
        List<Object[]> results = confirmationRecordRepository.findTerminatedActionsWithEmployeeDetails(companyId, year, month);
    
        return buildResponseList(results);
    }
    
    
    private List<Map<String, Object>> buildResponseList(List<Object[]> results) {
        List<Map<String, Object>> response = new ArrayList<>();
    
        for (Object[] row : results) {
            ConfirmationRecord cr = (ConfirmationRecord) row[0];
            Employee emp = (Employee) row[1];
            Employee rp = (Employee) row[2];
    
            Map<String, Object> map = new HashMap<>();
            map.put("recordId", cr.getId());
            map.put("date", cr.getDate());
            map.put("status", cr.isStatus());
            map.put("responsiblePersonAction", cr.isResponsiblePersonAction());
    
            map.put("employeeId", emp.getId());
            map.put("employeeName", emp.getFirstName() + " " + emp.getLastName());
            map.put("employeeEmail", emp.getEmail());
    
            map.put("responsiblePersonId", rp.getId());
            map.put("responsiblePersonName", rp.getFirstName() + " " + rp.getLastName());
            map.put("responsiblePersonEmail", rp.getEmail());
    
            response.add(map);
        }
    
        return response;
    }
    
}
