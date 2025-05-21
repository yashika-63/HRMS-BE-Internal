package com.spectrum.PerformanceManagement.GoalSetting.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.CompanyConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.CompanyConfigRepository;
import com.spectrum.PerformanceManagement.Feedback.repository.FeedBackGoalSettingRepository;
import com.spectrum.PerformanceManagement.GoalSetting.model.EmployeeGoalSetting;
import com.spectrum.PerformanceManagement.GoalSetting.repository.EmployeeGoalSettingRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.transaction.Transactional;

@Service
public class EmployeeGoalSettingService {

    @Autowired
    private EmployeeGoalSettingRepository employeeGoalSettingRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

      @Autowired
    private FeedBackGoalSettingRepository feedBackGoalSettingRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CompanyConfigRepository companyConfigRepository;

    // public void saveMultipleEmployeeGoalSettings(Long employeeId, Long
    // reportingManagerId,
    // List<EmployeeGoalSetting> goalSettings) {
    // Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
    // Optional<Employee> reportingManagerOpt =
    // employeeRepository.findById(reportingManagerId);

    // if (employeeOpt.isEmpty() || reportingManagerOpt.isEmpty()) {
    // throw new IllegalArgumentException("Invalid employeeId or
    // reportingManagerId");
    // }

    // Employee employee = employeeOpt.get();
    // Employee reportingManager = reportingManagerOpt.get();

    // for (EmployeeGoalSetting goalSetting : goalSettings) {
    // goalSetting.setEmployee(employee);
    // goalSetting.setEmployees(reportingManager);
    // goalSetting.setReviewStatus(true); // Set reviewStatus to true for new goals
    // }

    // employeeGoalSettingRepository.saveAll(goalSettings);
    // }
    @Autowired
    private JavaMailSender mailSender; // Inject JavaMailSender

    public void saveMultipleEmployeeGoalSettings(Long employeeId, Long reportingManagerId,
            List<EmployeeGoalSetting> goalSettings) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        Optional<Employee> reportingManagerOpt = employeeRepository.findById(reportingManagerId);

        if (employeeOpt.isEmpty() || reportingManagerOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid employeeId or reportingManagerId");
        }

        Employee employee = employeeOpt.get();
        Employee reportingManager = reportingManagerOpt.get();

        for (EmployeeGoalSetting goalSetting : goalSettings) {
            goalSetting.setEmployee(employee);
            goalSetting.setEmployees(reportingManager);
            goalSetting.setReviewStatus(false); // Set reviewStatus to true for new goals
            goalSetting.setStatus(true); // Set reviewStatus to true for new goals

        }

        employeeGoalSettingRepository.saveAll(goalSettings);

