package com.spectrum.workflow.model;

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

@Table
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveBucket {
      @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Boolean status;
    @Column
    private Integer probrationAllowedHolidays;
    @Column
    private Integer casualLeave;
    @Column
    private Integer sickLeave;
    @Column
    private Integer paid;
    @Column
    private Integer unPaid;
    @Column
    private String employeeType;

    

    @ManyToOne
	@JoinColumn(name = "company_id")
	private CompanyRegistration companyRegistration;


}
