package com.spectrum.PreOnboarding.EmployeeVerification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.PreOnboarding.EmployeeVerification.model.EducationalData;
import com.spectrum.PreOnboarding.EmployeeVerification.service.EducationalDataService;

@RestController
@RequestMapping("/api/education")
public class EducationalDataController {

    @Autowired
    private EducationalDataService educationalDataService;

    @PostMapping("/saveByTokenAndTicket")
public ResponseEntity<String> saveEducationalData(
        @RequestParam String preLoginToken,
        @RequestParam Long verificationId,
        @RequestBody List<EducationalData> educationalDataList) {
    String result = educationalDataService.saveEducationalData(preLoginToken, verificationId, educationalDataList);
    return ResponseEntity.ok(result);
}




@GetMapping("/getByVerificationAndToken")
public ResponseEntity<List<EducationalData>> getByVerificationIdAndPreLoginToken(
        @RequestParam Long verificationId,
        @RequestParam String preLoginToken) {
    List<EducationalData> data = educationalDataService.getByVerificationIdAndPreLoginToken(verificationId, preLoginToken);
    return ResponseEntity.ok(data);
}


    @PutMapping("/updateVerificationStatus/{id}")
    public ResponseEntity<String> updateVerificationStatus(@PathVariable Long id, @RequestParam boolean status) {
    educationalDataService.updateVerificationStatus(id, status);
    return ResponseEntity.ok("Verification status updated successfully");
}


@PutMapping("/partial-update/{id}")
public ResponseEntity<EducationalData> partiallyUpdateDataById(
        @PathVariable Long id,
        @RequestBody EducationalData educationalData) {
    
    EducationalData updated = educationalDataService.updateEducationalDataPartially(id, educationalData);
    return ResponseEntity.ok(updated);
}


}
