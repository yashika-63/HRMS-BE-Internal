package com.spectrum.PerformanceManagement.Appraisal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalKpi;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.CompanyConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.CompanyConfigRepository;
import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalGoal;
import com.spectrum.PerformanceManagement.Appraisal.repository.AppraisalKpiRepository;
import com.spectrum.PerformanceManagement.Appraisal.repository.AppraisalGoalRepository;
import com.spectrum.PerformanceManagement.GoalSetting.model.EmployeeGoalSetting;
import com.spectrum.PerformanceManagement.GoalSetting.repository.EmployeeGoalSettingRepository;
import com.spectrum.PerformanceManagement.KpiManagement.model.EmployeeKpiSetting;
import com.spectrum.PerformanceManagement.KpiManagement.repository.EmployeeKpiSettingRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AppraisalService {

    @Autowired
    private AppraisalKpiRepository appraisalKpiRepository;

    @Autowired
    private AppraisalGoalRepository appraisalGoalRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeKpiSettingRepository employeeKpiSettingRepository;

    @Autowired
    private EmployeeGoalSettingRepository employeeGoalSettingRepository;

         @Autowired
    private EmailService emailService;

        @Autowired
    private CompanyConfigRepository companyConfigRepository;

    // Save AppraisalKpi
    public AppraisalKpi saveAppraisalKpi(AppraisalKpi appraisalKpi) {
        Optional.ofNullable(appraisalKpi.getEmployee())
                .flatMap(emp -> employeeRepository.findById(emp.getId()))
                .ifPresent(appraisalKpi::setEmployee);

        Optional.ofNullable(appraisalKpi.getEmployees())
                .flatMap(emp -> employeeRepository.findById(emp.getId()))
                .ifPresent(appraisalKpi::setEmployees);

        Optional.ofNullable(appraisalKpi.getEmployeeKpiSetting())
                .flatMap(setting -> employeeKpiSettingRepository.findById(setting.getId()))
                .ifPresent(appraisalKpi::setEmployeeKpiSetting);

        return appraisalKpiRepository.save(appraisalKpi);
    }

    // Save AppraisalGoal
    public AppraisalGoal saveAppraisalGoal(AppraisalGoal appraisalGoal) {
        Optional.ofNullable(appraisalGoal.getEmployee())
                .flatMap(emp -> employeeRepository.findById(emp.getId()))
                .ifPresent(appraisalGoal::setEmployee);

        Optional.ofNullable(appraisalGoal.getEmployees())
                .flatMap(emp -> employeeRepository.findById(emp.getId()))
                .ifPresent(appraisalGoal::setEmployees);

        Optional.ofNullable(appraisalGoal.getEmployeeGoalSetting())
                .flatMap(setting -> employeeGoalSettingRepository.findById(setting.getId()))
                .ifPresent(appraisalGoal::setEmployeeGoalSetting);

        return appraisalGoalRepository.save(appraisalGoal);
    }






    @Transactional
public void generateAppraisalData() {
    LocalDate today = LocalDate.now();

    // Fetch all company configurations where today is the appraisal start date
    List<CompanyConfig> companyConfigs = companyConfigRepository.findByAppraisalStartDate(today);

    for (CompanyConfig companyConfig : companyConfigs) {
        Long companyId = companyConfig.getCompany().getId();

        // Fetch all employees of this company
        List<Employee> employees = employeeRepository.findByCompanyRegistration_Id(companyId)
                                                     .orElse(Collections.emptyList());

        if (employees.isEmpty()) {
            continue; // Skip processing if no employees are found
        }

        List<AppraisalGoal> appraisalGoals = new ArrayList<>();
        List<AppraisalKpi> appraisalKpis = new ArrayList<>();
        Map<Employee, Integer> employeeGoalCountMap = new HashMap<>();
        Map<Employee, Integer> employeeKpiCountMap = new HashMap<>();

        for (Employee employee : employees) {
            // Fetch active goal settings
            List<EmployeeGoalSetting> activeGoals = employeeGoalSettingRepository.findByEmployeeAndStatus(employee, true);
            for (EmployeeGoalSetting goalSetting : activeGoals) {
                AppraisalGoal appraisalGoal = new AppraisalGoal();
                appraisalGoal.setEmployee(employee);
                appraisalGoal.setEmployees(goalSetting.getEmployees()); // Reporting manager
                appraisalGoal.setEmployeeGoalSetting(goalSetting);
                appraisalGoal.setEmployeeSelfRating(0.0);
                appraisalGoal.setEmployeeManagerRating(0.0);
                appraisalGoal.setEmployeeModeratedFinalRating(0.0);
                appraisalGoal.setStatus(true);
                appraisalGoal.setParameterType(true);
                                appraisalGoal.setParameterType(true);

                appraisalGoal.setAppraisalYear(today.getYear());

                appraisalGoals.add(appraisalGoal);
                employeeGoalCountMap.put(employee, employeeGoalCountMap.getOrDefault(employee, 0) + 1);
            }

            // Fetch active KPI settings
            List<EmployeeKpiSetting> activeKpis = employeeKpiSettingRepository.findByEmployeeAndStatus(employee, true);
            for (EmployeeKpiSetting kpiSetting : activeKpis) {
                AppraisalKpi appraisalKpi = new AppraisalKpi();
                appraisalKpi.setEmployee(employee);
                appraisalKpi.setEmployees(kpiSetting.getEmployees()); // Reporting manager
                appraisalKpi.setEmployeeKpiSetting(kpiSetting);
                appraisalKpi.setEmployeeSelfRating(0.0);
                appraisalKpi.setEmployeeManagerRating(0.0);
                appraisalKpi.setEmployeeModeratedFinalRating(0.0);
                appraisalKpi.setEmployeeRatingStatus(false);
                appraisalKpi.setReportingRatingStatus(false);
                appraisalKpi.setReportingModratedRatingStatus(false);
                appraisalKpi.setStatus(true);

                appraisalKpi.setAppraisalYear(today.getYear());

                appraisalKpis.add(appraisalKpi);
                employeeKpiCountMap.put(employee, employeeKpiCountMap.getOrDefault(employee, 0) + 1);
            }
        }

        // Save goals and KPIs in batch
        if (!appraisalGoals.isEmpty()) {
            appraisalGoalRepository.saveAll(appraisalGoals);
        }
        if (!appraisalKpis.isEmpty()) {
            appraisalKpiRepository.saveAll(appraisalKpis);
        }

        // Send email notification per employee
        for (Employee employee : employees) {
            int goalCount = employeeGoalCountMap.getOrDefault(employee, 0);
            int kpiCount = employeeKpiCountMap.getOrDefault(employee, 0);

            if (goalCount == 0 && kpiCount == 0) {
                continue; // Skip email if no goals or KPIs assigned
            }

            String subject = "Complete Your Appraisal Process";
            String body = "Dear " + employee.getFirstName() + ",\n\n" +
                    "Your appraisal process has started for the year " + today.getYear() + ".\n" +
                    (goalCount > 0 ? "A total of " + goalCount + " goal(s) have been assigned to you.\n" : "") +
                    (kpiCount > 0 ? "A total of " + kpiCount + " KPI(s) have been assigned to you.\n" : "") +
                    "Please complete the self-assessment within the next 7 days.\n\n" +
                    "Best regards,\nHR Team";

            emailService.sendEmail(employee.getEmail(), subject, body);
        }
    }
}










//////////////






 // Temporary storage for OTPs (In real cases, use Redis or DB)
    private ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();

    public String sendOtpToEmployee(Long employeeId) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        if (employeeOpt.isEmpty()) {
            return "Employee not found";
        }
        
        Employee employee = employeeOpt.get();
        String email = employee.getEmail();
        String otp = generateOtp();
        otpStorage.put(email, otp); // Store OTP temporarily

        emailService.sendEmail(email, "OTP Verification", "Your OTP is: " + otp);
        return "OTP sent to " + email;
    }





    ///////////
    /// 
    
