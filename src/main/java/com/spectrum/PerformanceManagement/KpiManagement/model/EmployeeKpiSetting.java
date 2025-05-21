package com.spectrum.PerformanceManagement.KpiManagement.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Data

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeKpiSetting {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 500)
    private String kpi;

    @Column
    @CreationTimestamp
    private LocalDate date;

    // this is for active kpis 
    @Column
    private boolean status;
    
     // to active an deactive time to
     @Column
    private boolean reviewStatus;

    // to take approval from employee for kpi setted for that year
    @Column
    private boolean employeeApproval;

     @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "reporting_manager_id", referencedColumnName = "id", nullable = false)
    private Employee employees;
}
