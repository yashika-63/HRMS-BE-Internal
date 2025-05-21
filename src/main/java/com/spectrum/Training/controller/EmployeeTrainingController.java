package com.spectrum.Training.controller;

import com.spectrum.Training.model.EmployeeTrainingHeader;
import com.spectrum.Training.model.TrainingApprovalRequest;
import com.spectrum.Training.service.EmployeeTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training")
public class EmployeeTrainingController {

    @Autowired
    private EmployeeTrainingService trainingService;


    @PostMapping("/save/{employeeId}")
    public ResponseEntity<String> saveTrainingRecords(@PathVariable Long employeeId, 
                                                      @RequestBody List<EmployeeTrainingHeader> trainingHeaders) {
        try {
            trainingService.saveEmployeeTraining(trainingHeaders, employeeId);
            return ResponseEntity.ok("Training records saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while saving training records: " + e.getMessage());
        }
    }



    @GetMapping("/pending-approvals/{reportingManagerId}")
    public ResponseEntity<List<EmployeeTrainingHeader>> getPendingApprovals(
            @PathVariable Long reportingManagerId) {
        List<EmployeeTrainingHeader> pendingApprovals = trainingService.getPendingApprovalsByManager(reportingManagerId);
        return ResponseEntity.ok(pendingApprovals);
    }


    @GetMapping("/by-employee/{employeeId}/year/{year}")
    public ResponseEntity<List<EmployeeTrainingHeader>> getTrainingByEmployeeAndYear(
            @PathVariable Long employeeId, @PathVariable int year) {
        List<EmployeeTrainingHeader> trainingRecords = trainingService.getTrainingByEmployeeAndYear(employeeId, year);
        return ResponseEntity.ok(trainingRecords);
    }



   @PutMapping("/approve-reject/{reportingManagerId}")
public ResponseEntity<String> updateManagerApproval(
        @PathVariable int reportingManagerId,
        @RequestBody List<TrainingApprovalRequest> approvals) {

    trainingService.updateManagerApproval(reportingManagerId, approvals);
    return ResponseEntity.ok("Training records updated successfully.");
}





@PutMapping("/update/{id}")
public ResponseEntity<EmployeeTrainingHeader> updateTrainingHeader(
        @PathVariable Long id,
        @RequestBody EmployeeTrainingHeader updatedTrainingHeader) {
    EmployeeTrainingHeader updatedEntity = trainingService.updateTrainingHeader(id, updatedTrainingHeader);
    return ResponseEntity.ok(updatedEntity);
}


}
