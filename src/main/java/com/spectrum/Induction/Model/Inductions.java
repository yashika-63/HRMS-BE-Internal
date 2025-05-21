package com.spectrum.Induction.Model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.model.CompanyRegistration;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Inductions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
  
    @Column
    private Long  regionId;

    @Column
    private String region;
    @Column

    private boolean type;
    @Column

    private String heading;
   
    @Column(length = 1500)
    private String description;
    @Column
    private boolean status;
    @Column
    @CreationTimestamp
    private LocalDate date;

    @Column
    private String department;

    @Column 
    private Long departmentId;


    @Column
    private long createdByEmployeeId;
    
// many to one uni directional Relationship between company and induction 
@ManyToOne
@JsonIgnore
@JoinColumn(name = "company_id", referencedColumnName = "id", nullable = true) //  Foreign key in Inductions table
private CompanyRegistration company;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 


}
