package com.spectrum.Payroll.HoliDayCalender.model;

import com.spectrum.model.Employee;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWorkingDays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private int totalWorkingDays;

    @Column(nullable = false)
    private String calculatedPeriod; // e.g., "2024-01-01 to 2024-01-31"
}