//     public String verifyOtpAndUpdateStatus(Long employeeId, String otp) {
//         Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
//         if (employeeOpt.isEmpty()) {
//             return "Employee not found";
//         }
    
//         Employee employee = employeeOpt.get();
//         String email = employee.getEmail();
        
//         if (!otpStorage.containsKey(email) || !otpStorage.get(email).equals(otp)) {
//             return "Invalid or expired OTP";
//         }
    
//         // OTP Verified, update status for all records of the employee
//         List<AppraisalKpi> appraisals = appraisalKpiRepository.findByEmployeeId(employeeId);
        
//         if (appraisals.isEmpty()) {
//             return "Appraisal record not found";
//         }
    
//         for (AppraisalKpi appraisal : appraisals) {
//             appraisal.setEmployeeRatingStatus(true);
//             appraisalKpiRepository.save(appraisal);
//         }
    
//         otpStorage.remove(email); // Remove OTP after successful verification
    
//         return "Employee rating status updated successfully for all records";
//     }
    
    private String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // 6-digit OTP
    }






    ////////
    /// 
    public String verifyOtpAndUpdateStatus(Long employeeId, String otp) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        if (employeeOpt.isEmpty()) {
            return "Employee not found";
        }
    
        Employee employee = employeeOpt.get();
        String email = employee.getEmail();
        
        if (!otpStorage.containsKey(email) || !otpStorage.get(email).equals(otp)) {
            return "Invalid or expired OTP";
        }
    
        // Fetch appraisal records where status = true
        List<AppraisalKpi> appraisals = appraisalKpiRepository.findByEmployeeIdAndStatusTrue(employeeId);
    
        if (appraisals.isEmpty()) {
            return "No active appraisal records found for the employee";
        }
    
        // Update employeeRatingStatus for records with status = true
        for (AppraisalKpi appraisal : appraisals) {
            appraisal.setEmployeeRatingStatus(true);
            appraisalKpiRepository.save(appraisal);
        }
    
        otpStorage.remove(email); // Remove OTP after successful verification
    
        // Send email notification to the reporting manager
        Employee reportingManager = appraisals.get(0).getEmployees();
        if (reportingManager != null) {
            sendAppraisalNotification(reportingManager.getEmail(), employee.getFirstName(), appraisals);
        }
    
        return "Employee rating status updated successfully. Manager notified.";
    }
    



    private void sendAppraisalNotification(String managerEmail, String employeeName, List<AppraisalKpi> appraisals) {
        String subject = "Appraisal Request for Employee " + employeeName;
    
        StringBuilder body = new StringBuilder();
        body.append("Dear Manager,\n\n");
        body.append("You have received an appraisal request for the employee: ").append(employeeName).append("\n");
        body.append("The following KPIs require your managerial appraisal:\n\n");
    
        for (AppraisalKpi appraisal : appraisals) {
            body.append("- KPI ID: ").append(appraisal.getEmployeeKpiSetting().getId())
                .append(", Appraisal Year: ").append(appraisal.getAppraisalYear())
                .append("\n");
        }
    
        body.append("\nPlease complete the managerial appraisal process within **7 days**.\n\n");
        body.append("Best regards,\nHR Team");
    
        emailService.sendEmail(managerEmail, subject, body.toString());
    }
    





    //////////
    /// 
    


    private void sendAppraisalGoalNotification(String managerEmail, String employeeName, List<AppraisalGoal> appraisalGoals) {
        String subject = "Appraisal Request for Employee " + employeeName;
    
        StringBuilder body = new StringBuilder();
        body.append("Dear Manager,\n\n");
        body.append("You have received an appraisal request for the employee: ").append(employeeName).append("\n");
        body.append("The following Goals require your managerial appraisal:\n\n");
    
        for (AppraisalGoal appraisalGoal : appraisalGoals) {
            body.append("- Goal ID: ").append(appraisalGoal.getEmployeeGoalSetting().getId())
                .append(", Appraisal Year: ").append(appraisalGoal.getAppraisalYear())
                .append("\n");
        }
    
        body.append("\nPlease complete the managerial appraisal process within **7 days**.\n\n");
        body.append("Best regards,\nHR Team");
    
        emailService.sendEmail(managerEmail, subject, body.toString());
    }

    


    public String verifyOtpAndUpdateGoalStatus(Long employeeId, String otp) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        if (employeeOpt.isEmpty()) {
            return "Employee not found";
        }
    
        Employee employee = employeeOpt.get();
        String email = employee.getEmail();
        
        if (!otpStorage.containsKey(email) || !otpStorage.get(email).equals(otp)) {
            return "Invalid or expired OTP";
        }
    
        // Fetch active appraisal goals
        List<AppraisalGoal> appraisalGoals = appraisalGoalRepository.findByEmployeeIdAndStatusTrue(employeeId);
    
        if (appraisalGoals.isEmpty()) {
            return "No active appraisal goals found for the employee";
        }
    
        // Update employeeRatingStatus for active appraisal goals
        for (AppraisalGoal appraisalGoal : appraisalGoals) {
            appraisalGoal.setEmployeeRatingStatus(true);
            appraisalGoalRepository.save(appraisalGoal);
        }
    
        otpStorage.remove(email); // Remove OTP after successful verification
    
        // Send email notification to the reporting manager
        Employee reportingManager = appraisalGoals.get(0).getEmployees();
        if (reportingManager != null) {
            sendAppraisalGoalNotification(reportingManager.getEmail(), employee.getFirstName(), appraisalGoals);
        }
    
        return "Employee rating status updated successfully. Manager notified.";
    }
    



    //////
    /// 
    /// 
    /// 
    /// 
