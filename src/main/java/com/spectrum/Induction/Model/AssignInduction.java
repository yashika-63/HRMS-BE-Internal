package com.spectrum.Induction.Model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

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
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class AssignInduction {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long assignedBy;
     @Column
     @CreationTimestamp
    private LocalDate assignDate;

    @Column
    private Boolean expiryStatus;

    @Column 
    private Boolean completionStatus;

    @Column
    private Long companyId;

    private LocalDate expiryDate;

    // @Column
    // private Boolean completionStatus;


    @ManyToOne
    @JoinColumn(name = "induction_id",  referencedColumnName = "id",nullable = false)
    private Inductions induction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "Employee_id",  referencedColumnName = "id",nullable = true)
    private Employee employee;

}
