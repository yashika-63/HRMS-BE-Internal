package com.spectrum.Payroll.HoliDayCalender.repository;

import com.spectrum.Payroll.HoliDayCalender.model.EmployeeWorkingDays;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeWorkingDaysRepository extends JpaRepository<EmployeeWorkingDays, Long> {
}