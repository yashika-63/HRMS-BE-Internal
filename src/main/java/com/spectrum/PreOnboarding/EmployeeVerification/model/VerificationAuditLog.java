// package com.spectrum.PreOnboarding.EmployeeVerification.model;

// import java.time.LocalDate;

// import org.hibernate.annotations.CreationTimestamp;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.OneToOne;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Entity
// @Table
// @AllArgsConstructor
// @NoArgsConstructor
// @Data 

// public class VerificationAuditLog {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

// 	@Column(length = 50)
// 	private String firstName;

// 	@Column(length = 50)
// 	private String lastName;

//     @Column
//     private long companyId;

//     @Column
//     private long employeeId;

//     @Column
//     @CreationTimestamp
//     private LocalDate date;

//     @Column(length = 500)
// 	private String note;
    
//     @OneToOne
// 	@JoinColumn(name = "verification_ticket_id", referencedColumnName = "id")
// 	private VerificationTicket verificationTicket;


// }