//     public String verifyOtpAndUpdateAppraisalStatus(Long employeeId, String otp) {
//         Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
//         if (employeeOpt.isEmpty()) {
//             return "Employee not found";
//         }
    
//         Employee employee = employeeOpt.get();
//         String email = employee.getEmail();
    
//         if (!otpStorage.containsKey(email) || !otpStorage.get(email).equals(otp)) {
//             return "Invalid or expired OTP";
//         }
    
//         // Fetch active appraisal goals and KPIs
//         List<AppraisalGoal> appraisalGoals = appraisalGoalRepository.findByEmployeeIdAndStatusTrue(employeeId);
//         List<AppraisalKpi> appraisalKpis = appraisalKpiRepository.findByEmployeeIdAndStatusTrue(employeeId);
    
//         if (appraisalGoals.isEmpty() && appraisalKpis.isEmpty()) {
//             return "No active appraisal goals or KPIs found for the employee";
//         }
    
//         // Update employeeRatingStatus for Goals and KPIs
//         appraisalGoals.forEach(goal -> {
//             goal.setEmployeeRatingStatus(true);
//             appraisalGoalRepository.save(goal);
//         });
    
//         appraisalKpis.forEach(kpi -> {
//             kpi.setEmployeeRatingStatus(true);
//             appraisalKpiRepository.save(kpi);
//         });
    
