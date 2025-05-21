package com.spectrum.model;

import java.util.Date;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.spectrum.workflow.model.ExpenseManagement;

import java.util.HashSet;
import java.util.List;
import jakarta.persistence.Index;


@Entity
@Table(
	indexes = {
        @Index(name = "idx_employee_id", columnList = "employeeId"),
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_contact_no", columnList = "contactNo"),
        @Index(name = "idx_department", columnList = "department"),
        @Index(name = "idx_designation", columnList = "designation"),
        @Index(name = "idx_joining_date", columnList = "joiningDate"),
        @Index(name = "idx_dob", columnList = "dateOfBirth"),
        @Index(name = "idx_presence", columnList = "presence"),
        @Index(name = "idx_employee_type", columnList = "employeeType"),
        @Index(name = "idx_role", columnList = "role"),
        @Index(name = "idx_division", columnList = "division")
    }
)

public class Employee {
	@Id
	@Column
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
			@Column(length = 50)
		private String employeeId;
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
		private String  email;
			@Column(length = 10)
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
			// @Column
			// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
			// private Date abscondDate;
		@Column
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
		private Date joiningDate;
			// @Column
			// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
			// private Date exitDate;
		@Column
		private boolean presence;
			// @Column
			// private boolean resign;
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
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
			private Date dateOfBirth;
		
			
		
		@JsonIgnore
			@ManyToMany(fetch = FetchType.LAZY)
			@JoinTable(name = "employee_education",
					joinColumns = @JoinColumn(name = "employee_id"),
	            inverseJoinColumns = @JoinColumn(name = "education_id"))
	    private Set<Education> educations = new HashSet<>();
		
	    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
		@JsonIgnore
	    @JoinTable(name = "employee_generateproject",
	            joinColumns = @JoinColumn(name = "employee_id"),
	            inverseJoinColumns = @JoinColumn(name = "generateproject_id"))
	    private Set<GenerateProject> GenerateProjects = new HashSet<>();

//	    @OneToOne(mappedBy = "Employee", cascade = CascadeType.ALL)
//	    @JsonIgnore // Add this annotation
//	    private HiringChecklist HiringChecklist;
//timesheetid
	    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
	    @JsonIgnore
	    private HiringChecklist hiringChecklist;

	    
	    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	    @JsonIgnore
	    private List<TimeSheet> timesheet;
	    
		@JsonIgnore
		@ManyToOne
		@JoinColumn(name = "company_id", nullable = false)
		//@JsonBackReference
		private CompanyRegistration companyRegistration;
	    
		@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
		@JsonBackReference
		private List<ExpenseManagement> expenseManagement;

	    
		public Employee() {
			super();

		}


		public Employee(Long id, String employeeId, String firstName, String lastName, String middleName,
				String motherName, String contactNo, String contactNoCountryCode, String email, String gender,
				String nationality, String designation, String department, int experience, String panNo,
				String adhaarNo, Date joiningDate, boolean presence, String priorId, String employeeType,
				String currentHouseNo, String currentStreet, String currentCity, String currentState,
				String currentPostelcode, String currentCountry, String permanentHouseNo, String permanentStreet,
				String permanentCity, String permanentState, String permanentPostelcode, String permanentCountry,
				int age, String alternateEmail, String alternateContactNo, String alternateContactNoCountryCode,
				String maritalStatus, String division, String role, Date dateOfBirth, Set<Education> educations,
				Set<GenerateProject> generateProjects, HiringChecklist hiringChecklist, List<TimeSheet> timesheet,
				CompanyRegistration companyRegistration, List<ExpenseManagement> expenseManagement) {
			this.id = id;
			this.employeeId = employeeId;
			this.firstName = firstName;
			this.lastName = lastName;
			this.middleName = middleName;
			this.motherName = motherName;
			this.contactNo = contactNo;
			this.contactNoCountryCode = contactNoCountryCode;
			this.email = email;
			this.gender = gender;
			this.nationality = nationality;
			this.designation = designation;
			this.department = department;
			this.experience = experience;
			this.panNo = panNo;
			this.adhaarNo = adhaarNo;
			this.joiningDate = joiningDate;
			this.presence = presence;
			this.priorId = priorId;
			this.employeeType = employeeType;
			this.currentHouseNo = currentHouseNo;
			this.currentStreet = currentStreet;
			this.currentCity = currentCity;
			this.currentState = currentState;
			this.currentPostelcode = currentPostelcode;
			this.currentCountry = currentCountry;
			this.permanentHouseNo = permanentHouseNo;
			this.permanentStreet = permanentStreet;
			this.permanentCity = permanentCity;
			this.permanentState = permanentState;
			this.permanentPostelcode = permanentPostelcode;
			this.permanentCountry = permanentCountry;
			this.age = age;
			this.alternateEmail = alternateEmail;
			this.alternateContactNo = alternateContactNo;
			this.alternateContactNoCountryCode = alternateContactNoCountryCode;
			this.maritalStatus = maritalStatus;
			this.division = division;
			this.role = role;
			this.dateOfBirth = dateOfBirth;
			this.educations = educations;
			GenerateProjects = generateProjects;
			this.hiringChecklist = hiringChecklist;
			this.timesheet = timesheet;
			this.companyRegistration = companyRegistration;
			this.expenseManagement = expenseManagement;
		}


