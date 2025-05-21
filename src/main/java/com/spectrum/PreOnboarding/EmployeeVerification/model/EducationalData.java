package com.spectrum.PreOnboarding.EmployeeVerification.model;

import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EducationalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column
    private boolean verificationStatus;

    @ManyToOne
    @JoinColumn(name = "verification_ticket_id", referencedColumnName = "id", nullable = false)
    private VerificationTicket verificationTicket;

    
    @ManyToOne
    @JoinColumn(name = "pre_registration_id", referencedColumnName = "id", nullable = false)
    private PreRegistration preRegistration;


}
