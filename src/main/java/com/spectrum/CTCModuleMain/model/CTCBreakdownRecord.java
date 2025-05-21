package com.spectrum.CTCModuleMain.model;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.spectrum.Payroll.PayrollHours.model.EncryptedDoubleConverter;
import com.spectrum.model.Employee;

@Entity
@Table(name = "ctc_breakdown_records")
public class CTCBreakdownRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(nullable = false,columnDefinition = "TEXT")
    private double staticAmount;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(nullable = false,columnDefinition = "TEXT")
    private double variableAmount;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(nullable = false,columnDefinition = "TEXT")
    private double variableAmountPerHour;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean status = true; // Default status is 'unpaid'

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    // Constructors, Getters and Setters
    public CTCBreakdownRecord() {
    }

    public CTCBreakdownRecord(double staticAmount, double variableAmount, double variableAmountPerHour, LocalDate date,
            Employee employee) {
        this.staticAmount = staticAmount;
        this.variableAmount = variableAmount;
        this.variableAmountPerHour = variableAmountPerHour;
        this.date = date;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getStaticAmount() {
        return staticAmount;
    }

    public void setStaticAmount(double staticAmount) {
        this.staticAmount = staticAmount;
    }

    public double getVariableAmount() {
        return variableAmount;
    }

    public void setVariableAmount(double variableAmount) {
        this.variableAmount = variableAmount;
    }

    public double getVariableAmountPerHour() {
        return variableAmountPerHour;
    }

    public void setVariableAmountPerHour(double variableAmountPerHour) {
        this.variableAmountPerHour = variableAmountPerHour;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    
}
