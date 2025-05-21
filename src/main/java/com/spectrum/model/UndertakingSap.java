package com.spectrum.model;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table
public class UndertakingSap {

	@Id
	@Column(length=100)
	private String sapUserId;
	@Column
	private String name;
	@Column
	private Date date;
	@Column
	private String signature;
	 @ManyToOne
	    @JoinColumn(name = "employee_id")
	    private Employee employee;
	public UndertakingSap() {
		super();

	}
	public UndertakingSap(String sapUserId, String name, Date date, String signature, Employee employee) {
		super();
		this.sapUserId = sapUserId;
		this.name = name;
		this.date = date;
		this.signature = signature;
		this.employee = employee;
	}
	public String getSapUserId() {
		return sapUserId;
	}
	public void setSapUserId(String sapUserId) {
		this.sapUserId = sapUserId;
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
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	@Override
	public String toString() {
		return "UndertakingSap [sapUserId=" + sapUserId + ", name=" + name + ", date=" + date + ", signature="
				+ signature + ", employee=" + employee + "]";
	}
	
	
}
