package com.spectrum.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.spectrum.model.Education;
import com.spectrum.model.Employee;
import com.spectrum.repository.EducationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmployeeService;
 
@RestController
 
// @CrossOrigin("http://localhost:3000")
@RequestMapping("/employees")
public class EmployeeController {
 
	
	@Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;
 
    @Autowired
    private EducationRepository educationRepository;

 
    @PostMapping("/save/{companyId}")
    public ResponseEntity<?> saveEmployee(@PathVariable Long companyId, @RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.saveEmployee(companyId, employee);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save-and-mail/{companyId}")
    public ResponseEntity<?> saveEmployeeAndSendEmail(@PathVariable Long companyId, @RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.saveEmployeeAndSendEmail(companyId, employee);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    @GetMapping("/getAllEmployeeProjects")
    public ResponseEntity<List<Employee>> getAllEmployeeProjects() {
        List<Employee> employeeProjects = null;
        try {
            System.out.println("Inside getAllEmployeeProjects() method");
            employeeProjects = employeeRepository.findAll();
        } catch(Exception e) {
            System.err.println("Error in getAllEmployeeProjects() method: " + e.getMessage());
            return new ResponseEntity<>(employeeProjects, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(employeeProjects, HttpStatus.OK);
    }
    // @PostMapping("/saveEmployeeForCompany")
    // public ResponseEntity<?> saveEmployee(@RequestBody Employee employee) {
    // try {
    // Employee savedEmployee = employeeService.saveEmployee(employee);
    // return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    // } catch (Exception e) {
    // return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    // }
    // }
    
    @GetMapping("/distinctProjects")
    public ResponseEntity<?> getDistinctProjectHeadings() {
        try {
            List<String> headings = employeeRepository.findDistinctProjectHeadings();
            if (headings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No main project headings found.");
            }
            return ResponseEntity.ok(headings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching assign project's.");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addEmployee(@RequestBody Employee employeeDTO) {
        try {
            // Create an Employee object from the DTO
            Employee employee = new Employee();
            employee.setId(employeeDTO.getId());
            employee.setFirstName(employeeDTO.getFirstName());
            employee.setLastName(employeeDTO.getLastName());	
            employee.setMiddleName(employeeDTO.getMiddleName());
            employee.setMotherName(employeeDTO.getMotherName());
            employee.setContactNo(employeeDTO.getContactNo());
            employee.setEmail(employeeDTO.getEmail());
            employee.setGender(employeeDTO.getGender());
            employee.setNationality(employeeDTO.getNationality());
            employee.setDesignation(employeeDTO.getDesignation());
            employee.setDepartment(employeeDTO.getDepartment());
            employee.setExperience(employeeDTO.getExperience());
            employee.setPanNo(employeeDTO.getPanNo());
            employee.setAdhaarNo(employeeDTO.getAdhaarNo());
            // employee.setAbscondDate(employeeDTO.getAbscondDate());
            employee.setJoiningDate(employeeDTO.getJoiningDate());
            // employee.setExitDate(employeeDTO.getExitDate());
            employee.setPresence(employeeDTO.isPresence());
            // employee.setResign(employeeDTO.isResign());
            employee.setPriorId(employeeDTO.getPriorId());
            employee.setEmployeeType(employeeDTO.getEmployeeType());
            employee.setCurrentHouseNo(employeeDTO.getCurrentHouseNo());
            employee.setCurrentStreet(employeeDTO.getCurrentStreet());
            employee.setCurrentCity(employeeDTO.getCurrentCity());
            employee.setCurrentState(employeeDTO.getCurrentState());
            employee.setCurrentPostelcode(employeeDTO.getCurrentPostelcode());
            employee.setCurrentCountry(employeeDTO.getCurrentCountry());
            employee.setPermanentHouseNo(employeeDTO.getPermanentHouseNo());
            employee.setPermanentStreet(employeeDTO.getPermanentStreet());
            employee.setPermanentCity(employeeDTO.getPermanentCity());
            employee.setPermanentState(employeeDTO.getPermanentState());
            employee.setPermanentPostelcode(employeeDTO.getPermanentPostelcode());
            employee.setPermanentCountry(employeeDTO.getPermanentCountry());
       ;
            
            // Save the Employee to get its ID
            Employee savedEmployee = employeeRepository.save(employee);
 
            // Create Education objects for each education in the DTO
            for (Education educationDTO : employeeDTO.getEducations()) {
                Education education = new Education();
                education.setInstitute(educationDTO.getInstitute());
                education.setUniversity(educationDTO.getUniversity());
                education.setTypeOfStudy(educationDTO.getTypeOfStudy());
                education.setYearOfAddmisstion(educationDTO.getYearOfAddmisstion());
                education.setYearOfPassing(educationDTO.getYearOfPassing());
                education.setScore(educationDTO.getScore());
                education.setEmployee(savedEmployee); // Set the Employee for the Education
                educationRepository.save(education);
            }
 
            return ResponseEntity.ok("Employee and Education records saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save Employee and Education records");
        }
    }



	@GetMapping("/FindEmployeeByName/{firstName}")
	public ResponseEntity<?> getByFirstName(@PathVariable String firstName) {
	    try {
            List<Employee> EmployeeList = employeeService.getByFirstName(firstName);
	        if (!EmployeeList.isEmpty()) {
	            return ResponseEntity.ok(EmployeeList);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("employee not found for the given Name.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(firstName + " Error occurred while fetching employee by name.");
	    }
	}

    @GetMapping("/FindEmployeeByLastName/{lastName}")
	public ResponseEntity<?> getByLastName(@PathVariable String lastName) {
	    try {
            List<Employee> EmployeeList = employeeService.getByLastName(lastName);
	        if (!EmployeeList.isEmpty()) {
	            return ResponseEntity.ok(EmployeeList);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("employee not found for the given last Name.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(lastName + " Error occurred while fetching employee by name.");
	    }
	}

//
//	@GetMapping ("/getAllEmployees")
//	public ResponseEntity<List<Employee>> getEmployee(){
//		List<Employee> list = null;
//		try {
//		list =   employeeRepository.findAll();
//		}
//		catch(Exception e) {
//			return new ResponseEntity<List<Employee>> (list, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		return new ResponseEntity<List<Employee>> (list, HttpStatus.OK);
//}
	
@GetMapping("/byCompany/{companyId}")
    public ResponseEntity<Optional<List<Employee>>> getEmployeesByCompanyId(@PathVariable Long companyId) {
        Optional<List<Employee>> employees = employeeRepository.findByCompanyRegistration_Id(companyId);
        if (employees.isPresent() && !employees.get().isEmpty()) {
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }


	
	@GetMapping("/getAllEmployees")
	public ResponseEntity<List<Employee>> getEmployee() {
	    List<Employee> list = null;
	    try {
	        list = employeeRepository.findAll();
	        // Access educations to initialize them before closing the session
	        for (Employee employee : list) {
	            employee.getEducations().size();
	        }
	    } catch (Exception e) {
	        return new ResponseEntity<List<Employee>>(list, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    return new ResponseEntity<List<Employee>>(list, HttpStatus.OK);
	}


	@GetMapping("/EmployeeById/{EmployeeId}")
	public ResponseEntity<?> getById(@PathVariable long EmployeeId) {
	    try {
            Optional<Employee> Employee = employeeService.getById(EmployeeId);
	        if (Employee.isPresent()) {
	            return ResponseEntity.ok(Employee.get());
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found for the given ID.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching Employee by ID.");
	    }
	}


	  @DeleteMapping("/deleteEmployeeById/{employeeId}")
	  public ResponseEntity<String> deleteEmployeet(@PathVariable("employeeId") long employeeId) {
	      try {
	    	  employeeRepository.deleteById(employeeId);
	          return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
	      } catch (Exception e) {
	          return new ResponseEntity<>("Not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
	      }
	  }

 
 
    
	  @PutMapping("/EmployeeUpdateById/{employeeId}")
	    public ResponseEntity<?> updateById(@PathVariable long employeeId, @RequestBody Employee EmployeeData) {
	        try {
                Employee updatedEmployee = employeeService.updateById(employeeId, EmployeeData);
	            if (updatedEmployee != null) {
	                return ResponseEntity.ok(updatedEmployee);
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found for the given ID.");
	            }
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating Employee by ID.");
	        }
	    }
 
	  
	  
        // this is the method to get employee by pagination status is active and validated by company id
	  @GetMapping("/employees/active")
    public Page<Employee> getActiveEmployeesByCompanyId(
            @RequestParam Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return employeeService.getActiveEmployeesByCompanyId(companyId, pageable);
    }

	   
	    
    @GetMapping("/search")
    public ResponseEntity<?> searchEmployees(
            @RequestParam Long companyId,
            @RequestParam String searchTerm) {
        try {
            List<Employee> employees = employeeRepository.searchEmployeesByAnyField(companyId, searchTerm);
            if (!employees.isEmpty()) {
                return ResponseEntity.ok(employees);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No employees found with the given criteria.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while searching employees.");
        }
    }



    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveEmployeeCountByCompanyId(@RequestParam Long companyId) {
        Long count = employeeService.getActiveEmployeeCountByCompanyId(companyId);
        return ResponseEntity.ok(count);
    }

}
    
  










