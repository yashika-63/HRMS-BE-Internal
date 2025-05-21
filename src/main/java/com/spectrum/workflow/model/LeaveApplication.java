package com.spectrum.workflow.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;

@Entity
@Table
public class LeaveApplication {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String identifier;

	@Column
	private String name;

	@Column
	private String reason;

	@Column
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date fromDate;

	@Column
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date toDate;

	@Column
	private String workflowDivision;

	@Column
	private String workflowDepartment;

	@Column
	private String workflowRole;

	@Column
	private String requestStatus;

	@Column
	private String department;

	@Column
	private String designation;

	@Column
	private String managerName;

	@Column
	private String leaveType;

	@Column
	private String leaveCategory;

	@Column
	private String otherReason;
	@Column
	private int totalNoOfDays;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;




	@OneToMany(mappedBy = "leaveApplication", cascade = CascadeType.ALL)
    private List<LeaveApproval> LeaveApproval;


	@ManyToOne
	@JoinColumn(name = "company_id")
	private CompanyRegistration companyRegistration;

	@ManyToOne
	@JoinColumn(name = "workflow_main_id")
	private WorkflowMain workflowMain;

	// Constructors, getters, setters, and toString method
	// ...

	public LeaveApplication() {
		super();

	}

	public LeaveApplication(long id, String identifier, String name, String reason, Date fromDate, Date toDate,
			String workflowDivision, String workflowDepartment, String workflowRole, String requestStatus,
			String department, String designation, String managerName, String leaveType, String leaveCategory,
			String otherReason, int totalNoOfDays, Employee employee, CompanyRegistration companyRegistration,
			WorkflowMain workflowMain) {
		this.id = id;
		this.identifier = identifier;
		this.name = name;
		this.reason = reason;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.workflowDivision = workflowDivision;
		this.workflowDepartment = workflowDepartment;
		this.workflowRole = workflowRole;
		this.requestStatus = requestStatus;
		this.department = department;
		this.designation = designation;
		this.managerName = managerName;
		this.leaveType = leaveType;
		this.leaveCategory = leaveCategory;
		this.otherReason = otherReason;
		this.totalNoOfDays = totalNoOfDays;
		this.employee = employee;
		this.companyRegistration = companyRegistration;
		this.workflowMain = workflowMain;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getWorkflowDivision() {
		return workflowDivision;
	}

	public void setWorkflowDivision(String workflowDivision) {
		this.workflowDivision = workflowDivision;
	}

	public String getWorkflowDepartment() {
		return workflowDepartment;
	}

	public void setWorkflowDepartment(String workflowDepartment) {
		this.workflowDepartment = workflowDepartment;
	}

	public String getWorkflowRole() {
		return workflowRole;
	}

	public void setWorkflowRole(String workflowRole) {
		this.workflowRole = workflowRole;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getLeaveCategory() {
		return leaveCategory;
	}

	public void setLeaveCategory(String leaveCategory) {
		this.leaveCategory = leaveCategory;
	}

	public String getOtherReason() {
		return otherReason;
	}

	public void setOtherReason(String otherReason) {
		this.otherReason = otherReason;
	}

	public int getTotalNoOfDays() {
		return totalNoOfDays;
	}

	public void setTotalNoOfDays(int totalNoOfDays) {
		this.totalNoOfDays = totalNoOfDays;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public CompanyRegistration getCompanyRegistration() {
		return companyRegistration;
	}

	public void setCompanyRegistration(CompanyRegistration companyRegistration) {
		this.companyRegistration = companyRegistration;
	}

	public WorkflowMain getWorkflowMain() {
		return workflowMain;
	}

	public void setWorkflowMain(WorkflowMain workflowMain) {
		this.workflowMain = workflowMain;
	}

	@Override
	public String toString() {
		return "LeaveApplication [id=" + id + ", identifier=" + identifier + ", name=" + name + ", reason=" + reason
				+ ", fromDate=" + fromDate + ", toDate=" + toDate + ", workflowDivision=" + workflowDivision
				+ ", workflowDepartment=" + workflowDepartment + ", workflowRole=" + workflowRole + ", requestStatus="
				+ requestStatus + ", department=" + department + ", designation=" + designation + ", managerName="
				+ managerName + ", leaveType=" + leaveType + ", leaveCategory=" + leaveCategory + ", otherReason="
				+ otherReason + ", totalNoOfDays=" + totalNoOfDays + ", employee=" + employee + ", companyRegistration="
				+ companyRegistration + ", workflowMain=" + workflowMain + "]";
	}

}