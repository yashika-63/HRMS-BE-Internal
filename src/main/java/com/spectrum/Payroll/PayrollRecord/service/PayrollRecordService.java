package com.spectrum.Payroll.PayrollRecord.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.CTCModuleMain.model.CTCBreakdownHeader;
import com.spectrum.CTCModuleMain.model.CTCBreakdownRecord;
import com.spectrum.CTCModuleMain.repository.CTCBreakdownHeaderRepository;
import com.spectrum.CTCModuleMain.repository.CTCBreakdownRecordRepository;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.EmployeeConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.EmployeeConfigRepository;
import com.spectrum.Payroll.HoliDayCalender.service.EmployeeHolidayCalendarService;
import com.spectrum.Payroll.IncomeTax.model.EmployeeIncomeTax;
import com.spectrum.Payroll.IncomeTax.repository.EmployeeIncomeTaxRepository;
import com.spectrum.Payroll.PayrollHours.model.PayrollHours;
import com.spectrum.Payroll.PayrollHours.repository.PayrollHoursRepository;
import com.spectrum.Payroll.PayrollProcessed.model.PayrollProcessed;
import com.spectrum.Payroll.PayrollProcessed.repository.payrollProcessedRepository;
import com.spectrum.Payroll.PayrollRecord.model.PayrollRecord;
import com.spectrum.Payroll.PayrollRecord.repository.PayrollRecordRepository;

import jakarta.transaction.Transactional;

@Service
public class PayrollRecordService {

    @Autowired
    private PayrollHoursRepository payrollHoursRepository;
    @Autowired
    private CTCBreakdownRecordRepository ctcBreakdownRecordRepository;
    @Autowired
    private PayrollRecordRepository payrollRecordRepository;

    @Autowired
    private EmployeeHolidayCalendarService employeeHolidayCalendarService;

    @Autowired

    private payrollProcessedRepository payrollProcessedRepository;

    private static final Logger logger = LoggerFactory.getLogger(PayrollRecordService.class);

    @Autowired
    private CTCBreakdownHeaderRepository ctcBreakdownHeaderRepository;

    @Autowired
    private EmployeeConfigRepository employeeConfigRepository;


    @Autowired
    private EmployeeIncomeTaxRepository employeeIncomeTaxRepository;

    // public void storeMergedPayrollRecordsForCompany(Long companyId) {
    // // Fetch records from both sources
    // List<PayrollHours> payrollHoursList =
    // payrollHoursRepository.findRecordsByCompanyId(companyId);
    // List<CTCBreakdownRecord> ctcRecords =
    // ctcBreakdownRecordRepository.findActiveRecordsByCompanyId(companyId);

    // for (PayrollHours payrollHours : payrollHoursList) {
    // // Only process PayrollHours records with status = true
    // if (Boolean.TRUE.equals(payrollHours.getStatus())) {
    // PayrollRecord payrollRecord = new PayrollRecord();

    // // Populate PayrollRecord with data from PayrollHours
    // payrollRecord.setDate(LocalDate.now());
    // payrollRecord.setAssignHours(payrollHours.getAssignHours());
    // payrollRecord.setExpectedHours(payrollHours.getExpectedHours());
    // payrollRecord.setActualHours(payrollHours.getActualHours());
    // payrollRecord.setEmployee(payrollHours.getEmployee());
    // payrollRecord.setCompany(payrollHours.getEmployee().getCompanyRegistration());

    // // Match and populate data from CTCBreakdownRecord for the same employee
    // CTCBreakdownRecord ctcRecord = ctcRecords.stream()
    // .filter(record ->
    // record.getEmployee().getId().equals(payrollHours.getEmployee().getId()))
    // .findFirst()
    // .orElse(null);

    // if (ctcRecord != null) {
    // payrollRecord.setStaticAmount(ctcRecord.getStaticAmount());
    // payrollRecord.setVariableAmount(ctcRecord.getVariableAmount());
    // payrollRecord.setVariableAmountPerHour(ctcRecord.getVariableAmountPerHour());
    // payrollRecord.setStatus(ctcRecord.isStatus());
    // } else {
    // // If no matching CTC record, set default or null values for those fields
    // payrollRecord.setStaticAmount(0.0);
    // payrollRecord.setVariableAmount(0.0);
    // payrollRecord.setVariableAmountPerHour(0.0);
    // payrollRecord.setStatus(false);
    // }

    // // Save the new PayrollRecord
    // payrollRecordRepository.save(payrollRecord);
    // }
    // }
    // }

    // public void storeMergedPayrollRecordsForCompany(Long companyId) {
    //     // Fetch records from both sources
    //     List<PayrollHours> payrollHoursList = payrollHoursRepository.findRecordsByCompanyId(companyId);
    //     List<CTCBreakdownRecord> ctcRecords = ctcBreakdownRecordRepository.findActiveRecordsByCompanyId(companyId);
    
    //     // Loop through CTCBreakdownRecords (mandatory)
    //     for (CTCBreakdownRecord ctcRecord : ctcRecords) {
    //         double variableAmountPerHour = ctcRecord.getVariableAmountPerHour();
    //         if (ctcRecord == null || ctcRecord.getEmployee() == null) {
    //             logger.warn("Skipping CTC record with no associated employee.");
    //             continue; // Skip if CTC record or employee is null
    //         }
    
    //         Long employeeId = ctcRecord.getEmployee().getId();
    //         PayrollHours payrollHours = payrollHoursList.stream()
    //                 .filter(hours -> hours.getEmployee().getId().equals(employeeId))
    //                 .findFirst()
    //                 .orElse(null);
    
    //         PayrollRecord payrollRecord = new PayrollRecord();
    
    //         // Populate PayrollRecord with data from CTCBreakdownRecord
    //         payrollRecord.setDate(LocalDate.now());
    //         payrollRecord.setEmployee(ctcRecord.getEmployee());
    //         payrollRecord.setCompany(ctcRecord.getEmployee().getCompanyRegistration());
    //         payrollRecord.setStaticAmount(ctcRecord.getStaticAmount());
    //         payrollRecord.setVariableAmount(ctcRecord.getVariableAmount());
    //         payrollRecord.setVariableAmountPerHour(variableAmountPerHour != 0.0 ? variableAmountPerHour : 0.0);
    //         payrollRecord.setStatus(ctcRecord.isStatus());
    
    //         // If PayrollHours is present, populate corresponding fields
    //         if (payrollHours != null && Boolean.TRUE.equals(payrollHours.getStatus())) {
    //             payrollRecord.setAssignHours(payrollHours.getAssignHours());
    //             payrollRecord.setExpectedHours(payrollHours.getExpectedHours());
    //             payrollRecord.setActualHours(payrollHours.getActualHours());
    //         } else {
    //             logger.info("No PayrollHours found for Employee ID {}. Proceeding without hour details.", employeeId);
    //             payrollRecord.setAssignHours(0);
    //             payrollRecord.setExpectedHours(0);
    //             payrollRecord.setActualHours(0);
    //         }
    
