package com.spectrum.PreOnboarding.EmployeeVerification.model;

import java.util.Date;

import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Employeedata {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(length = 50)
	private String firstName;
	@Column(length = 50)
	private String lastName;
	@Column(length = 50)
	private String middleName;
	@Column(length = 50)
	private String motherName;
	@Column(length = 50)
	private String contactNo;
	@Column(length = 50)
	private String contactNoCountryCode;
	@Column(length = 75)
	private String gender;
	@Column(length = 50)
	private String nationality;
	@Column(length = 100)
	private String designation;
	@Column(length = 100)
	private String department;
	@Column
	private int experience;
	@Column
	private String panNo;
	@Column
	private String adhaarNo;
	@Column
	private Date joiningDate;
	@Column
	private boolean status;
	@Column(length=50)
	private String priorId;
	@Column(length=50)
	private String employeeType;	
	@Column(length=500)
	private String currentHouseNo;
	@Column(length=500)
	private String currentStreet;	
	@Column(length=50)
	private String currentCity;
	@Column(length=50)
	private String currentState;
	@Column(length=50)
	private String currentPostelcode;
	@Column(length=50)
	private String currentCountry;
	@Column(length=500)
	private String permanentHouseNo;
	@Column(length=500)
	private String permanentStreet;
	@Column(length=50)
	private String permanentCity;
	@Column(length=50)
	private String permanentState;
	@Column(length=50)
	private String permanentPostelcode;
	@Column(length=50)
	private String permanentCountry;	
	@Column
	private int age;
	@Column(length=50)
	private String alternateEmail;
	@Column(length=24)
	private String alternateContactNo;
	@Column(length=24)
	private String alternateContactNoCountryCode;
	@Column(length=24)
	private String maritalStatus;
	@Column(length=50)
	private String division;
	@Column(length=50)
	private String role;
	@Column
	private Date dateOfBirth;

    @Column
    private boolean verificationStatus;

    // here we establish required relation 
     @OneToOne
	 @JoinColumn(name = "verification_ticket_id", referencedColumnName = "id")
	 private VerificationTicket verificationTicket;


     // here we establish required relation 
     @OneToOne
	 @JoinColumn(name = "pre_registration_id", referencedColumnName = "id")
	 private PreRegistration preRegistration;
     

}
