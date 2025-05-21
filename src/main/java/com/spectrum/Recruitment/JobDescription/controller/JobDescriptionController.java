package com.spectrum.Recruitment.JobDescription.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.Recruitment.JobDescription.model.JobDescription;
import com.spectrum.Recruitment.JobDescription.repository.JobDescriptionRepository;
import com.spectrum.Recruitment.JobDescription.service.JobDescriptionService;

@RestController
@RequestMapping("/api/jobdescription")
public class JobDescriptionController {

    @Autowired
    private JobDescriptionService jobDescriptionService;
    
    @Autowired
    private JobDescriptionRepository jobDescriptionRepository;

    @PostMapping("/save/{companyId}")
    public ResponseEntity<JobDescription> saveJobDescription(@RequestBody JobDescription jobDescription,
                                                             @PathVariable Long companyId) {
        JobDescription saved = jobDescriptionService.saveJobDescription(jobDescription, companyId);
        return ResponseEntity.ok(saved);
    }



    @GetMapping("/get-by-company-and-status")
    public ResponseEntity<?> getByCompanyAndStatus(@RequestParam Long companyId, @RequestParam boolean status) {
        try {
            List<JobDescription> jobList = jobDescriptionRepository.findByCompanyIdAndStatus(companyId, status);
            if (jobList.isEmpty()) {
                return ResponseEntity.status(404).body("No job descriptions found for the given company and status.");
            }
            return ResponseEntity.ok(jobList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }



//     @GetMapping("/getByJobKey/{jobKey}")
// public ResponseEntity<?> getJobDescriptionByKey(@PathVariable String jobKey) {
//     try {
//         JobDescription jd = jobDescriptionService.getJobDescriptionByKey(jobKey);
//         return ResponseEntity.ok(jd);
//     } catch (RuntimeException ex) {
//         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//     }
// }

    
@GetMapping("/getByJobKey/{jobKey}")
public ResponseEntity<?> getJobDescriptionByJobKey(@PathVariable String jobKey) {
    try {
        Map<String, Object> jobDetails = jobDescriptionService.getJobDescriptionByKey(jobKey);
        return ResponseEntity.ok(jobDetails);
    } catch (RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }
}
}
