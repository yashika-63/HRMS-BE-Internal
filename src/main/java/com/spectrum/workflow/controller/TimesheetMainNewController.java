package com.spectrum.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.timesheet.modal.TimesheetMain;
import com.spectrum.timesheet.service.TimesheetMainService;

@RestController
@RequestMapping("/api")
public class TimesheetMainNewController {

    @Autowired
    private TimesheetMainService timesheetMainService;

    @PostMapping("/saveNew/{employeeId}/{companyId}/{workflowMainId}")
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
}
