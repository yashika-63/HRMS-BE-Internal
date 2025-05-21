package com.spectrum.timesheet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.timesheet.modal.TimesheetDayDetails;

public interface TimesheetDayDetailsRepository extends JpaRepository<TimesheetDayDetails, Long> {

    List<TimesheetDayDetails> findByTimesheetDayId(long timesheetDayId);

}
