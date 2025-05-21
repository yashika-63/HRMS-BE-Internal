package com.spectrum.CTCModuleMain.controller;

import com.spectrum.CTCModuleMain.model.CTCBreakdownHeader;
import com.spectrum.CTCModuleMain.model.CTCBreakdownRecord;
import com.spectrum.CTCModuleMain.model.CTCBreakdownRequest;
import com.spectrum.CTCModuleMain.service.CTCBreakdownHeaderService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ctcmain")
public class CTCBreakdownHeaderController {

    @Autowired
    private CTCBreakdownHeaderService ctcBreakdownHeaderService;

    // @PostMapping("/header/save/{employeeId}/{companyId}")
    // public ResponseEntity<CTCBreakdownHeader> saveCTCBreakdown(
    //         @PathVariable Long employeeId,
    //         @PathVariable Long companyId,
    //         @RequestBody CTCBreakdownRequest request) {

    //     CTCBreakdownHeader savedHeader = ctcBreakdownHeaderService.saveCTCBreakdown(
    //             employeeId, companyId, request.getHeader(), request.getStaticBreakdowns(),
    //             request.getVariableBreakdowns());

    //     return ResponseEntity.ok(savedHeader);
    // }
    // @PostMapping("/header/save/{employeeId}/{companyId}")
    // public ResponseEntity<CTCBreakdownHeader> saveCTCBreakdown(
    //         @PathVariable Long employeeId,
    //         @PathVariable Long companyId,
    //         @RequestBody CTCBreakdownRequest request) {

    //     CTCBreakdownHeader savedHeader = ctcBreakdownHeaderService.saveCTCBreakdown(
    //             employeeId, companyId, request.getHeader(), request.getStaticBreakdowns(),
    //             request.getVariableBreakdowns());

    //     return ResponseEntity.ok(savedHeader);
    // }
    @PostMapping("/header/save/{employeeId}/{companyId}")
    public ResponseEntity<CTCBreakdownHeader> saveCTCBreakdownAndGenerateRecords(
            @PathVariable Long employeeId,
            @PathVariable Long companyId,
            @RequestBody CTCBreakdownRequest request) {
    
    
        CTCBreakdownHeader savedHeader = ctcBreakdownHeaderService.saveCTCBreakdownAndGenerateRecords(
                employeeId, companyId, request.getHeader(), request.getStaticBreakdowns(),
                request.getVariableBreakdowns());
    
    
        return ResponseEntity.ok(savedHeader);
    }
    
    
    // Method to trigger the calculation and check status for all active CTC
    // Breakdown headers
    // @GetMapping("/header/check/status")
    // public ResponseEntity<String> checkAndCalculateCTCAmounts() {
    //     try {
    //         // Trigger the calculation (it will also check if the status is active)
    //         ctcBreakdownHeaderService.calculateAmountsAndCheckStatusForAllEmployees();
    // @GetMapping("/header/check/status")
    // public ResponseEntity<String> checkAndCalculateCTCAmounts() {
    //     try {
    //         // Trigger the calculation (it will also check if the status is active)
    //         ctcBreakdownHeaderService.calculateAmountsAndCheckStatusForAllEmployees();

    //         // Returning a success message
    //         return ResponseEntity.ok("CTC calculations completed successfully. Check the server logs for amounts.");
    //     } catch (Exception e) {
    //         // If there’s an error during calculation, return a failure response
    //         return ResponseEntity.status(500).body("Error during CTC calculation: " + e.getMessage());
    //     }
    // }
    //         // Returning a success message
    //         return ResponseEntity.ok("CTC calculations completed successfully. Check the server logs for amounts.");
    //     } catch (Exception e) {
    //         // If there’s an error during calculation, return a failure response
    //         return ResponseEntity.status(500).body("Error during CTC calculation: " + e.getMessage());
    //     }
    // }

    // @GetMapping("/test-ctc-breakdown")
    // public String testCTCBreakdown() {
    //     ctcBreakdownHeaderService.calculateAmountsAndCheckStatusForAllEmployees();
    //     return "CTC Breakdown calculation triggered!";
    // }
    // @GetMapping("/test-ctc-breakdown")
    // public String testCTCBreakdown() {
    //     ctcBreakdownHeaderService.calculateAmountsAndCheckStatusForAllEmployees();
    //     return "CTC Breakdown calculation triggered!";
    // }

