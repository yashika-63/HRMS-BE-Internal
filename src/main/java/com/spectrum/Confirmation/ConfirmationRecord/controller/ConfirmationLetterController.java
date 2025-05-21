package com.spectrum.Confirmation.ConfirmationRecord.controller;

import com.spectrum.Confirmation.ConfirmationRecord.model.ConfirmationLetter;
import com.spectrum.Confirmation.ConfirmationRecord.service.ConfirmationLetterService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/confirmationLetter")
public class ConfirmationLetterController {

    @Autowired
    private ConfirmationLetterService confirmationLetterService;

    @PostMapping("/save")
    public ConfirmationLetter saveConfirmationLetter(
            @RequestParam Long employeeId,
            @RequestParam Long createdById,
            @RequestParam Long companyId) {
        return confirmationLetterService.saveConfirmationLetter(employeeId, createdById, companyId);
    }

    @GetMapping("/getByEmployeeId/{employeeId}")
    public ConfirmationLetter getByEmployeeId(@PathVariable Long employeeId) {
        return confirmationLetterService.getByEmployeeId(employeeId);
    }

    @GetMapping("/getByCompanyId/{companyId}")
    public List<ConfirmationLetter> getByCompanyId(@PathVariable Long companyId) {
        return confirmationLetterService.getByCompanyId(companyId);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        confirmationLetterService.deleteById(id);
        return "Confirmation Letter deleted successfully with ID: " + id;
    }

    @PutMapping("/acceptConfirmationLetter/{confirmationId}")
    public ResponseEntity<ConfirmationLetter> acceptConfirmation(@PathVariable Long confirmationId) {
        ConfirmationLetter updated = confirmationLetterService.acceptConfirmationLetter(confirmationId);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/rejectConfirmationLetter/{confirmationId}")
    public ResponseEntity<ConfirmationLetter> rejectConfirmation(@PathVariable Long confirmationId) {
        ConfirmationLetter updated = confirmationLetterService.rejectConfirmationLetter(confirmationId);
        return ResponseEntity.ok(updated);
    }

}
