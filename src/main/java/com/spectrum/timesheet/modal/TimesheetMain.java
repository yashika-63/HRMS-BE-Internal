package com.spectrum.timesheet.modal;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.workflow.model.WorkflowMain;

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


@Entity
@Table(name = "timesheet_main")
public class TimesheetMain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private Date fromDate;

    @Column
    private Date toDate;

    @Column(length=250)
    private String workflowDivision;

    @Column(length=250)
    private String workflowDepartment;

    @Column(length=250)
    private String workflowRole;

    @Column(length=250)
    private String requestStatus;

    @Column
    private boolean sendForApproval;

    @Column
    private boolean managerApproved;

    @Column
    private boolean managerRejected;

    @Column
    private int reportingManagerId;

    
    @Column
    @CreationTimestamp
    private LocalDate dateSendForApproval;

    @ManyToOne
    @JoinColumn(name = "workflow_main_id")
    private WorkflowMain workflowMain;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "company_id")
    private CompanyRegistration companyRegistration;

    @OneToMany(mappedBy = "timesheetMain", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TimesheetDay> timesheetDays;

    public TimesheetMain() {
        super();
        
    }

    public TimesheetMain(long id, Date fromDate, Date toDate, String workflowDivision, String workflowDepartment,
            String workflowRole, String requestStatus, boolean sendForApproval, boolean managerApproved,
            boolean managerRejected, int reportingManagerId, LocalDate dateSendForApproval, WorkflowMain workflowMain,
            Employee employee, CompanyRegistration companyRegistration, List<TimesheetDay> timesheetDays) {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.workflowDivision = workflowDivision;
        this.workflowDepartment = workflowDepartment;
        this.workflowRole = workflowRole;
        this.requestStatus = requestStatus;
        this.sendForApproval = sendForApproval;
        this.managerApproved = managerApproved;
        this.managerRejected = managerRejected;
        this.reportingManagerId = reportingManagerId;
        this.dateSendForApproval = dateSendForApproval;
        this.workflowMain = workflowMain;
        this.employee = employee;
        this.companyRegistration = companyRegistration;
        this.timesheetDays = timesheetDays;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isSendForApproval() {
        return sendForApproval;
    }

    public void setSendForApproval(boolean sendForApproval) {
        this.sendForApproval = sendForApproval;
    }

    public boolean isManagerApproved() {
        return managerApproved;
    }

    public void setManagerApproved(boolean managerApproved) {
        this.managerApproved = managerApproved;
    }

    public boolean isManagerRejected() {
        return managerRejected;
    }

    public void setManagerRejected(boolean managerRejected) {
        this.managerRejected = managerRejected;
    }

    public int getReportingManagerId() {
        return reportingManagerId;
    }

    public void setReportingManagerId(int reportingManagerId) {
        this.reportingManagerId = reportingManagerId;
    }

    public LocalDate getDateSendForApproval() {
        return dateSendForApproval;
    }

    public void setDateSendForApproval(LocalDate dateSendForApproval) {
        this.dateSendForApproval = dateSendForApproval;
    }

    public WorkflowMain getWorkflowMain() {
        return workflowMain;
    }

    public void setWorkflowMain(WorkflowMain workflowMain) {
        this.workflowMain = workflowMain;
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

    public List<TimesheetDay> getTimesheetDays() {
        return timesheetDays;
    }

    public void setTimesheetDays(List<TimesheetDay> timesheetDays) {
        this.timesheetDays = timesheetDays;
    }

    @Override
    public String toString() {
        return "TimesheetMain [id=" + id + ", fromDate=" + fromDate + ", toDate=" + toDate + ", workflowDivision="
                + workflowDivision + ", workflowDepartment=" + workflowDepartment + ", workflowRole=" + workflowRole
                + ", requestStatus=" + requestStatus + ", sendForApproval=" + sendForApproval + ", managerApproved="
                + managerApproved + ", managerRejected=" + managerRejected + ", reportingManagerId="
                + reportingManagerId + ", dateSendForApproval=" + dateSendForApproval + ", workflowMain=" + workflowMain
                + ", employee=" + employee + ", companyRegistration=" + companyRegistration + ", timesheetDays="
                + timesheetDays + "]";
    }


    
}
