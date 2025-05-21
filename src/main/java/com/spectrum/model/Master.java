package com.spectrum.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;


@Entity
@Table
public class Master {

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
 	@Column(length=100)
 	private long masterId;
	@Column(length=1054)
	private String keyvalue;
	@Column(length=2000)
	private String data;
	@Column
	private String category;
	
	
	@ManyToOne
	@JoinColumn(name = "account_master_id")
	private AccountMaster accountMaster;

	public Master() {
		super();

	}

	public Master(long masterId, String keyvalue, String data, String category, AccountMaster accountMaster) {
		this.masterId = masterId;
		this.keyvalue = keyvalue;
		this.data = data;
		this.category = category;
		this.accountMaster = accountMaster;
	} 	

	public long getMasterId() {
		return masterId;
	}

	public void setMasterId(long masterId) {
		this.masterId = masterId;
	}

	public String getKeyvalue() {
		return keyvalue;
	}

	public void setKeyvalue(String keyvalue) {
		this.keyvalue = keyvalue;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public AccountMaster getAccountMaster() {
		return accountMaster;
	}

	public void setAccountMaster(AccountMaster accountMaster) {
		this.accountMaster = accountMaster;
	}

	@Override
	public String toString() {
		return "Master [masterId=" + masterId + ", keyvalue=" + keyvalue + ", data=" + data + ", category=" + category
				+ ", accountMaster=" + accountMaster + "]";
	}

}
