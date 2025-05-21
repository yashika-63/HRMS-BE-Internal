package com.spectrum.PreOnboarding.EmployeeVerification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.PreOnboarding.EmployeeVerification.model.Employeedata;
import com.spectrum.PreOnboarding.EmployeeVerification.model.EmploymentData;
import com.spectrum.PreOnboarding.EmployeeVerification.service.EmployeedataService;

@RestController
@RequestMapping("/api/employeedata")
public class EmployeedataController {

    @Autowired
    private EmployeedataService employeedataService;

    @PostMapping("/save")
    public ResponseEntity<?> saveEmployeedata(
            @RequestBody Employeedata employeedata,
            @RequestParam Long preRegistrationId,
            @RequestParam Long verificationTicketId,
            @RequestParam String preLoginToken) {
        try {
            Employeedata saved = employeedataService.saveEmployeedata(employeedata, preRegistrationId, verificationTicketId, preLoginToken);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/get/{id}")
public ResponseEntity<?> getEmployeeDataById(@PathVariable Long id) {
    try {
        Employeedata data = employeedataService.getEmployeeDataById(id);
        return ResponseEntity.ok(data);
    } catch (RuntimeException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}

@GetMapping("/get-by-prereg")
public ResponseEntity<?> getEmployeeDataByPreRegistration(
        @RequestParam Long preRegistrationId,
        @RequestParam String preLoginToken) {
    try {
        Employeedata data = employeedataService.getByPreRegistrationIdAndToken(preRegistrationId, preLoginToken);
        return ResponseEntity.ok(data);
    } catch (RuntimeException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}




@PutMapping("/update/{id}")
public ResponseEntity<Employeedata> updateEmployeeData(
        @PathVariable Long id,
        @RequestBody Employeedata updatedData) {
    Employeedata updated = employeedataService.updateEmployeedata(id, updatedData);
    return ResponseEntity.ok(updated);
}


//this is the api to make verification status = true

@PutMapping("/update-verification-status/{id}")
    public ResponseEntity<Employeedata> updateVerificationStatus(@PathVariable Long id) {
        Employeedata updated = employeedataService.updateVerificationStatusToTrue(id);
        return ResponseEntity.ok(updated);
    }




}