		public Long getId() {
			return id;
		}


		public void setId(Long id) {
			this.id = id;
		}


		public String getEmployeeId() {
			return employeeId;
		}


		public void setEmployeeId(String employeeId) {
			this.employeeId = employeeId;
		}


		public String getFirstName() {
			return firstName;
		}


		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}


		public String getLastName() {
			return lastName;
		}


		public void setLastName(String lastName) {
			this.lastName = lastName;
		}


		public String getMiddleName() {
			return middleName;
		}


		public void setMiddleName(String middleName) {
			this.middleName = middleName;
		}


		public String getMotherName() {
			return motherName;
		}


		public void setMotherName(String motherName) {
			this.motherName = motherName;
		}


		public String getContactNo() {
			return contactNo;
		}


		public void setContactNo(String contactNo) {
			this.contactNo = contactNo;
		}


		public String getContactNoCountryCode() {
			return contactNoCountryCode;
		}


		public void setContactNoCountryCode(String contactNoCountryCode) {
			this.contactNoCountryCode = contactNoCountryCode;
		}


		public String getEmail() {
			return email;
		}


		public void setEmail(String email) {
			this.email = email;
		}


		public String getGender() {
			return gender;
		}


		public void setGender(String gender) {
			this.gender = gender;
		}


		public String getNationality() {
			return nationality;
		}


		public void setNationality(String nationality) {
			this.nationality = nationality;
		}


		public String getDesignation() {
			return designation;
		}


		public void setDesignation(String designation) {
			this.designation = designation;
		}


		public String getDepartment() {
			return department;
		}


		public void setDepartment(String department) {
			this.department = department;
		}


		public int getExperience() {
			return experience;
		}


		public void setExperience(int experience) {
			this.experience = experience;
		}


		public String getPanNo() {
			return panNo;
		}


		public void setPanNo(String panNo) {
			this.panNo = panNo;
		}


		public String getAdhaarNo() {
			return adhaarNo;
		}


		public void setAdhaarNo(String adhaarNo) {
			this.adhaarNo = adhaarNo;
		}


		public Date getJoiningDate() {
			return joiningDate;
		}


		public void setJoiningDate(Date joiningDate) {
			this.joiningDate = joiningDate;
		}


		public boolean isPresence() {
			return presence;
		}


		public void setPresence(boolean presence) {
			this.presence = presence;
		}


		public String getPriorId() {
			return priorId;
		}


		public void setPriorId(String priorId) {
			this.priorId = priorId;
		}


		public String getEmployeeType() {
			return employeeType;
		}


		public void setEmployeeType(String employeeType) {
			this.employeeType = employeeType;
		}


		public String getCurrentHouseNo() {
			return currentHouseNo;
		}


		public void setCurrentHouseNo(String currentHouseNo) {
			this.currentHouseNo = currentHouseNo;
		}


		public String getCurrentStreet() {
			return currentStreet;
		}


		public void setCurrentStreet(String currentStreet) {
			this.currentStreet = currentStreet;
		}


		public String getCurrentCity() {
			return currentCity;
		}


		public void setCurrentCity(String currentCity) {
			this.currentCity = currentCity;
		}


		public String getCurrentState() {
			return currentState;
		}


		public void setCurrentState(String currentState) {
			this.currentState = currentState;
		}


		public String getCurrentPostelcode() {
			return currentPostelcode;
		}


		public void setCurrentPostelcode(String currentPostelcode) {
			this.currentPostelcode = currentPostelcode;
		}


		public String getCurrentCountry() {
			return currentCountry;
		}


		public void setCurrentCountry(String currentCountry) {
			this.currentCountry = currentCountry;
		}


		public String getPermanentHouseNo() {
			return permanentHouseNo;
		}


		public void setPermanentHouseNo(String permanentHouseNo) {
			this.permanentHouseNo = permanentHouseNo;
		}


		public String getPermanentStreet() {
			return permanentStreet;
		}


		public void setPermanentStreet(String permanentStreet) {
			this.permanentStreet = permanentStreet;
		}


		public String getPermanentCity() {
			return permanentCity;
		}


		public void setPermanentCity(String permanentCity) {
			this.permanentCity = permanentCity;
		}


		public String getPermanentState() {
			return permanentState;
		}


		public void setPermanentState(String permanentState) {
			this.permanentState = permanentState;
		}


		public String getPermanentPostelcode() {
			return permanentPostelcode;
		}


		public void setPermanentPostelcode(String permanentPostelcode) {
			this.permanentPostelcode = permanentPostelcode;
		}


		public String getPermanentCountry() {
			return permanentCountry;
		}


		public void setPermanentCountry(String permanentCountry) {
			this.permanentCountry = permanentCountry;
		}


		public int getAge() {
			return age;
		}


		public void setAge(int age) {
			this.age = age;
		}


		public String getAlternateEmail() {
			return alternateEmail;
		}


		public void setAlternateEmail(String alternateEmail) {
			this.alternateEmail = alternateEmail;
		}


		public String getAlternateContactNo() {
			return alternateContactNo;
		}


		public void setAlternateContactNo(String alternateContactNo) {
			this.alternateContactNo = alternateContactNo;
		}


		public String getAlternateContactNoCountryCode() {
			return alternateContactNoCountryCode;
		}


		public void setAlternateContactNoCountryCode(String alternateContactNoCountryCode) {
			this.alternateContactNoCountryCode = alternateContactNoCountryCode;
		}


		public String getMaritalStatus() {
			return maritalStatus;
		}


		public void setMaritalStatus(String maritalStatus) {
			this.maritalStatus = maritalStatus;
		}


		public String getDivision() {
			return division;
		}


		public void setDivision(String division) {
			this.division = division;
		}


		public String getRole() {
			return role;
		}


		public void setRole(String role) {
			this.role = role;
		}


		public Date getDateOfBirth() {
			return dateOfBirth;
		}


		public void setDateOfBirth(Date dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
		}


		public Set<Education> getEducations() {
			return educations;
		}


		public void setEducations(Set<Education> educations) {
			this.educations = educations;
		}


		public Set<GenerateProject> getGenerateProjects() {
			return GenerateProjects;
		}


		public void setGenerateProjects(Set<GenerateProject> generateProjects) {
			GenerateProjects = generateProjects;
		}


		public HiringChecklist getHiringChecklist() {
			return hiringChecklist;
		}


		public void setHiringChecklist(HiringChecklist hiringChecklist) {
			this.hiringChecklist = hiringChecklist;
		}


		public List<TimeSheet> getTimesheet() {
			return timesheet;
		}


		public void setTimesheet(List<TimeSheet> timesheet) {
			this.timesheet = timesheet;
		}


		public CompanyRegistration getCompanyRegistration() {
			return companyRegistration;
		}


		public void setCompanyRegistration(CompanyRegistration companyRegistration) {
			this.companyRegistration = companyRegistration;
		}


		public List<ExpenseManagement> getExpenseManagement() {
			return expenseManagement;
		}


		public void setExpenseManagement(List<ExpenseManagement> expenseManagement) {
			this.expenseManagement = expenseManagement;
		}


		@Override
		public String toString() {
			return "Employee [id=" + id + ", employeeId=" + employeeId + ", firstName=" + firstName + ", lastName="
					+ lastName + ", middleName=" + middleName + ", motherName=" + motherName + ", contactNo="
					+ contactNo + ", contactNoCountryCode=" + contactNoCountryCode + ", email=" + email + ", gender="
					+ gender + ", nationality=" + nationality + ", designation=" + designation + ", department="
					+ department + ", experience=" + experience + ", panNo=" + panNo + ", adhaarNo=" + adhaarNo
					+ ", joiningDate=" + joiningDate + ", presence=" + presence + ", priorId=" + priorId
					+ ", employeeType=" + employeeType + ", currentHouseNo=" + currentHouseNo + ", currentStreet="
					+ currentStreet + ", currentCity=" + currentCity + ", currentState=" + currentState
					+ ", currentPostelcode=" + currentPostelcode + ", currentCountry=" + currentCountry
					+ ", permanentHouseNo=" + permanentHouseNo + ", permanentStreet=" + permanentStreet
					+ ", permanentCity=" + permanentCity + ", permanentState=" + permanentState
					+ ", permanentPostelcode=" + permanentPostelcode + ", permanentCountry=" + permanentCountry
					+ ", age=" + age + ", alternateEmail=" + alternateEmail + ", alternateContactNo="
					+ alternateContactNo + ", alternateContactNoCountryCode=" + alternateContactNoCountryCode
					+ ", maritalStatus=" + maritalStatus + ", division=" + division + ", role=" + role
					+ ", dateOfBirth=" + dateOfBirth + ", educations=" + educations + ", GenerateProjects="
					+ GenerateProjects + ", hiringChecklist=" + hiringChecklist + ", timesheet=" + timesheet
					+ ", companyRegistration=" + companyRegistration + ", expenseManagement=" + expenseManagement + "]";
		}



		
	  }