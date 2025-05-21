package com.spectrum.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.spectrum.model.Employee;
import com.spectrum.model.HiringChecklist;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.repository.HiringChecklistRepository;

@RestController
// @CrossOrigin("http://localhost:3000")
@RequestMapping("/HiringChecklist")
public class HiringChecklistController {

	
	
	
	@Autowired
	private HiringChecklistRepository HiringChecklistRepository;
	
	@Autowired
	private EmployeeRepository EmployeeRepository;
	
	
	

    @PostMapping
    public HiringChecklist createHiringChecklis(@RequestBody HiringChecklist HiringChecklist) {
        // Make sure the Author is already saved
    	Employee Employee = HiringChecklist.getEmployee();
        if (Employee != null && Employee.getId() == null) {
        	Employee = EmployeeRepository.save(Employee);
        	HiringChecklist.setEmployee(Employee); // Set the saved Author to the Book
        }
        return HiringChecklistRepository.save(HiringChecklist);
    }
	
    @PostMapping("/{employeeId}")
    public ResponseEntity<HiringChecklist> createHiringChecklist(
            @PathVariable Long employeeId,
            @RequestBody HiringChecklist hiringChecklist) {
        try {
            Employee employee = EmployeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found with id " + employeeId));
            
            hiringChecklist.setEmployee(employee);
            HiringChecklist savedChecklist = HiringChecklistRepository.save(hiringChecklist);
            return ResponseEntity.ok(savedChecklist);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    
    
    @GetMapping("/{hiringChecklistId}")
    public HiringChecklist getHiringChecklistById(@PathVariable Long hiringChecklistId) {
        return HiringChecklistRepository.findById(hiringChecklistId)
                .orElseThrow(() -> new RuntimeException("HiringChecklistRepository not found with id: " + hiringChecklistId));
    }
    
    @GetMapping("/employee/{employeeId}/hiring-checklist")
    public Optional<HiringChecklist> getHiringChecklistByEmployeeId(@PathVariable Long employeeId) {
        return HiringChecklistRepository.findByEmployeeId(employeeId);
    }

	
	
	
	
	
}
