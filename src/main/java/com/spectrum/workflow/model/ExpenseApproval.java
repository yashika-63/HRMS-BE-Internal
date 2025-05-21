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

@Entity
public class ExpenseApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length=250)
    private String name;

    @Column
    private Date date;

    @Column
    private boolean action;

    @Column(length=550)
    private String note;

    @Column(length=250)
    private String actionTakenBy;

    @Column(length=250)
    private String designation;

    @Column(length=150)
    private String mail;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "expense_management_id")
    private ExpenseManagement expenseManagement;

    public ExpenseApproval() {
        super();

    }

    public ExpenseApproval(long id, String name, Date date, boolean action, String note, String actionTakenBy,
            String designation, String mail, ExpenseManagement expenseManagement) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.action = action;
        this.note = note;
        this.actionTakenBy = actionTakenBy;
        this.designation = designation;
        this.mail = mail;
        this.expenseManagement = expenseManagement;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getActionTakenBy() {
        return actionTakenBy;
    }

    public void setActionTakenBy(String actionTakenBy) {
        this.actionTakenBy = actionTakenBy;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public ExpenseManagement getExpenseManagement() {
        return expenseManagement;
    }

    public void setExpenseManagement(ExpenseManagement expenseManagement) {
        this.expenseManagement = expenseManagement;
    }

    @Override
    public String toString() {
        return "ExpenseApproval [id=" + id + ", name=" + name + ", date=" + date + ", action=" + action + ", note="
                + note + ", actionTakenBy=" + actionTakenBy + ", designation=" + designation + ", mail=" + mail
                + ", expenseManagement=" + expenseManagement + "]";
    }

    // Getters and setters (optional, if not using Lombok)

}
