package com.spectrum.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="education")
public class Education {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
    @Column(length=100)
	private String institute;
    @Column(length=100)
	private String university;
    @Column(length=50)
	private String typeOfStudy;
	@Column
	private long yearOfAddmisstion;
	@Column
	private long yearOfPassing;
    @Column(length=50)
	private String branch;
	@Column
	private double score;
    @Column(length=50)
	private String scoreType;
	
	 @ManyToOne
	 @JsonIgnore
	    @JoinColumn(name = "employee_id")
	    private Employee employee;
	
	 
	 
	 public Education() {
		super();

	}



	public Education(int id, String institute, String university, String typeOfStudy, long yearOfAddmisstion,
			long yearOfPassing, String branch, double score, String scoreType, Employee employee) {
		this.id = id;
		this.institute = institute;
		this.university = university;
		this.typeOfStudy = typeOfStudy;
		this.yearOfAddmisstion = yearOfAddmisstion;
		this.yearOfPassing = yearOfPassing;
		this.branch = branch;
		this.score = score;
		this.scoreType = scoreType;
		this.employee = employee;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getInstitute() {
		return institute;
	}



	public void setInstitute(String institute) {
		this.institute = institute;
	}



	public String getUniversity() {
		return university;
	}



	public void setUniversity(String university) {
		this.university = university;
	}



	public String getTypeOfStudy() {
		return typeOfStudy;
	}



	public void setTypeOfStudy(String typeOfStudy) {
		this.typeOfStudy = typeOfStudy;
	}



	public long getYearOfAddmisstion() {
		return yearOfAddmisstion;
	}



	public void setYearOfAddmisstion(long yearOfAddmisstion) {
		this.yearOfAddmisstion = yearOfAddmisstion;
	}



	public long getYearOfPassing() {
		return yearOfPassing;
	}



	public void setYearOfPassing(long yearOfPassing) {
		this.yearOfPassing = yearOfPassing;
	}



	public String getBranch() {
		return branch;
	}



	public void setBranch(String branch) {
		this.branch = branch;
	}



	public double getScore() {
		return score;
	}



	public void setScore(double score) {
		this.score = score;
	}



	public String getScoreType() {
		return scoreType;
	}



	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}



	public Employee getEmployee() {
		return employee;
	}



	public void setEmployee(Employee employee) {
		this.employee = employee;
	}



	@Override
	public String toString() {
		return "Education [id=" + id + ", institute=" + institute + ", university=" + university + ", typeOfStudy="
				+ typeOfStudy + ", yearOfAddmisstion=" + yearOfAddmisstion + ", yearOfPassing=" + yearOfPassing
				+ ", branch=" + branch + ", score=" + score + ", scoreType=" + scoreType + ", employee=" + employee
				+ "]";
	}


	
}
