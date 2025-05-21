package com.spectrum.PreOnboarding.PreRegistration.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

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
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PreRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String middleName;

    @Column
    private String email;

    @Column
    private boolean status;

    @Column
    @CreationTimestamp
    private LocalDate date;

    @Column
    private boolean offerGenerated;

    @Column
    private String assignByFirstName;

    @Column
    private String assignByLastName;

    @Column
    private boolean loginStatus;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private CompanyRegistration company;

    @Column(unique = true)
    private String preLoginToken;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reporting_person_id", referencedColumnName = "id", nullable = false)
    private Employee employees;

}
