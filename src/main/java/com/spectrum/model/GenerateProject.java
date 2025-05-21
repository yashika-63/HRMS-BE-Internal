package com.spectrum.model;
	
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.timesheet.modal.TimesheetMain;


@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class GenerateProject {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column
	private long ProjectId;
	
	@Column(length = 100)
	private String proId;
	
	@Column(length = 100)
	
	private String clientName;
	@Column(length = 255)
	private String projectName;
	@Column(length = 150)
	private String projectLead;
	@Column(length = 150)
	private String deliveryLead;
	@Column(length = 250)
	private String projectType;
	@Column(length = 250)
	private String industry;
	@Column(length = 554)
	private String technologies;
	@Column(length = 250)
	private String projectStatus;
	@Column
	private Date startDate;
	@Column
	private Date endDate;
	@Column(length = 250)
	private String totalEffort;
	@Column
	private long totalCost;
	@Column(length = 10)
	private String currencyCode;
	@Column
	private Date actualStartDate;
	@Column
	private Date expectedEndDate;
	@Column(length = 100)
	private String cityLocation;
	@Column(length = 100)
	private String currentPhase;
	@Column(length = 2000)
	private String description;
	@Column(length = 100)
	private String workType;
	@Column(length = 100)
	private String shift;

	@Column
	private Boolean Status;

	
//	
//		 @ManyToOne
//		    @JoinColumn(name = "employee_id")
//		    private Employee employee;
//		
//		 @ManyToOne
//		    @JoinColumn(name = "employee_id")
//		    private Employee employee;
	 
	 @JsonIgnore
	 @ManyToMany(fetch = FetchType.LAZY, mappedBy = "GenerateProjects", cascade = {CascadeType.PERSIST, CascadeType.MERGE}  )
	 private Set<Employee> employees = new HashSet<>();

	 @JsonIgnore
	@ManyToOne
	@JoinColumn(name = "company_id", nullable = false)
	//@JsonBackReference
	private CompanyRegistration companyRegistration;
	 
	 
	 
	//generateproject_id
//		 
//		  @ManyToMany(fetch = FetchType.LAZY)
//		    @JoinTable(name = "generateproject_employee",
//		            joinColumns = @JoinColumn(name = "generateproject_id"),
//		            inverseJoinColumns = @JoinColumn(name = "employee_id"))
//		    private Set<Employee> Employees = new HashSet<>();
//		 

// this mapping we are doing for to assign project leads

@ManyToOne
@JsonIgnore
@JoinColumn(name = "project_lead_id")
private Employee employee;

@ManyToOne
@JsonIgnore
@JoinColumn(name = "delivery_lead_id")
private Employee employeetwo;

	




}
