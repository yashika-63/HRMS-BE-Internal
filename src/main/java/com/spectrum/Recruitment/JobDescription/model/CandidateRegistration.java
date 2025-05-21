package com.spectrum.Recruitment.JobDescription.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.spectrum.model.CompanyRegistration;

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
public class CandidateRegistration {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String middelName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column
    private String highestQualification;

    @Column
    private int yearsOfExperience;

    @Column( length = 500)
    private String skills;

    @Column( length = 100)
    private String jobTitle;

    @Column
    @CreationTimestamp
    private LocalDate date;

    @Column
    private boolean status;

    @Column
    private boolean shortListed;

    @Column
    private boolean finalizedForInterview;

    @Column
    private boolean selectedCandidate;

    @Column
    private int relevantExperience;

    @ManyToOne
    @JoinColumn(name = "job_description_id", referencedColumnName = "id", nullable = false)
    private JobDescription jobDescription;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private CompanyRegistration company;

}
