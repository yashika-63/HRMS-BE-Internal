

package com.spectrum.PreOnboarding.PreRegistration.controller;

import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;
import com.spectrum.PreOnboarding.PreRegistration.service.PreRegistrationService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preregistration")
public class PreRegistrationController {

    @Autowired
    private PreRegistrationService preRegistrationService;


    @PostMapping("/save/{companyId}/{reportingPersonId}")
    public ResponseEntity<?> savePreRegistration(
            @PathVariable Long companyId, 
            @PathVariable Long reportingPersonId, 
            @RequestBody PreRegistration preRegistration) {
        try {
            PreRegistration savedData = preRegistrationService.savePreRegistration(preRegistration, companyId, reportingPersonId);
            return new ResponseEntity<>(savedData, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving pre-registration data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String preLoginToken) {
        PreRegistration preRegistration = preRegistrationService.login(email, preLoginToken);
        return ResponseEntity.ok(preRegistration);
    }

    @GetMapping("/getByCompanyAndReportingPersonOffferNotMade")
    public ResponseEntity<?> getPreRegistrations(
            @RequestParam Long companyId,
            @RequestParam Long reportingPersonId) {
        
        List<PreRegistration> preRegistrations = preRegistrationService.getPreRegistrations(companyId, reportingPersonId);
        
        if (preRegistrations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No PreRegistration records found matching criteria");
        }
        
        return ResponseEntity.ok(preRegistrations);
    }

    @GetMapping("/getByCompanyAndReportingPersonOfferMade")
    public ResponseEntity<?> getPreRegistrationsOfferMade(
            @RequestParam Long companyId,
            @RequestParam Long reportingPersonId) {
        
        List<PreRegistration> preRegistrations = preRegistrationService.getPreRegistrationsOfferMade(companyId, reportingPersonId);
        
        if (preRegistrations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No PreRegistration records found matching criteria");
        }
        
        return ResponseEntity.ok(preRegistrations);
    }
    @GetMapping("/getByCompanyAndStatus")
    public ResponseEntity<List<PreRegistration>> getByCompanyIdAndStatus(
            @RequestParam Long companyId,
            @RequestParam boolean status) {
        List<PreRegistration> list = preRegistrationService.getByCompanyIdAndStatus(companyId, status);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getByCompanyOfferNotMade")
    public ResponseEntity<?> getPreRegistrations(@RequestParam Long companyId) {
        List<PreRegistration> preRegistrations = preRegistrationService.getPreRegistrations(companyId);
        
        if (preRegistrations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No PreRegistration records found matching criteria");
        }
        
        return ResponseEntity.ok(preRegistrations);
    }
    
    @GetMapping("/getByCompanyOfferMade")
    public ResponseEntity<?> getPreRegistrationsOfferMade(@RequestParam Long companyId) {
        List<PreRegistration> preRegistrations = preRegistrationService.getPreRegistrationsOfferMade(companyId);
        
        if (preRegistrations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No PreRegistration records found matching criteria");
        }
        
        return ResponseEntity.ok(preRegistrations);
    }
    






    @GetMapping("/by-company-month-year")
public ResponseEntity<Page<PreRegistration>> getByCompanyIdAndMonthYear(
        @RequestParam Long companyId,
        @RequestParam int month,
        @RequestParam int year,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size) {

    Page<PreRegistration> result = preRegistrationService.getByCompanyMonthYear(companyId, month, year, page, size);
    return ResponseEntity.ok(result);
}

}
