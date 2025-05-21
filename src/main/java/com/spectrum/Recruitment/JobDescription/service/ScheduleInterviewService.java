package com.spectrum.Recruitment.JobDescription.service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.spectrum.Recruitment.JobDescription.model.ScheduleInterview;
import com.spectrum.Recruitment.JobDescription.model.ScheduleInterviewRequest;
import com.spectrum.Recruitment.JobDescription.repository.ScheduleInterviewRepository;
import com.spectrum.Recruitment.JobDescription.model.CandidateRegistration;
import com.spectrum.Recruitment.JobDescription.model.JobDescription;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.transaction.Transactional;

import com.spectrum.Recruitment.JobDescription.repository.CandidateRegistrationRepository;
import com.spectrum.Recruitment.JobDescription.repository.JobDescriptionRepository;

@Service
public class ScheduleInterviewService {

    @Autowired
    private ScheduleInterviewRepository scheduleInterviewRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private JobDescriptionRepository jobDescriptionRepository;

    @Autowired
    private CandidateRegistrationRepository candidateRegistrationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JavaMailSender mailSender;



    @Autowired
    private EmailService emailService;

    public ScheduleInterview saveInterview(ScheduleInterview interview, Long companyId, Long jobDescId, Long candidateId, Long interviewerId) {
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        JobDescription jobDescription = jobDescriptionRepository.findById(jobDescId)
                .orElseThrow(() -> new RuntimeException("Job Description not found"));

        CandidateRegistration candidate = candidateRegistrationRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        Employee interviewer = employeeRepository.findById(interviewerId)
                .orElseThrow(() -> new RuntimeException("Interviewer not found"));

        interview.setCompany(company);
        interview.setJobDescription(jobDescription);
        interview.setCandidateRegistration(candidate);
        interview.setEmployees(interviewer);

        interview.setInterviewStatus(true);
        interview.setInterviewComplitionStatus(false);
        interview.setInterviewResult(false);

        ScheduleInterview saved = scheduleInterviewRepository.save(interview);

        // Send email notifications
        sendEmail(candidate.getEmail(), "Interview Scheduled",
                "Dear " + candidate.getFirstName() + ",\n\nYour interview for the position of " + jobDescription.getJobTitle()
                        + " is scheduled on " + interview.getInterviewDate() + " at " + interview.getInterviewTime()
                        + ".\nInterview Mode: " + (interview.isInterviewMode() ? "Online" : "Offline")
                        + "\nJoin Link: " + interview.getInterviewUrl()
                        +"\nDesc: " + interview.getInterviewDescription()
                        + "\n\nBest regards,\n" + company.getCompanyName());

        sendEmail(interviewer.getEmail(), "You have been scheduled for an interview",
                "Dear " + interviewer.getFirstName() + ",\n\nYou are scheduled to interview "
                        + candidate.getFirstName() + " " + candidate.getLastName()
                        + " for the position of " + jobDescription.getJobTitle()
                        + " on " + interview.getInterviewDate() + " at " + interview.getInterviewTime()
                        +"\nDesc: " + interview.getInterviewDescription()

                        + ".\n\nBest regards,\n" + company.getCompanyName());

        return saved;
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@gmail.com"); // replace with your email
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }


    public List<ScheduleInterview> getInterviewsByCandidateIdAndStatus(Long candidateId) {
        return scheduleInterviewRepository.findByCandidateRegistrationIdAndInterviewStatusTrue(candidateId);
    }


   public List<ScheduleInterview> checkScheduleConflict(Long interviewerId, LocalDate date, Time time) {
    LocalTime localTime = time.toLocalTime();
    Time oneHourBefore = Time.valueOf(localTime.minusHours(1));
    Time oneHourAfter = Time.valueOf(localTime.plusHours(1));

    return scheduleInterviewRepository.findConflictingSchedules(interviewerId, date, oneHourBefore, oneHourAfter);
}


public List<ScheduleInterview> getUpcomingInterviewsByCompany(Long companyId) {
        LocalDate today = LocalDate.now();
        return scheduleInterviewRepository.findUpcomingInterviewsByCompany(companyId, today);
    }
    



    public List<ScheduleInterview> getInterviewsByStatusAndInterviewer(boolean status, Long interviewerId) {
        return scheduleInterviewRepository.findByStatusAndInterviewerId(status, interviewerId);
    }
    

    public List<ScheduleInterview> getInterviewsByJobDescriptionAndStatus(Long jobDescriptionId) {
        // Employee interviewer = interview.getEmployees();  // interview is your main object
        // response.put("Interviewer Name", interviewer.getFirstName());
        return scheduleInterviewRepository.findByJobDescriptionIdAndInterviewStatusTrue(jobDescriptionId);
    }


    public List<ScheduleInterview> getTodayInterviewsByInterviewer(Long interviewerId) {
        LocalDate today = LocalDate.now();
     
        return scheduleInterviewRepository.findByInterviewDateAndInterviewStatusTrueAndEmployeesId(today, interviewerId);
    }