    //         // Calculate actual working days and hours
    //         try {
    //             LocalDate today = LocalDate.now();
    //             int year = today.getYear();
    //             int month = today.getMonthValue();
    //             Map<String, Object> workingDaysAndHours = employeeHolidayCalendarService
    //                     .calculateActualWorkingDaysAndHoursNews(
    //                             ctcRecord.getEmployee(), year, month);
    
    //             payrollRecord.setActualWorkingDays((int) workingDaysAndHours.get("actualWorkingDays"));
    //             payrollRecord.setDailyWorkingHours((int) workingDaysAndHours.get("dailyWorkingHours"));
    //             payrollRecord.setTotalWorkingHours((int) workingDaysAndHours.get("totalWorkingHours"));
    //         } catch (Exception e) {
    //             logger.error("Error calculating working days and hours for employee ID {}: {}",
    //                     employeeId, e.getMessage(), e);
    //             payrollRecord.setActualWorkingDays(0);
    //             payrollRecord.setDailyWorkingHours(0);
    //             payrollRecord.setTotalWorkingHours(0);
    //         }
    
    //         // Save the new PayrollRecord
    //         payrollRecordRepository.save(payrollRecord);
    //     }
    // }
    




    /////////////////
    /// 
    /// 
    /// 
    /// 
    /// 
    

    public void storeMergedPayrollRecordsForCompany(Long companyId) {
        // Fetch records from both sources
        List<PayrollHours> payrollHoursList = payrollHoursRepository.findRecordsByCompanyId(companyId);
        List<CTCBreakdownRecord> ctcRecords = ctcBreakdownRecordRepository.findActiveRecordsByCompanyId(companyId);
    
        // Loop through CTCBreakdownRecords (mandatory)
        for (CTCBreakdownRecord ctcRecord : ctcRecords) {
            if (ctcRecord == null || ctcRecord.getEmployee() == null) {
                logger.warn("Skipping CTC record with no associated employee.");
                continue; // Skip if CTC record or employee is null
            }
    
            Long employeeId = ctcRecord.getEmployee().getId();
    
            // Fetch the PayrollHours record with status = true for this employee
            PayrollHours payrollHours = payrollHoursList.stream()
                    .filter(hours -> hours.getEmployee().getId().equals(employeeId) && Boolean.TRUE.equals(hours.getStatus()))
                    .findFirst()
                    .orElse(null);
    
            PayrollRecord payrollRecord = new PayrollRecord();
    
            // Populate PayrollRecord with data from CTCBreakdownRecord
            payrollRecord.setDate(LocalDate.now());
            payrollRecord.setEmployee(ctcRecord.getEmployee());
            payrollRecord.setCompany(ctcRecord.getEmployee().getCompanyRegistration());
            payrollRecord.setStaticAmount(ctcRecord.getStaticAmount());
            payrollRecord.setVariableAmount(ctcRecord.getVariableAmount());
            payrollRecord.setVariableAmountPerHour(ctcRecord.getVariableAmountPerHour() != 0.0 ? ctcRecord.getVariableAmountPerHour() : 0.0);
            payrollRecord.setStatus(ctcRecord.isStatus());
    
            // If PayrollHours is present and active, populate corresponding fields
            if (payrollHours != null) {
                payrollRecord.setAssignHours(payrollHours.getAssignHours());
                payrollRecord.setExpectedHours(payrollHours.getExpectedHours());
                payrollRecord.setActualHours(payrollHours.getActualHours());
            } else {
                logger.info("No active PayrollHours found for Employee ID {}. Proceeding without hour details.", employeeId);
                payrollRecord.setAssignHours(0);
                payrollRecord.setExpectedHours(0);
                payrollRecord.setActualHours(0);
            }
    
            // Calculate actual working days and hours
            try {
                LocalDate today = LocalDate.now();
                int year = today.getYear();
                int month = today.getMonthValue();
                Map<String, Object> workingDaysAndHours = employeeHolidayCalendarService
                        .calculateActualWorkingDaysAndHoursNews(ctcRecord.getEmployee(), year, month);
    
                payrollRecord.setActualWorkingDays((int) workingDaysAndHours.get("actualWorkingDays"));
                payrollRecord.setDailyWorkingHours((int) workingDaysAndHours.get("dailyWorkingHours"));
                payrollRecord.setTotalWorkingHours((int) workingDaysAndHours.get("totalWorkingHours"));
            } catch (Exception e) {
                logger.error("Error calculating working days and hours for employee ID {}: {}", employeeId, e.getMessage(), e);
                payrollRecord.setActualWorkingDays(0);
                payrollRecord.setDailyWorkingHours(0);
                payrollRecord.setTotalWorkingHours(0);
            }
    
            // Retrieve EmployeeConfig for the employee using Optional
            Optional<EmployeeConfig> optionalEmployeeConfig = employeeConfigRepository.findByEmployeeId(employeeId);
    
            if (optionalEmployeeConfig.isPresent()) {
                EmployeeConfig employeeConfig = optionalEmployeeConfig.get();
                payrollRecord.setOvertimeRate(employeeConfig.getOvertimeRate()); // Set overtimeRate
    
                int actualHours = payrollRecord.getActualHours() != null ? payrollRecord.getActualHours() : 0;
                int totalWorkingHours = payrollRecord.getTotalWorkingHours();
    
                if (employeeConfig.isOvertimeApplicable()) {
                    // Calculate overtime hours
                    double overtimeHours = Math.min(
                            employeeConfig.getAllowableOvertimeHours(),
                            Math.max(0, actualHours - totalWorkingHours)
                    );
                    payrollRecord.setOvertimeHours(overtimeHours);
                } else {
                    // If overtime is not applicable and actualHours > totalWorkingHours, set actualHours to totalWorkingHours
                    if (actualHours > totalWorkingHours) {
                        payrollRecord.setActualHours(totalWorkingHours);
                    }
                    payrollRecord.setOvertimeHours(0.0);
                }
            } else {
                logger.info("No EmployeeConfig found for Employee ID {}. Setting overtime hours to 0.", employeeId);
                payrollRecord.setOvertimeHours(0.0);
            }
    
            // Fetch the CTCBreakdownHeader for the employee and company
            Optional<CTCBreakdownHeader> optionalCTCHeader = ctcBreakdownHeaderRepository
                    .findByEmployeeAndCompanyAndCtcStatusTrue(ctcRecord.getEmployee(), ctcRecord.getEmployee().getCompanyRegistration());
    
            if (optionalCTCHeader.isPresent()) {
                CTCBreakdownHeader ctcBreakdownHeader = optionalCTCHeader.get();
    
                // Calculate basic amount
                double totalWorkingHours = payrollRecord.getTotalWorkingHours();
                if (totalWorkingHours > 0) {
                    double calculatedBasicAmount = ctcBreakdownHeader.getBasicAmount() / (12 * totalWorkingHours);
                    payrollRecord.setBasicAmount(calculatedBasicAmount);
                } else {
                    logger.warn("Total working hours is zero or less for Employee ID {}. Setting basicAmount to 0.", employeeId);
                    payrollRecord.setBasicAmount(0.0);
                }
            } else {
                logger.info("No active CTCBreakdownHeader found for Employee ID {}. Setting basicAmount to 0.", employeeId);
                payrollRecord.setBasicAmount(0.0);
            }
    
            // Save the new PayrollRecord
            payrollRecordRepository.save(payrollRecord);
    
            // Mark the CTCBreakdownRecord as used by setting its status to false
            ctcRecord.setStatus(false);
            ctcBreakdownRecordRepository.save(ctcRecord);
        }
    }
    

















