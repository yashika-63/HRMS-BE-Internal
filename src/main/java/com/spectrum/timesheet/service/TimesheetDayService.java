package com.spectrum.timesheet.service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spectrum.model.Employee;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.timesheet.modal.TimesheetDay;
import com.spectrum.timesheet.modal.TimesheetDayDetails;
import com.spectrum.timesheet.modal.TimesheetMain;
import com.spectrum.timesheet.repository.TimesheetDayDetailsRepository;
import com.spectrum.timesheet.repository.TimesheetDayRepository;
import com.spectrum.timesheet.repository.TimesheetMainRepository;

import com.spectrum.repository.CompanyRegistrationRepository;

@Service
public class TimesheetDayService {

    @Autowired
    private TimesheetDayRepository timesheetDayRepository;

    @Autowired
    private TimesheetMainRepository timesheetMainRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private TimesheetDayDetailsRepository timesheetDayDetailsRepository;

    public TimesheetDay saveTimesheetDay(TimesheetDay timesheetDay, Long timesheetMainId, Long employeeId,
            Long companyId) {
        // Fetch TimesheetMain by ID
        TimesheetMain timesheetMain = timesheetMainRepository.findById(timesheetMainId)
                .orElseThrow(() -> new RuntimeException("TimesheetMain not found"));

        // Fetch Employee by ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Fetch CompanyRegistration by ID
        CompanyRegistration companyRegistration = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Set associations
        timesheetDay.setTimesheetMain(timesheetMain);
        timesheetDay.setEmployee(employee);
        timesheetDay.setCompanyRegistration(companyRegistration);

        // Save the TimesheetDay entity
        return timesheetDayRepository.save(timesheetDay);
    }

    // public TimesheetDay saveTimesheetDay(long employeeId, long companyId, long
    // timesheetMainId,
    // TimesheetDay timesheetDay) {
    // // Fetch the necessary entities
    // Employee employee = employeeRepository.findById(employeeId)
    // .orElseThrow(() -> new RuntimeException("Employee with ID " + employeeId + "
    // not found"));
    // CompanyRegistration companyRegistration =
    // companyRegistrationRepository.findById(companyId)
    // .orElseThrow(() -> new RuntimeException("CompanyRegistration with ID " +
    // companyId + " not found"));
    // TimesheetMain timesheetMain =
    // timesheetMainRepository.findById(timesheetMainId)
    // .orElseThrow(() -> new RuntimeException("TimesheetMain with ID " +
    // timesheetMainId + " not found"));

    // // Associate TimesheetDay with Employee, Company, and TimesheetMain
    // timesheetDay.setEmployee(employee);
    // timesheetDay.setCompanyRegistration(companyRegistration);
    // timesheetDay.setTimesheetMain(timesheetMain);

    // // Save TimesheetDay entity
    // TimesheetDay savedTimesheetDay = timesheetDayRepository.save(timesheetDay);

    // // Save TimesheetDayDetails entities if not handled by cascade
    // for (TimesheetDayDetails detail : timesheetDay.getTimesheetDayDetails()) {
    // detail.setTimesheetDay(savedTimesheetDay);
    // timesheetDayDetailsRepository.save(detail);
    // }

    // return savedTimesheetDay;
    // }

    // Method to get timesheets by date and employee ID
    public List<TimesheetDay> getTimesheetsByDateAndEmployeeId(Date date, long employeeId) {
        return timesheetDayRepository.findByDateAndEmployeeId(date, employeeId);
    }

    // Method to get timesheets by employee ID
    public List<TimesheetDay> getTimesheetsByEmployeeId(Long employeeId) {
        return timesheetDayRepository.findByEmployeeId(employeeId);
    }

    // public TimesheetDay saveTimesheetDay(long employeeId, long companyId, long timesheetMainId,
    //         TimesheetDay timesheetDay) {
    //     // Fetch the necessary entities
    //     Employee employee = employeeRepository.findById(employeeId)
    //             .orElseThrow(() -> new RuntimeException("Employee with ID " + employeeId + " not found"));
    //     CompanyRegistration companyRegistration = companyRegistrationRepository.findById(companyId)
    //             .orElseThrow(() -> new RuntimeException("CompanyRegistration with ID " + companyId + " not found"));
    //     TimesheetMain timesheetMain = timesheetMainRepository.findById(timesheetMainId)
    //             .orElseThrow(() -> new RuntimeException("TimesheetMain with ID " + timesheetMainId + " not found"));

