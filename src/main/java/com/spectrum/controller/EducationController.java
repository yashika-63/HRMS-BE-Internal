package com.spectrum.controller;
 
import java.util.List;
import java.util.Optional;
 
import org.springframework.beans.factory.annotation.Autowired;
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
import com.spectrum.service.EducationService;
import com.spectrum.service.EmployeeService;
 
@RestController
// @CrossOrigin("http://localhost:3000")
@RequestMapping("/education")
public class EducationController {
 
	@Autowired
    private EducationRepository educationRepository;
	
    @Autowired
    private EducationService educationService;
 
    @Autowired
    private EmployeeService employeeService;
 
    @PostMapping("/saveForEmployee")
    public ResponseEntity<String> saveEducationForEmployee(@RequestBody List<Education> educations,
                                                           @RequestParam Long employeeId) {
        try {
            // Check if the employee exists
            Employee employee = employeeService.getById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

            for (Education education : educations) {
                // Associate the education with the employee
                education.setEmployee(employee);

                // Save the education
                educationService.saveEducation(education);
            }

            return ResponseEntity.ok("Education saved successfully for employee with ID: " + employeeId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save education for employee with ID: " + employeeId);
        }
    }
    
    @PostMapping ("/EducationSave")
	public ResponseEntity<String> saveEducation(@RequestBody Education Education){
		try {
            educationRepository.save(Education);
		}
		catch(Exception e) {
			return new ResponseEntity<String> ("not inserted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String> (Education+"sucessfully inserted", HttpStatus.OK);
		
	}
   
 
	
	@GetMapping ("/getAllEducation")
	public ResponseEntity<List<Education>> getEducation(){
		List<Education> list = null;
		try {
            list = educationRepository.findAll();
		}
		catch(Exception e) {
			return new ResponseEntity<List<Education>> (list, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<Education>> (list, HttpStatus.OK);
	
}
	
																				
    											
    																	
//	
//	 @GetMapping("/SustainabilityByYear/{Year}")
//	    public ResponseEntity<?> getByYear(@PathVariable String Year) {
//	        try {
//	            Sustainability sustainability = service.getByYear(Year);
//           if (sustainability != null) {
//	                return ResponseEntity.ok(sustainability);
//	            } else {
//	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sustainability not found for the given year.");
//           }
//       } catch (Exception e) {
//           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Year+"Error occurred while fetching Sustainability by year.");
//       }
//	    }
    
    
    
 
    // Method to update education for a specific employee
    @PutMapping("/updateForEmployee/{employeeId}")
    public ResponseEntity<String> updateEducationForEmployee(
            @RequestBody List<Education> educations,
            @PathVariable Long employeeId) {
        try { 
            // Check if the employee exists
            Optional<Employee> employeeOptional = employeeService.getById(employeeId);
            if (!employeeOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Employee not found with ID: " + employeeId);
            }

            // Iterate through each education in the array
            for (Education education : educations) {
                // Check if the education exists
                Optional<Education> existingEducationOptional = educationRepository.findById(education.getId());
                if (!existingEducationOptional.isPresent()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Education not found with ID: " + education.getId());
                }

                // Associate the education with the employee
                education.setEmployee(employeeOptional.get());

                // Save the updated education
                educationService.saveEducation(education);
            }

            return ResponseEntity.ok("Education updated successfully for employee with ID: " + employeeId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update education for employee with ID: " + employeeId);
        }
    }

 
    
    
// Method to get all educations for a specific employee
    @GetMapping("/getByEmployeeId/{employeeId}")
    public ResponseEntity<?> getEducationsByEmployeeId(@PathVariable Long employeeId) {
        try {
            // Check if the employee exists
            Employee employee = employeeService.getById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
 
            // Fetch educations associated with the employee
            List<Education> educations = educationRepository.findByEmployee(employee);
 
            if (educations.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No educations found for employee with ID: " + employeeId);
            }
 
            return ResponseEntity.ok(educations);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching educations for employee with ID: " + employeeId);
        }
    }

    @PutMapping("/update/{id}")
public ResponseEntity<String> updateEducationById(@PathVariable int id, @RequestBody Education updatedEducation) {
    Optional<Education> updated = educationService.updateEducationById(id, updatedEducation);
    if (updated.isPresent()) {
        return ResponseEntity.ok("Education updated successfully.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Education not found with ID: " + id);
    }
}

    // Delete Education by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEducationById(@PathVariable int id) {
        boolean isDeleted = educationService.deleteEducationById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Education deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Education not found with ID: " + id);
        }
    }

}