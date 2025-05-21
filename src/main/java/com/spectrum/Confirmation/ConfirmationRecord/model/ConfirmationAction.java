package com.spectrum.Confirmation.ConfirmationRecord.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

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
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConfirmationAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String note;

    @Column
    private boolean confirm;

    @Column
    private boolean extend;

    @Column
    private boolean terminate;

    @Column
    private String actionTakenBy;

    @Column
    @CreationTimestamp
    private LocalDate date;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "confirmation_record_id", referencedColumnName = "id", nullable = false)
    private ConfirmationRecord confirmationRecord;
}
