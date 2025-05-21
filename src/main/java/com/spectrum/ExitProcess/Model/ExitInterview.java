package com.spectrum.ExitProcess.Model;

import java.time.LocalDate;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExitInterview {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    
    @Column
    private String assigned;

    @Column
    private LocalDate date;

    @Column
    private Boolean status;

    @Column
    private LocalDate interviewDate;

    @Column
    private String name;

    @Column
    private String supervisorName;

    @Column
    private String title;


    @Column 
    private LocalDate lastDateOfWork;

    @Column
    private Double lengthOfService;


    @Column
    private String reason;

    







    @ManyToOne
@JoinColumn(name = "Employee_id",  referencedColumnName = "id")
private Employee employee;

@ManyToOne
@JoinColumn(name = "OffBoarding_id",  referencedColumnName = "id")
private OffBoarding offBoarding;

}
