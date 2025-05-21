package com.spectrum.model;



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
public class ItRecrutment {

	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	@Column(length=150)
	private String name;
	@Column(length=50)
	private String contactNo;
	@Column(length=100)
	private String department;
	@Column(length=100)
	private String designation;
	@Column(length=100)
	private String role;
	@Column
	private Boolean pcRequirement;
	@Column
	private Boolean laptopRequirement;
	@Column
	private Boolean macRequirement;
	@Column
	private Boolean vpnRequirement;
	@Column
	private Boolean serverIdRequirement;
	@Column
	private Boolean emailIdRequirement;
	@Column
	private Boolean sapUserIdRequirement;
	@Column
	private Boolean hrSimCardRequirement;
	@Column(length=150)
	private String plantNo;
	@Column(length=2054)
	private String remark;
	
	 @ManyToOne
	    @JoinColumn(name = "employee_id")
	    private Employee employee;
	
	
	
	
	public ItRecrutment() {
		super();

	}




	public ItRecrutment(Long id, String name, String contactNo, String department, String designation, String role,
			Boolean pcRequirement, Boolean laptopRequirement, Boolean macRequirement, Boolean vpnRequirement,
			Boolean serverIdRequirement, Boolean emailIdRequirement, Boolean sapUserIdRequirement,
			Boolean hrSimCardRequirement, String plantNo, String remark, Employee employee) {
		super();
		this.id = id;
		this.name = name;
		this.contactNo = contactNo;
		this.department = department;
		this.designation = designation;
		this.role = role;
		this.pcRequirement = pcRequirement;
		this.laptopRequirement = laptopRequirement;
		this.macRequirement = macRequirement;
		this.vpnRequirement = vpnRequirement;
		this.serverIdRequirement = serverIdRequirement;
		this.emailIdRequirement = emailIdRequirement;
		this.sapUserIdRequirement = sapUserIdRequirement;
		this.hrSimCardRequirement = hrSimCardRequirement;
		this.plantNo = plantNo;
		this.remark = remark;
		this.employee = employee;
	}




	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getContactNo() {
		return contactNo;
	}




	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}




	public String getDepartment() {
		return department;
	}




	public void setDepartment(String department) {
		this.department = department;
	}




	public String getDesignation() {
		return designation;
	}




	public void setDesignation(String designation) {
		this.designation = designation;
	}




	public String getRole() {
		return role;
	}




	public void setRole(String role) {
		this.role = role;
	}




	public Boolean getPcRequirement() {
		return pcRequirement;
	}




	public void setPcRequirement(Boolean pcRequirement) {
		this.pcRequirement = pcRequirement;
	}




	public Boolean getLaptopRequirement() {
		return laptopRequirement;
	}




	public void setLaptopRequirement(Boolean laptopRequirement) {
		this.laptopRequirement = laptopRequirement;
	}




	public Boolean getMacRequirement() {
		return macRequirement;
	}




	public void setMacRequirement(Boolean macRequirement) {
		this.macRequirement = macRequirement;
	}




	public Boolean getVpnRequirement() {
		return vpnRequirement;
	}




	public void setVpnRequirement(Boolean vpnRequirement) {
		this.vpnRequirement = vpnRequirement;
	}




	public Boolean getServerIdRequirement() {
		return serverIdRequirement;
	}




	public void setServerIdRequirement(Boolean serverIdRequirement) {
		this.serverIdRequirement = serverIdRequirement;
	}




	public Boolean getEmailIdRequirement() {
		return emailIdRequirement;
	}




	public void setEmailIdRequirement(Boolean emailIdRequirement) {
		this.emailIdRequirement = emailIdRequirement;
	}




	public Boolean getSapUserIdRequirement() {
		return sapUserIdRequirement;
	}




	public void setSapUserIdRequirement(Boolean sapUserIdRequirement) {
		this.sapUserIdRequirement = sapUserIdRequirement;
	}




	public Boolean getHrSimCardRequirement() {
		return hrSimCardRequirement;
	}




	public void setHrSimCardRequirement(Boolean hrSimCardRequirement) {
		this.hrSimCardRequirement = hrSimCardRequirement;
	}




	public String getPlantNo() {
		return plantNo;
	}




	public void setPlantNo(String plantNo) {
		this.plantNo = plantNo;
	}




	public String getRemark() {
		return remark;
	}




	public void setRemark(String remark) {
		this.remark = remark;
	}




	public Employee getEmployee() {
		return employee;
	}




	public void setEmployee(Employee employee) {
		this.employee = employee;
	}




	@Override
	public String toString() {
		return "ItRecrutment [id=" + id + ", name=" + name + ", contactNo=" + contactNo + ", department=" + department
				+ ", designation=" + designation + ", role=" + role + ", pcRequirement=" + pcRequirement
				+ ", laptopRequirement=" + laptopRequirement + ", macRequirement=" + macRequirement
				+ ", vpnRequirement=" + vpnRequirement + ", serverIdRequirement=" + serverIdRequirement
				+ ", emailIdRequirement=" + emailIdRequirement + ", sapUserIdRequirement=" + sapUserIdRequirement
				+ ", hrSimCardRequirement=" + hrSimCardRequirement + ", plantNo=" + plantNo + ", remark=" + remark
				+ ", employee=" + employee + "]";
	}




	
}
