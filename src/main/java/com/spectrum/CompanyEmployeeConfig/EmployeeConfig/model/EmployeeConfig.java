package com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.model.CompanyRegistration;
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
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String employeeType;

    @Column
    private int workingHours;

    @Column
    private int workStateCode;

    @Column
    private int workCategoryCode;

    @Column
    private boolean employeeSpecialHolidayAccess;

    @Column
    private boolean overtimeApplicable;

    @Column
    private double overtimeRate;

    @Column
    private double allowableOvertimeHours;

    @Column
    private double allowableOvertimedays;


    @Column
    private String workState;

    @Column
    private String workCategory;

////////////////
// this is updated section 

    @Column
    private int regionId;

    @Column
    private String region;

    @Column
    private int typeId;

    @Column
    private String type;

    @Column
    private int departmentId;

    @Column
    private String department;

    @Column
    private int levelCode;
    
    @Column
    private String level;

    // updated code for phase4 

    @Column
    private String designation;
 
    @Column
    private int designation_id;

    @Column
    private boolean confirmationStatus;

    @Column
    private int probationMonth;


    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "reporting_manager_id", referencedColumnName = "id", nullable = false)
    private Employee reportingManager;
 

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = true) //  Foreign key in Inductions table
    private CompanyRegistration company;


}
