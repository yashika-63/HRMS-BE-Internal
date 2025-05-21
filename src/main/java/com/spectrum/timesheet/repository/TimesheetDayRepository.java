package com.spectrum.timesheet.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.timesheet.modal.TimesheetDay;

public interface TimesheetDayRepository extends JpaRepository<TimesheetDay, Long> {
    List<TimesheetDay> findByDateAndEmployeeId(Date date, long employeeId);

    List<TimesheetDay> findByEmployeeId(Long employeeId);

    List<TimesheetDay> findByDateAndEmployeeId(LocalDate date, Long employeeId);

    List<TimesheetDay> findByDateBetweenAndEmployeeId(LocalDate startDate, LocalDate endDate, Long employeeId);

}
