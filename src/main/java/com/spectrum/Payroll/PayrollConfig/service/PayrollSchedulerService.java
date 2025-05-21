package com.spectrum.Payroll.PayrollConfig.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.CTCModuleMain.model.CTCBreakdownRecord;
import com.spectrum.CTCModuleMain.service.CTCBreakdownHeaderService;
import com.spectrum.Payroll.PayrollConfig.model.PayrollConfig;

import com.spectrum.Payroll.PayrollRecord.service.PayrollRecordService;

import java.time.LocalDate;
import java.util.List;

@Service
public class PayrollSchedulerService {

    @Autowired
    private PayrollConfigService payrollConfigService;

    @Autowired
    private CTCBreakdownHeaderService ctcBreakdownHeaderService;

    @Autowired
    private PayrollRecordService payrollRecordService;

    // @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    // public void checkAndTriggerPayrollCalculation() {
    // LocalDate today = LocalDate.now();

    // // Fetch PayrollConfig entries for today's date
    // List<PayrollConfig> payrollConfigs =
    // payrollConfigService.getPayrollConfigsByPayrollDate(today);

    // if (!payrollConfigs.isEmpty()) {
    // for (PayrollConfig config : payrollConfigs) {
    // Long companyId = config.getCompany().getId();
    // ctcBreakdownHeaderService.calculateAmountsAndCheckStatusForAllEmployees(companyId);
    // }
    // }
    // }

    @Scheduled(cron = "0 22 16 * * ?") // Runs at 14:02:00
    public void checkAndTriggerPayrollCalculation() {
        LocalDate today = LocalDate.now();

        // Fetch PayrollConfig entries for today's date
        List<PayrollConfig> payrollConfigs = payrollConfigService.getPayrollConfigsByPayrollDate(today);

        if (!payrollConfigs.isEmpty()) {
            for (PayrollConfig config : payrollConfigs) {
                Long companyId = config.getCompany().getId();
                ctcBreakdownHeaderService.calculateAmountsAndCheckStatusForAllEmployees(companyId);
            }
        }
    }

    // @Scheduled(cron = "0 15 14 * * ?") // Runs daily at 14:15:00
    // public void checkAndTriggerPayrollCalculation() {
    // LocalDate today = LocalDate.now();

    // // Fetch PayrollConfig entries for today's date
    // List<PayrollConfig> payrollConfigs =
    // payrollConfigService.getPayrollConfigsByPayrollDate(today);

    // if (!payrollConfigs.isEmpty()) {
    // for (PayrollConfig config : payrollConfigs) {
    // Long companyId = config.getCompany().getId();
    // ctcBreakdownHeaderService.calculateAmountsAndCheckStatusForAllEmployees(companyId);
    // }
    // }
    // }

    @Scheduled(cron = "0 23 16 * * ?") // Adjust cron expression as needed
    public void triggerPayrollProcessing() {
        LocalDate today = LocalDate.now();

        // Fetch PayrollConfig entries for today's date
        List<PayrollConfig> payrollConfigs = payrollConfigService.getPayrollConfigsByPayrollDate(today);

        if (!payrollConfigs.isEmpty()) {
            for (PayrollConfig config : payrollConfigs) {
                Long companyId = config.getCompany().getId();
                payrollRecordService.storeMergedPayrollRecordsForCompany(companyId);
            }
        }
    }

    @Scheduled(cron = "0 28 16 * * ?") // Runs daily at 1:00 AM
    public void triggerPayrollProcessingNew() {
        LocalDate today = LocalDate.now();
        int currentDayOfMonth = today.getDayOfMonth(); // Extract the day of the month

        // Fetch all PayrollConfig entries
        List<PayrollConfig> payrollConfigs = payrollConfigService.getAllPayrollConfigs();

        for (PayrollConfig config : payrollConfigs) {
            int payrollDay = config.getPayrollDate().getDayOfMonth(); // Extract day from payrollDate

            // Trigger only if today's day matches the payroll day
            if (currentDayOfMonth == payrollDay) {
                Long companyId = config.getCompany().getId();
                payrollRecordService.storeMergedPayrollRecordsForCompany(companyId);
            }
        }
    }





    @Scheduled(cron = "0 1 15 1 * ?") // Runs at midnight on the 1st day of each month
public void triggerMonthlyPayrollProcessing() {
    LocalDate today = LocalDate.now();

    // Check if today is the first day of the month
    if (today.getDayOfMonth() == 1) {
        // Fetch all PayrollConfig entries
        List<PayrollConfig> payrollConfigs = payrollConfigService.getAllPayrollConfigs();

        for (PayrollConfig config : payrollConfigs) {
            Long companyId = config.getCompany().getId();

            // Generate CTC records for the company
            List<CTCBreakdownRecord> ctcRecords = ctcBreakdownHeaderService.createCTCRecordsForAllEmployees(companyId);

            // Log or process the generated records as needed
            System.out.println("Generated " + ctcRecords.size() + " CTC records for company ID: " + companyId);
        }
    }
}

}
