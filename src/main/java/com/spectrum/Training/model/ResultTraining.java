package com.spectrum.Training.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultTraining {

       @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String note;
    @Column
    private boolean termsAndCondition;

    @Column
    private LocalDate completionDate;

@ManyToOne
@JsonIgnore
@JoinColumn(name = "TrainIng_id", referencedColumnName = "id", nullable = true) //  Foreign key in TrainingHRMS table
private TrainingHRMS trainingHRMS;


@ManyToOne(fetch = FetchType.LAZY)
@JsonIgnore
@JoinColumn(name = "Employee_id",  referencedColumnName = "id",nullable = true)
private Employee employee;


    @ManyToOne(fetch = FetchType.LAZY)
   
    @JoinColumn(name = "Acknowledge_id",  referencedColumnName = "id",nullable = true)
    private TrainingAcknowledge trainingAcknowledge;

    
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "Assign_id",  referencedColumnName = "id",nullable = true)
    private AssignTraining assignTraining;



}
