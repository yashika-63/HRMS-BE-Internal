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

public class JobDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String jobTitle; 

    @Column
    private String jobDesc;

    @Column
    private boolean status;

    @Column
    private String requiredSkills;

    @Column
    private String requiredExperience;

    @Column
    @CreationTimestamp
    private LocalDate date;

    @Column
    private long createdBy;

    @Column
    private String  contactPerson;

    @Column
    private String  contactPersonEmail;

    @Column(unique = true, nullable = false, length = 64)
    private String jobKey;

    @Column(length = 255)
    private String jdLink;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private CompanyRegistration company;
}
