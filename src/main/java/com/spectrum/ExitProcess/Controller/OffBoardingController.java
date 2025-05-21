package com.spectrum.ExitProcess.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.ExitProcess.Model.KnowledgeTransfer;
import com.spectrum.ExitProcess.Model.OffBoarding;
import com.spectrum.ExitProcess.Service.OffboardingService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/offboarding")
public class OffBoardingController {

    @Autowired
    private OffboardingService offBoardingService;

    @GetMapping
    public List<OffBoarding> getAllOffBoardings() {
        return offBoardingService.getAllOffBoardings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OffBoarding> getOffBoardingById(@PathVariable Long id) {
        OffBoarding offBoarding = offBoardingService.getOffBoardingById(id);
        if (offBoarding != null) {
            return ResponseEntity.ok(offBoarding);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public OffBoarding createOffBoarding(@RequestBody OffBoarding offBoarding) {
        return offBoardingService.createOffBoarding(offBoarding);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OffBoarding> updateOffBoarding(@PathVariable Long id, @RequestBody OffBoarding offBoarding) {
        OffBoarding updatedOffBoarding = offBoardingService.updateOffBoarding(id, offBoarding);
        if (updatedOffBoarding != null) {
            return ResponseEntity.ok(updatedOffBoarding);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffBoarding(@PathVariable Long id) {
        offBoardingService.deleteOffBoarding(id);
        return ResponseEntity.noContent().build();
    }


    ///////////////////////////////////////   
    @PostMapping("/save/{companyId}/{employeeId}/{managerId}/{hrId}")
    public ResponseEntity<?> saveOffBoarding(
        @RequestBody OffBoarding offBoarding,
        @PathVariable Long companyId,
        @PathVariable Long employeeId,
        @PathVariable Long managerId,
        @PathVariable Long hrId
    ) {
        
            OffBoarding savedEntity = offBoardingService.saveOffBoarding(offBoarding, companyId, employeeId, managerId, hrId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEntity);
        
    }

    // @PostMapping("/save")
    // public ResponseEntity<?> saveOffBoarding(
    //     @RequestBody OffBoarding offBoarding,
    //     @RequestParam(required = false) Long companyId,
    //     @RequestParam(required = false) Long employeeId,
    //     @RequestParam(required = false) Long managerId,
    //     @RequestParam(required = false) Long hrId
    // ) {
       
    //         OffBoarding savedEntity = offBoardingService.saveOffBoarding(offBoarding, companyId, employeeId, managerId, hrId);
    //         return ResponseEntity.status(HttpStatus.CREATED).body(savedEntity);
       
    // }

    @GetMapping("/fetch")
    public ResponseEntity<?> getOffBoardingByCompletionStatusAndCompany(
        @RequestParam boolean completionStatus,
        @RequestParam Long companyId
    ) {
        try {
            List<OffBoarding> offBoardings = offBoardingService.getByCompletionStatusAndCompany(completionStatus, companyId);
            return ResponseEntity.ok(offBoardings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching the data.");
        }
    }

    // @GetMapping("/fetchByNameAndEmpId")
    // public ResponseEntity<?> getOffBoardingByNameAndEmployeeId(
    //     @RequestParam String name,
    //     @RequestParam Long empId
    // ) {
    //     try {
    //         OffBoarding offBoarding = offBoardingService.getByNameAndempId(name, empId);
    //         return ResponseEntity.ok(offBoarding);
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    //     }
    // }

    @GetMapping("/search")
    public ResponseEntity<List<OffBoarding>> searchOffBoardings(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) String employeeName) {
        
        List<OffBoarding> results = offBoardingService.searchOffBoardings(employeeId, employeeName);
        return ResponseEntity.ok(results);
    }

        

    @GetMapping("/searchByStatus")
   public List<OffBoarding> getByStatusAndCompanyId(@RequestParam(required = false) Boolean status, @RequestParam(required = false) Long companyId) {
    return offBoardingService.getByStatusAndCompanyId(status, companyId);
 }

//  @GetMapping("/by-status-company-and-manager")
//  public List<OffBoarding> getByStatusCompanyAndManager(@RequestParam(required = false) Boolean status,
//  @RequestParam(required = false) Long companyId,
//  @RequestParam(required = false) Long managerId) {
//      return offBoardingService.getByStatusCompanyIdAndManagerId(status, companyId, managerId);
//  }
    

//  @GetMapping("/searchOffBoarding")
//  public ResponseEntity<List<OffBoarding>> searchOffBoarding(
//          @RequestParam(required = false) Long deptId,
//          @RequestParam(required = false) Boolean completionStatus,
//          @RequestParam(required = false) Integer year) {

//      List<OffBoarding> results = offBoardingService.searchOffBoarding(deptId, completionStatus, year);
//      return ResponseEntity.ok(results);
//  }
// @GetMapping("/searchOffBoarding")
// public ResponseEntity<Page<OffBoarding>> searchOffBoarding(
//         @RequestParam(required = false) Long deptId,
//         @RequestParam(required = false) Boolean completionStatus,
//         @RequestParam(required = false) Integer year,
//         @RequestParam(defaultValue = "0") int page,
//         @RequestParam(defaultValue = "10") int size) {

//     Pageable pageable = PageRequest.of(page, size);
//     Page<OffBoarding> results = offBoardingService.searchOffBoarding(deptId, completionStatus, year, pageable);
//     return ResponseEntity.ok(results);
// }




  @PutMapping("/complete/{id}")
    public ResponseEntity<String> completeOffBoarding(
            @PathVariable Long id,
            @RequestParam String completedBy) {
        try {
            OffBoarding updated = offBoardingService.completeOffBoarding(id, completedBy);
            return ResponseEntity.ok("Offboarding completed for: " + updated.getName());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//     @GetMapping("/offboardings")
// public ResponseEntity<Page<OffBoarding>> getOffBoardings(
//         @RequestParam(required = false) Boolean status,
//         @RequestParam(required = false) Long companyId,
//         @RequestParam(defaultValue = "0") int page,
//         @RequestParam(defaultValue = "10") int size) {

//     Page<OffBoarding> result = offBoardingService.getOffBoardings(status, companyId, page, size);
//     return ResponseEntity.ok(result);
// }
@GetMapping("/by-status-company-and-manager")
public Page<OffBoarding> getByStatusCompanyAndManager(
        @RequestParam Boolean status,
        @RequestParam Long companyId,
        @RequestParam Long managerId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    
    return offBoardingService.getByStatusCompanyIdAndManagerId(status, companyId, managerId, page, size);
}

@GetMapping("/offboardings")
public ResponseEntity<Page<OffBoarding>> getOffBoardings(
        @RequestParam(required = false) Boolean status,
        @RequestParam(required = false) Long companyId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
 
    Page<OffBoarding> result = offBoardingService.getOffBoardings(status, companyId, page, size);
    return ResponseEntity.ok(result);
}

@GetMapping("/searchByCompOrStatus")
public ResponseEntity<Page<OffBoarding>> getByStatusOrCompanyId(
        @RequestParam(required = false) Boolean status,
        @RequestParam(required = false) Long companyId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
) {
    Page<OffBoarding> results = offBoardingService.getByStatusOrCompanyId(status, companyId, page, size);
    return new ResponseEntity<>(results, HttpStatus.OK);
}

@GetMapping("/searchOffBoarding")
    public ResponseEntity<Page<OffBoarding>> searchOffBoarding(
    @RequestParam(required = false) Long deptId,
    @RequestParam(required = false) Boolean completionStatus,
    @RequestParam(required = false) Integer year,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size) {
 
    Pageable pageable = PageRequest.of(page, size);
    Page<OffBoarding> results = offBoardingService.searchOffBoarding(deptId,
    completionStatus, year, pageable);
    return ResponseEntity.ok(results);
    }
}
