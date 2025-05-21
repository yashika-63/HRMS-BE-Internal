package com.spectrum.workflow.model;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class WorkflowDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String workflowFromDivision;

	@Column
	private String workflowToDivision;

	@Column
	private String workflowFromDepartment;

	@Column
	private String workflowToDepartment;

	@Column
	private String workflowPreviousRole;

	@Column
	private String workflowNextRole;

	@Column
	private String workflowCurrentStatus;

	@Column
	private String workflowPreviousStatus;

	@Column
	private String workflowNextStatus;

	

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "workflow_main_id")
	private WorkflowMain workflowMain;

	public WorkflowDetail() {
		super();

	}

	public WorkflowDetail(long id, String workflowFromDivision, String workflowToDivision,
			String workflowFromDepartment, String workflowToDepartment, String workflowPreviousRole,
			String workflowNextRole, String workflowCurrentStatus, String workflowPreviousStatus,
			String workflowNextStatus, WorkflowMain workflowMain) {
		super();
		this.id = id;
		this.workflowFromDivision = workflowFromDivision;
		this.workflowToDivision = workflowToDivision;
		this.workflowFromDepartment = workflowFromDepartment;
		this.workflowToDepartment = workflowToDepartment;
		this.workflowPreviousRole = workflowPreviousRole;
		this.workflowNextRole = workflowNextRole;
		this.workflowCurrentStatus = workflowCurrentStatus;
		this.workflowPreviousStatus = workflowPreviousStatus;
		this.workflowNextStatus = workflowNextStatus;
		this.workflowMain = workflowMain;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWorkflowFromDivision() {
		return workflowFromDivision;
	}

	public void setWorkflowFromDivision(String workflowFromDivision) {
		this.workflowFromDivision = workflowFromDivision;
	}

	public String getWorkflowToDivision() {
		return workflowToDivision;
	}

	public void setWorkflowToDivision(String workflowToDivision) {
		this.workflowToDivision = workflowToDivision;
	}

	public String getWorkflowFromDepartment() {
		return workflowFromDepartment;
	}

	public void setWorkflowFromDepartment(String workflowFromDepartment) {
		this.workflowFromDepartment = workflowFromDepartment;
	}

	public String getWorkflowToDepartment() {
		return workflowToDepartment;
	}

	public void setWorkflowToDepartment(String workflowToDepartment) {
		this.workflowToDepartment = workflowToDepartment;
	}

	public String getWorkflowPreviousRole() {
		return workflowPreviousRole;
	}

	public void setWorkflowPreviousRole(String workflowPreviousRole) {
		this.workflowPreviousRole = workflowPreviousRole;
	}

	public String getWorkflowNextRole() {
		return workflowNextRole;
	}

	public void setWorkflowNextRole(String workflowNextRole) {
		this.workflowNextRole = workflowNextRole;
	}

	public String getWorkflowCurrentStatus() {
		return workflowCurrentStatus;
	}

	public void setWorkflowCurrentStatus(String workflowCurrentStatus) {
		this.workflowCurrentStatus = workflowCurrentStatus;
	}

	public String getWorkflowPreviousStatus() {
		return workflowPreviousStatus;
	}

	public void setWorkflowPreviousStatus(String workflowPreviousStatus) {
		this.workflowPreviousStatus = workflowPreviousStatus;
	}

	public String getWorkflowNextStatus() {
		return workflowNextStatus;
	}

	public void setWorkflowNextStatus(String workflowNextStatus) {
		this.workflowNextStatus = workflowNextStatus;
	}

	public WorkflowMain getWorkflowMain() {
		return workflowMain;
	}

	public void setWorkflowMain(WorkflowMain workflowMain) {
		this.workflowMain = workflowMain;
	}

	@Override
	public String toString() {
		return "WorkflowDetail [id=" + id + ", workflowFromDivision=" + workflowFromDivision + ", workflowToDivision="
				+ workflowToDivision + ", workflowFromDepartment=" + workflowFromDepartment + ", workflowToDepartment="
				+ workflowToDepartment + ", workflowPreviousRole=" + workflowPreviousRole + ", workflowNextRole="
				+ workflowNextRole + ", workflowCurrentStatus=" + workflowCurrentStatus + ", workflowPreviousStatus="
				+ workflowPreviousStatus + ", workflowNextStatus=" + workflowNextStatus + ", workflowMain="
				+ workflowMain + "]";
	}

}