    public void storeMergedPayrollRecordsForCompanyForMonthAndYear(Long companyId, int month, int year) {
        // Fetch records from both sources
        List<PayrollHours> payrollHoursList = payrollHoursRepository.findRecordsByCompanyIdAndMonthAndYear(companyId, month, year);
        List<CTCBreakdownRecord> ctcRecords = ctcBreakdownRecordRepository.findActiveRecordsByCompanyId(companyId);
    
        // Loop through CTCBreakdownRecords (mandatory)
        for (CTCBreakdownRecord ctcRecord : ctcRecords) {
            if (ctcRecord == null || ctcRecord.getEmployee() == null) {
                logger.warn("Skipping CTC record with no associated employee.");
                continue; // Skip if CTC record or employee is null
            }
    
            Long employeeId = ctcRecord.getEmployee().getId();
    
            // Fetch the PayrollHours record for this employee for the specified month and year
            PayrollHours payrollHours = payrollHoursList.stream()
                    .filter(hours -> hours.getEmployee().getId().equals(employeeId))
                    .findFirst()
                    .orElse(null);
    
            PayrollRecord payrollRecord = new PayrollRecord();
    
            // Populate PayrollRecord with data from CTCBreakdownRecord
            payrollRecord.setDate(LocalDate.of(year, month, 1)); // Set the date as the first day of the month
            payrollRecord.setEmployee(ctcRecord.getEmployee());
            payrollRecord.setCompany(ctcRecord.getEmployee().getCompanyRegistration());
            payrollRecord.setStaticAmount(ctcRecord.getStaticAmount());
            payrollRecord.setVariableAmount(ctcRecord.getVariableAmount());
            payrollRecord.setVariableAmountPerHour(ctcRecord.getVariableAmountPerHour() != 0.0 ? ctcRecord.getVariableAmountPerHour() : 0.0);
            payrollRecord.setStatus(ctcRecord.isStatus());
    
            // If PayrollHours is present, populate corresponding fields
            if (payrollHours != null) {
                payrollRecord.setAssignHours(payrollHours.getAssignHours());
                payrollRecord.setExpectedHours(payrollHours.getExpectedHours());
                payrollRecord.setActualHours(payrollHours.getActualHours());
            } else {
                logger.info("No PayrollHours found for Employee ID {} for month {} and year {}. Proceeding without hour details.", employeeId, month, year);
                payrollRecord.setAssignHours(0);
                payrollRecord.setExpectedHours(0);
                payrollRecord.setActualHours(0);
            }
    
            // Calculate actual working days and hours
            try {
                Map<String, Object> workingDaysAndHours = employeeHolidayCalendarService
                        .calculateActualWorkingDaysAndHoursNews(ctcRecord.getEmployee(), year, month);
    
                payrollRecord.setActualWorkingDays((int) workingDaysAndHours.get("actualWorkingDays"));
                payrollRecord.setDailyWorkingHours((int) workingDaysAndHours.get("dailyWorkingHours"));
                payrollRecord.setTotalWorkingHours((int) workingDaysAndHours.get("totalWorkingHours"));
            } catch (Exception e) {
                logger.error("Error calculating working days and hours for employee ID {}: {}", employeeId, e.getMessage(), e);
                payrollRecord.setActualWorkingDays(0);
                payrollRecord.setDailyWorkingHours(0);
                payrollRecord.setTotalWorkingHours(0);
            }
    
            // Retrieve EmployeeConfig for the employee
            Optional<EmployeeConfig> optionalEmployeeConfig = employeeConfigRepository.findByEmployeeId(employeeId);
    
            if (optionalEmployeeConfig.isPresent()) {
                EmployeeConfig employeeConfig = optionalEmployeeConfig.get();
                payrollRecord.setOvertimeRate(employeeConfig.getOvertimeRate()); // Set overtimeRate
    
                int actualHours = payrollRecord.getActualHours() != null ? payrollRecord.getActualHours() : 0;
                int totalWorkingHours = payrollRecord.getTotalWorkingHours();
    
                if (employeeConfig.isOvertimeApplicable()) {
                    // Calculate overtime hours
                    double overtimeHours = Math.min(
                            employeeConfig.getAllowableOvertimeHours(),
                            Math.max(0, actualHours - totalWorkingHours)
                    );
                    payrollRecord.setOvertimeHours(overtimeHours);
                } else {
                    // If overtime is not applicable and actualHours > totalWorkingHours, set actualHours to totalWorkingHours
                    if (actualHours > totalWorkingHours) {
                        payrollRecord.setActualHours(totalWorkingHours);
                    }
                    payrollRecord.setOvertimeHours(0.0);
                }
            } else {
                logger.info("No EmployeeConfig found for Employee ID {}. Setting overtime hours to 0.", employeeId);
                payrollRecord.setOvertimeHours(0.0);
            }
    
            // Fetch the CTCBreakdownHeader for the employee and company
            Optional<CTCBreakdownHeader> optionalCTCHeader = ctcBreakdownHeaderRepository
                    .findByEmployeeAndCompanyAndCtcStatusTrue(ctcRecord.getEmployee(), ctcRecord.getEmployee().getCompanyRegistration());
    
            if (optionalCTCHeader.isPresent()) {
                CTCBreakdownHeader ctcBreakdownHeader = optionalCTCHeader.get();
    

                // Check for active EmployeeIncomeTax record
                Optional<EmployeeIncomeTax> optionalIncomeTax = employeeIncomeTaxRepository
                        .findByEmployeeAndCtcBreakdownHeaderAndStatusTrue(ctcRecord.getEmployee(), ctcBreakdownHeader);

                if (optionalIncomeTax.isPresent()) {
                    double taxAmount = optionalIncomeTax.get().getIncomeTaxDeduction() / 12.0;
                    payrollRecord.setIncomeTaxDeduction(taxAmount);
                } else {
                    payrollRecord.setIncomeTaxDeduction(0.0);
                }

                // Calculate basic amount
                double totalWorkingHours = payrollRecord.getTotalWorkingHours();
                if (totalWorkingHours > 0) {
                    double calculatedBasicAmount = ctcBreakdownHeader.getBasicAmount() / (12 * totalWorkingHours);
                    payrollRecord.setBasicAmount(calculatedBasicAmount);
                } else {
                    logger.warn("Total working hours is zero or less for Employee ID {}. Setting basicAmount to 0.", employeeId);
                    payrollRecord.setBasicAmount(0.0);
                }
            } else {
                logger.info("No active CTCBreakdownHeader found for Employee ID {}. Setting basicAmount to 0.", employeeId);
                payrollRecord.setBasicAmount(0.0);
                payrollRecord.setIncomeTaxDeduction(0.0);

            }
    
            
            // Save the new PayrollRecord
            payrollRecordRepository.save(payrollRecord);
    
            // Mark the CTCBreakdownRecord as used by setting its status to false
            ctcRecord.setStatus(false);
            ctcBreakdownRecordRepository.save(ctcRecord);
        }
    }
    












