package com.spectrum.workflow.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class ExpenseManagement {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Date expenseFromDate;
    @Column
    private Date expenseTillDate;
    @Column(length = 250)
    private String expensePurpose;
    @Column(length = 250)
    private String expenseAmountSpent;
    @Column(length = 250)
    private String expenseTransectionType;

    @Column
    private String workflowDivision;

    @Column
    private String workflowDepartment;

    @Column
    private String workflowRole;

    @Column

    private String requestStatus;

    @Column(length = 10)
    private String currencyCode;

    @Column
    private String expenseType;

    @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "workflow_main_id")
    private WorkflowMain workflowMain;

    @OneToMany(mappedBy = "expenseManagement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseDetails> ExpenseDetails;

    @OneToMany(mappedBy = "expenseManagement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseApproval> ExpenseApproval;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JsonIgnore

    @JoinColumn(name = "company_id")
    private CompanyRegistration companyRegistration;

    public ExpenseManagement() {
        super();

    }

    public ExpenseManagement(Long id, Date expenseFromDate, Date expenseTillDate, String expensePurpose,
            String expenseAmountSpent, String expenseTransectionType, String workflowDivision,
            String workflowDepartment, String workflowRole, String requestStatus, String currencyCode,
            String expenseType, WorkflowMain workflowMain,
            List<com.spectrum.workflow.model.ExpenseDetails> expenseDetails,
            List<com.spectrum.workflow.model.ExpenseApproval> expenseApproval, Employee employee,
            CompanyRegistration companyRegistration) {
        this.id = id;
        this.expenseFromDate = expenseFromDate;
        this.expenseTillDate = expenseTillDate;
        this.expensePurpose = expensePurpose;
        this.expenseAmountSpent = expenseAmountSpent;
        this.expenseTransectionType = expenseTransectionType;
        this.workflowDivision = workflowDivision;
        this.workflowDepartment = workflowDepartment;
        this.workflowRole = workflowRole;
        this.requestStatus = requestStatus;
        this.currencyCode = currencyCode;
        this.expenseType = expenseType;
        this.workflowMain = workflowMain;
        ExpenseDetails = expenseDetails;
        ExpenseApproval = expenseApproval;
        this.employee = employee;
        this.companyRegistration = companyRegistration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExpenseFromDate() {
        return expenseFromDate;
    }

    public void setExpenseFromDate(Date expenseFromDate) {
        this.expenseFromDate = expenseFromDate;
    }

    public Date getExpenseTillDate() {
        return expenseTillDate;
    }

    public void setExpenseTillDate(Date expenseTillDate) {
        this.expenseTillDate = expenseTillDate;
    }

    public String getExpensePurpose() {
        return expensePurpose;
    }

    public void setExpensePurpose(String expensePurpose) {
        this.expensePurpose = expensePurpose;
    }

    public String getExpenseAmountSpent() {
        return expenseAmountSpent;
    }

    public void setExpenseAmountSpent(String expenseAmountSpent) {
        this.expenseAmountSpent = expenseAmountSpent;
    }

    public String getExpenseTransectionType() {
        return expenseTransectionType;
    }

    public void setExpenseTransectionType(String expenseTransectionType) {
        this.expenseTransectionType = expenseTransectionType;
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public WorkflowMain getWorkflowMain() {
        return workflowMain;
    }

    public void setWorkflowMain(WorkflowMain workflowMain) {
        this.workflowMain = workflowMain;
    }

    public List<ExpenseDetails> getExpenseDetails() {
        return ExpenseDetails;
    }

    public void setExpenseDetails(List<ExpenseDetails> expenseDetails) {
        ExpenseDetails = expenseDetails;
    }

    public List<ExpenseApproval> getExpenseApproval() {
        return ExpenseApproval;
    }

    public void setExpenseApproval(List<ExpenseApproval> expenseApproval) {
        ExpenseApproval = expenseApproval;
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

    @Override
    public String toString() {
        return "ExpenseManagement [id=" + id + ", expenseFromDate=" + expenseFromDate + ", expenseTillDate="
                + expenseTillDate + ", expensePurpose=" + expensePurpose + ", expenseAmountSpent=" + expenseAmountSpent
                + ", expenseTransectionType=" + expenseTransectionType + ", workflowDivision=" + workflowDivision
                + ", workflowDepartment=" + workflowDepartment + ", workflowRole=" + workflowRole + ", requestStatus="
                + requestStatus + ", currencyCode=" + currencyCode + ", expenseType=" + expenseType + ", workflowMain="
                + workflowMain + ", ExpenseDetails=" + ExpenseDetails + ", ExpenseApproval=" + ExpenseApproval
                + ", employee=" + employee + ", companyRegistration=" + companyRegistration + "]";
    }

    




    }
