package com.spectrum.Induction.Model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.model.Employee;

import jakarta.persistence.CascadeType;
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
import lombok.ToString;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ResultInduction {

        
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String note;
    @Column
    private boolean termsAndCondition;

      // Many ResultInduction can refer to one Inductions
    

 


    // Unidirectional relationship with Inductions
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "induction_id", referencedColumnName = "id", nullable = true)
    @JsonIgnore
    private Inductions induction;// ResultInduction references Inductions, not vice versa

    // Unidirectional relationship with InductionAck
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Acknowledge_id", referencedColumnName = "id", nullable = true)
    private InductionAck inductionAck; // ResultInduction references InductionAck, not vice versa

    // Unidirectional relationship with AssignInduction
    // ResultInduction references AssignInduction, not vice versa
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "Employee_id",  referencedColumnName = "id",nullable = true)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "assign_id",  referencedColumnName = "id",nullable = true)
    private AssignInduction assignInduction;
}
