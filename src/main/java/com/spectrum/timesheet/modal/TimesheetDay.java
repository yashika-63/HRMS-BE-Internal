package com.spectrum.timesheet.modal;

import java.sql.Time;
import java.time.LocalDate;
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
@Table(name = "timesheet_day")
public class TimesheetDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDate date;
    @Column
    private Time dayStartTime;
    @Column
    private Time dayEndTime;

    @Column
    private Time totalTime;

    @OneToMany(mappedBy = "timesheetDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimesheetDayDetails> TimesheetDayDetails;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "timesheet_main_id")
    private TimesheetMain timesheetMain;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "company_id")
    private CompanyRegistration companyRegistration;

    public TimesheetDay() {
        super();
        
    }

    public TimesheetDay(long id, LocalDate date, Time dayStartTime, Time dayEndTime, Time totalTime,
            List<com.spectrum.timesheet.modal.TimesheetDayDetails> timesheetDayDetails, TimesheetMain timesheetMain,
            Employee employee, CompanyRegistration companyRegistration) {
        this.id = id;
        this.date = date;
        this.dayStartTime = dayStartTime;
        this.dayEndTime = dayEndTime;
        this.totalTime = totalTime;
        TimesheetDayDetails = timesheetDayDetails;
        this.timesheetMain = timesheetMain;
        this.employee = employee;
        this.companyRegistration = companyRegistration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Time getDayStartTime() {
        return dayStartTime;
    }

    public void setDayStartTime(Time dayStartTime) {
        this.dayStartTime = dayStartTime;
    }

    public Time getDayEndTime() {
        return dayEndTime;
    }

    public void setDayEndTime(Time dayEndTime) {
        this.dayEndTime = dayEndTime;
    }

    public Time getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Time totalTime) {
        this.totalTime = totalTime;
    }

    public List<TimesheetDayDetails> getTimesheetDayDetails() {
        return TimesheetDayDetails;
    }

    public void setTimesheetDayDetails(List<TimesheetDayDetails> timesheetDayDetails) {
        TimesheetDayDetails = timesheetDayDetails;
    }

    public TimesheetMain getTimesheetMain() {
        return timesheetMain;
    }

    public void setTimesheetMain(TimesheetMain timesheetMain) {
        this.timesheetMain = timesheetMain;
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
        return "TimesheetDay [id=" + id + ", date=" + date + ", dayStartTime=" + dayStartTime + ", dayEndTime="
                + dayEndTime + ", totalTime=" + totalTime + ", TimesheetDayDetails=" + TimesheetDayDetails
                + ", timesheetMain=" + timesheetMain + ", employee=" + employee + ", companyRegistration="
                + companyRegistration + "]";
    }

    

}
