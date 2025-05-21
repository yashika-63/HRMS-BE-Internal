package com.spectrum.PerformanceManagement.Feedback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.model.Employee;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestedFeedbackDetails {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String Question;

    @Column
    private int rating;

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "requested_feedback_id", referencedColumnName = "id", nullable = false)
        private RequestedFeedback requestedFeedback;

    
}
