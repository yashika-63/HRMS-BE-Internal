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
import jakarta.persistence.Index;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(indexes = {
  @Index(name = "idx_assigned_by", columnList = "assignedBy"),
  @Index(name = "idx_company_id", columnList = "companyId")
})public class AssignTraining {
      @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long assignedBy;
     @Column
    private LocalDate assignDate;

    @Column
    private Boolean expiryStatus;

    @Column 
    private Boolean completionStatus;

    @Column
    private Long companyId;

    @Column
    private Integer expiryDays;
  


    
    @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "Training_id",  referencedColumnName = "id",nullable = false)
    private TrainingHRMS trainingHRMS;

     @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "Employee_id",  referencedColumnName = "id",nullable = false)
    private Employee employee;

}