        // Send Email Notification
        sendGoalNotificationEmail(employee.getEmail(), reportingManager.getEmail());
    }

    private void sendGoalNotificationEmail(String employeeEmail, String reportingManagerEmail) {
        if (employeeEmail == null || employeeEmail.isEmpty()) {
            return; // Avoid sending email if employee email is not set
        }

        String subject = "Goal Setting Notification";
        String message = "Dear Employee,\n\n"
                + "Your goals for this year have been set. Please review and approve them within 7 days.\n"
                + "If you have any concerns, please contact your reporting manager.\n\n"
                + "Reporting Manager: " + reportingManagerEmail + "\n\n"
                + "Best Regards,\nHR Team";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(employeeEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage); // Send email
    }

    /////
    ///

    public List<EmployeeGoalSetting> getGoalSettingsByEmployeeId(Long employeeId) {
        return employeeGoalSettingRepository.findByEmployeeId(employeeId);
    }


    public List<EmployeeGoalSetting> getGoalsByEmployeeAndDepartment(
            int regionId, Long employeeId, int departmentId, boolean goalType) {
        return employeeGoalSettingRepository.findGoalsWithDepartmentOrZero(regionId, employeeId, goalType, departmentId);
    }





    public List<Map<String, Object>> getGoalSettingsWithRatings(Long employeeId, int year) {
        List<EmployeeGoalSetting> goalSettings = employeeGoalSettingRepository.findByEmployeeIdAndYear(employeeId, year);
        List<Long> goalIds = goalSettings.stream().map(EmployeeGoalSetting::getId).collect(Collectors.toList());

        // Get Average Ratings
        List<Object[]> ratingData = feedBackGoalSettingRepository.findAverageRatingForGoal(goalIds);
        Map<Long, Map<String, Object>> ratingMap = new HashMap<>();
        for (Object[] row : ratingData) {
            Long goalId = (Long) row[0];
            Double avgRating = (Double) row[1];
            Long totalRatings = (Long) row[2];

            Map<String, Object> ratingInfo = new HashMap<>();
            ratingInfo.put("averageRating", avgRating);
            ratingInfo.put("totalRatings", totalRatings);

            ratingMap.put(goalId, ratingInfo);
        }

        // Get Individual Ratings with Reporting Manager Name
        List<Object[]> feedbackData = feedBackGoalSettingRepository.findFeedbackDetailsForGoal(goalIds);
        Map<Long, List<Map<String, Object>>> feedbackMap = new HashMap<>();
        for (Object[] row : feedbackData) {
            Long goalId = (Long) row[0];
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

            feedbackMap.computeIfAbsent(goalId, k -> new ArrayList<>()).add(feedbackInfo);
        }

        // Prepare Final Response
        List<Map<String, Object>> result = new ArrayList<>();
        for (EmployeeGoalSetting goal : goalSettings) {
            Map<String, Object> goalData = new HashMap<>();
            goalData.put("id", goal.getId());
            goalData.put("goal", goal.getGoal());
            // goalData.put("goalDescription", goal.getGoalDescription());
            goalData.put("region", goal.getRegion());
            goalData.put("department", goal.getDepartment());
            goalData.put("employeeId", goal.getEmployee().getId());

            // Add Average Rating Data
            if (ratingMap.containsKey(goal.getId())) {
                goalData.putAll(ratingMap.get(goal.getId()));
            } else {
                goalData.put("averageRating", 0.0);
                goalData.put("totalRatings", 0);
            }

            // Add Individual Feedbacks
            goalData.put("feedbacks", feedbackMap.getOrDefault(goal.getId(), new ArrayList<>()));

            result.add(goalData);
        }
        return result;
    }

    // this is the method we are using to shedule the goal feedback

    @Transactional
    @Scheduled(cron = "0 5 18 * * ?") // Runs daily at the scheduled time
    public void updateReviewStatusAndNotifyManagers() {
        List<CompanyConfig> companyConfigs = companyConfigRepository.findAll();
        LocalDate today = LocalDate.now();

        for (CompanyConfig config : companyConfigs) {
            LocalDate startDate = config.getFeedbackDate();
            int frequency = config.getFeedbackFrequency();

            // Generate review dates
            while (startDate.isBefore(today) || startDate.isEqual(today)) {
                if (startDate.equals(today)) {
                    List<EmployeeGoalSetting> goals = employeeGoalSettingRepository
                            .findByReviewStatusFalseAndCompany(config.getCompany().getId());

                    // Group goals by Employee
                    Map<Employee, List<EmployeeGoalSetting>> employeeGoalsMap = goals.stream()
                            .collect(Collectors.groupingBy(EmployeeGoalSetting::getEmployee));

                    for (Map.Entry<Employee, List<EmployeeGoalSetting>> entry : employeeGoalsMap.entrySet()) {
                        Employee employee = entry.getKey();
                        List<EmployeeGoalSetting> employeeGoals = entry.getValue();

                        // Update all records for this employee
                        for (EmployeeGoalSetting goal : employeeGoals) {
                            goal.setReviewStatus(true);
                        }
                        employeeGoalSettingRepository.saveAll(employeeGoals);

                        // Get the reporting manager from EmployeeGoalSetting
                        Employee reportingManager = employeeGoals.get(0).getEmployees();

                        // Send a separate email to the reporting manager for each employee
                        sendNotificationToManager(reportingManager, employee, employeeGoals);
                    }
                }
                startDate = startDate.plusMonths(frequency); // Move to next review period

            }

        }

    }

    private void sendNotificationToManager(Employee manager, Employee employee, List<EmployeeGoalSetting> goals) {
        if (manager == null)
            return; // Skip if there's no reporting manager assigned

        String subject = "📢 Goal Review requested  " + employee.getFirstName();
        StringBuilder message = new StringBuilder("Dear " + manager.getFirstName() + ",\n\n")
                .append("The following goals of ").append(employee.getFirstName())
                .append(" are now requested for review:\n\n");

        for (EmployeeGoalSetting goal : goals) {
            message.append("-").append(goal.getGoal()).append("\n");
        }

        message.append("\nPlease review them accordingly.\n\nBest Regards,\nCompany HR");

    }

    /////
    ///

    public List<EmployeeGoalSetting> getGoalsByEmployeeAndDepartmentAndReginANdGoaltTypeAndStatusAndGoalType(
            int regionId, Long employeeId, int departmentId, int typeId, boolean status, boolean goalType) {
        return employeeGoalSettingRepository.findGoalsWithDepartmentOrZeroAndRegionIdOrZeroAndTypeIdOrZero(regionId,
                employeeId, goalType, departmentId, typeId, status);
    }

    /// this is the method to check employee approval on goal setting notfy and auto
    /// aprrove if not done.
    ///
    ///
    ///
    // Schedule to run daily at midnight
    // Schedule to run daily at midnight
    @Scheduled(cron = "0 3 15 * * ?")
    public void checkEmployeeGoalApprovalStatus() {
        List<EmployeeGoalSetting> goals = employeeGoalSettingRepository.findByEmployeeApprovalFalse();
        LocalDate today = LocalDate.now();

        // Group goals by employee
        Map<Employee, List<EmployeeGoalSetting>> employeeGoalsMap = goals.stream()
                .collect(Collectors.groupingBy(EmployeeGoalSetting::getEmployee));

        for (Map.Entry<Employee, List<EmployeeGoalSetting>> entry : employeeGoalsMap.entrySet()) {
            Employee employee = entry.getKey();
            List<EmployeeGoalSetting> employeeGoals = entry.getValue();

            // Check for each goal's date and employee approval status
            for (EmployeeGoalSetting goal : employeeGoals) {
                LocalDate creationDate = goal.getDate();
                long daysSinceCreation = today.toEpochDay() - creationDate.toEpochDay();

                if (daysSinceCreation > 5 && daysSinceCreation < 7) {
                    // Send a reminder email if it's been more than 5 days but less than 7
                    sendApprovalReminderEmail(employee, employeeGoals);
                    break; // No need to send multiple reminder emails for the same employee
                } else if (daysSinceCreation >= 7) {
                    // Auto-approve after 7 days and send an auto-approval email
                    goal.setEmployeeApproval(true);
                    employeeGoalSettingRepository.save(goal); // Save the updated goal
                }
            }

            // If any goals were auto-approved, send the auto-approval email
            if (employeeGoals.stream().anyMatch(goal -> goal.isEmployeeApproval())) {
                sendAutoApprovalEmail(employee, employeeGoals);
            }
        }
    }

    private void sendApprovalReminderEmail(Employee employee, List<EmployeeGoalSetting> goals) {
        String subject = "Reminder: Goal Approval Pending";
        StringBuilder message = new StringBuilder(
                String.format("Dear %s,\n\nThe following goals are pending your approval:\n\n",
                        employee.getFirstName()));

        for (EmployeeGoalSetting goal : goals) {
            if (!goal.isEmployeeApproval()) {
                message.append("- ").append(goal.getGoal()).append("\n");
            }
        }

        message.append("\nPlease review and approve them as soon as possible.\n\nBest Regards,\nCompany HR");

        emailService.sendEmail(employee.getEmail(), subject, message.toString());
    }

    private void sendAutoApprovalEmail(Employee employee, List<EmployeeGoalSetting> goals) {
        String subject = "Goal Auto-Approved";
        StringBuilder message = new StringBuilder(
                String.format("Dear %s,\n\nThe following goals have been auto-approved due to inactivity:\n\n",
                        employee.getFirstName()));

        List<EmployeeGoalSetting> autoApprovedGoals = new ArrayList<>();
        for (EmployeeGoalSetting goal : goals) {
            if (!goal.isEmployeeApproval()) {
                goal.setEmployeeApproval(true); // Auto-approve the goal
                autoApprovedGoals.add(goal); // Collect the goals that were auto-approved
            }
        }

        // Save the auto-approved goals to the database
        employeeGoalSettingRepository.saveAll(autoApprovedGoals);

        for (EmployeeGoalSetting goal : autoApprovedGoals) {
            message.append("- ").append(goal.getGoal()).append("\n");
        }

        message.append("\nThese goals have been automatically approved.\n\nBest Regards,\nCompany HR");

        emailService.sendEmail(employee.getEmail(), subject, message.toString());
    }

    ////

    public List<EmployeeGoalSetting> getEmployeeGoalsByYear(Long employeeId, int year) {
        return employeeGoalSettingRepository.findByEmployeeIdAndYear(employeeId, year);
    }

    // public int updateEmployeeApprovalStatus(Long employeeId) {
    // return
    // employeeGoalSettingRepository.updateEmployeeApprovalByEmployeeId(employeeId);
    // }

    @Transactional
    public String updateEmployeeApprovalStatus(Long employeeId) {
        // Fetch only goals that are NOT approved
        List<EmployeeGoalSetting> unapprovedGoals = employeeGoalSettingRepository.findUnapprovedGoalsByEmployeeId(employeeId);

        if (!unapprovedGoals.isEmpty()) {
            // Update approval status
            employeeGoalSettingRepository.updateUnapprovedGoalsByEmployeeId(employeeId);

            // Send email notification
            sendApprovalNotificationToManager(unapprovedGoals);

            return "Approved " + unapprovedGoals.size() + " goal(s) for Employee ID: " + employeeId;
        } else {
            return "No unapproved goals found for Employee ID: " + employeeId;
        }
    }

    private void sendApprovalNotificationToManager(List<EmployeeGoalSetting> approvedGoals) {
        Employee employee = approvedGoals.get(0).getEmployee();
        Employee reportingManager = approvedGoals.get(0).getEmployees(); // Reporting manager

        if (reportingManager != null) {
            String subject = "Employee Goal Approval Notification";
            StringBuilder message = new StringBuilder("Dear " + reportingManager.getFirstName() + ",\n\n");
            message.append("The following goals have been approved by " + employee.getFirstName() + ":\n\n");

            for (EmployeeGoalSetting goal : approvedGoals) {
                message.append("- ").append(goal.getGoal()).append("\n");
            }

            message.append("\nPlease review if needed.\n\nBest Regards,\nHR Team");

            // Send email to the reporting manager
            emailService.sendEmail(reportingManager.getEmail(), subject, message.toString());
        }
    }
}
