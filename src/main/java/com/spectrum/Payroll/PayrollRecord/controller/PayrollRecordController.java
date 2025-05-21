package com.spectrum.Payroll.PayrollRecord.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.Payroll.PayrollRecord.model.PayrollRecord;
import com.spectrum.Payroll.PayrollRecord.service.PayrollRecordService;
@RestController
@RequestMapping("/api/PayrollRecord")
public class PayrollRecordController {

    @Autowired
    private PayrollRecordService payrollRecordService;


    @PostMapping("/storePayrollRecords/{companyId}")
    public ResponseEntity<String> storePayrollRecords(@PathVariable Long companyId) {
        try {
            payrollRecordService.storeMergedPayrollRecordsForCompany(companyId);
            return ResponseEntity.ok("Payroll records stored successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error storing payroll records.");
        }
    }


  // New method to get payroll records by companyId with status false and proceedForPayrollStatus false
    @GetMapping("/getPayrollRecords")
    public ResponseEntity<List<PayrollRecord>> getPayrollRecordsByCompanyIdAndStatus(@RequestParam Long companyId) {
        try {
            List<PayrollRecord> payrollRecords = payrollRecordService.getPayrollRecordsByCompanyIdAndStatus(companyId);
            if (payrollRecords.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(payrollRecords);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
        
    }


    @PostMapping("/process")
    public ResponseEntity<String> processPayrollRecords() {
        try {
            payrollRecordService.processPayrollRecords();
            return ResponseEntity.ok("Payroll records processed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing payroll records: " + e.getMessage());
        }
    }


    // New method to get payroll records by companyId with status true and proceedForPayrollStatus false
@GetMapping("/getPayrollRecordsWithStatusTrueAndPayrollStatusFalse")
public ResponseEntity<List<PayrollRecord>> getPayrollRecordsByCompanyIdWithStatusTrueAndPayrollStatusFalse(@RequestParam Long companyId) {
    try {
        List<PayrollRecord> payrollRecords = payrollRecordService.getPayrollRecordsByCompanyIdWithStatusTrueAndPayrollStatusFalse(companyId);
        if (payrollRecords.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payrollRecords);
    } catch (Exception e) {
        return ResponseEntity.status(500).body(null);
    }
}

@GetMapping("/getPayrollRecordsWithStatusTrueAndPayrollStatusTrue")
public ResponseEntity<List<PayrollRecord>> getPayrollRecordsByCompanyIdWithStatusTrueAndPayrollStatusTrue(@RequestParam Long companyId) {
    try {
        List<PayrollRecord> payrollRecords = payrollRecordService.getPayrollRecordsByCompanyIdWithStatusTrueAndPayrollStatusTrue(companyId);
        if (payrollRecords.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payrollRecords);
    } catch (Exception e) {
        return ResponseEntity.status(500).body(null);
    }
}




@GetMapping("/getPayrollRecordsByCompanyIdAndDate")
public ResponseEntity<List<PayrollRecord>> getPayrollRecordsByCompanyIdAndDate(
        @RequestParam Long companyId,
        @RequestParam int year,
        @RequestParam int month) {
    try {
        // Validate year and month
        if (year <= 0 || month < 1 || month > 12) {
            return ResponseEntity.badRequest().body(null);
        }

        List<PayrollRecord> payrollRecords = payrollRecordService.getPayrollRecordsByCompanyIdAndDate(companyId, year,
                month);
        if (payrollRecords.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payrollRecords);
    } catch (Exception e) {
        return ResponseEntity.status(500).body(null);
    }
}

@GetMapping("/getPayrollRecordsByCompanyIdAndDateStatusFalse")
public ResponseEntity<?> getPayrollRecordsByCompanyIdAndDateStatusFalse(
        @RequestParam Long companyId,
        @RequestParam int year,
        @RequestParam int month) {
    try {
        // Validate year and month
        if (!isValidYearAndMonth(year, month)) {
            return ResponseEntity.badRequest().body("Invalid year or month provided.");
        }

        List<PayrollRecord> payrollRecords = payrollRecordService.getPayrollRecordsByCompanyIdAndDateForFalse(companyId,
                year, month);
        if (payrollRecords.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payrollRecords);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error fetching payroll records: " + e.getMessage());
    }
}

private boolean isValidYearAndMonth(int year, int month) {
    return year > 0 && month >= 1 && month <= 12;
}



   @PutMapping("/updateProceedStatus/{id}")
   public ResponseEntity<String> updateProceedForPayrollStatus(@PathVariable Long id) {
       String result = payrollRecordService.updateProceedForPayrollStatus(id);
       return ResponseEntity.ok(result);
   }










   ///////////////////////////////////////////////////////////
   /// 
   /// 
   /// 
   /// 
   /// 
   /// 
   /// 
   /// 

   // payroll process method's 

   @PostMapping("/processByCompany/{companyId}")
public ResponseEntity<String> processPayrollRecordsByCompanyId(@PathVariable Long companyId) {
    try {
        payrollRecordService.processPayrollRecordsByCompanyId(companyId);
        return ResponseEntity.ok("Payroll records processed successfully for companyId: " + companyId);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error processing payroll records for companyId " + companyId + ": " + e.getMessage());
    }
}




@PostMapping("/processByPayrollRecord/{payrollRecordId}")
public ResponseEntity<String> processPayrollRecordById(@PathVariable Long payrollRecordId) {
    try {
        payrollRecordService.processPayrollRecordById(payrollRecordId);
        return ResponseEntity.ok("Payroll record processed successfully for payrollRecordId: " + payrollRecordId);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error processing payroll record for payrollRecordId " + payrollRecordId + ": " + e.getMessage());
    }
}





@PutMapping("/updateFields/{id}")
public ResponseEntity<String> updateSpecificFields(
        @PathVariable Long id, 
        @RequestBody PayrollRecord updatedFields) {
    try {
        payrollRecordService.updateSpecificFields(id, updatedFields);
        return ResponseEntity.ok("Fields updated successfully for PayrollRecord ID: " + id);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error updating fields for PayrollRecord ID " + id + ": " + e.getMessage());
    }
}




 @PostMapping("/processRecords")
    public ResponseEntity<String> processPayrollRecords(
            @RequestParam Long companyId,
            @RequestParam int month,
            @RequestParam int year) {
        try {
            payrollRecordService.storeMergedPayrollRecordsForCompany(companyId, month, year);
            return ResponseEntity.ok("Payroll records processed and stored successfully for Company ID: " + companyId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing payroll records: " + e.getMessage());
        }
    }

    @PostMapping("/merge")
    public ResponseEntity<String> storeMergedPayrollRecords(
            @RequestParam Long companyId,
            @RequestParam int month,
            @RequestParam int year) {
        try {
            // Trigger the service method to process and store payroll records
            payrollRecordService.storeMergedPayrollRecordsForCompanyForMonthAndYear(companyId, month, year);

            return ResponseEntity.ok("Merged payroll records stored successfully for Company ID: "
                    + companyId + ", Month: " + month + ", Year: " + year);
        } catch (Exception e) {
            // Log and return error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while merging payroll records: " + e.getMessage());
        }
    }







    @GetMapping("/company/{companyId}")
public ResponseEntity<List<PayrollRecord>> getPayrollRecords(
        @PathVariable Long companyId,
        @RequestParam boolean proceedForPayment,
        @RequestParam int year,
        @RequestParam int month) {
    List<PayrollRecord> records = payrollRecordService.getPayrollRecordsByCompanyAndStatusAndDate(companyId, proceedForPayment, year, month);
    return ResponseEntity.ok(records);
}
}
