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
import com.spectrum.model.ItAsset;
import com.spectrum.service.EmployeeService;
import com.spectrum.service.ItAssetService;
import com.spectrum.repository.ItAssetRepository;

@RestController
// @CrossOrigin("http://localhost:3000")
@RequestMapping("/Asset")
 
public class ItAssetController {


	@Autowired
	private ItAssetRepository ItAssetRepository ;
	@Autowired
	private ItAssetService ItAssetService  ;
	@Autowired
	private EmployeeService EmployeeService;

	
	
	
	
	 @PostMapping("/ItAssetSaveForEmployee")
	    public ResponseEntity<String> saveItAssetForEmployee(@RequestBody ItAsset ItAsset ,
	                                                         @RequestParam Long employeeId) {
	        try {
	            // Check if the employee exists
	            Employee employee = EmployeeService.getById(employeeId)
	                    .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
	            
	            // Associate the project with the employee
	            ItAsset.setEmployee(employee);
	            
	            // Save the project
	            ItAssetService.saveItAssettForEmployee(ItAsset);

	            return ResponseEntity.ok("ItAsset saved successfully for employee with ID: " + employeeId);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Failed to save ItAsset for employee with ID: " + employeeId);
	        }
	    }
	
	
	

	 @GetMapping("/GetAllItAssetDataByEmployeeID/{employeeId}")
	    public ResponseEntity<Page<ItAsset>> searchItAssetByEmployeeId(
	            @PathVariable long employeeId,
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {
	        try {
	            Page<ItAsset> searchResult = ItAssetRepository.findByEmployeeId(employeeId, PageRequest.of(page, size));
	            return ResponseEntity.ok(searchResult);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
	
	
	
	
	
	
	
}
