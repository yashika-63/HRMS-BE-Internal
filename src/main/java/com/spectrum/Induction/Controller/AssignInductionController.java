package com.spectrum.Induction.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.Induction.Model.AssignInduction;
import com.spectrum.Induction.Service.AssignInductionService;

@RestController
@RequestMapping("/api/assign-inductions")
public class AssignInductionController {
          @Autowired
    private AssignInductionService assignInductionService;

    @PostMapping
    public ResponseEntity<AssignInduction> create(@RequestBody AssignInduction assignInduction) {
        return ResponseEntity.ok(assignInductionService.save(assignInduction));
    }

    @GetMapping
    public ResponseEntity<List<AssignInduction>> getAll() {
        return ResponseEntity.ok(assignInductionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignInduction> getById(@PathVariable Long id) {
        AssignInduction var1  =assignInductionService.getById(id);
        return ResponseEntity.ok(var1);
                
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignInduction> update(@PathVariable Long id, @RequestBody AssignInduction assignInduction) {
        try {
            AssignInduction updated = assignInductionService.update(id, assignInduction);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> delete(@PathVariable Long id) {
    //     assignInductionService.delete(id);
    //     return ResponseEntity.noContent().build();
    // }


    ////////////
    @PostMapping("/save")
    public ResponseEntity<List<AssignInduction>> saveWithNotification(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long inductionId,
            @RequestParam(required = false) Long assignedById,
            @RequestParam(required = false) Long compID,
            @RequestBody List<AssignInduction> assignList) {
    
        List<AssignInduction> savedList = assignInductionService
                .saveAllWithNotification(assignList, employeeId, inductionId, assignedById,compID);
        return new ResponseEntity<>(savedList, HttpStatus.CREATED);
    }




    @GetMapping("/assignments/employee/{employeeId}")
public ResponseEntity<List<AssignInduction>> getAssignmentsByEmployeeId(@PathVariable Long employeeId) {
    List<AssignInduction> assignments = assignInductionService.getAssignmentsByEmployeeId(employeeId);
    return ResponseEntity.ok(assignments);
}





@GetMapping("/assigned/by-company-year/{companyId}/{year}")
public ResponseEntity<List<AssignInduction>> getByCompanyIdAndYear(
        @PathVariable Long companyId,
        @PathVariable int year) {

    List<AssignInduction> list = assignInductionService.getByCompanyIdAndYear(companyId, year);
    return new ResponseEntity<>(list, HttpStatus.OK);
}
//  @GetMapping("/company/{companyId}")
//     public ResponseEntity<?> getAssignedInductionsByCompanyAndDate(
//             @PathVariable Long companyId,
//             @RequestParam(value="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//             @RequestParam(value="endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//                 if (startDate == null || endDate == null) {
//                     return ResponseEntity.badRequest()
//                         .body("Both startDate and endDate must be provided in YYYY-MM-DD format");
//                 }
              

//         List<AssignInduction> list = assignInductionService.getAssignedByCompanyAndDateRange(companyId, startDate, endDate);

//         if (list.isEmpty()) {
//             return ResponseEntity.status(404).body("No assignments found for the given company ID and date range.");
//         }

//         return ResponseEntity.ok(list);
//     }
// @GetMapping("/company/{companyId}")
// public ResponseEntity<?> getAssignedInductionsByCompanyAndDate(
//         @PathVariable Long companyId,
//         @RequestParam(value = "startDate", required = false)
//         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//         @RequestParam(value = "endDate", required = false)
//         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

//     if (startDate == null || endDate == null) {
//         return ResponseEntity.badRequest()
//                 .body("Both startDate and endDate must be provided in YYYY-MM-DD format.");
//     }

//     List<AssignInduction> list = assignInductionService.getAssignedByCompanyAndDateRange(companyId, startDate, endDate);

//     if (list.isEmpty()) {
//         return ResponseEntity.status(404)
//                 .body("No assignments found for the given company ID and date range.");
//     }

//     return ResponseEntity.ok(list);
// }


       

     
      // Endpoint to filter inductions based on employeeId, completionStatus, and expiryStatus
    
      @GetMapping("/filter")
      public ResponseEntity<List<AssignInduction>> filterInductions(
              @RequestParam Long employeeId,
              @RequestParam(required = false) Boolean completionStatus,
              @RequestParam(required = false) Boolean expiryStatus) {
  
          List<AssignInduction> result = assignInductionService.filterInductions(employeeId, completionStatus, expiryStatus);
  
          if (result.isEmpty()) {
              return ResponseEntity.noContent().build();  // 204 No Content
          }
          return ResponseEntity.ok(result);
      }

     // @GetMapping("/company/{companyId}")
    //   public ResponseEntity<?> getAssignedInductionsByCompanyAndDate(
    //           @PathVariable Long companyId) {
         
    //       List<AssignInduction> list = assignInductionService.getAssignedByCompany(companyId);
   
    //       if (list.isEmpty()) {
    //           return ResponseEntity.status(404).body("No assignments found for the given company ID and date range.");
    //       }
   
    //       return ResponseEntity.ok(list);
    //   }
}
