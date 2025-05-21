package com.spectrum.CTCModuleMain.controller;

import com.spectrum.CTCModuleMain.model.CTCMaster;
import com.spectrum.CTCModuleMain.service.CTCMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ctcMaster")
public class CTCMasterController {

    @Autowired
    private CTCMasterService ctcMasterService;

    // Save multiple CTCMaster entries
@PostMapping("/saveMultiple/{companyId}")
public ResponseEntity<List<CTCMaster>> saveMultipleCTCMasters(
        @PathVariable Long companyId,
        @RequestBody List<CTCMaster> ctcMastersRequest) {
    try {
        List<CTCMaster> savedCTCMasters = ctcMasterService.saveMultipleCTCMasters(companyId, ctcMastersRequest);
        return ResponseEntity.ok(savedCTCMasters);
    } catch (Exception e) {
        return ResponseEntity.status(500).body(null);
    }
}

    // Update CTCMaster by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<CTCMaster> updateCTCMaster(@PathVariable Long id, @RequestBody CTCMaster ctcMasterRequest) {
        try {
            CTCMaster updatedCTCMaster = ctcMasterService.updateCTCMaster(id, ctcMasterRequest.getLabel(), ctcMasterRequest.getCategory(), ctcMasterRequest.getPercentValue());
            return ResponseEntity.ok(updatedCTCMaster);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Get CTCMaster by companyId and category
    @GetMapping("/company/{companyId}/category/{category}")
    public ResponseEntity<List<CTCMaster>> getCTCMastersByCompanyIdAndCategory(@PathVariable Long companyId, @PathVariable String category) {
        List<CTCMaster> ctcMasters = ctcMasterService.getCTCMastersByCompanyIdAndCategory(companyId, category);
        return ResponseEntity.ok(ctcMasters);
    }

    // Get all CTCMaster entries by companyId
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CTCMaster>> getCTCMastersByCompanyId(@PathVariable Long companyId) {
        List<CTCMaster> ctcMasters = ctcMasterService.getCTCMastersByCompanyId(companyId);
        return ResponseEntity.ok(ctcMasters);
    }


    @DeleteMapping("/delete/company/{companyId}/category/{category}")
    public ResponseEntity<String> deleteCTCMastersByCompanyIdAndCategory(
            @PathVariable Long companyId,
            @PathVariable String category) {
        try {
            ctcMasterService.deleteCTCMastersByCompanyIdAndCategory(companyId, category);
            return ResponseEntity.ok("CTC Master records deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting CTC Master records.");
        }
    }
}