    // Endpoint to calculate and save CTC breakdown amounts for a specific company
    @GetMapping("/calculateAmounts/{companyId}")
    public ResponseEntity<String> calculateCTCForCompany(@PathVariable Long companyId) {
        try {
            // Call service method to calculate and save amounts
            ctcBreakdownHeaderService.calculateAmountsAndCheckStatusForAllEmployees(companyId);
            return ResponseEntity.ok("CTC breakdown calculation completed for company ID " + companyId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calculating CTC breakdown: " + e.getMessage());
        }
    }

    // // Endpoint to calculate and save CTC breakdown amounts for a specific employee
    // @GetMapping("/calculateAmounts/employee/{employeeId}")
    // public ResponseEntity<String> calculateCTCForEmployee(@PathVariable Long employeeId) {
    //     try {
    //         // Call service method to calculate and save amounts for the specified employee
    //         ctcBreakdownHeaderService.calculateAmountsForEmployee(employeeId);
    //         return ResponseEntity.ok("CTC breakdown calculation completed for employee ID " + employeeId);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(500)
    //                 .body("Error calculating CTC breakdown for employee ID " + employeeId + ": " + e.getMessage());
    //     }
    // }




     // Endpoint to delete a specific CTC breakdown record by its ID if ctcStatus is false
     @DeleteMapping("/header/deleteById/{ctcBreakdownHeaderId}")
     public ResponseEntity<String> deleteCTCBreakdownById(@PathVariable Long ctcBreakdownHeaderId) {
         try {
             // Call service method to perform the deletion
             boolean isDeleted = ctcBreakdownHeaderService.deleteCTCBreakdownById(ctcBreakdownHeaderId);
 
             // Return appropriate response based on deletion result
             if (isDeleted) {
                 return ResponseEntity.ok("CTC record with ID " + ctcBreakdownHeaderId + " and ctcStatus=false has been deleted successfully.");
             } else {
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No CTC record found with ID " + ctcBreakdownHeaderId + " and ctcStatus=false.");
             }
         } catch (Exception e) {
             // Handle exceptions and return internal server error response
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting CTC record: " + e.getMessage(), e);
         }
     }





      // 1. Get records by companyId where ctcStatus = true
    @GetMapping("/byCompany/{companyId}")
    public ResponseEntity<List<CTCBreakdownHeader>> getByCompanyIdAndCtcStatusTrue(@PathVariable Long companyId) {
        List<CTCBreakdownHeader> ctcBreakdownHeaders = ctcBreakdownHeaderService.getByCompanyIdAndCtcStatusTrue(companyId);
        return ResponseEntity.ok(ctcBreakdownHeaders);
    }

    // 2. Get records by employeeId where ctcStatus = true
    @GetMapping("/byEmployee/ctcStatusTrue/{employeeId}")
    public ResponseEntity<?> getByEmployeeIdAndCtcStatusTrue(@PathVariable Long employeeId) {
        Optional<CTCBreakdownHeader> ctcBreakdownHeader = ctcBreakdownHeaderService.getByEmployeeIdAndCtcStatusTrue(employeeId);
    
        if (ctcBreakdownHeader.isPresent()) {
            return ResponseEntity.ok(ctcBreakdownHeader.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("CTC Breakdown Header not found for employeeId: " + employeeId);
        }
    }
    

    // 3. Get records by employeeId where ctcStatus = false
    @GetMapping("/byEmployee/ctcStatusFalse/{employeeId}")
    public ResponseEntity<List<CTCBreakdownHeader>> getByEmployeeIdAndCtcStatusFalse(@PathVariable Long employeeId) {
        List<CTCBreakdownHeader> ctcBreakdownHeaders = ctcBreakdownHeaderService.getByEmployeeIdAndCtcStatusFalse(employeeId);
        return ResponseEntity.ok(ctcBreakdownHeaders);
    }





    @PostMapping("/createRecordsForCompany/{companyId}")
    public ResponseEntity<List<CTCBreakdownRecord>> createRecordsForCompany(@PathVariable Long companyId) {
        try {
            List<CTCBreakdownRecord> records = ctcBreakdownHeaderService.createCTCRecordsForAllEmployees(companyId);
            return ResponseEntity.ok(records);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }



    @PostMapping("/createRecords/{companyId}")
    public ResponseEntity<?> createCTCRecordsForSpecificMonthAndYear(
            @PathVariable Long companyId,
            @RequestParam int month,
            @RequestParam int year) {
        try {
            List<CTCBreakdownRecord> records = ctcBreakdownHeaderService.createCTCRecordsForSpecificMonthAndYear(companyId, month, year);
            return ResponseEntity.ok(records);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    
}

