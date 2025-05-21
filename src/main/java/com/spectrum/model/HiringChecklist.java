package com.spectrum.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
@Table
@Entity
public class HiringChecklist {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long hiringChecklistId;
	@Column
	private boolean employeeDataInfoSheet;
	@Column
	private boolean employeeCheckAcquiredPassed;
	@Column
	private boolean employeeAppointmentLetterSign;
	@Column
	private boolean employeePayrollBenifits;
	@Column
	private boolean employeePersonalFile;
	@Column
	private boolean employeeStateNewHireReporting;
	@Column
	private boolean employeeHandbookSign;
	@Column
	private boolean employeePolicyDocumentReviewedSign;
	@Column
	private boolean employeeEmployeeBenifitsEnrollmentFormSign;
	@Column
	private boolean employeeWorkSpaceSetup;
	@Column
	private boolean employeeTimeCardAndEntryCard;
	@Column
	private boolean employeeLoginsAndComputerApplicationAcess;
	@Column
	private boolean employeemeetGreet;
	@Column
	private boolean employeeOrientationShedule; 
	@Column
	private boolean employeeNewHireTreaningShedule;
	@Column
	private boolean employeeIdCard;

//	 @OneToOne
//	    @JoinColumn(name = "employee_id", referencedColumnName = "id")
//	    private Employee employee;
	 
	 @OneToOne
	 @JoinColumn(name = "employee_id", referencedColumnName = "id")
	 private Employee employee;
	 
	 
	 
	 
	public HiringChecklist() {
		super();

	}

	public HiringChecklist(long hiringChecklistId, boolean employeeDataInfoSheet, boolean employeeCheckAcquiredPassed,
			boolean employeeAppointmentLetterSign, boolean employeePayrollBenifits, boolean employeePersonalFile,
			boolean employeeStateNewHireReporting, boolean employeeHandbookSign,
			boolean employeePolicyDocumentReviewedSign, boolean employeeEmployeeBenifitsEnrollmentFormSign,
			boolean employeeWorkSpaceSetup, boolean employeeTimeCardAndEntryCard,
			boolean employeeLoginsAndComputerApplicationAcess, boolean employeemeetGreet,
			boolean employeeOrientationShedule, boolean employeeNewHireTreaningShedule, boolean employeeIdCard,
			Employee employee) {
		super();
		this.hiringChecklistId = hiringChecklistId;
		this.employeeDataInfoSheet = employeeDataInfoSheet;
		this.employeeCheckAcquiredPassed = employeeCheckAcquiredPassed;
		this.employeeAppointmentLetterSign = employeeAppointmentLetterSign;
		this.employeePayrollBenifits = employeePayrollBenifits;
		this.employeePersonalFile = employeePersonalFile;
		this.employeeStateNewHireReporting = employeeStateNewHireReporting;
		this.employeeHandbookSign = employeeHandbookSign;
		this.employeePolicyDocumentReviewedSign = employeePolicyDocumentReviewedSign;
		this.employeeEmployeeBenifitsEnrollmentFormSign = employeeEmployeeBenifitsEnrollmentFormSign;
		this.employeeWorkSpaceSetup = employeeWorkSpaceSetup;
		this.employeeTimeCardAndEntryCard = employeeTimeCardAndEntryCard;
		this.employeeLoginsAndComputerApplicationAcess = employeeLoginsAndComputerApplicationAcess;
		this.employeemeetGreet = employeemeetGreet;
		this.employeeOrientationShedule = employeeOrientationShedule;
		this.employeeNewHireTreaningShedule = employeeNewHireTreaningShedule;
		this.employeeIdCard = employeeIdCard;
		this.employee = employee;
	}






	public long getHiringChecklistId() {
		return hiringChecklistId;
	}






	public void setHiringChecklistId(long hiringChecklistId) {
		this.hiringChecklistId = hiringChecklistId;
	}






	public boolean isEmployeeDataInfoSheet() {
		return employeeDataInfoSheet;
	}






	public void setEmployeeDataInfoSheet(boolean employeeDataInfoSheet) {
		this.employeeDataInfoSheet = employeeDataInfoSheet;
	}






	public boolean isEmployeeCheckAcquiredPassed() {
		return employeeCheckAcquiredPassed;
	}






	public void setEmployeeCheckAcquiredPassed(boolean employeeCheckAcquiredPassed) {
		this.employeeCheckAcquiredPassed = employeeCheckAcquiredPassed;
	}






	public boolean isEmployeeAppointmentLetterSign() {
		return employeeAppointmentLetterSign;
	}






	public void setEmployeeAppointmentLetterSign(boolean employeeAppointmentLetterSign) {
		this.employeeAppointmentLetterSign = employeeAppointmentLetterSign;
	}






	public boolean isEmployeePayrollBenifits() {
		return employeePayrollBenifits;
	}






	public void setEmployeePayrollBenifits(boolean employeePayrollBenifits) {
		this.employeePayrollBenifits = employeePayrollBenifits;
	}






	public boolean isEmployeePersonalFile() {
		return employeePersonalFile;
	}






