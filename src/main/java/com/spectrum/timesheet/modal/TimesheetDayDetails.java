package com.spectrum.timesheet.modal;

import java.sql.Time;

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
public class TimesheetDayDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length=250)
    private String taskName; // Renamed to match the JSON

    @Column
    private Time hoursSpent;

    @Column(length=1250)
    private String taskDescription;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "timesheet_day_id")
    private TimesheetDay timesheetDay;

    // Getters and Setters

    public TimesheetDayDetails() {
    }

    public TimesheetDayDetails(long id, String taskName, Time hoursSpent, String taskDescription,
            TimesheetDay timesheetDay) {
        this.id = id;
        this.taskName = taskName;
        this.hoursSpent = hoursSpent;
        this.taskDescription = taskDescription;
        this.timesheetDay = timesheetDay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Time getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(Time hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public TimesheetDay getTimesheetDay() {
        return timesheetDay;
    }

    public void setTimesheetDay(TimesheetDay timesheetDay) {
        this.timesheetDay = timesheetDay;
    }

    @Override
    public String toString() {
        return "TimesheetDayDetails [id=" + id + ", taskName=" + taskName + ", hoursSpent=" + hoursSpent
                + ", taskDescription=" + taskDescription + ", timesheetDay=" + timesheetDay + "]";
    }


}
