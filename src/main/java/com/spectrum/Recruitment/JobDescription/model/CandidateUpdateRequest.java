package com.spectrum.Recruitment.JobDescription.model;

import lombok.Data;

@Data
public class CandidateUpdateRequest {
    private Long id;
    private String firstName;
    private String middelName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String highestQualification;
    private Integer yearsOfExperience;
    private String skills;
    private String jobTitle;
    private Boolean status;
    private Boolean shortListed;
    private Boolean finalizedForInterview;
    private Boolean selectedCandidate;
    private Integer relevantExperience;
}