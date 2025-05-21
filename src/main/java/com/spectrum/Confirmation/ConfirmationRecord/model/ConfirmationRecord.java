package com.spectrum.Confirmation.ConfirmationRecord.model;

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
@NoArgsConstructor
@AllArgsConstructor
@Data

public class ConfirmationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @CreationTimestamp
    private LocalDate date;

    @Column
    private boolean status;

    @Column
    private boolean responsiblePersonAction;

    @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
    
    @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "responsible_person_id", referencedColumnName = "id", nullable = false)
    private Employee employees;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "hr_id", referencedColumnName = "id", nullable = false)
    private Employee employeehr;
 
    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private CompanyRegistration company;
    
}
