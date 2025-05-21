package com.spectrum.CTCModuleMain.controller;

import com.spectrum.CTCModuleMain.model.CTCBreakdown;
import com.spectrum.CTCModuleMain.service.CTCBreakdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/ctc")
public class CTCBreakdownController {

    @Autowired
    private CTCBreakdownService ctcBreakdownService;

    // POST method to save CTC data by employeeId
    @PostMapping("/save/{employeeId}")
    public ResponseEntity<CTCBreakdown> saveCTCData(@PathVariable Long employeeId,
            @RequestBody CTCBreakdown ctcBreakdown) {
        try {
            CTCBreakdown savedData = ctcBreakdownService.saveCTCData(employeeId, ctcBreakdown);
            return ResponseEntity.ok(savedData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Get CTCBreakdown by ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<CTCBreakdown> getCTCBreakdownById(@PathVariable Long id) {
        Optional<CTCBreakdown> ctcBreakdown = ctcBreakdownService.getCTCBreakdownById(id);
        return ctcBreakdown.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get CTCBreakdown by Employee ID
    @GetMapping("/getByEmployeeId/{employeeId}")
    public ResponseEntity<CTCBreakdown> getCTCBreakdownByEmployeeId(@PathVariable Long employeeId) {
        Optional<CTCBreakdown> ctcBreakdown = ctcBreakdownService.getCTCBreakdownByEmployeeId(employeeId);
        return ctcBreakdown.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update CTCBreakdown by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<CTCBreakdown> updateCTCBreakdown(@PathVariable Long id,
            @RequestBody CTCBreakdown updatedCTC) {
        return ResponseEntity.ok(ctcBreakdownService.updateCTCBreakdown(id, updatedCTC));
    }

    // Delete CTCBreakdown by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCTCBreakdown(@PathVariable Long id) {
        ctcBreakdownService.deleteCTCBreakdown(id);
        return ResponseEntity.noContent().build();
    }
}
