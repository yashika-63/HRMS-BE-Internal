package com.spectrum.Recruitment.JobDescription.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class InterviewNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String note;

    @Column
    private String actionTakenByName;

    @Column
    private int interviewScore;

    @Column
    @CreationTimestamp
    private LocalDate date;

    @Column
    private boolean status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "schedule_interview_id", referencedColumnName = "id", nullable = false)
    private ScheduleInterview scheduleInterview;
    
    @ManyToOne
    @JoinColumn(name = "job_description_id", referencedColumnName = "id", nullable = false)
    private JobDescription jobDescription;
}