//         otpStorage.remove(email); // Remove OTP after successful verification
    
//         // Determine Reporting Manager (Assuming first goal/KPI has the manager)
//         Employee reportingManager = !appraisalGoals.isEmpty() ? appraisalGoals.get(0).getEmployees() : 
//                                    (!appraisalKpis.isEmpty() ? appraisalKpis.get(0).getEmployees() : null);
    
//         if (reportingManager != null) {
//             sendAppraisalNotification(reportingManager.getEmail(), employee.getFirstName(), appraisalGoals, appraisalKpis);
//         }
    
//         return "Employee rating status updated successfully. Manager notified.";
//     }
    
//     /**
//      * Sends a single email notification for both Goals and KPIs
//      */
//     private void sendAppraisalNotification(String managerEmail, String employeeName, 
//                                            List<AppraisalGoal> appraisalGoals, List<AppraisalKpi> appraisalKpis) {
//         String subject = "Appraisal Request for Employee " + employeeName;
    
//         StringBuilder body = new StringBuilder();
//         body.append("Dear Manager,\n\n")
//             .append("You have received an appraisal request for the employee: ").append(employeeName).append("\n");
    
//         if (!appraisalGoals.isEmpty()) {
//             body.append("\n**Goals requiring appraisal:**\n");
//             for (AppraisalGoal goal : appraisalGoals) {
//                 body.append("- Goal ID: ").append(goal.getEmployeeGoalSetting().getId())
//                     .append(", Appraisal Year: ").append(goal.getAppraisalYear())
//                     .append("\n");
//             }
//         }
    
//         if (!appraisalKpis.isEmpty()) {
//             body.append("\n**KPIs requiring appraisal:**\n");
//             for (AppraisalKpi kpi : appraisalKpis) {
//                 body.append("- KPI ID: ").append(kpi.getEmployeeKpiSetting().getId())
//                     .append(", Appraisal Year: ").append(kpi.getAppraisalYear())
//                     .append("\n");
//             }
//         }
    
//         body.append("\nPlease complete the managerial appraisal process within **7 days**.\n\n")
//             .append("Best regards,\nHR Team");
    
