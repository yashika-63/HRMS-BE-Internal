package com.spectrum.PreOnboarding.EmployeeVerification.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class EmploymentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @Column(length=100)
        private String companyName;

		@Column(length=50)
        private String jobRole;

		@Column(length=100)
        private String responsibilities;
        @Column
        private Date startDate;

        @Column
        private Date endDate;

		@Column(length=50)
        private String jobDuration;

        @Column 
        private int latestCtc;

		@Column(length=100)
        private String supervisorContact;

		@Column(length=100)
        private String reasonOfLeaving;

		@Column(length=250)
        private String achievements;
        
		@Column(length=100)
        private String employeementType;

		@Column(length=100)
        private String location;

		@Column(length=100)
        private String industry;

		@Column(length=100)
        private String companySize;

		@Column(length=100)
        private String latestMonthGross;

        @Column
        private int teamSize;


        @Column
        private boolean verificationStatus;
        
        @ManyToOne
        @JoinColumn(name = "verification_ticket_id", referencedColumnName = "id", nullable = false)
        private VerificationTicket verificationTicket;

        @ManyToOne
        @JoinColumn(name = "pre_registration_id", referencedColumnName = "id", nullable = false)
        private PreRegistration preRegistration;


}
