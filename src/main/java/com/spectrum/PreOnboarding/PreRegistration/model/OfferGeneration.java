package com.spectrum.PreOnboarding.PreRegistration.model;
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
@Table(name = "offer_generation")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OfferGeneration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 50)
    private String middleName;

    // full time Part time 

    @Column(nullable = false)
    private String employeeType;

    @Column
    private Integer workingHours;

    @Column
    private Integer workStateCode;

    @Column(length = 100)
    private String workState;

    @Column
    private Integer workCategoryCode;

    @Column(length = 100)
    private String workCategory;

    @Column
    private double allowableOvertimedays;

    @Column
    private Integer regionId;

    @Column(length = 100)
    private String region;

    @Column
    private Integer departmentId;

    @Column(length = 100)
    private String department;

    @Column
    private boolean overtimeApplicable;

    @Column
    private boolean sendForApproval;

    @Column
    private boolean rejectedByCandidate;

    @Column
    private boolean acceptedByCandidate;

    @Column
    private boolean remarkedByCandidate;

    @Column
    private boolean actionTakenByCandidate;

    @Column
    private boolean status;

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