    public void deleteScheduleInterviewById(Long id) {
        if (!scheduleInterviewRepository.existsById(id)) {
            throw new RuntimeException("Interview not found with ID: " + id);
        }
        scheduleInterviewRepository.deleteById(id);
    }
   

    public ScheduleInterview updateInterviewResult(Long interviewId, boolean passed) {
        ScheduleInterview interview = scheduleInterviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found with ID: " + interviewId));

        interview.setInterviewComplitionStatus(true); // always true when result is evaluated
        interview.setInterviewResult(passed); // true = passed, false = failed

        return scheduleInterviewRepository.save(interview);
    }






    public void sendTodayInterviewRemindersToInterviewer() {
        LocalDate today = LocalDate.now();
        List<ScheduleInterview> todayInterviews = scheduleInterviewRepository.findByInterviewDateAndInterviewStatus(today, true);

        // Group by interviewer
        Map<Employee, List<ScheduleInterview>> grouped = todayInterviews.stream()
                .collect(Collectors.groupingBy(ScheduleInterview::getEmployees));

        for (Map.Entry<Employee, List<ScheduleInterview>> entry : grouped.entrySet()) {
            Employee interviewer = entry.getKey();
            List<ScheduleInterview> interviews = entry.getValue();

            String to = interviewer.getEmail(); // make sure Employee has email field
            String subject = "Today's Interview Schedule";
            StringBuilder body = new StringBuilder("Dear " + interviewer.getFirstName() + ",\n\nHere are your scheduled interviews for today:\n\n");

            for (ScheduleInterview si : interviews) {
                body.append("- ")
                    .append(si.getInterviewTitle())
                    .append(" at ")
                    .append(si.getInterviewTime())
                    .append(" with Candidate: ")
                    .append(si.getCandidateRegistration().getFirstName())
                    .append("\n");
            }

            body.append("\nGood luck!\nRecruitment Team");

            emailService.sendEmail(to, subject, body.toString());
        }
    }



    
    public ScheduleInterview getScheduleInterviewById(Long id) {
        return scheduleInterviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ScheduleInterview not found with id: " + id));
    }

    @Transactional
    public String scheduleMultipleInterviews(ScheduleInterviewRequest request) {
        // Fetch interviewer, job description, and company details
        Employee interviewer = new Employee();  // Assuming you fetch from the database by ID
        interviewer.setId(request.getInterviewerId());

        JobDescription jobDescription = new JobDescription();  // Assuming you fetch from the database by ID
        jobDescription.setId(request.getJobDescriptionId());

        CompanyRegistration company = new CompanyRegistration();  // Assuming you fetch from the database by ID
        company.setId(request.getCompanyId());

        // Iterate through the candidates and schedule an interview for each
        for (Long candidateId : request.getCandidateIds()) {
            CandidateRegistration candidate = candidateRegistrationRepository.findById(candidateId)
                    .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + candidateId));

            // Create and populate the ScheduleInterview object
            ScheduleInterview scheduleInterview = new ScheduleInterview();
            scheduleInterview.setInterviewTitle(request.getInterviewTitle());
            scheduleInterview.setInterviewUrl(request.getInterviewUrl());
            scheduleInterview.setInterviewMode(request.isInterviewMode());
            scheduleInterview.setInterviewDescription(request.getInterviewDescription());
            scheduleInterview.setInterviewStatus(request.isInterviewStatus());
            scheduleInterview.setInterviewDate(request.getInterviewDate());
            // scheduleInterview.setInterviewTime(request.getInterviewTime());
            scheduleInterview.setInterviewComplitionStatus(request.isInterviewCompletionStatus());
            scheduleInterview.setInterviewResult(request.isInterviewResult());
            scheduleInterview.setCandidateRegistration(candidate);
            scheduleInterview.setJobDescription(jobDescription);
            scheduleInterview.setCompany(company);
            scheduleInterview.setEmployees(interviewer);

            // Save the scheduled interview record
            scheduleInterviewRepository.save(scheduleInterview);
        }

        return "Interviews scheduled successfully for multiple candidates.";
    }


    public List<ScheduleInterview> getInterviewsByInterviewIdAndMonthYear(Long interviewId, int month, int year) {
        // Calculate the start and end date for the given month and year
        LocalDate startDate = LocalDate.of(year, Month.of(month), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // Fetch interviews by interview ID and date range
        return scheduleInterviewRepository.findByIdAndInterviewDateBetween(interviewId, startDate, endDate);
    }

    public List<ScheduleInterview> getInterviewsByInterviewerIdAndMonthYear(Long interviewerId, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return scheduleInterviewRepository.findByEmployeesIdAndInterviewDateBetween(interviewerId, startDate, endDate);
    }
    
    
}
