package com.spectrum.timesheet.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.timesheet.modal.TimesheetDay;
import com.spectrum.timesheet.service.TimesheetDayService;

@RestController
@RequestMapping("/api/timesheet-day")
public class TimesheetDayController {

    @Autowired
    private TimesheetDayService timesheetDayService;

    @PostMapping("/save")
    public ResponseEntity<TimesheetDay> saveTimesheetDay(
            @RequestBody TimesheetDay timesheetDay,
            @RequestParam Long timesheetMainId,
            @RequestParam Long employeeId,
            @RequestParam Long companyId) {

        // Save the timesheetDay and its details
        TimesheetDay savedTimesheetDay = timesheetDayService.saveTimesheetDay(employeeId, companyId, timesheetMainId,
                timesheetDay);
        return ResponseEntity.ok(savedTimesheetDay);
    }

    @PostMapping("/SaveTimesheet/{employeeId}/{companyId}/{timesheetMainId}")
    public ResponseEntity<TimesheetDay> saveTimesheetDay(
            @PathVariable long employeeId,
            @PathVariable long companyId,
            @PathVariable long timesheetMainId,
            @RequestBody TimesheetDay timesheetDay) {
        try {
            TimesheetDay savedTimesheetDay = timesheetDayService.saveTimesheetDay(employeeId, companyId,
                    timesheetMainId, timesheetDay);
            return ResponseEntity.ok(savedTimesheetDay);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Catching known exceptions
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // For unexpected errors
        }
    }

    @GetMapping("/timesheet")
    public ResponseEntity<List<TimesheetDay>> getTimesheetsByDateAndEmployeeId(
            @RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date date,
            @RequestParam("employeeId") long employeeId) {

        List<TimesheetDay> timesheets = timesheetDayService.getTimesheetsByDateAndEmployeeId(date, employeeId);

        if (timesheets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(timesheets);
    }

    // Endpoint to get timesheet by employeeId
    @GetMapping("/timesheet/byEmployeeId")
    public ResponseEntity<List<TimesheetDay>> getTimesheetsByEmployeeId(
            @RequestParam("employeeId") Long employeeId) {

        List<TimesheetDay> timesheets = timesheetDayService.getTimesheetsByEmployeeId(employeeId);

        if (timesheets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(timesheets);
    }

    @GetMapping("/timesheet/byEmployeeIdAndDate")
    public ResponseEntity<List<TimesheetDay>> getTimesheetsByEmployeeIdAndDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("employeeId") Long employeeId) {

        List<TimesheetDay> timesheets = timesheetDayService.getTimesheetsByEmployeeIdAndDate(date, employeeId);

        if (timesheets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(timesheets);
    }

    @GetMapping("/timesheet/byEmployeeIdAndDateRange")
    public ResponseEntity<?> getTimesheetsBetweenDatesAndEmployeeId(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDateStr,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDateStr,
            @RequestParam("employeeId") Long employeeId) {

        try {
            // Parse the string dates into LocalDate
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            List<TimesheetDay> timesheets = timesheetDayService.getTimesheetsBetweenDatesAndEmployeeId(startDate,
                    endDate, employeeId);

            if (timesheets.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(timesheets);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTimesheetDayById(@PathVariable long id) {
        try {
            // Attempt to delete the timesheet
            timesheetDayService.deleteTimesheetDayById(id);
            return ResponseEntity.noContent().build(); // Return 204 No Content on success
        } catch (RuntimeException e) {
            // Handle case where the entity with the given ID is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 Not Found if not found
        } catch (Exception e) {
            // Handle other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return 500 Internal Server Error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimesheetDay> updateTimesheetDay(
            @PathVariable Long id,
            @RequestBody TimesheetDay updatedDay) {
        return timesheetDayService.updateTimesheetDay(id, updatedDay);
    }

}
