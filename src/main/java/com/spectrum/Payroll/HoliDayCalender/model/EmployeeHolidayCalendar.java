package com.spectrum.Payroll.HoliDayCalender.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.spectrum.model.Employee;

@Entity
@Table(name = "employee_holiday_calendar")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeHolidayCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    @Column(name = "working_days")
    private Integer workingDays; // Add this field to store working days



    
    
}