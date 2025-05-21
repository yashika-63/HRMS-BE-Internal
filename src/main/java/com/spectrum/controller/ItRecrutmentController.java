package com.spectrum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.model.Employee;
import com.spectrum.model.ItRecrutment;
import com.spectrum.repository.ItRecrutmentRepository;
import com.spectrum.service.EmployeeService;
import com.spectrum.service.ItRecrutmentService;

@RestController
// @CrossOrigin("http://localhost:3000")
@RequestMapping("/recruitment")

public class ItRecrutmentController {

	
	
	
	@Autowired
	private ItRecrutmentRepository ItRecrutmentRepository;
	
	@Autowired
	private ItRecrutmentService ItRecrutmentService;
	
	@Autowired
	private EmployeeService EmployeeService;

	
	

	
//	@PostMapping ("/recrutmentEmployee")
//	public ResponseEntity<String> saverecrutment(@RequestBody ItRecrutment ItRecrutment){
//		try {
//		repo.save(ItRecrutment);
//		}
//		catch(Exception e) {
//			return new ResponseEntity<String> ("not inserted", HttpStatus.INTERNAL_SERVER_ERROR); 
//		}
//		return new ResponseEntity<String> (ItRecrutment+"sucessfully inserted", HttpStatus.OK); 
//		
//	}
	
	
	
	 @PostMapping("/RecrutmentSaveForEmployee")
	    public ResponseEntity<String> saveRecrutmentForEmployee(@RequestBody ItRecrutment ItRecrutment ,
	                                                         @RequestParam Long employeeId) {
	        try {
	            // Check if the employee exists
	            Employee employee = EmployeeService.getById(employeeId)
	                    .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
	            
	            // Associate the project with the employee
	            ItRecrutment.setEmployee(employee);
	            
	            // Save the project
	            ItRecrutmentService.saveItRecrutmentForEmployee(ItRecrutment);

	            return ResponseEntity.ok("Undertaking saved successfully for employee with ID: " + employeeId);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Failed to save Undertaking for employee with ID: " + employeeId);
	        }
	    }
	
	
	
	 @GetMapping("/GetAllItRecrutmentDataByEmployeeID/{employeeId}")
	    public ResponseEntity<Page<ItRecrutment>> searchItRecrutmentByEmployeeId(
	            @PathVariable long employeeId,
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {
	        try {
	            Page<ItRecrutment> searchResult = ItRecrutmentRepository.findByEmployeeId(employeeId, PageRequest.of(page, size));
	            return ResponseEntity.ok(searchResult);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
	
	
	
}
