package com.spectrum.model;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table
public class AccountMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountId;

    @Column(length=50)
    private String accountName;

    @Column(length=100)
    private String accountDetails;

    @OneToMany(mappedBy = "accountMaster", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CompanyRegistration> companyRegistration;

    public AccountMaster() {
        super();
    }

    public AccountMaster(long accountId, String accountName, String accountDetails,
                         List<CompanyRegistration> companyRegistration) {
        super();
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountDetails = accountDetails;
        this.companyRegistration = companyRegistration;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(String accountDetails) {
        this.accountDetails = accountDetails;
    }

    public List<CompanyRegistration> getCompanyRegistration() {
        return companyRegistration;
    }

    public void setCompanyRegistration(List<CompanyRegistration> companyRegistration) {
        this.companyRegistration = companyRegistration;
    }

    @Override
    public String toString() {
        return "AccountMaster [accountId=" + accountId + ", accountName=" + accountName + ", accountDetails="
                + accountDetails + ", companyRegistration=" + companyRegistration + "]";
    }
}