    ///////
    /// 
    /// 
    /// 
    /// 
    /// 
    /// 























    // public void storeMergedPayrollRecordsForCompany(Long companyId) {
    //     // Fetch records from both sources
    //     List<PayrollHours> payrollHoursList = payrollHoursRepository.findRecordsByCompanyId(companyId);
    //     List<CTCBreakdownRecord> ctcRecords = ctcBreakdownRecordRepository.findActiveRecordsByCompanyId(companyId);
    
    //     // Loop through CTCBreakdownRecords (mandatory)
    //     for (CTCBreakdownRecord ctcRecord : ctcRecords) {
    //         if (ctcRecord == null || ctcRecord.getEmployee() == null) {
    //             logger.warn("Skipping CTC record with no associated employee.");
    //             continue; // Skip if CTC record or employee is null
    //         }
            
    //         Long employeeId = ctcRecord.getEmployee().getId();
    
    //         // Fetch the PayrollHours record with status = true for this employee
    //         PayrollHours payrollHours = payrollHoursList.stream()
    //                 .filter(hours -> hours.getEmployee().getId().equals(employeeId) && Boolean.TRUE.equals(hours.getStatus()))
    //                 .findFirst()
    //                 .orElse(null);

    //         PayrollRecord payrollRecord = new PayrollRecord();
    
    //         // Populate PayrollRecord with data from CTCBreakdownRecord
    //         payrollRecord.setDate(LocalDate.now());
    //         payrollRecord.setEmployee(ctcRecord.getEmployee());
    //         payrollRecord.setCompany(ctcRecord.getEmployee().getCompanyRegistration());
    //         payrollRecord.setStaticAmount(ctcRecord.getStaticAmount());
    //         payrollRecord.setVariableAmount(ctcRecord.getVariableAmount());
    //         payrollRecord.setVariableAmountPerHour(ctcRecord.getVariableAmountPerHour() != 0.0 ? ctcRecord.getVariableAmountPerHour() : 0.0);
    //         payrollRecord.setStatus(ctcRecord.isStatus());
    
    //         // If PayrollHours is present and active, populate corresponding fields
    //         if (payrollHours != null) {
    //             payrollRecord.setAssignHours(payrollHours.getAssignHours());
    //             payrollRecord.setExpectedHours(payrollHours.getExpectedHours());
    //             payrollRecord.setActualHours(payrollHours.getActualHours());
    //         } else {
    //             logger.info("No active PayrollHours found for Employee ID {}. Proceeding without hour details.", employeeId);
    //             payrollRecord.setAssignHours(0);
    //             payrollRecord.setExpectedHours(0);
    //             payrollRecord.setActualHours(0);
    //         }
    
    //         // Calculate actual working days and hours
    //         try {
    //             LocalDate today = LocalDate.now();
    //             int year = today.getYear();
    //             int month = today.getMonthValue();
    //             Map<String, Object> workingDaysAndHours = employeeHolidayCalendarService
    //                     .calculateActualWorkingDaysAndHoursNews(ctcRecord.getEmployee(), year, month);
    
    //             payrollRecord.setActualWorkingDays((int) workingDaysAndHours.get("actualWorkingDays"));
    //             payrollRecord.setDailyWorkingHours((int) workingDaysAndHours.get("dailyWorkingHours"));
    //             payrollRecord.setTotalWorkingHours((int) workingDaysAndHours.get("totalWorkingHours"));
    //         } catch (Exception e) {
    //             logger.error("Error calculating working days and hours for employee ID {}: {}", employeeId, e.getMessage(), e);
    //             payrollRecord.setActualWorkingDays(0);
    //             payrollRecord.setDailyWorkingHours(0);
    //             payrollRecord.setTotalWorkingHours(0);
    //         }
    
    //         // Retrieve EmployeeConfig for the employee using Optional
    //         Optional<EmployeeConfig> optionalEmployeeConfig = employeeConfigRepository.findByEmployeeId(employeeId);
    
    //         if (optionalEmployeeConfig.isPresent()) {
    //             EmployeeConfig employeeConfig = optionalEmployeeConfig.get();
    //             payrollRecord.setOvertimeRate(employeeConfig.getOvertimeRate()); // Set overtimeRate
    
    //             if (employeeConfig.isOvertimeApplicable()) {
    //                 int totalWorkingHours = payrollRecord.getTotalWorkingHours();
    //                 int actualHours = payrollRecord.getActualHours() != null ? payrollRecord.getActualHours() : 0;
    
    //                 // Calculate overtime hours
    //                 double overtimeHours = Math.min(
    //                         employeeConfig.getAllowableOvertimeHours(),
    //                         Math.max(0, actualHours - totalWorkingHours)
    //                 );
    
    //                 payrollRecord.setOvertimeHours(overtimeHours);
    //             } else {
    //                 payrollRecord.setOvertimeHours(0.0);
    //             }
    //         } else {
    //             logger.info("No EmployeeConfig found for Employee ID {}. Setting overtime hours to 0.", employeeId);
    //             payrollRecord.setOvertimeHours(0.0);
    //         }
    
    //        // Fetch the CTCBreakdownHeader for the employee and company
    //             Optional<CTCBreakdownHeader> optionalCTCHeader = ctcBreakdownHeaderRepository
    //             .findByEmployeeAndCompanyAndCtcStatusTrue(ctcRecord.getEmployee(), ctcRecord.getEmployee().getCompanyRegistration());

    //             if (optionalCTCHeader.isPresent()) {
    //             CTCBreakdownHeader ctcBreakdownHeader = optionalCTCHeader.get();

    //             // Calculate basic amount
    //             double totalWorkingHours = payrollRecord.getTotalWorkingHours();
    //             if (totalWorkingHours > 0) {
    //             double calculatedBasicAmount = ctcBreakdownHeader.getBasicAmount() / (12 * totalWorkingHours);
    //             payrollRecord.setBasicAmount(calculatedBasicAmount);
    //             } else {
    //             logger.warn("Total working hours is zero or less for Employee ID {}. Setting basicAmount to 0.", employeeId);
    //             payrollRecord.setBasicAmount(0.0);
    //             }
    //             } else {
    //             logger.info("No active CTCBreakdownHeader found for Employee ID {}. Setting basicAmount to 0.", employeeId);
    //             payrollRecord.setBasicAmount(0.0);
    //             }
    //         // Save the new PayrollRecord
    //         payrollRecordRepository.save(payrollRecord);

    //                 // Mark the CTCBreakdownRecord as used by setting its status to false
    //     ctcRecord.setStatus(false);
    //     ctcBreakdownRecordRepository.save(ctcRecord);

    //     }
    // }
    









    public List<PayrollRecord> getPayrollRecordsByCompanyIdAndStatus(Long companyId) {
        try {
            // Query to get records by companyId where status is false and
            // proceedForPayrollStatus is false
            return payrollRecordRepository.findByCompanyIdAndStatusFalseAndProceedForPayrollStatusFalse(companyId);
        } catch (Exception e) {
            logger.error("Error fetching payroll records for companyId {}: {}", companyId, e.getMessage(), e);
            return null;
        }
    }
    ////////////    ////////////////
    ///
    ///
    ///
    ///
    /// 
    /// 
    

