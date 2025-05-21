package com.spectrum.workflow.model;

import java.util.Date;

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

public class ExpenseDetails {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Date expenseDate;

    @Column(length = 1554)
    private String expenseDescription;
    @Column(length = 250)
    private String expenseCategory;
    @Column
    private long expenseCost;
    @Column(length = 250)
    private String expenseTransectionType;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "expense_management_id")
    private ExpenseManagement expenseManagement;

    public ExpenseDetails() {
        super();
    }

    public ExpenseDetails(Long id, Date expenseDate, String expenseDescription, String expenseCategory,
            long expenseCost, String expenseTransectionType, ExpenseManagement expenseManagement) {
        this.id = id;
        this.expenseDate = expenseDate;
        this.expenseDescription = expenseDescription;
        this.expenseCategory = expenseCategory;
        this.expenseCost = expenseCost;
        this.expenseTransectionType = expenseTransectionType;
        this.expenseManagement = expenseManagement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public long getExpenseCost() {
        return expenseCost;
    }

    public void setExpenseCost(long expenseCost) {
        this.expenseCost = expenseCost;
    }

    public String getExpenseTransectionType() {
        return expenseTransectionType;
    }

    public void setExpenseTransectionType(String expenseTransectionType) {
        this.expenseTransectionType = expenseTransectionType;
    }

    public ExpenseManagement getExpenseManagement() {
        return expenseManagement;
    }

    public void setExpenseManagement(ExpenseManagement expenseManagement) {
        this.expenseManagement = expenseManagement;
    }

    @Override
    public String toString() {
        return "ExpenseDetails [id=" + id + ", expenseDate=" + expenseDate + ", expenseDescription="
                + expenseDescription + ", expenseCategory=" + expenseCategory + ", expenseCost=" + expenseCost
                + ", expenseTransectionType=" + expenseTransectionType + ", expenseManagement=" + expenseManagement
                + "]";
    }






    
}
