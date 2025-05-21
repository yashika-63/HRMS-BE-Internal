package com.spectrum.ExitProcess.Model;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KnowledgeTransfer {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    @CreationTimestamp
    private LocalDate date;

    @Column
    private Boolean completionStatus;

    @Column
    private String description;

   
    @ManyToOne
    @JoinColumn(name = "Employee_to", referencedColumnName = "id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "Employee_by", referencedColumnName = "id")
    private Employee employees;

    @ManyToOne
    @JoinColumn(name = "offBoarding_id", referencedColumnName = "id")
    private OffBoarding offBoarding;

    
}
