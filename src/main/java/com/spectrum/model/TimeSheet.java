package com.spectrum.model;



import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.timesheet.modal.TimesheetDay;

import jakarta.persistence.*;

@Entity
@Table
public class TimeSheet {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Date fromDate;

	@Column
	private Date toDate;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "company_id")
	private CompanyRegistration companyRegistration;

	// @OneToMany(mappedBy = "TimeSheet", cascade = CascadeType.ALL, fetch =
	// FetchType.LAZY)
	// private List<TimesheetDay> timesheetDays;

	public TimeSheet() {
		super();

	}

	public TimeSheet(Long id, Date fromDate, Date toDate, Employee employee, CompanyRegistration companyRegistration) {
		this.id = id;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.employee = employee;
		this.companyRegistration = companyRegistration;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
		return "TimeSheet [id=" + id + ", fromDate=" + fromDate + ", toDate=" + toDate + ", employee=" + employee
				+ ", companyRegistration=" + companyRegistration + "]";
	}


}