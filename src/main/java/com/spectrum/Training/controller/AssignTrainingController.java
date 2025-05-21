package com.spectrum.Training.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.PageRequest;

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

import com.spectrum.Training.model.AssignTraining;
import com.spectrum.Training.service.AssignTrainingService;

@RestController
@RequestMapping("/api/assign-trainings")
public class AssignTrainingController {

      @Autowired
    private AssignTrainingService assignTrainingService;

    // Get all assigned trainings
    @GetMapping
    public List<AssignTraining> getAllAssignTrainings() {
        return assignTrainingService.getAllAssignedTrainings();
    }

    // Get assigned training by ID
    @GetMapping("/{id}")
    public AssignTraining getAssignTrainingById(@PathVariable Long id) {
        return assignTrainingService.getAssignTrainingById(id);
    }

    // Create new assigned training
    @PostMapping
    public AssignTraining saveAssignTraining(@RequestBody AssignTraining assignTraining) {
        return assignTrainingService.saveAssignTraining(assignTraining);
    }

    // Update existing assigned training
    @PutMapping("/{id}")
    public AssignTraining updateAssignTraining(@PathVariable Long id, @RequestBody AssignTraining updatedTraining) {
        return assignTrainingService.updateAssignTraining(id, updatedTraining);
    }

    // Delete assigned training by ID
    @DeleteMapping("/{id}")
    public void deleteAssignTraining(@PathVariable Long id) {
        assignTrainingService.deleteAssignTraining(id);
    }  

    //////
    //  @PostMapping("/assign")
    // public ResponseEntity<List<AssignTraining>> assignTrainingToEmployee(
    //         @RequestBody List<AssignTraining> assignList,
    //         @RequestParam List<Long> employeeIds,
    //         @RequestParam Long trainingId,
    //         @RequestParam Long assignedById,
    //         @RequestParam Long companyId) {

    //     try {
    //         List<AssignTraining> savedList = assignTrainingService.saveAllWithNotification(
    //                 assignList, employeeId, trainingId, assignedById, companyId);

    //         return new ResponseEntity<>(savedList, HttpStatus.CREATED);

    //     } catch (RuntimeException e) {
    //         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    //     }
    // }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<AssignTraining>> getAssignedByEmployee(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AssignTraining> result = assignTrainingService.getAssignedTrainingsByEmployeeId(employeeId, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/assigned/company/{companyId}")
public ResponseEntity<Page<AssignTraining>> getAssignedByCompanyAndYear(
        @PathVariable Long companyId,
        @RequestParam int year,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
) {
    Pageable pageable = PageRequest.of(page, size);
    Page<AssignTraining> result = assignTrainingService.getAssignedTrainingsByCompanyAndYear(companyId, year, pageable);
    return ResponseEntity.ok(result);
}


@GetMapping("/company/{companyId}/assignments")
public ResponseEntity<Page<AssignTraining>> getAssignmentsByCompanyAndDate(
        @PathVariable Long companyId,
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

    Pageable pageable = PageRequest.of(page, size);
    Page<AssignTraining> trainings = assignTrainingService.getAssignedTrainingsByCompanyAndDateRange(companyId, startDate, endDate, pageable);

    return ResponseEntity.ok(trainings);
}
@GetMapping("/assignTrain/{employeeId}")
public ResponseEntity<Page<AssignTraining>> getAssignedTrainings(
        @PathVariable Long employeeId,
        @RequestParam Boolean completionStatus,
        @RequestParam Boolean expiryStatus,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size);
    Page<AssignTraining> assignedTrainings = assignTrainingService
            .getAssignedTrainingsByEmployeeAndStatus(employeeId, completionStatus, expiryStatus, pageable);

    return new ResponseEntity<>(assignedTrainings, HttpStatus.OK);
}     


@PostMapping("/assign")
    public ResponseEntity<?> assignTrainingToMultipleEmployees(
            @RequestParam List<Long> employeeIds,
            @RequestParam Long trainingId,
            @RequestParam Long assignedById,
            @RequestParam Long compId) {

        try {
            List<AssignTraining> assignedTrainings = assignTrainingService.saveAllWithNotification(
                    employeeIds, trainingId, assignedById, compId
            );
            return ResponseEntity.ok(assignedTrainings);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
//     @GetMapping("/assigned/company/{companyId}")
// public ResponseEntity<Page<AssignTraining>> getAssignedByCompanyAndYear(
//         @PathVariable Long companyId,
//         @RequestParam(defaultValue = "0") int page,
//         @RequestParam(defaultValue = "5") int size
// ) {
//     Pageable pageable = PageRequest.of(page, size);
//     Page<AssignTraining> result = assignTrainingService.getAssignedTrainingsByCompany(companyId, pageable);
//     return ResponseEntity.ok(result);
// }
}
