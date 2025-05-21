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

public class LeaveApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 150)
    private String Name;

    @Column
    private Date date;

    @Column
    private boolean action;

    @Column(length = 550)
    private String Note;

    @Column(length = 150)
    private String actionTakenBy;

    @Column
    private String designation;

    @Column(length = 150)
    private String mail;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "leave_application_id")
    private LeaveApplication leaveApplication;

    public LeaveApproval() {
        super();

    }

    public LeaveApproval(long id, String name, Date date, boolean action, String note, String actionTakenBy,
            String designation, String mail, LeaveApplication leaveApplication) {
        this.id = id;
        Name = name;
        this.date = date;
        this.action = action;
        Note = note;
        this.actionTakenBy = actionTakenBy;
        this.designation = designation;
        this.mail = mail;
        this.leaveApplication = leaveApplication;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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
        return Note;
    }

    public void setNote(String note) {
        Note = note;
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

    public LeaveApplication getLeaveApplication() {
        return leaveApplication;
    }

    public void setLeaveApplication(LeaveApplication leaveApplication) {
        this.leaveApplication = leaveApplication;
    }

    @Override
    public String toString() {
        return "LeaveApproval [id=" + id + ", Name=" + Name + ", date=" + date + ", action=" + action + ", Note=" + Note
                + ", actionTakenBy=" + actionTakenBy + ", designation=" + designation + ", mail=" + mail
                + ", leaveApplication=" + leaveApplication + "]";
    }

}
