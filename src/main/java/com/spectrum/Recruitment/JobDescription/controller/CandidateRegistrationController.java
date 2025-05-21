package com.spectrum.Recruitment.JobDescription.controller;

import com.spectrum.Recruitment.JobDescription.model.CandidateRegistration;
import com.spectrum.Recruitment.JobDescription.model.CandidateUpdateRequest;
import com.spectrum.Recruitment.JobDescription.service.CandidateRegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate")
public class CandidateRegistrationController {

    @Autowired
    private CandidateRegistrationService candidateRegistrationService;

    @PostMapping("/saveAll/{jobDescriptionId}/{companyId}")
    public ResponseEntity<?> saveMultipleCandidates(
            @RequestBody List<CandidateRegistration> candidates,
            @PathVariable Long jobDescriptionId,
            @PathVariable Long companyId) {
        try {
            List<CandidateRegistration> saved = candidateRegistrationService.saveAll(candidates, jobDescriptionId, companyId);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CandidateRegistration>> getByJobDescriptionAndFilters(
        @RequestParam Long jobDescriptionId,
        @RequestParam boolean status,
        @RequestParam boolean shortListed,
        @RequestParam boolean finalizedForInterview,
        @RequestParam boolean selectedCandidate
    ) {
        List<CandidateRegistration> candidates = candidateRegistrationService.getByJobDescriptionAndFilters(
            jobDescriptionId,
            status,
            shortListed,
            finalizedForInterview,
            selectedCandidate
        );
        return ResponseEntity.ok(candidates);
    }

    @PutMapping("/updateMultiple")
    public String updateMultipleCandidates(@RequestBody List<CandidateUpdateRequest> updateRequests) {
        candidateRegistrationService.updateCandidates(updateRequests);
        return "Candidates updated successfully.";
    }
}