	public void setEmployeePersonalFile(boolean employeePersonalFile) {
		this.employeePersonalFile = employeePersonalFile;
	}






	public boolean isEmployeeStateNewHireReporting() {
		return employeeStateNewHireReporting;
	}






	public void setEmployeeStateNewHireReporting(boolean employeeStateNewHireReporting) {
		this.employeeStateNewHireReporting = employeeStateNewHireReporting;
	}






	public boolean isEmployeeHandbookSign() {
		return employeeHandbookSign;
	}






	public void setEmployeeHandbookSign(boolean employeeHandbookSign) {
		this.employeeHandbookSign = employeeHandbookSign;
	}






	public boolean isEmployeePolicyDocumentReviewedSign() {
		return employeePolicyDocumentReviewedSign;
	}






	public void setEmployeePolicyDocumentReviewedSign(boolean employeePolicyDocumentReviewedSign) {
		this.employeePolicyDocumentReviewedSign = employeePolicyDocumentReviewedSign;
	}






	public boolean isEmployeeEmployeeBenifitsEnrollmentFormSign() {
		return employeeEmployeeBenifitsEnrollmentFormSign;
	}






	public void setEmployeeEmployeeBenifitsEnrollmentFormSign(boolean employeeEmployeeBenifitsEnrollmentFormSign) {
		this.employeeEmployeeBenifitsEnrollmentFormSign = employeeEmployeeBenifitsEnrollmentFormSign;
	}






	public boolean isEmployeeWorkSpaceSetup() {
		return employeeWorkSpaceSetup;
	}






	public void setEmployeeWorkSpaceSetup(boolean employeeWorkSpaceSetup) {
		this.employeeWorkSpaceSetup = employeeWorkSpaceSetup;
	}






	public boolean isEmployeeTimeCardAndEntryCard() {
		return employeeTimeCardAndEntryCard;
	}






	public void setEmployeeTimeCardAndEntryCard(boolean employeeTimeCardAndEntryCard) {
		this.employeeTimeCardAndEntryCard = employeeTimeCardAndEntryCard;
	}






	public boolean isEmployeeLoginsAndComputerApplicationAcess() {
		return employeeLoginsAndComputerApplicationAcess;
	}






	public void setEmployeeLoginsAndComputerApplicationAcess(boolean employeeLoginsAndComputerApplicationAcess) {
		this.employeeLoginsAndComputerApplicationAcess = employeeLoginsAndComputerApplicationAcess;
	}






	public boolean isEmployeemeetGreet() {
		return employeemeetGreet;
	}






	public void setEmployeemeetGreet(boolean employeemeetGreet) {
		this.employeemeetGreet = employeemeetGreet;
	}






	public boolean isEmployeeOrientationShedule() {
		return employeeOrientationShedule;
	}






	public void setEmployeeOrientationShedule(boolean employeeOrientationShedule) {
		this.employeeOrientationShedule = employeeOrientationShedule;
	}






	public boolean isEmployeeNewHireTreaningShedule() {
		return employeeNewHireTreaningShedule;
	}






	public void setEmployeeNewHireTreaningShedule(boolean employeeNewHireTreaningShedule) {
		this.employeeNewHireTreaningShedule = employeeNewHireTreaningShedule;
	}






	public boolean isEmployeeIdCard() {
		return employeeIdCard;
	}






	public void setEmployeeIdCard(boolean employeeIdCard) {
		this.employeeIdCard = employeeIdCard;
	}






	public Employee getEmployee() {
		return employee;
	}






	public void setEmployee(Employee employee) {
		this.employee = employee;
	}






	@Override
	public String toString() {
		return "HiringChecklist [hiringChecklistId=" + hiringChecklistId + ", employeeDataInfoSheet="
				+ employeeDataInfoSheet + ", employeeCheckAcquiredPassed=" + employeeCheckAcquiredPassed
				+ ", employeeAppointmentLetterSign=" + employeeAppointmentLetterSign + ", employeePayrollBenifits="
				+ employeePayrollBenifits + ", employeePersonalFile=" + employeePersonalFile
				+ ", employeeStateNewHireReporting=" + employeeStateNewHireReporting + ", employeeHandbookSign="
				+ employeeHandbookSign + ", employeePolicyDocumentReviewedSign=" + employeePolicyDocumentReviewedSign
				+ ", employeeEmployeeBenifitsEnrollmentFormSign=" + employeeEmployeeBenifitsEnrollmentFormSign
				+ ", employeeWorkSpaceSetup=" + employeeWorkSpaceSetup + ", employeeTimeCardAndEntryCard="
				+ employeeTimeCardAndEntryCard + ", employeeLoginsAndComputerApplicationAcess="
				+ employeeLoginsAndComputerApplicationAcess + ", employeemeetGreet=" + employeemeetGreet
				+ ", employeeOrientationShedule=" + employeeOrientationShedule + ", employeeNewHireTreaningShedule="
				+ employeeNewHireTreaningShedule + ", employeeIdCard=" + employeeIdCard + ", employee=" + employee
				+ "]";
	}
	
}
