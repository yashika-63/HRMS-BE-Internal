package com.spectrum.ExitProcess.Model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Table
@Entity

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OffBoarding {
     @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    @CreationTimestamp
    private LocalDate date;

    @Column
    private String reason;

    @Column
    private String other;

    @Column
    private Boolean completionStatus;

    @Column
    private Boolean applied;

    @Column
    private Boolean status;

    @Column
    private String department;

    @Column
    private Long deptId;

    @Column
    private LocalDate lastWorkingDate;

    @Column
    private Long regionId;

    @Column (length= 500)
    private String completedBy;

    

@ManyToOne
@JoinColumn(name = "Company_id", referencedColumnName = "id") 
private CompanyRegistration company;

@ManyToOne
@JoinColumn(name = "Employee_id",  referencedColumnName = "id")
private Employee employee;


@ManyToOne
@JoinColumn(name = "ReportingManager_id",  referencedColumnName = "id")
private Employee employees;

@ManyToOne
@JoinColumn(name = "HR_id",  referencedColumnName = "id")
private Employee employeeOne;



}
