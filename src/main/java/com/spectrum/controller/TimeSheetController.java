package com.spectrum.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.model.TimeSheet;
import com.spectrum.service.TimeSheetService;

@RestController
// @CrossOrigin("http://localhost:3000")
@RequestMapping("/api/schedules")
public class TimeSheetController {

	
	@Autowired
	private TimeSheetService timeSheetService;

	@PostMapping("/save/{employeeId}/{companyId}")
	public ResponseEntity<TimeSheet> saveTimeSheet(@PathVariable("employeeId") Long employeeId,
			@PathVariable("companyId") Long companyId,
			@RequestBody TimeSheet timeSheet) {
		// Call the service to save the timesheet
		TimeSheet savedTimeSheet = timeSheetService.saveTimeSheet(timeSheet.getFromDate(),
				timeSheet.getToDate(),
				employeeId,
				companyId);

		return new ResponseEntity<>(savedTimeSheet, HttpStatus.CREATED);
	}

	// @PostMapping("/batch/{employeeId}")
	// public ResponseEntity<?> createMultipleSchedules(@PathVariable Long
	// employeeId,
	// @RequestBody List<TimeSheet> timeSheets) {
	// try {
	// List<TimeSheet> savedTimeSheets = timeSheetService.saveMultiple(employeeId,
	// timeSheets);
	// return ResponseEntity.status(HttpStatus.CREATED).body(savedTimeSheets);
	// } catch (Exception e) {
	// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	// .body("Error occurred while saving TimeSheets.");
	// }
	// }

	// @GetMapping("/{id}")
	// public ResponseEntity<TimeSheet> getTimeSheetById(@PathVariable Long id) {
	// 	TimeSheet timeSheet = timeSheetService.getTimeSheetById(id)
	// 			.orElseThrow(() -> new IllegalArgumentException("Time-Sheet not found with ID: " + id));
	// 	return ResponseEntity.ok(timeSheet);
    // }

	// @GetMapping("/date")
	// public ResponseEntity<List<TimeSheet>> getTimeSheetsByDate(
	// 		@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date date) {
	// 	List<TimeSheet> timeSheets = timeSheetService.getTimeSheetsByDate(date);
	// 	if (timeSheets.isEmpty()) {
	// 		return ResponseEntity.noContent().build();
	// 	}
	// 	return ResponseEntity.ok(timeSheets);
	// }

	// @GetMapping("/employee-date")
	// public ResponseEntity<List<TimeSheet>> getTimeSheetsByEmployeeIdAndDate(
	// 		@RequestParam Long employeeId,
	// 		@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date date) {
	// 	try {
	// 		List<TimeSheet> timeSheets = timeSheetService.getTimeSheetsByEmployeeIdAndDate(employeeId, date);
	// 		if (timeSheets.isEmpty()) {
	// 			return ResponseEntity.noContent().build();
	// 		}
	// 		return ResponseEntity.ok(timeSheets);
	// 	} catch (RuntimeException e) {
	// 		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	// 	} catch (Exception e) {
	// 		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	// 				.body(null);
	// 	}
	// }

	// @GetMapping("/{employeeId}/{year}/{month}/{week}")
	// public ResponseEntity<List<TimeSheet>> getTimeSheetsByYearMonthWeek(
	// 		@PathVariable Long employeeId,
	// 		@PathVariable int year,
	// 		@PathVariable int month,
	// 		@PathVariable int week) {
	// 	try {
	// 		List<TimeSheet> timeSheets = timeSheetService.getTimeSheetsByYearMonthWeek(employeeId, year, month, week);
	// 		if (timeSheets.isEmpty()) {
	// 			return ResponseEntity.noContent().build();
	// 		}
	// 		return ResponseEntity.ok(timeSheets);
	// 	} catch (Exception e) {
	// 		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	// 	}
	// }
}
