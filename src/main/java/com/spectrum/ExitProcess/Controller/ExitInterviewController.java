package com.spectrum.ExitProcess.Controller;

import com.spectrum.ExitProcess.Model.ExitInterview;
import com.spectrum.ExitProcess.Service.ExitInterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exit-interviews")
public class ExitInterviewController {
    @Autowired
    private  ExitInterviewService exitInterviewService;

    

    // Create a new ExitInterview
    @PostMapping
    public ResponseEntity<ExitInterview> createExitInterview(@RequestBody ExitInterview exitInterview) {
        ExitInterview savedInterview = exitInterviewService.createExitInterview(exitInterview);
        return ResponseEntity.ok(savedInterview);
    }

    // Get all ExitInterviews
    @GetMapping
    public ResponseEntity<List<ExitInterview>> getAllExitInterviews() {
        List<ExitInterview> interviews = exitInterviewService.getAllExitInterviews();
        return ResponseEntity.ok(interviews);
    }

    // Get ExitInterview by ID
    @GetMapping("/{id}")
    public ResponseEntity<ExitInterview> getExitInterviewById(@PathVariable Long id) {
        ExitInterview interview = exitInterviewService.getExitInterviewById(id);
        return ResponseEntity.ok(interview);
    }

    // Update an ExitInterview
    @PutMapping("/{id}")
    public ResponseEntity<ExitInterview> updateExitInterview(
            @PathVariable Long id,
            @RequestBody ExitInterview updatedInterview) {
        ExitInterview interview = exitInterviewService.updateExitInterview(id, updatedInterview);
        return ResponseEntity.ok(interview);
    }

    // Delete an ExitInterview
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExitInterview(@PathVariable Long id) {
        exitInterviewService.deleteExitInterview(id);
        return ResponseEntity.noContent().build();
    }
     @PostMapping("/employee/{employeeId}/exitInterview/{offBoardingId}")
    public ResponseEntity<ExitInterview> createExitInterview(
            @PathVariable Long employeeId,
            @PathVariable Long offBoardingId,
            @RequestBody ExitInterview exitInterview) {
        ExitInterview savedInterview = exitInterviewService.createExitInterview(
                employeeId, 
                offBoardingId, 
                exitInterview
        );
        return ResponseEntity.ok(savedInterview);
    }
    @GetMapping("/offboarding/{offBoardingId}")
    public List<ExitInterview> getExitInterviewsByOffBoardingId(@PathVariable Long offBoardingId) {
        return exitInterviewService.getInterviewsByOffBoardingId(offBoardingId);
    }
    
}
