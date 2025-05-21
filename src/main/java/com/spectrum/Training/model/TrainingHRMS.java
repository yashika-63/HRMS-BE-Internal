package com.spectrum.Training.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.model.CompanyRegistration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table( indexes = {
        @Index(name = "idx_region_id", columnList = "regionId"),
        @Index(name = "idx_type", columnList = "type"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_date", columnList = "date"),
        @Index(name = "idx_created_by_emp_id", columnList = "createdByEmpId"),
        @Index(name = "idx_department_id", columnList = "departmentId")

    })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingHRMS {

    
        @Id
        @Column
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column
        private String region;
        @Column
        private Long regionId;
        @Column
        private String type;
        @Column
        private boolean status;
        @Column
        @CreationTimestamp
        private LocalDate date;
        @Column
        private Long createdByEmpId;
        
        @Column
        private String department;
        @Column(length = 1500)
        private String description;
        @Column
        private String heading;
        @Column 
        private Long departmentId;

        @Column 
        private float time;


        
@ManyToOne
@JoinColumn(name = "company_id", referencedColumnName = "id", nullable = true) //  Foreign key in Inductions table
private CompanyRegistration company;



}
