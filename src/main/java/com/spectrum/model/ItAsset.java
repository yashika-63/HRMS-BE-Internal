package com.spectrum.model;

import java.util.Date;

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
public class ItAsset {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long assetId;
	
	@Column
	private String deviceSN;
	@Column
	private String assetNo;
	@Column
	private String model;
	@Column
	private String acAdapterSN;
	@Column
	private String useMouse;
	@Column
	private String laptopUser;
	@Column
	private String userPassword;
	@Column
	private String internetAcssesLink;
	@Column
	private String internetUser;
	@Column
	private String internetPassword;
	@Column
	private String vpnUser;
	@Column
	private String vpnPassword;
	@Column
	private String userEmail;
	@Column
	private String userContact;
	@Column
	private String emailPassword;
	@Column
	private String sapUserName;
	@Column
	private String signatureOfEmployee;
	@Column
	private String signatureOfItDepartmant;
	@Column
	private String signatureOfHrDepartmant;
	@Column
	private Date assetDate;	
	
	 @ManyToOne
	    @JoinColumn(name = "employee_id")
	    private Employee employee;

	public ItAsset() {
		super();

	}

	public ItAsset(Long assetId, String deviceSN, String assetNo, String model, String acAdapterSN, String useMouse,
			String laptopUser, String userPassword, String internetAcssesLink, String internetUser,
			String internetPassword, String vpnUser, String vpnPassword, String userEmail, String userContact,
			String emailPassword, String sapUserName, String signatureOfEmployee, String signatureOfItDepartmant,
			String signatureOfHrDepartmant, Date assetDate, Employee employee) {
		super();
		this.assetId = assetId;
		this.deviceSN = deviceSN;
		this.assetNo = assetNo;
		this.model = model;
		this.acAdapterSN = acAdapterSN;
		this.useMouse = useMouse;
		this.laptopUser = laptopUser;
		this.userPassword = userPassword;
		this.internetAcssesLink = internetAcssesLink;
		this.internetUser = internetUser;
		this.internetPassword = internetPassword;
		this.vpnUser = vpnUser;
		this.vpnPassword = vpnPassword;
		this.userEmail = userEmail;
		this.userContact = userContact;
		this.emailPassword = emailPassword;
		this.sapUserName = sapUserName;
		this.signatureOfEmployee = signatureOfEmployee;
		this.signatureOfItDepartmant = signatureOfItDepartmant;
		this.signatureOfHrDepartmant = signatureOfHrDepartmant;
		this.assetDate = assetDate;
		this.employee = employee;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public String getDeviceSN() {
		return deviceSN;
	}

	public void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}

	public String getAssetNo() {
		return assetNo;
	}

	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getAcAdapterSN() {
		return acAdapterSN;
	}

	public void setAcAdapterSN(String acAdapterSN) {
		this.acAdapterSN = acAdapterSN;
	}

	public String getUseMouse() {
		return useMouse;
	}

	public void setUseMouse(String useMouse) {
		this.useMouse = useMouse;
	}

	public String getLaptopUser() {
		return laptopUser;
	}

	public void setLaptopUser(String laptopUser) {
		this.laptopUser = laptopUser;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getInternetAcssesLink() {
		return internetAcssesLink;
	}

	public void setInternetAcssesLink(String internetAcssesLink) {
		this.internetAcssesLink = internetAcssesLink;
	}

	public String getInternetUser() {
		return internetUser;
	}

	public void setInternetUser(String internetUser) {
		this.internetUser = internetUser;
	}

	public String getInternetPassword() {
		return internetPassword;
	}

	public void setInternetPassword(String internetPassword) {
		this.internetPassword = internetPassword;
	}

	public String getVpnUser() {
		return vpnUser;
	}

	public void setVpnUser(String vpnUser) {
		this.vpnUser = vpnUser;
	}

	public String getVpnPassword() {
		return vpnPassword;
	}

	public void setVpnPassword(String vpnPassword) {
		this.vpnPassword = vpnPassword;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserContact() {
		return userContact;
	}

	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public String getSapUserName() {
		return sapUserName;
	}

	public void setSapUserName(String sapUserName) {
		this.sapUserName = sapUserName;
	}

	public String getSignatureOfEmployee() {
		return signatureOfEmployee;
	}

	public void setSignatureOfEmployee(String signatureOfEmployee) {
		this.signatureOfEmployee = signatureOfEmployee;
	}

	public String getSignatureOfItDepartmant() {
		return signatureOfItDepartmant;
	}

	public void setSignatureOfItDepartmant(String signatureOfItDepartmant) {
		this.signatureOfItDepartmant = signatureOfItDepartmant;
	}

	public String getSignatureOfHrDepartmant() {
		return signatureOfHrDepartmant;
	}

	public void setSignatureOfHrDepartmant(String signatureOfHrDepartmant) {
		this.signatureOfHrDepartmant = signatureOfHrDepartmant;
	}

	public Date getAssetDate() {
		return assetDate;
	}

	public void setAssetDate(Date assetDate) {
		this.assetDate = assetDate;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return "ItAsset [assetId=" + assetId + ", deviceSN=" + deviceSN + ", assetNo=" + assetNo + ", model=" + model
				+ ", acAdapterSN=" + acAdapterSN + ", useMouse=" + useMouse + ", laptopUser=" + laptopUser
				+ ", userPassword=" + userPassword + ", internetAcssesLink=" + internetAcssesLink + ", internetUser="
				+ internetUser + ", internetPassword=" + internetPassword + ", vpnUser=" + vpnUser + ", vpnPassword="
				+ vpnPassword + ", userEmail=" + userEmail + ", userContact=" + userContact + ", emailPassword="
				+ emailPassword + ", sapUserName=" + sapUserName + ", signatureOfEmployee=" + signatureOfEmployee
				+ ", signatureOfItDepartmant=" + signatureOfItDepartmant + ", signatureOfHrDepartmant="
				+ signatureOfHrDepartmant + ", assetDate=" + assetDate + ", employee=" + employee + "]";
	}
	
}
