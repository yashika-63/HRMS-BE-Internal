    package com.spectrum.PerformanceManagement.Feedback.model;

    import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spectrum.Payroll.PayrollRecord.model.PayrollRecord;
    import com.spectrum.model.Employee;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
    import jakarta.persistence.Table;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Table
    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class RequestedFeedback {


        @Id
        @Column
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(length = 1500)
        private String feedbackDescription;

        @Column(length = 1500)
        private String  Notes;

        @Column
        private int overallRating;

        @Column
        @CreationTimestamp
        private LocalDate date;


        // on this date status automatically should get change to false validity over
        @Column
        private LocalDate endDate;

        // status for active or inactive if active then only view and give feedback onse feedback done make status false.
        @Column
        private boolean status;

        // approval for accept or decline request if accept then only able to give feedback.
        @Column
        private boolean approval;

        @Column
        private int requestedToEmployeeId;

        // here we establish required relation 
        @ManyToOne
        @JoinColumn(name = "employee_id", nullable = false)
        private Employee employee;


        @OneToMany(mappedBy = "requestedFeedback", cascade = CascadeType.ALL)
        private List<RequestedFeedbackDetails> feedbackDetails;
    

        
    }
 