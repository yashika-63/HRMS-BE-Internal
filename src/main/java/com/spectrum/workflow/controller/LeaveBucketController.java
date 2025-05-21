package com.spectrum.workflow.controller;

import com.spectrum.workflow.model.LeaveBucket;
import com.spectrum.workflow.service.LeaveBucketService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave-buckets")
public class LeaveBucketController {

    @Autowired
    private LeaveBucketService leaveBucketService;

    // @PostMapping("/save")
    // public ResponseEntity<LeaveBucket> saveLeaveBucket(@RequestBody LeaveBucket leaveBucket, @RequestParam(required=false) Long companyId) {
    //     LeaveBucket savedLeaveBucket = leaveBucketService.saveLeaveBucket(leaveBucket, companyId);
    //     return ResponseEntity.ok(savedLeaveBucket);
    // }

    @PostMapping("/save/{companyId}")
    public ResponseEntity<?> saveLeaveBuckets(
            @PathVariable Long companyId,
            @RequestBody List<LeaveBucket> leaveBuckets) {

        try {
            List<LeaveBucket> savedBuckets = leaveBucketService.saveLeaveBuckets(leaveBuckets, companyId);
            return ResponseEntity.ok(savedBuckets);
            
        } catch (RuntimeException ex) {
            // Simple error response without DTO
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

//     @GetMapping("/leaves/{completionStatus}/{companyId}/{status}")
// public ResponseEntity<List<LeaveBucket>> getLeaveBuckets(
//         @PathVariable boolean completionStatus,
//         @PathVariable Long companyId,
//         @PathVariable boolean status) {
//             System.out.println("completionStatus"+completionStatus);
//         Boolean value=completionStatus;
//         System.out.println("value:"+value);
//     List<LeaveBucket> result = leaveBucketService.getByLeaveBucket(value, companyId, status);
//     return ResponseEntity.ok(result);
// }
// @GetMapping("/by-criteria")
//     public ResponseEntity<List<LeaveBucket>> getLeaveBucketByCriteria(
//             @RequestParam boolean completionStatus,
//             @RequestParam Long companyID,
//             @RequestParam boolean status,
//             @RequestParam String employee_type) {

//         List<LeaveBucket> leaveBuckets = leaveBucketService.getByLeaveBucket(completionStatus, companyID, status, employee_type);
        
//         return new ResponseEntity<>(leaveBuckets, HttpStatus.OK);


        
//     }

    //  @GetMapping("/by-criteria")
    // public ResponseEntity<?> getLeaveBucketByCriteria(
    //         @RequestParam boolean completionStatus,
    //         @RequestParam Long companyID,
    //         @RequestParam boolean status,
    //         @RequestParam String employee_type) {

    //     List<LeaveBucket> leaveBuckets = leaveBucketService.getByLeaveBucket(completionStatus, companyID, status, employee_type);

    //     // For "intern" or "probation", return only selected fields
    //     if (employee_type.equalsIgnoreCase("intern") || employee_type.equalsIgnoreCase("probation")|| employee_type.equalsIgnoreCase("trainee")) {
    //         List<Map<String, Object>> filtered = leaveBuckets.stream()
    //                 .map(lb -> {
    //                     Map<String, Object> map = new HashMap<>();
    //                     map.put("id", lb.getId());
    //                     map.put("status", lb.getStatus());
    //                     map.put("probrationAllowedHolidays", lb.getProbrationAllowedHolidays());
    //                     return map;
    //                 })
    //                 .collect(Collectors.toList());
    //         return new ResponseEntity<>(filtered, HttpStatus.OK);
    //     }

    //     // For all other employee types, return full data
    //     return new ResponseEntity<>(leaveBuckets, HttpStatus.OK);
    // }

    @GetMapping("/by-criteria")
    public ResponseEntity<?> getLeaveBucketByCriteria(
            @RequestParam boolean completionStatus,
            @RequestParam Long companyID,
            @RequestParam boolean status,
            @RequestParam String employee_type) {

        // ✅ Validate completionStatus for specific employee types
        boolean isSpecialType = employee_type.equalsIgnoreCase("intern")
                || employee_type.equalsIgnoreCase("probation")
                || employee_type.equalsIgnoreCase("trainee");

        if (isSpecialType && !completionStatus) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Completion status must be true for intern/probation/trainee.");
        }

        if (!isSpecialType && completionStatus) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Completion status must be false for permanent/full-time/contract employees.");
        }

        // ✅ Call the service method
        List<LeaveBucket> leaveBuckets =  leaveBucketService
                .getByLeaveBucket(completionStatus, companyID, status, employee_type);

        // ✅ Conditionally return trimmed response for special types
        if (isSpecialType) {
            List<Map<String, Object>> filtered = leaveBuckets.stream()
                    .map(lb -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", lb.getId());
                        map.put("status", lb.getStatus());
                        map.put("probrationAllowedHolidays", lb.getProbrationAllowedHolidays());
                        return map;
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(filtered, HttpStatus.OK);
        }

        // ✅ Return full data for others
        return new ResponseEntity<>(leaveBuckets, HttpStatus.OK);
    }

    @GetMapping("/leave-buckets")
    public ResponseEntity<?> getLeaveBucketByCriteria(
            @RequestParam Long companyID,
            @RequestParam String employeeType,
            @RequestParam boolean status,
            @RequestParam boolean fullData) {

        // Fetch the filtered list of LeaveBucket
        List<LeaveBucket> leaveBuckets = leaveBucketService.getByCriteria(companyID, employeeType, status, fullData);

        // Return the response with the appropriate data
        return new ResponseEntity<>(leaveBuckets, HttpStatus.OK);
    }
    @GetMapping("/active/{companyId}")
    public ResponseEntity<?> getActiveLeaveBucketsByCompanyId(@PathVariable("companyId") Long companyId) {
        try {
            List<LeaveBucket> leaveBuckets = leaveBucketService.getActiveLeaveBuckets(companyId);
 
            if (leaveBuckets.isEmpty()) {
                return ResponseEntity
                        .status(404)
                        .body("No active leave buckets found for company ID: " + companyId);
            }
 
            return ResponseEntity.ok(leaveBuckets);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid request: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity
                    .status(500)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }
}
