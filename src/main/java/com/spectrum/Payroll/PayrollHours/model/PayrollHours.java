package com.spectrum.Payroll.PayrollHours.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

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
public class PayrollHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int assignHours;
    
    @Column
    private int expectedHours;

    @Column
    private int actualHours;

    @Column
    private Boolean Status = true;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @Column(updatable = false)
    private LocalDate date;
}
