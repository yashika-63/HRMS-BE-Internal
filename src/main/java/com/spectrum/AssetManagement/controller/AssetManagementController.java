package com.spectrum.AssetManagement.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.AssetManagement.model.AssetManagement;
import com.spectrum.AssetManagement.model.AssetManagementDetails;
import com.spectrum.AssetManagement.model.AssetManagementDetailsUpdateDTO;
import com.spectrum.AssetManagement.repository.AssetManagementRepository;
import com.spectrum.AssetManagement.service.AssetManagementService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/asset")
public class AssetManagementController {

    @Autowired
    private AssetManagementService assetManagementService;


    @Autowired
    private AssetManagementRepository assetManagementRepository;

    @PostMapping("/save/{employeeId}")
    public ResponseEntity<String> saveAssetWithDetails(
            @PathVariable Long employeeId,
            @RequestBody List<AssetManagementDetails> details) {

        String message = assetManagementService.saveAssetWithDetails(employeeId, details);
        return ResponseEntity.ok(message);
    }



    
    @GetMapping("/by-employee")
    public ResponseEntity<List<AssetManagement>> getByEmployeeAndAction(
            @RequestParam Long employeeId,
            @RequestParam boolean employeeAction) {

        List<AssetManagement> list = assetManagementService.getByEmployeeIdAndEmployeeAction(employeeId, employeeAction);
        return ResponseEntity.ok(list);
    }






    @PutMapping("/send-to-employee/{id}")
    public ResponseEntity<AssetManagement> sendForEmployeeAction(@PathVariable Long id) {
        AssetManagement updated = assetManagementService.updateSentForEmployeeAction(id);
        return ResponseEntity.ok(updated);
    }



    @GetMapping("/by-employee-action")
    public ResponseEntity<List<AssetManagement>> getAssetsByEmployeeAndAction(
            @RequestParam Long employeeId) {
        List<AssetManagement> list = assetManagementService.getByEmployeeIdAndSentForActionTrue(employeeId);
        return ResponseEntity.ok(list);
    }


    @PutMapping("/updateAssetAction/{id}")
    public ResponseEntity<String> updateAssetAction(
            @PathVariable Long id,
            @RequestParam String actiontakenBy
          ) {
    
        String response = assetManagementService.updateAssetAction(id, actiontakenBy);
        return ResponseEntity.ok(response);
    }
    

    @PutMapping("/updateDescriptions/{employeeId}")
    public ResponseEntity<?> updateDescriptionsByEmployee(
            @PathVariable Long employeeId,
            @RequestBody List<AssetManagementDetailsUpdateDTO> updateList) {
    
        try {
            assetManagementService.updateDescriptions(updateList, employeeId);
            return ResponseEntity.ok("Descriptions updated successfully.");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
    



    @GetMapping("/getByEmployee/{employeeId}")
public ResponseEntity<List<AssetManagementDetails>> getByEmployeeId(@PathVariable Long employeeId) {
    List<AssetManagementDetails> details = assetManagementService.getByEmployeeId(employeeId);
    return ResponseEntity.ok(details);
}

    


@GetMapping("/getWithDetailsByEmployee/{employeeId}")
public ResponseEntity<List<AssetManagement>> getByEmployeeIdWithDetails(@PathVariable Long employeeId) {
    List<AssetManagement> result = assetManagementService.getByEmployeeIdWithDetails(employeeId);
    return ResponseEntity.ok(result);
}
@PutMapping("/{id}/submit")
    public ResponseEntity<String> submitAsset(@PathVariable Long id) {
        if(id==null){
            throw new RuntimeException("Asset Id is null");
        }
        try {
           
            assetManagementService.updateSubmittedToTrue(id);
            return ResponseEntity.ok("Asset submitted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
