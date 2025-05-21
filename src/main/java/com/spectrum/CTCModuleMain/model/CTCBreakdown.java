package com.spectrum.CTCModuleMain.model;

import java.time.LocalDate;

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
public class CTCBreakdown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private long basicSalary;

    @Column
    private long houseRentAllowance;

    @Column
    private long medicalAllowance;

    @Column
    private long otherAllowance;

    @Column
    private long lunchAllowance;

    @Column
    private long totalCtcAmount;

    @Column
    private long providentFund;

    @Column
    private long conveyanceAllowance;

    @Column
    private long performanceBonus;

    @Column
    private long gratuity;

    @Column
    private long specialAllowances;

    @Column
    private LocalDate ctcDate;

    @Column
    private LocalDate performanceBonusDate;

    // New fields for effective date range
    @Column
    private LocalDate effectiveFromDate;

    @Column
    private LocalDate effectiveToDate;

    @Column
    private boolean ctcStatus;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
}
