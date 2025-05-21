package com.spectrum.ExitProcess.Controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spectrum.ExitProcess.Model.KnowledgeTransfer;
import com.spectrum.ExitProcess.Service.KTService;
import com.spectrum.ExitProcess.Service.OTPService;
import com.spectrum.model.Employee;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/knowledgeTransfer")
public class KnowledgeTransferController {

    @Autowired
    private KTService ktService;

    @Autowired
    private OTPService otpService;

    @GetMapping("/all")
    public ResponseEntity<List<KnowledgeTransfer>> getAllKnowledgeTransfers() {
        List<KnowledgeTransfer> knowledgeTransfers = ktService.getAllKnowledgeTransfers();
        return ResponseEntity.ok(knowledgeTransfers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeTransfer> getKnowledgeTransferById(@PathVariable Long id) {
        KnowledgeTransfer knowledgeTransfer = ktService.getKnowledgeTransferById(id);
        return ResponseEntity.ok(knowledgeTransfer);
    }

    // @PostMapping("/save")
    // public ResponseEntity<?> createKnowledgeTransfer(
    //         @RequestParam Long offBoardingId,
    //         @RequestParam Long employeeById,
    //         @RequestParam List<Long> employeeToIds,
    //         @RequestBody List<KnowledgeTransfer> knowledgeTransfers) {
        
    //     try {
    //         // Validate at least one template is provided
    //         if (knowledgeTransfers == null || knowledgeTransfers.isEmpty()) {
    //             return ResponseEntity.badRequest().body("At least one knowledge transfer template must be provided");
    //         }
            
    //         // Validate all templates have required fields
           
            
    //         List<KnowledgeTransfer> savedTransfers = ktService
    //             .saveKnowledgeTransfer(offBoardingId, employeeById, employeeToIds, knowledgeTransfers);
            
    //         return ResponseEntity.ok(savedTransfers);
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     } catch (Exception e) {
    //         return ResponseEntity.internalServerError().body("Error creating knowledge transfer: " + e.getMessage());
    //     }
    // }

//   @PostMapping("/save")
// @Transactional
// public ResponseEntity<?> createKnowledgeTransfer(
//         @RequestParam Long offBoardingId,
//         @RequestParam Long employeeById,
//         @RequestBody List<Map<String, Object>> knowledgeTransferRequests) {
    
//     try {
//         List<KnowledgeTransfer> savedTransfers = new ArrayList<>();
        
//         for (Map<String, Object> request : knowledgeTransferRequests) {
//             // Extract employeeToId from each JSON object
//             Long employeeToId = Long.valueOf(request.get("employeeToId").toString());
            
//             // Create KnowledgeTransfer from remaining properties
//             KnowledgeTransfer knowledgeTransfer = new KnowledgeTransfer();
//             BeanUtils.copyProperties(request, knowledgeTransfer);
            
//             // Remove employeeToId to avoid copying to entity
//             request.remove("employeeToId");
            
//             // Call existing service method
//             KnowledgeTransfer saved = ktService.saveKnowledgeTransfer(
//                 offBoardingId,
//                 employeeById,
//                 employeeToId,
//                 knowledgeTransfer);
            
//             savedTransfers.add(saved);
//         }
        
//         return ResponseEntity.ok(savedTransfers);
//     } catch (IllegalArgumentException e) {
//         return ResponseEntity.badRequest().body(e.getMessage());
//     } catch (Exception e) {
//         return ResponseEntity.internalServerError().body("Error creating knowledge transfer");
//     }
// }


@PostMapping("/save")

public ResponseEntity<?> createKnowledgeTransfer(
        @RequestParam Long offBoardingId,
        @RequestParam Long employeeById,
        @RequestBody List<Map<String, Object>> knowledgeTransferRequests) {
    
    try {
        ObjectMapper mapper = new ObjectMapper();
        List<KnowledgeTransfer> savedTransfers = new ArrayList<>();
        
        for (Map<String, Object> request : knowledgeTransferRequests) {
            // 1. Extract and remove employeeToId (to prevent it from being copied to entity)
            Long employeeToId = Long.valueOf(request.remove("employeeToId").toString());
            
            // 2. Convert Map to KnowledgeTransfer
            KnowledgeTransfer knowledgeTransfer = mapper.convertValue(request, KnowledgeTransfer.class);
            
            // 3. Call service
            KnowledgeTransfer saved = ktService.saveKnowledgeTransfer(
                offBoardingId,
                employeeById,
                employeeToId,
                knowledgeTransfer);
            
            savedTransfers.add(saved);
        }
        
        return ResponseEntity.ok(savedTransfers);
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
    }
}
    // @PostMapping("/save/{offBoardingId}/{employeeById}")
    // public ResponseEntity<List<KnowledgeTransfer>> createKnowledgeTransfers(
    //         @PathVariable  Long offBoardingId,
    //         @PathVariable  Long employeeById,
    //         @RequestParam  List<Long> employeeToIds,
    //         @RequestBody  List<KnowledgeTransfer> templates) {

    //     List<KnowledgeTransfer> savedTransfers = ktService.saveKnowledgeTransfer(
    //         offBoardingId, employeeById, employeeToIds, templates);

    //     return ResponseEntity.ok(savedTransfers);
    // }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteKnowledgeTransfer(@PathVariable Long id) {
        ktService.deleteKnowledgeTransfer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/assigned-by")
 public List<KnowledgeTransfer> getKnowledgeTransfersByEmployeeBy(@RequestParam(required = false) Long employeeToId) {
return ktService.getKnowledgeTransfersByEmployeeBy(employeeToId);
 }

 
 @GetMapping("/resignations")
  public List<KnowledgeTransfer> getFilteredResignations(
  @RequestParam(required = false) Long employeeById,
 @RequestParam(required = false) Long employeeToId,
 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
 @RequestParam(required = false) Boolean completionStatus) {
 return ktService.getFilteredResignations(employeeById, employeeToId, startDate, endDate,completionStatus);
 }
 

 @PostMapping("/generate/{employeeId}")
 public ResponseEntity<String> generateOTP(@PathVariable Long employeeId) {
     String otp = otpService.generateAndSendOTP(employeeId);
     return ResponseEntity.ok(otp);
    }  
    
    
    @PutMapping("/update/{id}/{employeeToId}/{employeeById}")
    public ResponseEntity<?> updateKnowledgeTransfer(
            @PathVariable Long id,
            @PathVariable Long employeeToId,
            @PathVariable Long employeeById,
            @RequestParam String otp,
            @RequestBody KnowledgeTransfer updatedKnowledgeTransfer) {

        // Verify OTP for either employee without creating objects
        if (!otpService.verifyOTP(employeeToId, otp) && 
            !otpService.verifyOTP(employeeById, otp)) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        updatedKnowledgeTransfer.setId(id);

        KnowledgeTransfer result = ktService.updateKnowledgeTransfer(
                id,
                employeeToId, 
                employeeById, 
                updatedKnowledgeTransfer);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/searchByStatus")
 public ResponseEntity<List<KnowledgeTransfer>> getByCompletionStatusAndEmpToId(
 @RequestParam(required = false) Boolean completionStatus,
 @RequestParam(required = false) Long empToId) {
 
 List<KnowledgeTransfer> result = ktService.getByCompletionStatusAndEmpToId(completionStatus, empToId);
 return ResponseEntity.ok(result);
 }
//  @PutMapping("/{knowledgeTransferId}/employee/{employeeToId}/completion-status")
//     public ResponseEntity<KnowledgeTransfer> updateCompletionStatus(
//             @PathVariable Long knowledgeTransferId,
//             @PathVariable Long employeeToId) {

//         // Assuming 'status' is automatically determined by some logic within the service.
//         KnowledgeTransfer updatedKt = ktService.updateCompletionStatus(
//                 knowledgeTransferId, employeeToId);

//         return ResponseEntity.ok(updatedKt); // Respond with the updated KnowledgeTransfer
//     }




@GetMapping("/offBoarding")
 public List<KnowledgeTransfer> getKnowledgeTransfersByOffBoardingId(@RequestParam(required = false) Long offBoardingId) {
return ktService.getKnowledgeTransfersByOffBoardingId(offBoardingId);
}





@PutMapping("/complete")
public ResponseEntity<String> completeTransfer(
        @RequestParam(required = false) Long recordId,
        @RequestParam(required = false) Long employeeToId) {
    
    String response = ktService.markKnowledgeTransferComplete(recordId, employeeToId);
    return ResponseEntity.ok(response);
}
@GetMapping("/source")
public ResponseEntity<List<KnowledgeTransfer>> getTransfersBySourceEmployee(
        @RequestParam(value = "employeeById", required = false) Long employeeById) {
 
 
        List<KnowledgeTransfer> transfers = ktService.getTransfersBySourceEmployeeId(employeeById);
        return ResponseEntity.ok(transfers);
   
}
 
// 🔹 Get transfers by Target Employee ID
@GetMapping("/target")
public ResponseEntity<List<KnowledgeTransfer>> getTransfersByTargetEmployee(
        @RequestParam(value = "employeeToId", required = false) Long employeeToId)  {
 
 
        List<KnowledgeTransfer> transfers = ktService.getTransfersByTargetEmployeeId(employeeToId);
        return ResponseEntity.ok(transfers);
   
}   
 
}

