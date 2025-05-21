package com.spectrum.PreOnboarding.EmployeeVerification.controller;

import com.spectrum.PreOnboarding.EmployeeVerification.model.EmploymentData;
import com.spectrum.PreOnboarding.EmployeeVerification.service.EmploymentDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employment-data")
public class EmploymentDataController {

    @Autowired
    private EmploymentDataService employmentDataService;

    @PostMapping("/save-all/{verificationId}/{preLoginToken}")
    public ResponseEntity<List<EmploymentData>> saveAllEmploymentData(
            @PathVariable Long verificationId,
            @PathVariable String preLoginToken,
            @RequestBody List<EmploymentData> dataList
    ) {
        List<EmploymentData> saved = employmentDataService.saveAllByVerificationAndToken(dataList, verificationId, preLoginToken);
        return ResponseEntity.ok(saved);
    }




    @PutMapping("/update/{id}")
public ResponseEntity<EmploymentData> updateEmploymentData(
        @PathVariable Long id,
        @RequestBody EmploymentData updatedData) {
    EmploymentData updated = employmentDataService.updateEmploymentDataById(id, updatedData);
    return ResponseEntity.ok(updated);
}




@GetMapping("/getByVerificationAndToken")
public ResponseEntity<List<EmploymentData>> getByVerificationAndToken(
        @RequestParam Long verificationId,
        @RequestParam String preLoginToken) {
    List<EmploymentData> dataList = employmentDataService.getByVerificationIdAndPreLoginToken(verificationId, preLoginToken);
    return ResponseEntity.ok(dataList);
}



@PutMapping("/updateVerificationStatus/{id}")
public ResponseEntity<String> updateVerificationStatus(@PathVariable Long id, @RequestParam boolean status) {
    employmentDataService.updateVerificationStatus(id, status);
    return ResponseEntity.ok("Verification status updated successfully.");
}
}
