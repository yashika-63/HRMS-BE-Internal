package com.spectrum.PerformanceManagement.GoalSetting.model;

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
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeGoalSetting {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String goal;

    // @Column(length = 1500)
    // private String goalDescription;

    @Column
    private int regionId;

    @Column
    private String region;

    @Column
    private int typeId;
    // type will be technical or non technical
    @Column
    private String type;

    @Column
    private int departmentId;

    @Column
    private String department;

    // for all or personal
    @Column
    private boolean goalType;

    @Column
    @CreationTimestamp
    private LocalDate date;

    // this is for active goals 
    @Column
    private boolean status;

    // to active an deactive time to
    @Column
    private boolean reviewStatus;

    // to take approval from employee for goal setted for that year
    @Column
    private boolean employeeApproval;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reporting_manager_id", referencedColumnName = "id", nullable = false)
    private Employee employees;
}