//         emailService.sendEmail(managerEmail, subject, body.toString());
//     }
private void sendAppraisalKpiNotification(String managerEmail, String employeeName, List<AppraisalKpi> appraisalKpis) {
        String subject = "Appraisal KPI Request for Employee " + employeeName;
    
        StringBuilder body = new StringBuilder();
        body.append("Dear Manager,\n\n")
            .append("You have received an appraisal request for the employee: ").append(employeeName).append("\n\n")
            .append("**KPIs requiring appraisal:**\n");
    
        for (AppraisalKpi kpi : appraisalKpis) {
            body.append("- KPI ID: ").append(kpi.getEmployeeKpiSetting().getId())
                .append(", Appraisal Year: ").append(kpi.getAppraisalYear())
                .append("\n");
        }
    
        body.append("\nPlease complete the managerial appraisal process within **7 days**.\n\n")
            .append("Best regards,\nHR Team");
    
        emailService.sendEmail(managerEmail, subject, body.toString());
    }
    

    public String verifyOtpAndUpdateAppraisalStatus(Long employeeId, String otp) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        if (employeeOpt.isEmpty()) {
            return "Employee not found";
        }
    
        Employee employee = employeeOpt.get();
        String email = employee.getEmail();
    
        if (!otpStorage.containsKey(email) || !otpStorage.get(email).equals(otp)) {
            return "Invalid or expired OTP";
        }
    
        // Fetch active appraisal goals and KPIs
        List<AppraisalGoal> appraisalGoals = appraisalGoalRepository.findByEmployeeIdAndStatusTrue(employeeId);
        List<AppraisalKpi> appraisalKpis = appraisalKpiRepository.findByEmployeeIdAndStatusTrue(employeeId);
    
        if (appraisalGoals.isEmpty() && appraisalKpis.isEmpty()) {
            return "No active appraisal goals or KPIs found for the employee";
        }
    
        // Update employeeRatingStatus for Goals and KPIs
        appraisalGoals.forEach(goal -> {
            goal.setEmployeeRatingStatus(true);
            appraisalGoalRepository.save(goal);
        });
    
        appraisalKpis.forEach(kpi -> {
            kpi.setEmployeeRatingStatus(true);
            appraisalKpiRepository.save(kpi);
        });
    
        otpStorage.remove(email); // Remove OTP after successful verification
    
        // Determine Reporting Manager (Assuming first goal/KPI has the manager)
        Employee reportingManager = !appraisalGoals.isEmpty() ? appraisalGoals.get(0).getEmployees() : 
                                   (!appraisalKpis.isEmpty() ? appraisalKpis.get(0).getEmployees() : null);
    
        if (reportingManager != null) {
            if (!appraisalGoals.isEmpty() && !appraisalKpis.isEmpty()) {
                sendCombinedAppraisalNotification(reportingManager.getEmail(), employee.getFirstName(), appraisalGoals, appraisalKpis);
            } else if (!appraisalGoals.isEmpty()) {
                sendAppraisalGoalNotification(reportingManager.getEmail(), employee.getFirstName(), appraisalGoals);
            } else if (!appraisalKpis.isEmpty()) {
                sendAppraisalKpiNotification(reportingManager.getEmail(), employee.getFirstName(), appraisalKpis);
            }
        }
    
        return "Employee rating status updated successfully. Manager notified.";
    }
    


    private void sendCombinedAppraisalNotification(String managerEmail, String employeeName, 
                                               List<AppraisalGoal> appraisalGoals, List<AppraisalKpi> appraisalKpis) {
    String subject = "Appraisal Goals & KPIs Request for Employee " + employeeName;

    StringBuilder body = new StringBuilder();
    body.append("Dear Manager,\n\n")
        .append("You have received an appraisal request for the employee: ").append(employeeName).append("\n\n");

    if (!appraisalGoals.isEmpty()) {
        body.append("**Goals requiring appraisal:**\n");
        for (AppraisalGoal goal : appraisalGoals) {
            body.append("- Goal ID: ").append(goal.getEmployeeGoalSetting().getId())
                .append(", Appraisal Year: ").append(goal.getAppraisalYear())
                .append("\n");
        }
        body.append("\n");
    }

    if (!appraisalKpis.isEmpty()) {
        body.append("**KPIs requiring appraisal:**\n");
        for (AppraisalKpi kpi : appraisalKpis) {
            body.append("- KPI ID: ").append(kpi.getEmployeeKpiSetting().getId())
                .append(", Appraisal Year: ").append(kpi.getAppraisalYear())
                .append("\n");
        }
        body.append("\n");
    }

    body.append("Please complete the managerial appraisal process within **7 days**.\n\n")
        .append("Best regards,\nHR Team");

    emailService.sendEmail(managerEmail, subject, body.toString());
}




/////////////////////
/// 
/// 




