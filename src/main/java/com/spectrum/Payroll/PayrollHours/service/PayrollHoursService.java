package com.spectrum.Payroll.PayrollHours.service;

import com.spectrum.Payroll.PayrollHours.model.PayrollHours;
import com.spectrum.Payroll.PayrollHours.repository.PayrollHoursRepository;
import com.spectrum.Payroll.PayrollRecord.model.PayrollRecord;
import com.spectrum.Payroll.PayrollRecord.repository.PayrollRecordRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

import com.spectrum.Payroll.PayrollRecord.model.PayrollRecord;
import com.spectrum.Payroll.PayrollRecord.repository.PayrollRecordRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PayrollHoursService {

    @Autowired
    private PayrollHoursRepository payrollHoursRepository;

    @Autowired
    private PayrollRecordRepository payrollRecordRepository;

    @Autowired

    private EmployeeRepository employeeRepository;
   
    // Save PayrollHours data
    public List<PayrollHours> savePayrollHoursList(List<PayrollHours> payrollHoursList) {
        return payrollHoursRepository.saveAll(payrollHoursList);
    }

    // Get PayrollHours data by employeeId
    public List<PayrollHours> getPayrollHoursByEmployeeId(Long employeeId) {
        return payrollHoursRepository.findByEmployeeId(employeeId);
    }

    // Update PayrollHours data by id
    public PayrollHours updatePayrollHours(Long id, PayrollHours updatedPayrollHours) {
        Optional<PayrollHours> optionalPayrollHours = payrollHoursRepository.findById(id);
        if (optionalPayrollHours.isPresent()) {
            PayrollHours existingPayrollHours = optionalPayrollHours.get();
            existingPayrollHours.setAssignHours(updatedPayrollHours.getAssignHours());
            existingPayrollHours.setExpectedHours(updatedPayrollHours.getExpectedHours());
            existingPayrollHours.setActualHours(updatedPayrollHours.getActualHours());
            return payrollHoursRepository.save(existingPayrollHours);
        }
        return null; // Handle null or throw an exception as required
    }

    // Delete PayrollHours data by id
    public void deletePayrollHours(Long id) {
        payrollHoursRepository.deleteById(id);
    }

    // // Method to store Payroll Hours for a company by companyId
    // public void storePayrollHoursForCompany(Long companyId) {
    //     List<PayrollHours> payrollHoursList = payrollHoursRepository.findRecordsByCompanyId(companyId);

    //     for (PayrollHours payrollHours : payrollHoursList) {
    //         // Create new PayrollHours record
    //         PayrollRecord payrollRecord = new PayrollRecord();

    //         // PayrollHours newPayrollHours = new PayrollHours();
    //         payrollRecord.setDate(LocalDate.now());
    //         payrollRecord.setAssignHours(payrollHours.getAssignHours());
    //         payrollRecord.setExpectedHours(payrollHours.getExpectedHours());
    //         payrollRecord.setActualHours(payrollHours.getActualHours());
    //         payrollRecord.setEmployee(payrollHours.getEmployee());
    //         payrollRecord.setCompany(payrollHours.getEmployee().getCompanyRegistration());

    //         // You can set additional fields like assign hours, expected hours, actual hours
    //         // based on your logic.

    //         // Save the new PayrollHours record
    //         // payrollHoursRepository.save(newPayrollHours);
    //         payrollRecordRepository.save(payrollRecord);

    //     }
    // }

// Method to store Payroll Hours for a company by companyId
public void storePayrollHoursForCompany(Long companyId) {
    // Fetch PayrollHours records for the specified company
    List<PayrollHours> payrollHoursList = payrollHoursRepository.findRecordsByCompanyId(companyId);

    for (PayrollHours payrollHours : payrollHoursList) {
        // Only process records with status = true
        if (Boolean.TRUE.equals(payrollHours.getStatus())) {
            // Create a new PayrollRecord object
            PayrollRecord payrollRecord = new PayrollRecord();

            // Set the properties of PayrollRecord from PayrollHours
            payrollRecord.setDate(LocalDate.now());
            payrollRecord.setAssignHours(payrollHours.getAssignHours());
            payrollRecord.setExpectedHours(payrollHours.getExpectedHours());
            payrollRecord.setActualHours(payrollHours.getActualHours());
            payrollRecord.setEmployee(payrollHours.getEmployee());
            payrollRecord.setCompany(payrollHours.getEmployee().getCompanyRegistration());

            // Save the new PayrollRecord
            payrollRecordRepository.save(payrollRecord);
        }
    }
}

    


public List<PayrollHours> getPayrollHoursByCompanyAndMonth(Long companyId, int month) {
    return payrollHoursRepository.findByCompanyIdAndStatusAndMonth(companyId, month);
}

@Transactional
public void generatePayrollRecordsForCompany(Long companyId) {
    // Fetch employees with presence = true for the given company ID
    List<Employee> employees = employeeRepository.findByPresenceAndCompanyRegistration_Id(true, companyId);

    // If no employees are found, exit gracefully
    if (employees.isEmpty()) {
        System.out.println("No employees with presence = true found for company ID: " + companyId);
        return;
    }

    // Process the employees and create PayrollHours records
    for (Employee employee : employees) {
        PayrollHours payrollHours = new PayrollHours();
        payrollHours.setAssignHours(0); // Default value
        payrollHours.setExpectedHours(0); // Default value
        payrollHours.setActualHours(0); // Default value
        payrollHours.setStatus(true); // Default status
        payrollHours.setEmployee(employee);
        payrollHoursRepository.save(payrollHours);
    }
}




public List<PayrollHours> getPayrollHoursByCompanyAndMonthAndYear(Long companyId, int month, int year) {
    return payrollHoursRepository.findByCompanyIdAndMonthAndYear(companyId, month, year);
}





public void storePayrollHoursForCompany(Long companyId, List<PayrollHours> payrollHoursList) {
    for (PayrollHours payrollHours : payrollHoursList) {
        Long employeeId = payrollHours.getEmployee().getId();
        LocalDate date = payrollHours.getDate();
        int month = date.getMonthValue();
        int year = date.getYear();

        // Check if a record exists for the employee for the given month and year
        PayrollHours existingRecord = payrollHoursRepository
                .findByEmployeeAndMonthAndYear(employeeId, month, year);

        if (existingRecord != null) {
            // Update the existing record
            existingRecord.setAssignHours(payrollHours.getAssignHours());
            existingRecord.setExpectedHours(payrollHours.getExpectedHours());
            existingRecord.setActualHours(payrollHours.getActualHours());
            payrollHoursRepository.save(existingRecord);
        } else {
            // Save a new record
            payrollHoursRepository.save(payrollHours);
        }
    }
}
}
