package com.spectrum.Recruitment.JobDescription.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ScheduleInterviewRequest {

    private Long interviewerId;
    private LocalDate interviewDate;
    private LocalTime interviewTime;
    private String interviewTitle;
    private String interviewUrl;
    private boolean interviewMode; // true for online, false for offline
    private String interviewDescription;
    private boolean interviewStatus;
    private boolean interviewCompletionStatus;
    private boolean interviewResult;
    private List<Long> candidateIds; // List of CandidateRegistration ids

    private Long jobDescriptionId;
    private Long companyId;
}
