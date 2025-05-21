package com.spectrum.PerformanceManagement.Appraisal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class AppraisalGoalNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String note;  // Renamed to follow Java naming conventions

    // If true, it's a self note; if false, it's a manager note
    @Column
    private boolean selfOrManagerNote;

    // Many notes can belong to one appraisal goal
    @ManyToOne
    @JsonIgnore

    @JoinColumn(name = "appraisal_goal_id", referencedColumnName = "id")
    private AppraisalGoal appraisalGoal;


}
