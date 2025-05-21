package com.spectrum.Training.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.model.CompanyRegistration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingAcknowledge {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String Question;

    @Column
    private int rating;
    @Column
    private boolean termAndCondition;
        
@ManyToOne
@JsonIgnore
@JoinColumn(name = "TrainIng_id", referencedColumnName = "id", nullable = true) //  Foreign key in TrainingHRMS table
private TrainingHRMS trainingHRMS;
}
