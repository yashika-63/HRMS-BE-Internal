package com.spectrum.timesheet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spectrum.timesheet.modal.TimesheetDayDetails;
import com.spectrum.timesheet.repository.TimesheetDayDetailsRepository;

@Service
public class TimesheetDayDetailsService {

    @Autowired
    private TimesheetDayDetailsRepository timesheetDayDetailsRepository;

    public List<TimesheetDayDetails> getByTimesheetDayId(long timesheetDayId) {
        return timesheetDayDetailsRepository.findByTimesheetDayId(timesheetDayId);
    }

    // Update TimesheetDayDetails by ID
    public ResponseEntity<TimesheetDayDetails> updateTimesheetDayDetails(Long id, TimesheetDayDetails updatedDetails) {
        Optional<TimesheetDayDetails> existingDetails = timesheetDayDetailsRepository.findById(id);

        if (existingDetails.isPresent()) {
            TimesheetDayDetails details = existingDetails.get();

            // Update fields without touching the TimesheetDay relationship
            details.setTaskName(updatedDetails.getTaskName());
            details.setHoursSpent(updatedDetails.getHoursSpent());
            details.setTaskDescription(updatedDetails.getTaskDescription());

            TimesheetDayDetails savedDetails = timesheetDayDetailsRepository.save(details);
            return ResponseEntity.ok(savedDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete TimesheetDayDetails by ID
    public ResponseEntity<Void> deleteTimesheetDayDetails(Long id) {
        Optional<TimesheetDayDetails> existingDetails = timesheetDayDetailsRepository.findById(id);

        if (existingDetails.isPresent()) {
            timesheetDayDetailsRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
