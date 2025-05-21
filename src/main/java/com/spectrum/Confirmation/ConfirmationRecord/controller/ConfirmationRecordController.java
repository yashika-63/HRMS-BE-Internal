package com.spectrum.Confirmation.ConfirmationRecord.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.Confirmation.ConfirmationRecord.model.ConfirmationRecord;
import com.spectrum.Confirmation.ConfirmationRecord.repository.ConfirmationRecordRepository;
import com.spectrum.Confirmation.ConfirmationRecord.service.ConfirmationRecordService;

@RestController
@RequestMapping("/api/confirmation")
public class ConfirmationRecordController {

    @Autowired
    private ConfirmationRecordService confirmationRecordService;

    @Autowired
    private ConfirmationRecordRepository confirmationRecordRepository;

    @PostMapping("/save")
    public ResponseEntity<ConfirmationRecord> saveConfirmation(
            @RequestParam Long employeeId,
            @RequestParam Long responsiblePersonId,
            @RequestParam Long hrId,
            @RequestParam Long companyId,
            @RequestBody ConfirmationRecord recordData) {
    
        ConfirmationRecord savedRecord = confirmationRecordService.saveConfirmationRecord(
            employeeId, responsiblePersonId, hrId, companyId, recordData.isStatus());
    
        return new ResponseEntity<>(savedRecord, HttpStatus.CREATED);
    }
    
    @GetMapping("/ActiveByCompany")
    public ResponseEntity<List<ConfirmationRecord>> getConfirmedRecords(@RequestParam Long companyId) {
        List<ConfirmationRecord> confirmed = confirmationRecordService.getActiveRecordsByCompanyId(companyId);
        return ResponseEntity.ok(confirmed);
    }

    @GetMapping("/ActiveByResponsiblePerson")
    public ResponseEntity<List<ConfirmationRecord>> getActiveRecords(@RequestParam Long responsiblePersonId) {
        List<ConfirmationRecord> confirmed = confirmationRecordService.getActiveRecordsByResponsiblePersionId(responsiblePersonId);
        return ResponseEntity.ok(confirmed);
    }

    @GetMapping("/byResponsiblePerson/{responsiblePersonId}")
public ResponseEntity<List<ConfirmationRecord>> getByResponsiblePerson(@PathVariable Long responsiblePersonId) {
    List<ConfirmationRecord> records = confirmationRecordRepository
            .findByEmployeesIdAndResponsiblePersonActionTrue(responsiblePersonId);
    return ResponseEntity.ok(records);
}
 


@GetMapping("/pending/responsible/{responsiblePersonId}")
public ResponseEntity<List<ConfirmationRecord>> getPendingConfirmations(
        @PathVariable Long responsiblePersonId) {
    List<ConfirmationRecord> records = confirmationRecordService
            .getPendingConfirmationRecordsByResponsiblePerson(responsiblePersonId);
    return ResponseEntity.ok(records);
}


// @GetMapping("/confirmed")
// public ResponseEntity<List<ConfirmationRecord>> getConfirmedActionsByMonthAndYear(
//         @RequestParam Long companyId,
//         @RequestParam int year,
//         @RequestParam int month) {

//     List<ConfirmationRecord> records = confirmationRecordService
//             .getConfirmedActionsByMonthAndYear(companyId, year, month);
//     return ResponseEntity.ok(records);
// }



// @GetMapping("/rejected")
// public ResponseEntity<List<ConfirmationRecord>> getRejectedActionsByMonthAndYear(
//         @RequestParam Long companyId,
//         @RequestParam int year,
//         @RequestParam int month) {

//     List<ConfirmationRecord> records = confirmationRecordService
//             .getRejectedActionsByMonthAndYear(companyId, year, month);
//     return ResponseEntity.ok(records);
// }



// @GetMapping("/extended")
// public ResponseEntity<List<ConfirmationRecord>> getExtendedActionsByMonthAndYear(
//         @RequestParam Long companyId,
//         @RequestParam int year,
//         @RequestParam int month) {

//     List<ConfirmationRecord> records = confirmationRecordService
//             .getExtendedActionsByMonthAndYear(companyId, year, month);
//     return ResponseEntity.ok(records);
// }

@GetMapping("/extended")
public ResponseEntity<List<Map<String, Object>>> getExtendedActionsByMonthAndYear(
        @RequestParam Long companyId,
        @RequestParam int year,
        @RequestParam int month) {

    List<Map<String, Object>> result = confirmationRecordService
            .getExtendedActionsWithDetails(companyId, year, month);
    
    return ResponseEntity.ok(result);
}






@GetMapping("/confirm")
public ResponseEntity<List<Map<String, Object>>> getConfirmedActionsByMonthAndYear(
        @RequestParam Long companyId,
        @RequestParam int year,
        @RequestParam int month) {

    List<Map<String, Object>> result = confirmationRecordService
            .getConfirmedActionsWithDetails(companyId, year, month);

    return ResponseEntity.ok(result);
}


@GetMapping("/terminate")
public ResponseEntity<List<Map<String, Object>>> getTerminatedActionsByMonthAndYear(
        @RequestParam Long companyId,
        @RequestParam int year,
        @RequestParam int month) {

    List<Map<String, Object>> result = confirmationRecordService
            .getTerminatedActionsWithDetails(companyId, year, month);

    return ResponseEntity.ok(result);
}


}
