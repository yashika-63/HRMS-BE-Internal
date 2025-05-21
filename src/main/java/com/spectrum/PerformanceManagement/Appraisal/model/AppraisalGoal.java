package com.spectrum.PerformanceManagement.Appraisal.model;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.spectrum.PerformanceManagement.GoalSetting.model.EmployeeGoalSetting;
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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AppraisalGoal {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double employeeSelfRating;

    @Column
    private double employeeManagerRating;

    @Column
    private double employeeModeratedFinalRating;

    @Column
    private int appraisalYear;

    // if status is true then active updatable only ones set final can't update
    @Column
    private boolean status;

    // if true then rating for kpi or goal
    @Column
    private boolean parameterType;

    @Column
    @CreationTimestamp
    private LocalDate date;

      // to record process for them done or not 
      @Column
      private boolean employeeRatingStatus;
  
      // to record process for them done or not 
      @Column
      private boolean reportingRatingStatus;
  
       // to record process for them done or not 
       @Column
       private boolean reportingModratedRatingStatus;

    @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @ManyToOne
     //@JsonIgnore
    @JoinColumn(name = "reporting_manager_id", referencedColumnName = "id", nullable = false)
    private Employee employees;

    @ManyToOne
    //  @JsonIgnore
    @JoinColumn(name = "employee_goal_setting_id", referencedColumnName = "id", nullable = false)
    private EmployeeGoalSetting employeeGoalSetting;

    
     @OneToMany(mappedBy = "appraisalGoal", cascade = CascadeType.ALL, orphanRemoval = true)
	
	    private List<AppraisalGoalNotes> appraisalGoalNotes;

}
