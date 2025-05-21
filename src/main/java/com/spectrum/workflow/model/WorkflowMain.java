package com.spectrum.workflow.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table

public class WorkflowMain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column
    private int thisWorkflowId;
    
    @Column
    private String workflowName;
    
    @Column
    private long companyId;
    
    @Column
    private long accountId;
    
    @Column
    private boolean workflowActivityStatus;

    @OneToMany(mappedBy = "workflowMain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowDetail> workflowDetails;

	public WorkflowMain() {
		super();

	}

	public WorkflowMain(long id, int thisWorkflowId, String workflowName, long companyId, long accountId,
			boolean workflowActivityStatus, List<WorkflowDetail> workflowDetails) {
		super();
		this.id = id;
		this.thisWorkflowId = thisWorkflowId;
		this.workflowName = workflowName;
		this.companyId = companyId;
		this.accountId = accountId;
		this.workflowActivityStatus = workflowActivityStatus;
		this.workflowDetails = workflowDetails;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getThisWorkflowId() {
		return thisWorkflowId;
	}

	public void setThisWorkflowId(int thisWorkflowId) {
		this.thisWorkflowId = thisWorkflowId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public boolean isWorkflowActivityStatus() {
		return workflowActivityStatus;
	}

	public void setWorkflowActivityStatus(boolean workflowActivityStatus) {
		this.workflowActivityStatus = workflowActivityStatus;
	}

	public List<WorkflowDetail> getWorkflowDetails() {
		return workflowDetails;
	}

	public void setWorkflowDetails(List<WorkflowDetail> workflowDetails) {
		this.workflowDetails = workflowDetails;
	}

	@Override
	public String toString() {
		return "WorkflowMain [id=" + id + ", thisWorkflowId=" + thisWorkflowId + ", workflowName=" + workflowName
				+ ", companyId=" + companyId + ", accountId=" + accountId + ", workflowActivityStatus="
				+ workflowActivityStatus + ", workflowDetails=" + workflowDetails + "]";
	}

    
}
