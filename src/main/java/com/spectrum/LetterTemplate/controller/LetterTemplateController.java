package com.spectrum.LetterTemplate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.LetterTemplate.model.LetterTemplate;
import com.spectrum.LetterTemplate.service.LetterTemplateService;

@RestController
@RequestMapping("/api/letter-template")
public class LetterTemplateController {

    @Autowired
    private LetterTemplateService letterTemplateService;

    @PostMapping("/save/{companyId}")
    public ResponseEntity<LetterTemplate> saveTemplate(
            @RequestBody LetterTemplate template,
            @PathVariable Long companyId) {
        LetterTemplate savedTemplate = letterTemplateService.saveLetterTemplate(template, companyId);
        return ResponseEntity.ok(savedTemplate);
    }



    
    @GetMapping("/active")
    public ResponseEntity<?> getActiveTemplate(
        @RequestParam Long companyId,
        @RequestParam String templateIdentityKey) {

        return letterTemplateService
            .getActiveTemplateByCompanyAndKey(companyId, templateIdentityKey)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }



    @PutMapping("/update/{id}")
public ResponseEntity<?> updateLetterTemplate(
    @PathVariable Long id,
    @RequestBody LetterTemplate updatedTemplate) {

    try {
        LetterTemplate savedTemplate = letterTemplateService.updateLetterTemplate(id, updatedTemplate);
        return ResponseEntity.ok(savedTemplate);
    } catch (RuntimeException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}




@PutMapping("/updateStatus/{id}")
public ResponseEntity<?> updateStatus(
    @PathVariable Long id,
    @RequestParam boolean status) {

    try {
        LetterTemplate updatedTemplate = letterTemplateService.updateLetterTemplateStatus(id, status);
        return ResponseEntity.ok(updatedTemplate);
    } catch (RuntimeException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}




@GetMapping("/by-company-and-status")
public ResponseEntity<?> getTemplatesByCompanyAndStatus(
        @RequestParam Long companyId,
        @RequestParam boolean status) {
    
    try {
        return ResponseEntity.ok(
            letterTemplateService.getTemplatesByCompanyIdAndStatus(companyId, status)
        );
    } catch (RuntimeException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}

}
