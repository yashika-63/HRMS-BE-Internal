package com.spectrum.timesheet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.timesheet.modal.TimesheetDayDetails;
import com.spectrum.timesheet.service.TimesheetDayDetailsService;

@RestController
@RequestMapping("/api/timesheetDetail")
public class TimesheetDayDetailsController {



    @Autowired
    private TimesheetDayDetailsService timesheetDayDetailsService;

    @GetMapping("/by-timesheet-day")
    public ResponseEntity<List<TimesheetDayDetails>> getByTimesheetDayId(@RequestParam long timesheetDayId) {
        List<TimesheetDayDetails> details = timesheetDayDetailsService.getByTimesheetDayId(timesheetDayId);
        return ResponseEntity.ok(details);
    }

    // Update TimesheetDayDetails by ID
    @PutMapping("/{id}")
    public ResponseEntity<TimesheetDayDetails> updateTimesheetDayDetails(
            @PathVariable Long id,
            @RequestBody TimesheetDayDetails updatedDetails) {
        return timesheetDayDetailsService.updateTimesheetDayDetails(id, updatedDetails);
    }

    // Delete TimesheetDayDetails by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimesheetDayDetails(@PathVariable Long id) {
        return timesheetDayDetailsService.deleteTimesheetDayDetails(id);
    }
}