public List<AppraisalGoal> getAppraisalGoalsByReportingManager(Long reportingManagerId) {
        return appraisalGoalRepository.findByReportingManagerIdAndStatus(reportingManagerId);
    }



    public List<AppraisalKpi> getAppraisalKpisByReportingManager(Long reportingManagerId) {
        return appraisalKpiRepository.findByReportingManagerIdAndStatus(reportingManagerId);
    }



    public <T> List<T> getAppraisalsByReportingManager(Long reportingManagerId, Class<T> type) {
        if (type == AppraisalGoal.class) {
            return (List<T>) appraisalGoalRepository.findByReportingManagerIdAndStatus(reportingManagerId);
        } else if (type == AppraisalKpi.class) {
            return (List<T>) appraisalKpiRepository.findByReportingManagerIdAndStatus(reportingManagerId);
        }
        throw new IllegalArgumentException("Unsupported appraisal type: " + type.getSimpleName());
    }






    // public List<AppraisalGoal> getAppraisalGoalsByEmployeeId(Long employeeId) {
    //     return appraisalGoalRepository.findByEmployeeIdAndStatus(employeeId, true);
    // }


    public List<AppraisalGoal> getAppraisalGoalsByEmployeeId(Long employeeId) {
        return appraisalGoalRepository.findByEmployeeIdAndStatusAndEmployeeRatingStatus(employeeId, true, false);
    }

    // public List<AppraisalKpi> getAppraisalKpisByEmployeeId(Long employeeId) {
    //     return appraisalKpiRepository.findByEmployeeIdAndStatus(employeeId, true);
    // }
    public List<AppraisalKpi> getAppraisalKpisByEmployeeId(Long employeeId) {
        return appraisalKpiRepository.findByEmployeeIdAndStatusAndEmployeeRatingStatus(employeeId, true, false);
    }


    public String verifyOtpAndUpdateReportingStatus(Long employeeId, Long reportingManagerId, String otp) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        Optional<Employee> managerOpt = employeeRepository.findById(reportingManagerId);
    
        if (employeeOpt.isEmpty()) {
            return "Employee not found";
        }
        if (managerOpt.isEmpty()) {
            return "Reporting manager not found";
        }
    
        Employee employee = employeeOpt.get();
        Employee reportingManager = managerOpt.get();
        String email = reportingManager.getEmail(); // Use manager's email (same as in sendOtpToManager)
    
        // Check if OTP exists and matches
        if (!otpStorage.containsKey(email) || !otpStorage.get(email).equals(otp)) {
            return "Invalid or expired OTP";
        }
    
        // Fetch active appraisal goals and KPIs by employeeId and reportingManagerId
        List<AppraisalGoal> appraisalGoals = appraisalGoalRepository.findByEmployeeIdAndEmployeesIdAndStatusTrue(employeeId, reportingManagerId);
        List<AppraisalKpi> appraisalKpis = appraisalKpiRepository.findByEmployeeIdAndEmployeesIdAndStatusTrue(employeeId, reportingManagerId);
    
        if (appraisalGoals.isEmpty() && appraisalKpis.isEmpty()) {
            return "No active appraisal goals or KPIs found for the employee";
        }
    
        // Update reportingRatingStatus and reportingModeratedRatingStatus
        appraisalGoals.forEach(goal -> {
            goal.setReportingRatingStatus(true);
            goal.setReportingModratedRatingStatus(true);
            appraisalGoalRepository.save(goal);
        });
    
        appraisalKpis.forEach(kpi -> {
            kpi.setReportingRatingStatus(true);
            kpi.setReportingModratedRatingStatus(true);
            appraisalKpiRepository.save(kpi);
        });
    
        otpStorage.remove(email); // Remove OTP after successful verification
    
        // Send email notification to employee
        sendAppraisalCompletionNotification(employee.getEmail(), employee.getFirstName());
    
        return "Appraisal status updated successfully. Employee notified.";
    }
    


///////////////////////////


    public String sendOtpToManager(Long employeeId, Long reportingManagerId) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        Optional<Employee> managerOpt = employeeRepository.findById(reportingManagerId);
    
        if (employeeOpt.isEmpty()) {
            return "Employee not found";
        }
        if (managerOpt.isEmpty()) {
            return "Reporting manager not found";
        }
    
        Employee reportingManager = managerOpt.get();
        String email = reportingManager.getEmail(); // Use manager's email
        String otp = generateOtp();
        
        otpStorage.put(email, otp); // Store OTP with manager's email
    
        emailService.sendEmail(email, "OTP Verification", "Your OTP is: " + otp);
        return "OTP sent to " + email;
    }


    private void sendAppraisalCompletionNotification(String email, String employeeName) {
        String subject = "Your Appraisal Review is Completed";
        String message = "Dear " + employeeName + ",\n\n"
                + "Your appraisal review has been completed by your reporting manager. "
                + "You can check the details in the system.\n\n"
                + "Best Regards,\nHR Team";
    
        emailService.sendEmail(email, subject, message);
    
}

}
