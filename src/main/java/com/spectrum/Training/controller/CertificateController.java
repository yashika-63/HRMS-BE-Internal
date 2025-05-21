package com.spectrum.Training.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.spectrum.Training.model.Certification;
import com.spectrum.Training.service.CertificateService;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {


    
    @Autowired
    private CertificateService certificateService;

    /**
     * Create a new certificate based on Employee, Company, Training, and Result IDs.
     * 
     * @param empId      - Employee ID
     * @param companyId  - Company ID
     * @param trainingId - TrainingHRMS ID
     * @param resultId   - ResultTraining ID
     * @param certificate - Certification object (only fields like signature passed from client side)
     * @return saved Certification object
     */
    @PostMapping("/generate")
    public ResponseEntity<Certification> createCertificate(@RequestParam Long empId,
                                                           @RequestParam Long companyId,
                                                           @RequestParam Long trainingId,
                                                           @RequestParam Long resultId,
                                                           @RequestBody Certification certificate) {

        Certification savedCertificate = certificateService.getCertificateDetails(
                empId, companyId, trainingId, resultId, certificate
        );

        return ResponseEntity.ok(savedCertificate);
    }

    // @GetMapping("/employee/{employeeId}/company/{companyId}")
    // public Certification getCertification(
    //         @PathVariable String employeeId,
    //         @PathVariable Long companyId) {
        
    //     try {
    //         return certificateService.findByEmployeeIdAndCompanyId(employeeId, companyId);
    //     } catch (RuntimeException ex) {
    //         throw new ResponseStatusException(
    //             HttpStatus.NOT_FOUND, 
    //             ex.getMessage()
    //         );
    //     }
    // }

    @GetMapping("/by-employee-and-company")
    public ResponseEntity<Certification> getCertificationByEmployeeAndCompany(
            @RequestParam Long employeeId,
            @RequestParam Long companyId) {
        Certification certification = certificateService.certificateServicecertificateService(employeeId, companyId);
        return ResponseEntity.ok(certification);
    }

    @GetMapping("/by-employee")
    public ResponseEntity<Certification> getCertificationByEmployeeId(@RequestParam Long employeeId) {
        Certification certification = certificateService.findCertificationByEmployeeId(employeeId);
        return ResponseEntity.ok(certification);
    }

    
@GetMapping("/{certificateId}")
public ResponseEntity<Certification> getCertificationById(@PathVariable String certificateId) {
 Certification certification = certificateService.findByCertificateID(certificateId);
 return ResponseEntity.ok(certification);
}


 @GetMapping("/training/{trainingId}/employee/{employeeId}")
public ResponseEntity<Certification> getCertificationByTrainingIdAndEmployeeId(
 @PathVariable Long trainingId, @PathVariable Long employeeId) {
 Optional<Certification> certification = certificateService.getCertificationByTrainingIdAndEmployeeId(trainingId, employeeId);
 return certification.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
 }



}