    //     // Associate TimesheetDay with Employee, Company, and TimesheetMain
    //     timesheetDay.setEmployee(employee);
    //     timesheetDay.setCompanyRegistration(companyRegistration);
    //     timesheetDay.setTimesheetMain(timesheetMain);

    //     // First save the TimesheetDay entity to generate the ID
    //     TimesheetDay savedTimesheetDay = timesheetDayRepository.save(timesheetDay);

    //     // Now, save the TimesheetDayDetails entities, linking them to the saved
    //     // TimesheetDay
    //     if (timesheetDay.getTimesheetDayDetails() != null) {
    //         for (TimesheetDayDetails detail : timesheetDay.getTimesheetDayDetails()) {
    //             // Associate each detail with the saved TimesheetDay
    //             detail.setTimesheetDay(savedTimesheetDay);
    //             timesheetDayDetailsRepository.save(detail);
    //         }
    //     }

    //     return savedTimesheetDay;
    // }



    public TimesheetDay saveTimesheetDay(long employeeId, long companyId, long timesheetMainId,
                                     TimesheetDay timesheetDay) {
    // Fetch the necessary entities
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee with ID " + employeeId + " not found"));
    CompanyRegistration companyRegistration = companyRegistrationRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("CompanyRegistration with ID " + companyId + " not found"));
    TimesheetMain timesheetMain = timesheetMainRepository.findById(timesheetMainId)
            .orElseThrow(() -> new RuntimeException("TimesheetMain with ID " + timesheetMainId + " not found"));

    // Check if the current date is within the valid range
    Date toDate = timesheetMain.getToDate();
    Date currentDate = new Date();

    // Calculate the last valid date for adding days (toDate + 2 days)
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(toDate);
    calendar.add(Calendar.DAY_OF_YEAR, 2);
    Date validUntilDate = calendar.getTime();

    if (currentDate.after(validUntilDate)) {
        throw new RuntimeException("You can only add days to this timesheet before " + validUntilDate);
    }

    // Associate TimesheetDay with Employee, Company, and TimesheetMain
    timesheetDay.setEmployee(employee);
    timesheetDay.setCompanyRegistration(companyRegistration);
    timesheetDay.setTimesheetMain(timesheetMain);

    // Save the TimesheetDay entity to generate the ID
    TimesheetDay savedTimesheetDay = timesheetDayRepository.save(timesheetDay);

    // Save the TimesheetDayDetails entities, linking them to the saved TimesheetDay
    if (timesheetDay.getTimesheetDayDetails() != null) {
        for (TimesheetDayDetails detail : timesheetDay.getTimesheetDayDetails()) {
            // Associate each detail with the saved TimesheetDay
            detail.setTimesheetDay(savedTimesheetDay);
            timesheetDayDetailsRepository.save(detail);
        }
    }

    return savedTimesheetDay;
}



    public List<TimesheetDay> getTimesheetsByEmployeeIdAndDate(LocalDate date, Long employeeId) {
        return timesheetDayRepository.findByDateAndEmployeeId(date, employeeId);
    }

    public List<TimesheetDay> getTimesheetsBetweenDatesAndEmployeeId(LocalDate startDate, LocalDate endDate,
            Long employeeId) {
        return timesheetDayRepository.findByDateBetweenAndEmployeeId(startDate, endDate, employeeId);
    }

    public void deleteTimesheetDayById(long id) {
        // Fetch the TimesheetDay entity or throw a custom exception if not found
        TimesheetDay timesheetDay = timesheetDayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimesheetDay with ID " + id + " not found"));

        // Delete the entity
        timesheetDayRepository.delete(timesheetDay);
    }

    // Update TimesheetDayDetails by ID
    public ResponseEntity<TimesheetDay> updateTimesheetDay(Long id, TimesheetDay updatedDay) {
        Optional<TimesheetDay> existingDay = timesheetDayRepository.findById(id);

        if (existingDay.isPresent()) {
            TimesheetDay day = existingDay.get();

            // Update fields without touching the TimesheetDay relationship
            day.setDate(updatedDay.getDate());
            day.setDayStartTime(updatedDay.getDayStartTime());
            day.setDayEndTime(updatedDay.getDayEndTime());
            day.setTotalTime(updatedDay.getTotalTime());
            TimesheetDay savedDay = timesheetDayRepository.save(day);
            return ResponseEntity.ok(savedDay);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
