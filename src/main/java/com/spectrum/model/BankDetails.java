package com.spectrum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table

public class BankDetails {

    @Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;

     @Column(length = 50)
     private String bankName;

     @Column(length = 50)

    private String branch;

    @Column(length = 100)
    private String accountHolderName;

    @Column(length = 24)
    private String accountNumber;
    @Column
    private String accountifscCode;
    @Column(length = 24)
    private String branchCode;

    @Column(length = 50)
    private String branchAdress;

    @Column(length = 24)
    private String accountType;

    @Column(length = 24)
    private String linkedContactNo;

    @Column(length = 50)
    private String  linkedEmail;

    @ManyToOne
	 @JsonIgnore
	    @JoinColumn(name = "employee_id")
	    private Employee employee;



    public BankDetails() {
        super();

    }



    public BankDetails(Long id, String bankName, String branch, String accountHolderName, String accountNumber,
            String accountifscCode, String branchCode, String branchAdress, String accountType, String linkedContactNo,
            String linkedEmail, Employee employee) {
        this.id = id;
        this.bankName = bankName;
        this.branch = branch;
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.accountifscCode = accountifscCode;
        this.branchCode = branchCode;
        this.branchAdress = branchAdress;
        this.accountType = accountType;
        this.linkedContactNo = linkedContactNo;
        this.linkedEmail = linkedEmail;
        this.employee = employee;
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getBankName() {
        return bankName;
    }



    public void setBankName(String bankName) {
        this.bankName = bankName;
    }



    public String getBranch() {
        return branch;
    }



    public void setBranch(String branch) {
        this.branch = branch;
    }



    public String getAccountHolderName() {
        return accountHolderName;
    }



    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }



    public String getAccountNumber() {
        return accountNumber;
    }



    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }



    public String getAccountifscCode() {
        return accountifscCode;
    }



    public void setAccountifscCode(String accountifscCode) {
        this.accountifscCode = accountifscCode;
    }



    public String getBranchCode() {
        return branchCode;
    }



    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }



    public String getBranchAdress() {
        return branchAdress;
    }



    public void setBranchAdress(String branchAdress) {
        this.branchAdress = branchAdress;
    }



    public String getAccountType() {
        return accountType;
    }



    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }



    public String getLinkedContactNo() {
        return linkedContactNo;
    }



    public void setLinkedContactNo(String linkedContactNo) {
        this.linkedContactNo = linkedContactNo;
    }



    public String getLinkedEmail() {
        return linkedEmail;
    }



    public void setLinkedEmail(String linkedEmail) {
        this.linkedEmail = linkedEmail;
    }



    public Employee getEmployee() {
        return employee;
    }



    public void setEmployee(Employee employee) {
        this.employee = employee;
    }



    @Override
    public String toString() {
        return "BankDetails [id=" + id + ", bankName=" + bankName + ", branch=" + branch + ", accountHolderName="
                + accountHolderName + ", accountNumber=" + accountNumber + ", accountifscCode=" + accountifscCode
                + ", branchCode=" + branchCode + ", branchAdress=" + branchAdress + ", accountType=" + accountType
                + ", linkedContactNo=" + linkedContactNo + ", linkedEmail=" + linkedEmail + ", employee=" + employee
                + "]";
    }












    


}
