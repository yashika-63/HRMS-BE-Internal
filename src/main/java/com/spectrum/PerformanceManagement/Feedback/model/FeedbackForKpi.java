package com.spectrum.PerformanceManagement.Feedback.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.spectrum.PerformanceManagement.KpiManagement.model.EmployeeKpiSetting;
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
@Data
@AllArgsConstructor
@NoArgsConstructor

public class FeedbackForKpi {


    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column
    private int ratingForKpi; // Corrected spelling


    @Column
    private String note;


    @Column
    @CreationTimestamp
    private LocalDate date;
     @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
      
    @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "reporting_manager_id", referencedColumnName = "id", nullable = false)
    private Employee employees;

    @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "employee_kpi_setting_id", referencedColumnName = "id", nullable = false)
    private EmployeeKpiSetting employeeKpiSetting;
}
