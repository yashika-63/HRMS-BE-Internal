package com.spectrum.PerformanceManagement.KpiManagement.service;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.CompanyConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.CompanyConfigRepository;
import com.spectrum.PerformanceManagement.Feedback.repository.FeedbackForKpiRepository;
import com.spectrum.PerformanceManagement.KpiManagement.model.EmployeeKpiSetting;
import com.spectrum.PerformanceManagement.KpiManagement.repository.EmployeeKpiSettingRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.transaction.Transactional;

import com.spectrum.model.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeKpiSettingService {

    @Autowired
    private  EmployeeKpiSettingRepository employeeKpiSettingRepository;

    @Autowired
    private  EmployeeRepository employeeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CompanyConfigRepository companyConfigRepository;
    
    @Autowired
    private  FeedbackForKpiRepository feedbackForKpiRepository;


       @Autowired
    private JavaMailSender mailSender; // Inject JavaMailSender

   public List<EmployeeKpiSetting> saveMultipleKpiSettings(Long employeeId, Long reportingManagerId, List<EmployeeKpiSetting> kpiSettings) {
    Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
    Optional<Employee> reportingManagerOpt = employeeRepository.findById(reportingManagerId);

    if (employeeOpt.isEmpty() || reportingManagerOpt.isEmpty()) {
        throw new IllegalArgumentException("Invalid Employee ID or Reporting Manager ID");
    }

    Employee employee = employeeOpt.get();
    Employee reportingManager = reportingManagerOpt.get();

    for (EmployeeKpiSetting kpiSetting : kpiSettings) {
        kpiSetting.setEmployee(employee);
        kpiSetting.setEmployees(reportingManager);
        kpiSetting.setReviewStatus(false); // Set reviewStatus to true for new KPIs
        kpiSetting.setStatus(true); // Set review to true for new KPIs

    }

    List<EmployeeKpiSetting> savedKpiSettings = employeeKpiSettingRepository.saveAll(kpiSettings);

    // Send Email Notification
    sendKpiNotificationEmail(employee.getEmail(), reportingManager.getEmail());

    return savedKpiSettings;
}

private void sendKpiNotificationEmail(String employeeEmail, String reportingManagerEmail) {
    if (employeeEmail == null || employeeEmail.isEmpty()) {
        return; // Avoid sending email if employee email is not set
    }

    String subject = "KPI Approval Notification";
    String message = "Dear Employee,\n\n"
            + "Your Key Performance Indicators (KPIs) for this year have been set. Please review and approve them within 7 days.\n"
            + "If you have any concerns, please contact your reporting manager.\n\n"
            + "Reporting Manager: " + reportingManagerEmail + "\n\n"
            + "Best Regards,\nHR Team";

    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(employeeEmail);
    mailMessage.setSubject(subject);
    mailMessage.setText(message);

    mailSender.send(mailMessage); // Send email
}


    public List<EmployeeKpiSetting> getKpiSettingsByEmployeeIdAndReportingManagerIdAndYear(Long employeeId, Long reportingManagerId, int year) {
        return employeeKpiSettingRepository.findByEmployeeIdAndReportingManagerIdAndYear(employeeId, reportingManagerId, year);
    }

    public List<EmployeeKpiSetting> getKpiSettingsByEmployeeIdAndYear(Long employeeId, int year) {
        return employeeKpiSettingRepository.findByEmployeeIdAndYear(employeeId, year);
    }



    

    // public List<Map<String, Object>> getKpiSettingsWithRatings(Long employeeId, int year) {
    //     List<EmployeeKpiSetting> kpiSettings = employeeKpiSettingRepository.findByEmployeeIdAndYear(employeeId, year);

    //     List<Long> kpiIds = kpiSettings.stream().map(EmployeeKpiSetting::getId).collect(Collectors.toList());

    //     List<Object[]> ratingData = feedbackForKpiRepository.findAverageRatingForKpi(kpiIds);

    //     Map<Long, Map<String, Object>> ratingMap = new HashMap<>();
    //     for (Object[] row : ratingData) {
    //         Long kpiId = (Long) row[0];
    //         Double avgRating = (Double) row[1];
    //         Long totalRatings = (Long) row[2];

    //         Map<String, Object> ratingInfo = new HashMap<>();
    //         ratingInfo.put("averageRating", avgRating);
    //         ratingInfo.put("totalRatings", totalRatings);

    //         ratingMap.put(kpiId, ratingInfo);
    //     }

    //     List<Map<String, Object>> result = new ArrayList<>();
    //     for (EmployeeKpiSetting kpi : kpiSettings) {
    //         Map<String, Object> kpiData = new HashMap<>();
    //         kpiData.put("id", kpi.getId());
    //         kpiData.put("kpi", kpi.getKpi());
    //         kpiData.put("date", kpi.getDate());
    //         kpiData.put("employeeId", kpi.getEmployee().getId());

    //         if (ratingMap.containsKey(kpi.getId())) {
    //             kpiData.putAll(ratingMap.get(kpi.getId()));
    //         } else {
    //             kpiData.put("averageRating", 0.0);
    //             kpiData.put("totalRatings", 0);
    //         }

    //         result.add(kpiData);
    //     }
    //     return result;
    // }





      public List<Map<String, Object>> getKpiSettingsWithRatings(Long employeeId, int year) {
        List<EmployeeKpiSetting> kpiSettings = employeeKpiSettingRepository.findByEmployeeIdAndYear(employeeId, year);
        List<Long> kpiIds = kpiSettings.stream().map(EmployeeKpiSetting::getId).collect(Collectors.toList());

        // Get Average Ratings
        List<Object[]> ratingData = feedbackForKpiRepository.findAverageRatingForKpi(kpiIds);
        Map<Long, Map<String, Object>> ratingMap = new HashMap<>();
        for (Object[] row : ratingData) {
            Long kpiId = (Long) row[0];
            Double avgRating = (Double) row[1];
            Long totalRatings = (Long) row[2];

            Map<String, Object> ratingInfo = new HashMap<>();
            ratingInfo.put("averageRating", avgRating);
            ratingInfo.put("totalRatings", totalRatings);

            ratingMap.put(kpiId, ratingInfo);
        }

        // Get Individual Ratings with Reporting Manager Name
        List<Object[]> feedbackData = feedbackForKpiRepository.findFeedbackDetailsForKpi(kpiIds);
        Map<Long, List<Map<String, Object>>> feedbackMap = new HashMap<>();
        for (Object[] row : feedbackData) {
            Long kpiId = (Long) row[0];
            Integer rating = (Integer) row[1];
            String note = (String) row[2];
            LocalDate date = (LocalDate) row[3];
            String managerFirstName = (String) row[4];
            String managerLastName = (String) row[5];
            String managerName = managerFirstName + " " + managerLastName;

            Map<String, Object> feedbackInfo = new HashMap<>();
            feedbackInfo.put("rating", rating);
            feedbackInfo.put("note", note);
            feedbackInfo.put("date", date);
            feedbackInfo.put("reportingManager", managerName);

            feedbackMap.computeIfAbsent(kpiId, k -> new ArrayList<>()).add(feedbackInfo);
        }

        // Prepare Final Response
        List<Map<String, Object>> result = new ArrayList<>();
        for (EmployeeKpiSetting kpi : kpiSettings) {
            Map<String, Object> kpiData = new HashMap<>();
            kpiData.put("id", kpi.getId());
            kpiData.put("kpi", kpi.getKpi());
            kpiData.put("date", kpi.getDate());
            kpiData.put("employeeId", kpi.getEmployee().getId());

            // Add Average Rating Data
            if (ratingMap.containsKey(kpi.getId())) {
                kpiData.putAll(ratingMap.get(kpi.getId()));
            } else {
                kpiData.put("averageRating", 0.0);
                kpiData.put("totalRatings", 0);
            }

            // Add Individual Feedbacks
            kpiData.put("feedbacks", feedbackMap.getOrDefault(kpi.getId(), new ArrayList<>()));

            result.add(kpiData);
        }
        return result;
    }

    public List<EmployeeKpiSetting> getActiveReviewedKpiByEmployeeId(Long employeeId) {
        return employeeKpiSettingRepository.findByEmployeeIdAndStatusTrueAndReviewStatusTrue(employeeId);
    }

    @Transactional
    @Scheduled(cron = "0 5 18 * * ?") // Runs daily at 3:00 PM
    public void updateKpiReviewStatusAndNotifyManagers() {
        List<CompanyConfig> companyConfigs = companyConfigRepository.findAll();
        LocalDate today = LocalDate.now();

        for (CompanyConfig config : companyConfigs) {
            LocalDate startDate = config.getFeedbackDate();
            int frequency = config.getFeedbackFrequency();

            // Generate review dates
            while (startDate.isBefore(today) || startDate.isEqual(today)) {
                if (startDate.equals(today)) {
                    List<EmployeeKpiSetting> kpis = employeeKpiSettingRepository
                            .findByReviewStatusFalseAndStatusTrueAndCompany(config.getCompany().getId());

                    // Group KPIs by Employee
                    Map<Employee, List<EmployeeKpiSetting>> employeeKpiMap = kpis.stream()
                            .collect(Collectors.groupingBy(EmployeeKpiSetting::getEmployee));

                    for (Map.Entry<Employee, List<EmployeeKpiSetting>> entry : employeeKpiMap.entrySet()) {
                        Employee employee = entry.getKey();
                        List<EmployeeKpiSetting> employeeKpis = entry.getValue();

                        // Update review status for each KPI
                        for (EmployeeKpiSetting kpi : employeeKpis) {
                            kpi.setReviewStatus(true);
                        }
                        employeeKpiSettingRepository.saveAll(employeeKpis);

                        // Get the reporting manager from EmployeeKpiSetting
                        Employee reportingManager = employeeKpis.get(0).getEmployees();

                        // Send notification to the manager
                        sendKpiNotificationToManager(reportingManager, employee, employeeKpis);
                    }
                }
                startDate = startDate.plusMonths(frequency); // Move to next review period
            }
        }
    }

    private void sendKpiNotificationToManager(Employee manager, Employee employee, List<EmployeeKpiSetting> kpis) {
        if (manager == null)
            return; // Skip if there's no reporting manager assigned

        String subject = "📢 KPI Review Requested for " + employee.getFirstName();
        StringBuilder message = new StringBuilder("Dear " + manager.getFirstName() + ",\n\n")
                .append("The following KPIs of ").append(employee.getFirstName())
                .append(" are now requested for review:\n\n");

        for (EmployeeKpiSetting kpi : kpis) {
            message.append("- ").append(kpi.getKpi()).append("\n");
        }

        message.append("\nPlease review them accordingly.\n\nBest Regards,\nCompany HR");

        emailService.sendEmail(manager.getEmail(), subject, message.toString());
    }

    ///////////////////////////

    // @Transactional
    // public String updateKpiSetting(Long id, EmployeeKpiSetting updatedKpi) {
    //     Optional<EmployeeKpiSetting> existingKpiOpt = employeeKpiSettingRepository.findById(id);

    //     if (existingKpiOpt.isPresent()) {
    //         EmployeeKpiSetting existingKpi = existingKpiOpt.get();

    //         if (!existingKpi.isEmployeeApproval()) {
    //             if (updatedKpi.getKpi() != null) {
    //                 existingKpi.setKpi(updatedKpi.getKpi());
    //             }
    //             if (updatedKpi.getDate() != null) {
    //                 existingKpi.setDate(updatedKpi.getDate());
    //             }
    //             if (updatedKpi.getReviewStatus() != existingKpi.isReviewStatus()) {
    //                 existingKpi.setReviewStatus(updatedKpi.isReviewStatus());
    //             }
    //             if (updatedKpi.getStatus() != existingKpi.isStatus()) {
    //                 existingKpi.setStatus(updatedKpi.isStatus());
    //             }

    //             employeeKpiSettingRepository.save(existingKpi);
    //             return "KPI updated successfully for ID: " + id;
    //         } else {
    //             return "Cannot update KPI after approval!";
    //         }
    //     } else {
    //         return "KPI not found with ID: " + id;
    //     }
    // }


    @Transactional
    public String updateEmployeeKpiApprovalStatus(Long employeeId) {
        // Fetch only KPIs that are NOT approved
        List<EmployeeKpiSetting> unapprovedKpis = employeeKpiSettingRepository.findUnapprovedKpisByEmployeeId(employeeId);
    
        if (!unapprovedKpis.isEmpty()) {
            // Update approval status
            employeeKpiSettingRepository.updateUnapprovedKpisByEmployeeId(employeeId);
    
            // Send email notification
            sendKpiApprovalNotificationToManager(unapprovedKpis);
    
            return "Approved " + unapprovedKpis.size() + " KPI(s) for Employee ID: " + employeeId;
        } else {
            return "No unapproved KPIs found for Employee ID: " + employeeId;
        }
    }
    
    private void sendKpiApprovalNotificationToManager(List<EmployeeKpiSetting> approvedKpis) {
        Employee employee = approvedKpis.get(0).getEmployee();
        Employee reportingManager = approvedKpis.get(0).getEmployees(); // Reporting manager
    
        if (reportingManager != null) {
            String subject = "Employee KPI Approval Notification";
            StringBuilder message = new StringBuilder("Dear " + reportingManager.getFirstName() + ",\n\n");
            message.append("The following KPIs have been approved by " + employee.getFirstName() + ":\n\n");
    
            for (EmployeeKpiSetting kpi : approvedKpis) {
                message.append("- ").append(kpi.getKpi()).append("\n");
            }
    
            message.append("\nPlease review if needed.\n\nBest Regards,\nHR Team");
    
            // Send email to the reporting manager
            emailService.sendEmail(reportingManager.getEmail(), subject, message.toString());
        }
    }
    


    public List<EmployeeKpiSetting> getKpiByYearAndEmployeeId(int year, Long employeeId) {
        return employeeKpiSettingRepository.findByYearAndEmployeeId(year, employeeId);
    }
    



    
}    