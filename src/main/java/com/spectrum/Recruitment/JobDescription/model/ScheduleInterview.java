package com.spectrum.Recruitment.JobDescription.model;

import java.sql.Time;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScheduleInterview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String interviewTitle ;

    @Column
    private String interviewUrl ;

    // this is for online or offline 

    @Column
    private boolean interviewMode ;

    @Column
    private String interviewDescription ;

    @Column
    private boolean interviewStatus ;

    @Column
    private LocalDate interviewDate;

    @Column
    private Time interviewTime;

    @Column
    private boolean interviewComplitionStatus;

    @Column
    private boolean interviewResult;

    @ManyToOne
    @JoinColumn(name = "candidate_registration_id", referencedColumnName = "id", nullable = false)
    private CandidateRegistration candidateRegistration;

    @ManyToOne
    @JoinColumn(name = "job_description_id", referencedColumnName = "id", nullable = false)
    private JobDescription jobDescription;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private CompanyRegistration company;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "interviewer_id", referencedColumnName = "id", nullable = false)
    private Employee employees;
}
