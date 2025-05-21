package com.spectrum.timesheet.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.timesheet.modal.TimesheetMain;
import com.spectrum.timesheet.service.TimesheetMainService;

@RestController
@RequestMapping("/api/timesheetmain")
public class TimesheetMainController {

    @Autowired
    private TimesheetMainService timesheetMainService;

    // @PostMapping("/saveTimesheetMain/{employeeId}/{companyId}")
    // public ResponseEntity<?> saveTimesheetMain(
    // @PathVariable long employeeId,
    // @PathVariable long companyId,
    // @RequestBody TimesheetMain timesheetMain) {
    // try {
    // // Call the service method to save the timesheet
    // TimesheetMain savedTimesheetMain =
    // timesheetMainService.saveTimesheetMain(employeeId, companyId,
    // timesheetMain);
    // return ResponseEntity.ok(savedTimesheetMain);
    // } catch (Exception e) {
    // e.printStackTrace(); // Log the error for debugging
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: "
    // + e.getMessage());
    // }
    // }

    @PostMapping("/saveTimesheetMain/{employeeId}/{companyId}")
    public ResponseEntity<?> saveTimesheetMain(
            @PathVariable long employeeId,
            @PathVariable long companyId,
            @RequestBody TimesheetMain timesheetMain) {
        try {
            // Save the timesheet, but check for conflicting date ranges first
            TimesheetMain savedTimesheetMain = timesheetMainService.saveTimesheetMain(employeeId, companyId,
                    timesheetMain);
            return ResponseEntity.ok(savedTimesheetMain);
        } catch (IllegalArgumentException e) {
            // Handle the conflict error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/saveTimesheet/{employeeId}/{companyId}/{workflowMainId}")
    public ResponseEntity<?> saveTimesheetMain(
            @PathVariable long employeeId,
            @PathVariable long companyId,
            @PathVariable long workflowMainId,
            @RequestBody TimesheetMain timesheetMain) {
        try {
            TimesheetMain savedTimesheetMain = timesheetMainService.saveTimesheetMain(
                    employeeId, companyId, workflowMainId, timesheetMain);
            return ResponseEntity.ok(savedTimesheetMain);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // @GetMapping("/getBetweenDates")
    // public ResponseEntity<List<TimesheetMain>> getTimesheetsBetweenDates(
    // @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date
    // fromDate,
    // @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate)
    // {
    // List<TimesheetMain> timesheetMains =
    // timesheetMainService.getTimesheetsBetweenDates(fromDate, toDate);
    // return ResponseEntity.ok(timesheetMains);
    // }

    // @GetMapping("/getBetweenDates")
    // public ResponseEntity<?> getTimesheetsBetweenDates(
    // @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date
    // fromDate,
    // @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
    // @RequestParam("companyId") long companyId,
    // @RequestParam("employeeId") long employeeId) {

    // // Validate the date range
    // if (toDate.before(fromDate)) {
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date
    // range: 'toDate' must be after or equal to 'fromDate'.");
    // }

    // // Fetch the timesheet data
    // List<TimesheetMain> timesheetMains =
    // timesheetMainService.getTimesheetsBetweenDates(employeeId, companyId,
    // fromDate, toDate);

    // // Handle case where no data is found
    // if (timesheetMains.isEmpty()) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No timesheets found
    // for the given date range, employeeId, and companyId.");
    // }

    // return ResponseEntity.ok(timesheetMains);

    // }

    @GetMapping("/getBetweenDates")
    public ResponseEntity<List<TimesheetMain>> getTimesheetsBetweenDates(
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        List<TimesheetMain> timesheetMains = timesheetMainService.getTimesheetsBetweenDates(fromDate, toDate);
        return ResponseEntity.ok(timesheetMains);
    }

    // @GetMapping("/getBetweenDates")
    // public ResponseEntity<?> getTimesheetsBetweenDates(
    // @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date
    // fromDate,
    // @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
    // @RequestParam("companyId") long companyId,
    // @RequestParam("employeeId") long employeeId) {

    // // Validate the date range
    // if (toDate.before(fromDate)) {
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    // .body("Invalid date range: 'toDate' must be after or equal to 'fromDate'.");
    // }

    // try {
    // // Fetch the timesheets for the given employee, company, and date range
    // List<TimesheetMain> timesheets =
    // timesheetMainService.getTimesheetsBetweenDates(fromDate, toDate);

    // if (timesheets.isEmpty()) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND)
    // .body("No timesheets found for the given criteria.");
    // }
    // return ResponseEntity.ok(timesheets);
    // } catch (Exception e) {
    // e.printStackTrace(); // Log the error
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body("Error fetching timesheets: " + e.getMessage());
    // }
    // }

    @GetMapping("/getByDates")
    public ResponseEntity<List<TimesheetMain>> getTimesheetByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        // Call the service method to fetch the timesheets by date
        List<TimesheetMain> timesheets = timesheetMainService.getTimesheetsByDate(date);

        // Check if any timesheets are found
        if (timesheets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(timesheets);
        }
        return ResponseEntity.ok(timesheets);
    }

    @GetMapping("/getByDatesAndEmployeeId")
    public ResponseEntity<List<TimesheetMain>> getTimesheetByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam("employeeId") long employeeId) {

        // Call the service method to fetch the timesheets by date and employeeId
        List<TimesheetMain> timesheets = timesheetMainService.getTimesheetsByDateAndEmployeeId(date, employeeId);

        // Check if any timesheets are found
        if (timesheets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(timesheets);
        }
        return ResponseEntity.ok(timesheets);
    }









    ///////////////////////
    


        @PutMapping("/update/{id}")
    public ResponseEntity<TimesheetMain> updateTimesheet(@PathVariable Long id) {
        TimesheetMain updatedTimesheet = timesheetMainService.updateTimesheetStatus(id);
        return ResponseEntity.ok(updatedTimesheet);
    }









    @GetMapping("/timesheets/pending/{managerId}")
public ResponseEntity<List<TimesheetMain>> getPendingTimesheets(@PathVariable int managerId) {
    List<TimesheetMain> timesheets = timesheetMainService.getTimesheetsForApproval(managerId);
    return ResponseEntity.ok(timesheets);
}



@PutMapping("/timesheets/approveOrReject/{id}")
public ResponseEntity<TimesheetMain> approveOrRejectTimesheet(
        @PathVariable Long id,
        @RequestParam boolean approved,
        @RequestParam boolean rejected) {

    TimesheetMain updatedTimesheet = timesheetMainService.updateManagerApproval(id, approved, rejected);
    return ResponseEntity.ok(updatedTimesheet);
}



@GetMapping("/rejected/{employeeId}")
public ResponseEntity<List<TimesheetMain>> getRejectedTimesheets(@PathVariable Long employeeId) {
    List<TimesheetMain> rejectedTimesheets = timesheetMainService.getRejectedTimesheetsByEmployeeId(employeeId);
    return ResponseEntity.ok(rejectedTimesheets);
}

}
