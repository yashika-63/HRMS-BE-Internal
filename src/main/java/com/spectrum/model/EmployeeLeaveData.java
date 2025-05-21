package com.spectrum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class EmployeeLeaveData {
@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;

	@Column
	private int employeeTotalAssignLeave;
	@Column
	private int employeeTotalPaidLeave;
	@Column
	private int employeeTotalunpaidLeave;

 
	 @OneToOne
	 @JoinColumn(name = "employee_id", referencedColumnName = "id")
	 private Employee employee;
	 
	 





}
