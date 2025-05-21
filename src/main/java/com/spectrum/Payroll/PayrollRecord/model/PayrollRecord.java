package com.spectrum.Payroll.PayrollRecord.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.Payroll.PayrollHours.model.EncryptedDoubleConverter;
import com.spectrum.Payroll.PayrollHours.model.EncryptedIntegerConverter;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;

@Entity
@Table(name = "payroll_records")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PayrollRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(nullable = false ,columnDefinition = "TEXT")
    private double staticAmount;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(nullable = false ,columnDefinition = "TEXT" )
    private double variableAmount;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(nullable = false,columnDefinition = "TEXT")
    private double variableAmountPerHour;

    

    @Column(nullable = false)
    private boolean status;

    @Convert(converter = EncryptedIntegerConverter.class)
    @Column(columnDefinition = "TEXT")
    private int assignHours;

    @Column
    private int expectedHours;

    @Column
    private Integer actualHours;

// following data is from holiday employee and company calendar calculation.
    @Column
    private int totalWorkingHours;
    @Column
    private int actualWorkingDays;
    @Column
    private int dailyWorkingHours;
    @Column
    private double overtimeHours;
    @Column
    private double overtimeRate;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double basicAmount;
    @Column
    private boolean proceedForPayrollStatus = false;
    @Column
    private boolean proceedForPayment = false;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double incomeTaxDeduction = 0; 

    @ManyToOne
    //@JsonIgnore
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private CompanyRegistration company;

}
 