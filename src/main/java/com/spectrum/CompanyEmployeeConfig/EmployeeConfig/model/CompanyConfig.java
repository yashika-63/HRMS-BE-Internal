package com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model;

import java.time.LocalDate;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // for set feedback start
    @Column
    private LocalDate feedbackDate;
    
    // by this from feedback start data every no of month get status false and 
    @Column
    private int feedbackFrequency;


   // for set feedback start
   @Column
   private LocalDate appraisalStartDate;

   // for set feedback start
   @Column
   private LocalDate performanceManagementEndDate;

    
    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private CompanyRegistration company;

}
