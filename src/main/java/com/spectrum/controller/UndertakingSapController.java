package com.spectrum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.model.Employee;
import com.spectrum.model.UndertakingSap;
import com.spectrum.repository.UndertakingSapRepository;
import com.spectrum.service.EmployeeService;
import com.spectrum.service.UndertakingSapService;

@RestController
// @CrossOrigin("http://localhost:3000")
@RequestMapping("undertaking")
public class UndertakingSapController {

	
	@Autowired
	private UndertakingSapRepository UndertakingSapRepository;
	@Autowired
	private UndertakingSapService UndertakingSapService;
	@Autowired
	private EmployeeService EmployeeService;
	// @Autowired
	// private EmployeeRepository EmployeeRepository;
	

    @PostMapping("/Undertaking-save-for-employee")
    public ResponseEntity<String> saveUndertakingForEmployee(@RequestBody UndertakingSap UndertakingSap ,
                                                         @RequestParam Long employeeId) {
        try {
            // Check if the employee exists
            Employee employee = EmployeeService.getById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
            
            // Associate the project with the employee
            UndertakingSap.setEmployee(employee);
            
            // Save the project
            UndertakingSapService.saveUndertakingSapForEmployee(UndertakingSap);

            return ResponseEntity.ok("Undertaking saved successfully for employee with ID: " + employeeId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save Undertaking for employee with ID: " + employeeId);
        }
    }
	
    @GetMapping("/GetAllUndertakingDataByEmployeeID/{employeeId}")
    public ResponseEntity<Page<UndertakingSap>> searchUndertakingByEmployeeId(
            @PathVariable long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<UndertakingSap> searchResult = UndertakingSapRepository.findByEmployeeId(employeeId, PageRequest.of(page, size));
            return ResponseEntity.ok(searchResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/getAllIUndertakings")
    public ResponseEntity<Page< UndertakingSap>> getAllUndertakingSap(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<UndertakingSap> undertakingPage = UndertakingSapService.getAllUndertakingSap(page, size);
            return ResponseEntity.ok(undertakingPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @DeleteMapping("/deleteSapUndertakingByName/{name}")
    public ResponseEntity<String> deleteSapUndertakingByName(@PathVariable String name) {
        try {
            // Check if any undertakings exist with the given name
            List<UndertakingSap> undertakings = UndertakingSapRepository.findByName(name);
            
            if (undertakings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No undertakings found with name: " + name);
            }
            
            // Delete all undertakings with the given name
            UndertakingSapRepository.deleteByName(name);

            return ResponseEntity.ok("Undertakings deleted successfully with name: " + name);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete undertakings with name: " + name);
        }
    }
	   
	  @DeleteMapping("/deleteSapUndertakingId/{sapUserId}")
	  public ResponseEntity<String> deleteSapUndertaking(@PathVariable("sapUserId") String sapUserId) {
	      try {
	    	  UndertakingSapRepository.deleteById(sapUserId);
	          return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
	      } catch (Exception e) {
	          return new ResponseEntity<>("Not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
	      }
	  }
	  
	  
	  
	  
}
