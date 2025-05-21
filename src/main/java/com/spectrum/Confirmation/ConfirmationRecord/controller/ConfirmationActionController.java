package com.spectrum.Confirmation.ConfirmationRecord.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.Confirmation.ConfirmationRecord.model.ConfirmationAction;
import com.spectrum.Confirmation.ConfirmationRecord.service.ConfirmationActionService;

@RestController
@RequestMapping("/api/confirmationAction")

public class ConfirmationActionController {

    @Autowired
    private ConfirmationActionService confirmationActionService;

    @PostMapping("/confirm/{confirmationRecordId}")
    public ResponseEntity<ConfirmationAction> confirmEmployee(
            @PathVariable Long confirmationRecordId,
            @RequestBody ConfirmationAction inputAction) {

        ConfirmationAction savedAction = confirmationActionService
                .saveConfirmationAction(confirmationRecordId, inputAction);
        return ResponseEntity.ok(savedAction);
    }

    @PostMapping("/extendProbation/{confirmationRecordId}")
    public ResponseEntity<?> extendProbation(
            @PathVariable Long confirmationRecordId,
            @RequestParam int newTotalMonths,
            @RequestBody ConfirmationAction inputAction) {

        Map<String, Object> response = confirmationActionService.extendProbation(
                confirmationRecordId,
                newTotalMonths,
                inputAction.getActionTakenBy(),
                inputAction.getNote());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/terminate/{confirmationRecordId}")
    public ResponseEntity<?> terminateEmployee(
            @PathVariable Long confirmationRecordId,
            @RequestBody ConfirmationAction inputAction) {

        Map<String, Object> response = confirmationActionService.terminateEmployee(
                confirmationRecordId,
                inputAction.getActionTakenBy(),
                inputAction.getNote());
        return ResponseEntity.ok(response);
    }

}
