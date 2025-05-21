package com.spectrum.Training.model;

import java.time.LocalDate;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Certification {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    @Column
    private String employeeName;
    @Column
    private  String courseDescription;
    @Column
    private LocalDate completionDate;
    @Column
    private String empRole;
 
    @Column
    private String signature;

    @Column
    private String certificationId;

    @Column
    private boolean applied;

   
    @ManyToOne  

    @JoinColumn(name = "Employee_id",  referencedColumnName = "id",nullable = true)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
@JsonIgnore
@JoinColumn(name = "company_id", referencedColumnName = "id", nullable = true) //  Foreign key in Inductions table
private CompanyRegistration company;


@ManyToOne(fetch = FetchType.LAZY)
@JsonIgnore
@JoinColumn(name = "Result_id",  referencedColumnName = "id",nullable = true)
    private ResultTraining resultTraining; 




    @ManyToOne

    @JoinColumn(name = "TrainIng_id", referencedColumnName = "id", nullable = true) //  Foreign key in TrainingHRMS table
    private TrainingHRMS trainingHRMS;




    


}
