package com.spectrum.Training.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.spectrum.Training.model.TrainingHRMS;
import com.spectrum.Training.service.TrainingService;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/training")
public class TrainingController {
 @Autowired
    private TrainingService trainingService;

    @PostMapping("/saveBy/{companyId}")
    public ResponseEntity<?> saveTraining(
            @RequestBody TrainingHRMS training,
            @PathVariable Long companyId) {
        try {
            TrainingHRMS savedTraining = trainingService.saveTraining(training, companyId);
            return ResponseEntity.ok(savedTraining);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/save-with-employee/{companyId}/{employeeId}")
public ResponseEntity<?> saveTrainingWithEmployee(
        @RequestBody TrainingHRMS training,
        @PathVariable Long companyId,
        @PathVariable Long employeeId) {
    try {
        TrainingHRMS savedTraining = trainingService.saveTrainingWithEmployee(training, companyId, employeeId);
        return ResponseEntity.ok(savedTraining);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}

@GetMapping("/by-year-and-company/{year}/{companyId}")
public ResponseEntity<?> getTrainingsByYearAndCompanyId(
    @PathVariable int year,
    @PathVariable Long companyId) {
    try {
        List<TrainingHRMS> results = trainingService.getTrainingByYearAndCompanyId(year, companyId);
        return ResponseEntity.ok(results);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}
@GetMapping("/by-status-year-company")
public ResponseEntity<?> getTrainingsByStatusYearAndCompany(
        @RequestParam boolean status,
        @RequestParam int year,
        @RequestParam Long companyId) {
    try {
        List<TrainingHRMS> trainings = trainingService.getTrainingByStatusYearAndCompanyId(status, year, companyId);
        return ResponseEntity.ok(trainings);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
    
}

@GetMapping("/by-year-region-company")
public ResponseEntity<?> getByYearRegionAndCompany(
        @RequestParam int year,
        @RequestParam Long regionId,
        @RequestParam Long companyId) {
    try {
        List<TrainingHRMS> list = trainingService.getByYearRegionIdAndCompanyId(year, regionId, companyId);
        return ResponseEntity.ok(list);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}

// @GetMapping("/search")
// public ResponseEntity<Page<TrainingHRMS>> searchTrainings(
//         @RequestParam(required = false) String description,
//         @RequestParam(required = false) String heading,
//         @RequestParam(required = false) Integer year,
//         @RequestParam(required = false) Long companyId,
//         @RequestParam(defaultValue = "0") int page,
//         @RequestParam(defaultValue = "10") int size,
//         @RequestParam(defaultValue = "id") String sortBy,
//         @RequestParam(defaultValue = "asc") String direction
// ) {
//     Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//     Pageable pageable = PageRequest.of(page, size, sort);
//     Page<TrainingHRMS> result = trainingService.searchTrainingRecords(description, heading, year, companyId, pageable);
//     return ResponseEntity.ok(result);
// }

@GetMapping("/search")
public ResponseEntity<Page<TrainingHRMS>> searchTrainings(
        @RequestParam(required = false) String description,
        @RequestParam(required = false) String heading,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Long companyId,
        @RequestParam(required = false) Long departmentId,
        @RequestParam(required = false) Long regionId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction
) {
    Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<TrainingHRMS> result = trainingService.searchTrainingRecords(
        description, heading, year, companyId, departmentId, regionId, pageable);
    return ResponseEntity.ok(result);
}

@GetMapping("/active/{companyId}")
    public Page<TrainingHRMS> getActiveTraining(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
 
        PageRequest pageable = PageRequest.of(page, size);
        return trainingService.getActiveTrainingByCompanyId(companyId, pageable);
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<TrainingHRMS> getTrainingById(@PathVariable Long id) {
        try {
            TrainingHRMS training = trainingService.getByID(id);
            return ResponseEntity.ok(training);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
            // Alternatively, you could return:
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTraining(@PathVariable Long id, @RequestBody TrainingHRMS updatedTraining) {
        Optional<TrainingHRMS> result = trainingService.updateTraining(id, updatedTraining);
       
        return result.map(training ->
                ResponseEntity.ok().body(training))
            .orElseGet(() ->
                ResponseEntity.notFound().build());
    }
   
}
