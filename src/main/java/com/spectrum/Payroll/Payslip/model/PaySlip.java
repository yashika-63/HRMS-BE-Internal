package com.spectrum.Payroll.Payslip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.Payroll.PayrollHours.model.EncryptedDoubleConverter;
import com.spectrum.Payroll.PayrollHours.model.EncryptedIntegerConverter;
import com.spectrum.Payroll.PayrollRecord.model.PayrollRecord;
import com.spectrum.model.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
public class PaySlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptedIntegerConverter.class)
    @Column(columnDefinition = "TEXT")
    private int actualWorkingHours;

    @Convert(converter = EncryptedIntegerConverter.class)
    @Column(columnDefinition = "TEXT")
    private int totalWorkingHours;

    @Column
    private String lable;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double amount;

    @Column
    private boolean type;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double netSalary;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double overtimeAmount;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double incomeTaxDeduction;

    // here we establish required relation
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "payrollRecord_id", referencedColumnName = "id", nullable = false)
    private PayrollRecord payrollRecord;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
}
