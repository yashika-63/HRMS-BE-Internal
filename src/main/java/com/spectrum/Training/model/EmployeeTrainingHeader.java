    package com.spectrum.Training.model;

    import java.time.LocalDate;

    import org.hibernate.annotations.CreationTimestamp;

    import com.spectrum.model.Employee;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.ManyToOne;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class EmployeeTrainingHeader {

        @Id
        @Column
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(updatable = false)
        @CreationTimestamp
        private LocalDate date;

        @Column
        private String training;  

        @Column
        private Boolean status;
        
        @Column
        private Boolean managerApprovalStatus;
        
        @Column
        private Boolean managerApproval;
        
        @Column
        private int reportingmanagerId;

        @Column
        private int percentageComplete;


        @ManyToOne
        @JoinColumn(name = "employee_id", nullable = false)
        private Employee employee;

    }
