package com.spectrum.Induction.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.Induction.Model.ResultInduction;
import com.spectrum.Induction.Service.ResultService;

@RestController
@RequestMapping("/api/results")
public class ResultController {

         @Autowired
    private ResultService resultService;

    // Create or Update Result
    @PostMapping
    public ResultInduction saveResult(@RequestBody ResultInduction resultInduction) {
        return resultService.saveResult(resultInduction);
    }

    // Get all Results
    @GetMapping
    public List<ResultInduction> getAllResults() {
        return resultService.getAllResults();
    }

    // Get Result by ID
    @GetMapping("/{id}")
    public ResultInduction getResultById(@PathVariable Long id) {
        return resultService.getResultById(id);
    }

    // Delete Result by ID
    @DeleteMapping("/{id}")
    public String deleteResult(@PathVariable Long id) {
        resultService.deleteResult(id);
        return "Result with ID " + id + " has been deleted successfully.";
    }
    ///////////////////////////////////////////////////////////////////////////
    @PostMapping("/save")
    public ResponseEntity<?> saveResultInduction(@RequestBody List<ResultInduction> resultInductions,
                                                 @RequestParam Long employeeId,
                                                 @RequestParam Long inductionId,
                                                 @RequestParam Long assignInductionId) {

        try {
            List<ResultInduction> savedResultInductions = resultService.saveResultInductionList(resultInductions, employeeId, inductionId, assignInductionId);
            return ResponseEntity.ok(savedResultInductions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/employee/{employeeId}/induction/{inductionId}")
    public ResponseEntity<?> getResultsByEmployeeAndInduction(
            @PathVariable Long employeeId,
            @PathVariable Long inductionId) {

        List<ResultInduction> results = resultService.getResultsByEmployeeIdAndInductionId(employeeId, inductionId);

        if (results.isEmpty()) {
            return ResponseEntity.status(404).body("No results found for the given employee and induction IDs.");
        }

        return ResponseEntity.ok(results);
    }

    @GetMapping("/employees/{employeeId}/induction/{inductionId}")
    public ResponseEntity<?> getResultByEmployeeAndInduction(
            @PathVariable Long employeeId,
            @PathVariable Long inductionId) {
    
        return resultService.getResultByEmployeeIdAndInductionId(employeeId, inductionId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("No result found for given employee and induction ID."));
    }
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getResultsByEmployee(@PathVariable Long employeeId) {

        List<ResultInduction> results = resultService.getResultsByEmployeeId(employeeId);

        if (results.isEmpty()) {
            return ResponseEntity.status(404).body("No results found for the given employee ID.");
        }

        return ResponseEntity.ok(results);
    }
}
