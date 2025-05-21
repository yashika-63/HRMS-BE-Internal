package com.spectrum.CTCModuleMain.controller;

import com.spectrum.CTCModuleMain.service.CTCBreakdownRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/ctcrecord/")

public class CTCBreakdownRecordController {

    private final CTCBreakdownRecordService ctcBreakdownRecordService;

    @Autowired
    public CTCBreakdownRecordController(CTCBreakdownRecordService ctcBreakdownRecordService) {
        this.ctcBreakdownRecordService = ctcBreakdownRecordService;
    }

    // // Endpoint to get CTC Breakdown Records by companyId
    // @GetMapping("/ctc-breakdown-records/{companyId}")
    // public ResponseEntity<List<CTCBreakdownRecord>> getCTCBreakdownRecordsByCompanyId(@PathVariable Long companyId) {
    //     try {
    //         List<CTCBreakdownRecord> records = ctcBreakdownRecordService.getCTCBreakdownRecordsByCompanyId(companyId);
    //         return ResponseEntity.ok(records);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(500).body(null);
    //     }
    // }


    // Endpoint to store Payroll Records for a specific company
    @PostMapping("/generate-payroll/{companyId}")
    public ResponseEntity<String> generatePayrollForCompany(@PathVariable Long companyId) {
        try {
            ctcBreakdownRecordService.storePayrollRecordsForCompany(companyId);
            return ResponseEntity.ok("Payroll records generated successfully for companyId: " + companyId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating payroll records: " + e.getMessage());
        }
    }

}
