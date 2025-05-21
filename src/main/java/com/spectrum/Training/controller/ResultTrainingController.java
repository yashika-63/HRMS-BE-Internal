package com.spectrum.Training.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.Training.model.ResultTraining;
import com.spectrum.Training.service.ResultTrainingService;
@RestController
@RequestMapping("/api/resultTraining")
public class ResultTrainingController {
          @Autowired
    private ResultTrainingService resultTrainingService;

    // Create
    @PostMapping
    public ResponseEntity<ResultTraining> createResult(@RequestBody ResultTraining resultTraining) {
        return ResponseEntity.ok(resultTrainingService.createResult(resultTraining));
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<ResultTraining>> getAllResults() {
        return ResponseEntity.ok(resultTrainingService.getAllResults());
    }

    // Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getResultById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(resultTrainingService.getResultById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateResult(@PathVariable Long id, @RequestBody ResultTraining updatedResult) {
        try {
            return ResponseEntity.ok(resultTrainingService.updateResult(id, updatedResult));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResult(@PathVariable Long id) {
        try {
            resultTrainingService.deleteResult(id);
            return ResponseEntity.ok("Deleted Successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // @PostMapping("/save")
    // public ResponseEntity<List<ResultTraining>> saveResultTrainingList(
    //         @RequestBody List<ResultTraining> results,
    //         @RequestParam Long employeeId,
    //         @RequestParam Long trainingId,
    //         @RequestParam Long assignId) {

    //     List<ResultTraining> savedResults = resultTrainingService.saveResultTrainingList(results, employeeId, trainingId, assignId);
    //     return ResponseEntity.ok(savedResults);
    // }

     @PostMapping("/save")
    public ResponseEntity<List<ResultTraining>> saveResultTraining(
            @RequestBody List<ResultTraining> results,
            @RequestParam Long employeeId,
            @RequestParam Long trainingId,
            @RequestParam Long assignId) {

        // Basic check for acknowledgment
        for (ResultTraining result : results) {
            if (result.getTrainingAcknowledge() == null || result.getTrainingAcknowledge().getId() == null) {
                throw new RuntimeException("TrainingAcknowledge ID is missing in one of the objects!");
            }
        }

        List<ResultTraining> savedResults = resultTrainingService.saveResultTrainingList(results, employeeId, trainingId, assignId);
        return new ResponseEntity<>(savedResults, HttpStatus.CREATED);
    }
    
    @GetMapping("/byEmployee/{employeeId}")
    public ResponseEntity<List<ResultTraining>> getResultsByEmployee(@PathVariable Long employeeId) {
        List<ResultTraining> results = resultTrainingService.getResultsByEmployeeId(employeeId);
        return ResponseEntity.ok(results);
    }
}