    public List<PayrollRecord> getPayrollRecordsByCompanyIdWithStatusTrueAndPayrollStatusFalse(Long companyId) {
        try {
            // Query to get records by companyId where status is true and proceedForPayrollStatus is false
            return payrollRecordRepository.findByCompanyIdAndStatusTrueAndProceedForPayrollStatusFalse(companyId);
        } catch (Exception e) {
            logger.error("Error fetching payroll records for companyId {}: {}", companyId, e.getMessage(), e);
            return null;
        }
    }
    

    public List<PayrollRecord> getPayrollRecordsByCompanyIdAndDate(Long companyId, int year, int month) {
        try {
            // Fetch records based on companyId, year, and month
            return payrollRecordRepository.findByCompanyIdAndStatusTrueAndProceedForPayrollStatusTrueAndYearAndMonth(companyId, year, month);
        } catch (Exception e) {
            logger.error("Error fetching payroll records for companyId {}, year {}, month {}: {}", companyId, year, month, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
    
    
    public List<PayrollRecord> getPayrollRecordsByCompanyIdAndDateForFalse(Long companyId, int year, int month) {
        try {
            // Fetch records based on companyId, year, and month
            return payrollRecordRepository.findByCompanyIdAndStatusTrueAndProceedForPayrollStatusFalseAndYearAndMonth(companyId, year, month);
        } catch (Exception e) {
            logger.error("Error fetching payroll records for companyId {}, year {}, month {}: {}", companyId, year, month, e.getMessage(), e);
            return Collections.emptyList();
        }
    }



    public List<PayrollRecord> getPayrollRecordsByCompanyIdWithStatusTrueAndPayrollStatusTrue(Long companyId) {
        try {
            // Query to get records by companyId where status is true and proceedForPayrollStatus is false
            return payrollRecordRepository.findByCompanyIdAndStatusTrueAndProceedForPayrollStatusTrue(companyId);
        } catch (Exception e) {
            logger.error("Error fetching payroll records for companyId {}: {}", companyId, e.getMessage(), e);
            return null;
        }
    }
    ///
    ///
    ///
    ///
    ///

    public void processPayrollRecords() {
        // Fetch all PayrollRecords where proceedForPayrollStatus is true
        List<PayrollRecord> payrollRecords = payrollRecordRepository.findByProceedForPayrollStatusTrue();

        for (PayrollRecord payrollRecord : payrollRecords) {
            // Perform null and zero checks to avoid errors in calculation
            int actualWorkingDays = payrollRecord.getActualWorkingDays();
            int dailyWorkingHours = payrollRecord.getDailyWorkingHours();
            int actualHours = payrollRecord.getActualHours() != null ? payrollRecord.getActualHours() : 0;
            int totalWorkingHours = payrollRecord.getTotalWorkingHours();
            double variableAmount = payrollRecord.getVariableAmount();
            double variableAmountPerHour = payrollRecord.getVariableAmountPerHour();
            double staticAmount = payrollRecord.getStaticAmount();

            if (actualWorkingDays <= 0 || dailyWorkingHours <= 0) {
                logger.warn("Invalid data for PayrollRecord ID {}: actualWorkingDays={}, dailyWorkingHours={}",
                        payrollRecord.getId(), actualWorkingDays, dailyWorkingHours);
                continue;
            }

            // Calculate the values for PayrollProcessed
            PayrollProcessed payrollProcessed = new PayrollProcessed();

            payrollProcessed.setPayrollRecordayrollRecord(payrollRecord);
            payrollProcessed.setEmployee(payrollRecord.getEmployee());

            payrollProcessed
                    .setIdealAmountForEmployeeToPaidForDays((actualWorkingDays * variableAmount) + staticAmount);
            payrollProcessed.setIdealAmountForEmployeeToPaidForHours(
                    (totalWorkingHours * variableAmountPerHour) + staticAmount);
            payrollProcessed.setIdealWorkingdaysForThisPayroll(actualWorkingDays);
            payrollProcessed.setIdealWorkinghoursForThisPayroll(actualWorkingDays * dailyWorkingHours);

            payrollProcessed.setActualAmountForEmployeeToPaidForDays(
                    ((double) actualHours / dailyWorkingHours * variableAmount) + staticAmount);
            payrollProcessed.setActualAmountForEmployeeToPaidForHours((actualHours * variableAmountPerHour) + staticAmount  );
            payrollProcessed.setActualWorkingdaysForThisPayroll(actualHours / dailyWorkingHours);
            payrollProcessed.setActualWorkinghoursForThisPayroll(actualHours);
            payrollProcessed.setTotalDiduction(staticAmount);
            payrollProcessed.setTotalInhand((actualHours * variableAmountPerHour));
            payrollProcessed.setPaymentPaidStatus(false); // Set to false initially
            payrollProcessed.setDate(LocalDate.now());

            // Save the PayrollProcessed entity
            payrollProcessedRepository.save(payrollProcessed);
        }
    }



    
/////////
/// 
// public void processPayrollRecordsByCompanyId(Long companyId) {
//     // Fetch all PayrollRecords for the given companyId where proceedForPayrollStatus is true and proceedForPayment is false
//     List<PayrollRecord> payrollRecords = payrollRecordRepository
//             .findByCompanyIdAndProceedForPayrollStatusTrueAndProceedForPaymentFalse(companyId);

//     for (PayrollRecord payrollRecord : payrollRecords) {
//         // Perform null and zero checks to avoid errors in calculation
//         int actualWorkingDays = payrollRecord.getActualWorkingDays();
//         int dailyWorkingHours = payrollRecord.getDailyWorkingHours();
//         int actualHours = payrollRecord.getActualHours() != null ? payrollRecord.getActualHours() : 0;
//         int totalWorkingHours = payrollRecord.getTotalWorkingHours();
//         double variableAmount = payrollRecord.getVariableAmount();
//         double variableAmountPerHour = payrollRecord.getVariableAmountPerHour();
//         double staticAmount = payrollRecord.getStaticAmount();
//         double overtimeHours = payrollRecord.getOvertimeHours(); // Retrieve the total overtime hours
//         double overtimeRate = payrollRecord.getOvertimeRate(); // Retrieve the rate for overtime hours
//         double basicAmount = payrollRecord.getBasicAmount();

//         if (actualWorkingDays <= 0 || dailyWorkingHours <= 0) {
//             logger.warn("Invalid data for PayrollRecord ID {}: actualWorkingDays={}, dailyWorkingHours={}",
//                     payrollRecord.getId(), actualWorkingDays, dailyWorkingHours);
//             continue;
//         }

//         // Calculate the values for PayrollProcessed
//         PayrollProcessed payrollProcessed = new PayrollProcessed();
//         payrollProcessed.setPayrollRecordayrollRecord(payrollRecord);
//         payrollProcessed.setEmployee(payrollRecord.getEmployee());

//         payrollProcessed.setIdealAmountForEmployeeToPaidForDays(
//                 (actualWorkingDays * variableAmount) + staticAmount);
//         payrollProcessed.setIdealAmountForEmployeeToPaidForHours(
//                 (totalWorkingHours * variableAmountPerHour) + staticAmount);
//         payrollProcessed.setIdealWorkingdaysForThisPayroll(actualWorkingDays);
//         payrollProcessed.setIdealWorkinghoursForThisPayroll(actualWorkingDays * dailyWorkingHours);

//         payrollProcessed.setActualAmountForEmployeeToPaidForDays(
//                 ((double) ((actualHours / dailyWorkingHours * variableAmount) + staticAmount
//                         + (basicAmount * overtimeRate * overtimeHours))));
//         payrollProcessed.setActualAmountForEmployeeToPaidForHours(
//                 (actualHours * variableAmountPerHour) + staticAmount + (basicAmount * overtimeRate * overtimeHours));
//         payrollProcessed.setActualWorkingdaysForThisPayroll(actualHours / dailyWorkingHours);
//         payrollProcessed.setActualWorkinghoursForThisPayroll(actualHours);
//         payrollProcessed.setOvertimeAmount(basicAmount * overtimeRate * overtimeHours);
//         payrollProcessed.setPaymentPaidStatus(false); // Set to false initially
//         payrollProcessed.setTotalDiduction(staticAmount);
//         payrollProcessed.setTotalInhand((actualHours * variableAmountPerHour)
//                 + (basicAmount * overtimeRate * overtimeHours));
//         payrollProcessed.setDate(LocalDate.now());

//         // Save the PayrollProcessed entity
//         payrollProcessedRepository.save(payrollProcessed);

//         // Update proceedForPayment to true after processing
//         payrollRecord.setProceedForPayment(true);
//         payrollRecordRepository.save(payrollRecord);
//     }
// }
public void processPayrollRecordsByCompanyId(Long companyId) {
    // Fetch all PayrollRecords for the given companyId where proceedForPayrollStatus is true and proceedForPayment is false
    List<PayrollRecord> payrollRecords = payrollRecordRepository
            .findByCompanyIdAndProceedForPayrollStatusTrueAndProceedForPaymentFalse(companyId);

    for (PayrollRecord payrollRecord : payrollRecords) {
        // Perform null and zero checks to avoid errors in calculation
        int actualWorkingDays = payrollRecord.getActualWorkingDays();
        int dailyWorkingHours = payrollRecord.getDailyWorkingHours();
        int actualHours = payrollRecord.getActualHours() != null ? payrollRecord.getActualHours() : 0;
        int totalWorkingHours = payrollRecord.getTotalWorkingHours();
        double variableAmount = payrollRecord.getVariableAmount();
        double variableAmountPerHour = payrollRecord.getVariableAmountPerHour();
        double staticAmount = payrollRecord.getStaticAmount();
        double overtimeHours = payrollRecord.getOvertimeHours();
        double overtimeRate = payrollRecord.getOvertimeRate();
        double basicAmount = payrollRecord.getBasicAmount();
        double incomeTaxDeduction = payrollRecord.getIncomeTaxDeduction();
        
        if (actualWorkingDays <= 0 || dailyWorkingHours <= 0) {
            logger.warn("Invalid data for PayrollRecord ID {}: actualWorkingDays={}, dailyWorkingHours= {}",
                    payrollRecord.getId(), actualWorkingDays, dailyWorkingHours);
            continue;
        }

        // Calculate the values for PayrollProcessed
        PayrollProcessed payrollProcessed = new PayrollProcessed();
        payrollProcessed.setPayrollRecordayrollRecord(payrollRecord);
        payrollProcessed.setEmployee(payrollRecord.getEmployee());

        payrollProcessed.setIdealAmountForEmployeeToPaidForDays(
                (actualWorkingDays * variableAmount) + staticAmount);
        payrollProcessed.setIdealAmountForEmployeeToPaidForHours(
                (totalWorkingHours * variableAmountPerHour) + staticAmount);
        payrollProcessed.setIdealWorkingdaysForThisPayroll(actualWorkingDays);
        payrollProcessed.setIdealWorkinghoursForThisPayroll(actualWorkingDays * dailyWorkingHours);

        payrollProcessed.setActualAmountForEmployeeToPaidForDays(
                (((actualHours / dailyWorkingHours * variableAmount) + staticAmount
                        + (basicAmount * overtimeRate * overtimeHours)) - incomeTaxDeduction));
        payrollProcessed.setActualAmountForEmployeeToPaidForHours(
                ((actualHours * variableAmountPerHour) + staticAmount
                        + (basicAmount * overtimeRate * overtimeHours)) - incomeTaxDeduction);
        payrollProcessed.setActualWorkingdaysForThisPayroll(actualHours / dailyWorkingHours);
        payrollProcessed.setActualWorkinghoursForThisPayroll(actualHours);

        payrollProcessed.setOvertimeAmount(basicAmount * overtimeRate * overtimeHours);
        payrollProcessed.setPaymentPaidStatus(false); // Set to false initially
        payrollProcessed.setTotalDiduction(staticAmount + incomeTaxDeduction);
        payrollProcessed.setTotalInhand((actualHours * variableAmountPerHour)
                + (basicAmount * overtimeRate * overtimeHours) - incomeTaxDeduction);
        payrollProcessed.setIncomeTaxDeduction(incomeTaxDeduction);
        payrollProcessed.setDate(LocalDate.now());

        // Save the PayrollProcessed entity
        payrollProcessedRepository.save(payrollProcessed);

        // Update proceedForPayment to true after processing
        payrollRecord.setProceedForPayment(true);
        payrollRecordRepository.save(payrollRecord);
    }
}

// Above method is correct below is update fpr payroll
//////////////
/// 
/// 
/// 
////////
/// 
///

// public void processPayrollRecordsByCompanyId(Long companyId) {
//     // Fetch all PayrollRecords for the given companyId where proceedForPayrollStatus is true
//     List<PayrollRecord> payrollRecords = payrollRecordRepository.findByCompanyIdAndProceedForPayrollStatusTrue(companyId);

//     for (PayrollRecord payrollRecord : payrollRecords) {
//         // Perform null and zero checks to avoid errors in calculation
//         int actualWorkingDays = payrollRecord.getActualWorkingDays();
//         int dailyWorkingHours = payrollRecord.getDailyWorkingHours();
//         int actualHours = payrollRecord.getActualHours() != null ? payrollRecord.getActualHours() : 0;
//         int totalWorkingHours = payrollRecord.getTotalWorkingHours();
//         double variableAmount = payrollRecord.getVariableAmount();
//         double variableAmountPerHour = payrollRecord.getVariableAmountPerHour();
//         double staticAmount = payrollRecord.getStaticAmount();
//         double overtimeHours = payrollRecord.getOvertimeHours();

//         if (actualWorkingDays <= 0 || dailyWorkingHours <= 0) {
//             logger.warn("Invalid data for PayrollRecord ID {}: actualWorkingDays={}, dailyWorkingHours={}",
//                     payrollRecord.getId(), actualWorkingDays, dailyWorkingHours);
//             continue;
//         }

//         // Fetch related EmployeeConfig and CTCBreakdownHeader
//         Optional<EmployeeConfig> optionalEmployeeConfig = employeeConfigRepository.findByEmployeeId(payrollRecord.getEmployee().getId());
//         CTCBreakdownHeader ctcBreakdownHeader = ctcBreakdownHeaderRepository.findCurrentCTCByEmployeeId(payrollRecord.getEmployee().getId());

//         if (!optionalEmployeeConfig.isPresent() || ctcBreakdownHeader == null) {
//             logger.warn("Missing EmployeeConfig or CTCBreakdownHeader for PayrollRecord ID {}", payrollRecord.getId());
//             continue;
//         }

//         EmployeeConfig employeeConfig = optionalEmployeeConfig.get();

//         double overtimeRate = employeeConfig.getOvertimeRate();
//         int workingHours = employeeConfig.getWorkingHours();
//         double basicAmount = ctcBreakdownHeader.getBasicAmount();

//         // Calculate the values for PayrollProcessed
//         PayrollProcessed payrollProcessed = new PayrollProcessed();
//         payrollProcessed.setPayrollRecordayrollRecord(payrollRecord);
//          payrollProcessed.setEmployee(payrollRecord.getEmployee());

//         payrollProcessed.setIdealAmountForEmployeeToPaidForDays(
//                 (actualWorkingDays * variableAmount) + staticAmount);
//         payrollProcessed.setIdealAmountForEmployeeToPaidForHours(
//                 (totalWorkingHours * variableAmountPerHour) + staticAmount);
//         payrollProcessed.setIdealWorkingdaysForThisPayroll(actualWorkingDays);
//         payrollProcessed.setIdealWorkinghoursForThisPayroll(actualWorkingDays * dailyWorkingHours);

//         payrollProcessed.setActualAmountForEmployeeToPaidForDays(
//                 ((double) actualHours / dailyWorkingHours * variableAmount) + staticAmount);
//         payrollProcessed.setActualAmountForEmployeeToPaidForHours(
//                 (actualHours * variableAmountPerHour) + staticAmount);
//         payrollProcessed.setActualWorkingdaysForThisPayroll(actualHours / dailyWorkingHours);
//         payrollProcessed.setActualWorkinghoursForThisPayroll(actualHours);

//         // Calculate overtimeAmount
//         double overtimeAmount = (overtimeHours * overtimeRate) *
//                 ((basicAmount / 12) / (actualWorkingDays * workingHours));
//         payrollProcessed.setOvertimeAmount(overtimeAmount);

//         payrollProcessed.setPaymentPaidStatus(false); // Set to false initially
//         payrollProcessed.setDate(LocalDate.now());

//         // Save the PayrollProcessed entity
//         payrollProcessedRepository.save(payrollProcessed);
//     }
// }



/// 
/// 
/// 
/// 
/// 
public void processPayrollRecordById(Long payrollRecordId) {
    // Fetch the PayrollRecord by ID
    PayrollRecord payrollRecord = payrollRecordRepository.findById(payrollRecordId)
            .orElseThrow(() -> new RuntimeException("PayrollRecord with ID " + payrollRecordId + " not found"));

    if (!payrollRecord.isProceedForPayrollStatus()) {
        logger.warn("PayrollRecord ID {} is not marked for processing.", payrollRecordId);
        return;
    }

    // Perform null and zero checks to avoid errors in calculation
    int actualWorkingDays = payrollRecord.getActualWorkingDays();
    int dailyWorkingHours = payrollRecord.getDailyWorkingHours();
    int actualHours = payrollRecord.getActualHours() != null ? payrollRecord.getActualHours() : 0;
    int totalWorkingHours = payrollRecord.getTotalWorkingHours();
    double variableAmount = payrollRecord.getVariableAmount();
    double variableAmountPerHour = payrollRecord.getVariableAmountPerHour();
    double staticAmount = payrollRecord.getStaticAmount();
    double overtimeHours = payrollRecord.getOvertimeHours(); // Retrieve the total overtime hours
    double overtimeRate = payrollRecord.getOvertimeRate(); // Retrieve the rate for overtime hours
    double basicAmount = payrollRecord.getBasicAmount(); 
    double incomeTaxDeduction = payrollRecord.getIncomeTaxDeduction();
    if (actualWorkingDays <= 0 || dailyWorkingHours <= 0) {
        logger.warn("Invalid data for PayrollRecord ID {}: actualWorkingDays={}, dailyWorkingHours={}",
                payrollRecordId, actualWorkingDays, dailyWorkingHours);
        return;
    }

    // Calculate the values for PayrollProcessed
    PayrollProcessed payrollProcessed = new PayrollProcessed();
    payrollProcessed.setPayrollRecordayrollRecord(payrollRecord);
    payrollProcessed.setEmployee(payrollRecord.getEmployee());

    payrollProcessed.setIdealAmountForEmployeeToPaidForDays(
            (actualWorkingDays * variableAmount) + staticAmount);
    payrollProcessed.setIdealAmountForEmployeeToPaidForHours(
            (totalWorkingHours * variableAmountPerHour) + staticAmount);
    payrollProcessed.setIdealWorkingdaysForThisPayroll(actualWorkingDays);
    payrollProcessed.setIdealWorkinghoursForThisPayroll(actualWorkingDays * dailyWorkingHours);

    payrollProcessed.setActualAmountForEmployeeToPaidForDays(
            ((double) (((actualHours / dailyWorkingHours * variableAmount) + staticAmount +(basicAmount*overtimeRate*overtimeHours)))-(incomeTaxDeduction)));
    payrollProcessed.setActualAmountForEmployeeToPaidForHours(
           ( (actualHours * variableAmountPerHour) + staticAmount +(basicAmount*overtimeRate*overtimeHours))-(incomeTaxDeduction));
    payrollProcessed.setActualWorkingdaysForThisPayroll(actualHours / dailyWorkingHours);
    payrollProcessed.setActualWorkinghoursForThisPayroll(actualHours);

    
    payrollProcessed.setPaymentPaidStatus(false); // Set to false initially
    payrollProcessed.setOvertimeAmount(basicAmount*overtimeRate*overtimeHours);
    payrollProcessed.setTotalDiduction(staticAmount + (incomeTaxDeduction));
    payrollProcessed.setTotalInhand((actualHours * variableAmountPerHour)+((basicAmount*overtimeRate*overtimeHours)));
    payrollProcessed.setIncomeTaxDeduction(incomeTaxDeduction);

    payrollProcessed.setDate(LocalDate.now());

    // Save the PayrollProcessed entity
    payrollProcessedRepository.save(payrollProcessed);
}





















    @Transactional
    public String updateProceedForPayrollStatus(Long id) {
        int rowsUpdated = payrollRecordRepository.updateProceedForPayrollStatusById(id);
        if (rowsUpdated > 0) {
            return "PayrollRecord with ID " + id + " successfully updated to proceed for payroll.";
        } else {
            return "No PayrollRecord found with ID " + id + ". Update failed.";
        }
    }




    public void updateSpecificFields(Long id, PayrollRecord updatedFields) {
        PayrollRecord existingRecord = payrollRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PayrollRecord not found for ID: " + id));

        // Update specific fields
        if (updatedFields.getActualWorkingDays() != 0) {
            existingRecord.setActualWorkingDays(updatedFields.getActualWorkingDays());
        }
        if (updatedFields.getAssignHours() != 0) {
            existingRecord.setAssignHours(updatedFields.getAssignHours());
        }
        if (updatedFields.getExpectedHours() != 0) {
            existingRecord.setExpectedHours(updatedFields.getExpectedHours());
        }
        if (updatedFields.getDailyWorkingHours() != 0) {
            existingRecord.setDailyWorkingHours(updatedFields.getDailyWorkingHours());
        }
        if (updatedFields.getTotalWorkingHours() != 0) {
            existingRecord.setTotalWorkingHours(updatedFields.getTotalWorkingHours());
        }
        if (updatedFields.getIncomeTaxDeduction() != 0) {
            existingRecord.setIncomeTaxDeduction(updatedFields.getIncomeTaxDeduction());
        }

        // Save updated record
        payrollRecordRepository.save(existingRecord);
    }












    public void storeMergedPayrollRecordsForCompany(Long companyId, int month, int year) {
        // Fetch records from both sources
        List<PayrollHours> payrollHoursList = payrollHoursRepository.findRecordsByCompanyId(companyId);
        List<CTCBreakdownRecord> ctcRecords = ctcBreakdownRecordRepository.findActiveRecordsByCompanyId(companyId);
    
        // Loop through CTCBreakdownRecords (mandatory)
        for (CTCBreakdownRecord ctcRecord : ctcRecords) {
            if (ctcRecord == null || ctcRecord.getEmployee() == null) {
                logger.warn("Skipping CTC record with no associated employee.");
                continue; // Skip if CTC record or employee is null
            }
    
            Long employeeId = ctcRecord.getEmployee().getId();
    
            // Fetch the PayrollHours record with status = true for this employee
            PayrollHours payrollHours = payrollHoursList.stream()
                    .filter(hours -> hours.getEmployee().getId().equals(employeeId) && Boolean.TRUE.equals(hours.getStatus()))
                    .findFirst()
                    .orElse(null);
    
            PayrollRecord payrollRecord = new PayrollRecord();
    
            // Populate PayrollRecord with data from CTCBreakdownRecord
            payrollRecord.setDate(LocalDate.of(year, month, 1));
            payrollRecord.setEmployee(ctcRecord.getEmployee());
            payrollRecord.setCompany(ctcRecord.getEmployee().getCompanyRegistration());
            payrollRecord.setStaticAmount(ctcRecord.getStaticAmount());
            payrollRecord.setVariableAmount(ctcRecord.getVariableAmount());
            payrollRecord.setVariableAmountPerHour(ctcRecord.getVariableAmountPerHour() != 0.0 ? ctcRecord.getVariableAmountPerHour() : 0.0);
            payrollRecord.setStatus(ctcRecord.isStatus());
    
            // If PayrollHours is present and active, populate corresponding fields
            if (payrollHours != null) {
                payrollRecord.setAssignHours(payrollHours.getAssignHours());
                payrollRecord.setExpectedHours(payrollHours.getExpectedHours());
                payrollRecord.setActualHours(payrollHours.getActualHours());
            } else {
                logger.info("No active PayrollHours found for Employee ID {}. Proceeding without hour details.", employeeId);
                payrollRecord.setAssignHours(0);
                payrollRecord.setExpectedHours(0);
                payrollRecord.setActualHours(0);
            }
    
            // Calculate actual working days and hours
            try {
                Map<String, Object> workingDaysAndHours = employeeHolidayCalendarService
                        .calculateActualWorkingDaysAndHoursNews(ctcRecord.getEmployee(), year, month);
    
                payrollRecord.setActualWorkingDays((int) workingDaysAndHours.get("actualWorkingDays"));
                payrollRecord.setDailyWorkingHours((int) workingDaysAndHours.get("dailyWorkingHours"));
                payrollRecord.setTotalWorkingHours((int) workingDaysAndHours.get("totalWorkingHours"));
            } catch (Exception e) {
                logger.error("Error calculating working days and hours for employee ID {}: {}", employeeId, e.getMessage(), e);
                payrollRecord.setActualWorkingDays(0);
                payrollRecord.setDailyWorkingHours(0);
                payrollRecord.setTotalWorkingHours(0);
            }
    
            // Retrieve EmployeeConfig for the employee using Optional
            Optional<EmployeeConfig> optionalEmployeeConfig = employeeConfigRepository.findByEmployeeId(employeeId);
    
            if (optionalEmployeeConfig.isPresent()) {
                EmployeeConfig employeeConfig = optionalEmployeeConfig.get();
                payrollRecord.setOvertimeRate(employeeConfig.getOvertimeRate()); // Set overtimeRate
    
                if (employeeConfig.isOvertimeApplicable()) {
                    int totalWorkingHours = payrollRecord.getTotalWorkingHours();
                    int actualHours = payrollRecord.getActualHours() != null ? payrollRecord.getActualHours() : 0;
    
                    // Calculate overtime hours
                    double overtimeHours = Math.min(
                            employeeConfig.getAllowableOvertimeHours(),
                            Math.max(0, actualHours - totalWorkingHours)
                    );
    
                    payrollRecord.setOvertimeHours(overtimeHours);
                } else {
                    payrollRecord.setOvertimeHours(0.0);
                }
            } else {
                logger.info("No EmployeeConfig found for Employee ID {}. Setting overtime hours to 0.", employeeId);
                payrollRecord.setOvertimeHours(0.0);
            }
    
            // Fetch the CTCBreakdownHeader for the employee and company
            Optional<CTCBreakdownHeader> optionalCTCHeader = ctcBreakdownHeaderRepository
                    .findByEmployeeAndCompanyAndCtcStatusTrue(ctcRecord.getEmployee(), ctcRecord.getEmployee().getCompanyRegistration());
    
            if (optionalCTCHeader.isPresent()) {
                CTCBreakdownHeader ctcBreakdownHeader = optionalCTCHeader.get();
    
                // Calculate basic amount
                double totalWorkingHours = payrollRecord.getTotalWorkingHours();
                if (totalWorkingHours > 0) {
                    double calculatedBasicAmount = ctcBreakdownHeader.getBasicAmount() / (12 * totalWorkingHours);
                    payrollRecord.setBasicAmount(calculatedBasicAmount);
                } else {
                    logger.warn("Total working hours is zero or less for Employee ID {}. Setting basicAmount to 0.", employeeId);
                    payrollRecord.setBasicAmount(0.0);
                }
            } else {
                logger.info("No active CTCBreakdownHeader found for Employee ID {}. Setting basicAmount to 0.", employeeId);
                payrollRecord.setBasicAmount(0.0);
            }
    
            // Save the new PayrollRecord
            payrollRecordRepository.save(payrollRecord);
        }
    }
    


    public List<PayrollRecord> getPayrollRecordsByCompanyAndStatus(Long companyId, boolean proceedForPayment) {
        return payrollRecordRepository.findByCompanyAndPayrollStatusAndPaymentStatus(companyId, proceedForPayment);
    }

    public List<PayrollRecord> getPayrollRecordsByCompanyAndStatusAndDate(Long companyId, boolean proceedForPayment, int year, int month) {
        return payrollRecordRepository.findByCompanyAndStatusAndDate(companyId, proceedForPayment, year, month);
    }
}
