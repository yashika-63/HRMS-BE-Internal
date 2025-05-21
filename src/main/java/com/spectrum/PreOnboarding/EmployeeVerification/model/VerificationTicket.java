package com.spectrum.PreOnboarding.EmployeeVerification.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;
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

@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VerificationTicket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean status;

    @Column
    private boolean sendForVerification;

    @Column
    private boolean verificationStatus;

    @Column
    @CreationTimestamp
    private LocalDate date;
 
    @Column
    private boolean employeeAction;

    @Column
    private boolean sentForEmployeeAction;

    @Column
    private boolean employeeDataUpdateAccess;

    @Column
    private boolean educationDataUpdateAccess;

    @Column
    private boolean employeementDataUpdateAccess;

    @Column
    private boolean sentBack;

    @Column
    private boolean reportingPersonAction;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reporting_person_id", referencedColumnName = "id", nullable = false)
    private Employee employees;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private CompanyRegistration company;

    @ManyToOne
    @JoinColumn(name = "pre_registration_id", referencedColumnName = "id", nullable = false)
    private PreRegistration preRegistration;


}
