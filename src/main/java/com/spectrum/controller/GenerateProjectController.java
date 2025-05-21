package com.spectrum.controller;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.spectrum.model.Employee;
import com.spectrum.model.GenerateProject;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.repository.GenerateProjectRepository;
import com.spectrum.service.EmployeeService;
import com.spectrum.service.GenerateProjectService;

@RestController
// @CrossOrigin("http://localhost:3000")
@RequestMapping("/project")

public class GenerateProjectController {

	@Autowired
    private GenerateProjectRepository generateProjectRepository;
	
	
	@Autowired
	private GenerateProjectService service;
	

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;
	
	@PostMapping ("/GenerateProject")
	public ResponseEntity<String> saveproject(@RequestBody GenerateProject GenerateProject){
		try {
            generateProjectRepository.save(GenerateProject);
		}
		catch(Exception e) {
			return new ResponseEntity<String> ("not inserted", HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		return new ResponseEntity<String> (GenerateProject+"sucessfully inserted", HttpStatus.OK); 
		
	}
	

    @PostMapping("/save/{companyId}")
    public ResponseEntity<GenerateProject> saveProject(@PathVariable long companyId,
            @RequestBody GenerateProject project) {
        GenerateProject savedProject = service.saveProjectByCompanyId(companyId, project);
        return ResponseEntity.ok(savedProject);
    }

    @PostMapping("/saveone/{companyId}/{projectLeadEmployeeId}/{deliveryLeadEmployeeId}")
public ResponseEntity<GenerateProject> saveGenerateProject(
        @PathVariable long companyId,
        @PathVariable Long projectLeadEmployeeId,  // Changed to Long
        @PathVariable Long deliveryLeadEmployeeId,  // Changed to Long
        @RequestBody GenerateProject project) {
    
    // Call the service method to save the project
    GenerateProject savedProject = service.saveProjectByCompanyIdAndLeads(companyId, projectLeadEmployeeId, deliveryLeadEmployeeId, project);
    
    return ResponseEntity.ok(savedProject);
}


    @GetMapping("/byCompany/{companyId}")
    public ResponseEntity<List<GenerateProject>> getProjectsByCompanyId(@PathVariable long companyId) {
        List<GenerateProject> projects = service.getProjectsByCompanyId(companyId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/getAllProjects")
    public ResponseEntity<Page<GenerateProject>> getAllGenerateProject(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "35") int size) {
        try {
            Page<GenerateProject> newsPage = service.getAllGenerateProject(page, size);
            return ResponseEntity.ok(newsPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    
    @GetMapping("/searchProjectsByClientName/{clientName}")
    public ResponseEntity<Page<GenerateProject>> searchProjectsByClientName(
            @PathVariable String clientName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<GenerateProject> searchResult = generateProjectRepository
                    .findByClientNameContainingIgnoreCase(clientName, PageRequest.of(page, size));
            return ResponseEntity.ok(searchResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	
    
    @GetMapping("/searchProjectsByProjectName/{projectName}")
    public ResponseEntity<Page<GenerateProject>> searchProjectsByprojectName(
            @PathVariable String projectName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<GenerateProject> searchResult = generateProjectRepository
                    .findByClientNameContainingIgnoreCase(projectName, PageRequest.of(page, size));
            return ResponseEntity.ok(searchResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    
    
    
    
  
    
    @GetMapping("/getProjectById/{projectId}")
    public ResponseEntity<GenerateProject> getProjectById(@PathVariable long projectId) {
        try {
            Optional<GenerateProject> projectOptional = generateProjectRepository.findById(projectId);
            if (projectOptional.isPresent()) {
            	GenerateProject project = projectOptional.get();
                return ResponseEntity.ok(project);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    
    
    
    
    
    
    

    @PutMapping("/updateProject/{projectName}")
    public ResponseEntity<String> updateProjectByName(
            @PathVariable String projectName,
            @RequestBody GenerateProject updatedProject) {
        return service.updateProjectByName(projectName, updatedProject);
    }
    
    
    @PutMapping("/ProjectUpdateById/{projectId}")
    public ResponseEntity<?> updateById(@PathVariable long projectId, @RequestBody GenerateProject generateProjectData) {
        try {
            GenerateProject updatedGenerateProject = service.updateById(projectId, generateProjectData);
            if (updatedGenerateProject != null) {
                return ResponseEntity.ok(updatedGenerateProject);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found for the given ID.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating Project by ID.");
        }
    }

    
    
    
	  @DeleteMapping("/deleteProjectById/{projectId}")
	  public ResponseEntity<String> deleteProject(@PathVariable("projectId") long projectId) {
	      try {
              generateProjectRepository.deleteById(projectId);
	          return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
	      } catch (Exception e) {
	          return new ResponseEntity<>("Not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
	      }
	  }
	  
    
	  
	  
	  
	  

	    
      // @GetMapping("/SearchProjectData")
      // public ResponseEntity<List<GenerateProject>>
      // searchMaster(@RequestParam("searchText") String searchText) {
      // try {
      // List<GenerateProject> searchResult = service.searchByAnyField(searchText);
      // if (searchResult.isEmpty()) {
      // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(searchResult);
      // }
      // return ResponseEntity.ok(searchResult);
      // } catch (Exception e) {
      // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
      // }
      // }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  @GetMapping("/by-employee")
    public Set<GenerateProject> getProjectsByEmployeeId(@RequestParam("employeeId") String employeeId) {
        return service.getProjectsByEmployeeId(employeeId);
    }
	  

	  
	  
	  
    @GetMapping("/search")
    public ResponseEntity<?> searchProjects(
            @RequestParam Long companyId,
            @RequestParam String searchTerm) {
        try {
            List<GenerateProject> projects = generateProjectRepository.searchGenerateProjectByAnyField(companyId,
                    searchTerm);
            if (!projects.isEmpty()) {
                return ResponseEntity.ok(projects);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No projects found with the given criteria.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while searching projects.");
        }
    }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
//
//	    @PostMapping("/save-for-employee")
//	    public ResponseEntity<String> saveProjectForEmployee(@RequestBody GenerateProject generateProject,
//	                                                         @RequestParam Long employeeId) {
//	        try {
//	            // Check if the employee exists
//	            Employee employee = employeeService.getById(employeeId)
//	                    .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
//	            
//	            // Associate the project with the employee
//	            generateProject.setEmployee(employee);
//	            
//	            // Save the project
//	            service.saveGenerateProjectForEmployee(generateProject);
//
//	            return ResponseEntity.ok("Project saved successfully for employee with ID: " + employeeId);
//	        } catch (Exception e) {
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                    .body("Failed to save project for employee with ID: " + employeeId);
//	        }
//	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
//	    @GetMapping("/project/{projectId}/employees")
//	    public ResponseEntity<?> getEmployeesAssignedToProject(@PathVariable Long projectId) {
//	        try {
//	            // Check if the project exists
//	            GenerateProject project = repo.findById(projectId)
//	                    .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));
//	            
//	            // Get the set of employees associated with the project
//	            Set<Employee> employees = new HashSet<>(Arrays.asList(project.getEmployee()));
//	            
//	            return ResponseEntity.ok(employees);
//	        } catch (Exception e) {
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                    .body("Failed to retrieve employees assigned to project with ID: " + projectId);
//	        }
//	    }

	  
	  
	  
	  
	  
	  
	  
	  
//	  
//	  
//	  @PostMapping("/assignEmployeesToProject/{projectId}")
//	  public ResponseEntity<String> assignEmployeesToProject(
//	          @PathVariable long projectId,
//	          @RequestBody Set<Long> employeeIds) {
//	      try {
//	          // Find the project by ID
//	          Optional<GenerateProject> projectOptional = repo.findById(projectId);
//	          if (projectOptional.isPresent()) {
//	              GenerateProject project = projectOptional.get();
//	              
//	              // Fetch employees by their IDs
//	              Set<Employee> employees = new HashSet<>();
//	              for (Long employeeId : employeeIds) {
//	                  Optional<Employee> employeeOptional = employeeService.getById(employeeId);
//	                  if (employeeOptional.isPresent()) {
//	                      employees.add(employeeOptional.get());
//	                  } else {
//	                      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + employeeId);
//	                  }
//	              }
//	              
//	              // Assign employees to the project
//	              project.setEmployees(employees);
//	              repo.save(project);
//	              
//	              return ResponseEntity.ok("Employees assigned successfully to the project.");
//	          } else {
//	              return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found with ID: " + projectId);
//	          }
//	      } catch (Exception e) {
//	          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to assign employees to the project.");
//	      }
//	  }
//
//	  
	  


  // Remove all employees associated with the given projectId
//   @DeleteMapping("/removeAllEmployees/{projectId}")
//   public ResponseEntity<String> removeAllEmployeesFromProject(@PathVariable Long projectId) {
//       Optional<GenerateProject> projectOpt = generateProjectRepository.findById(projectId);

//       if (projectOpt.isPresent()) {
//           GenerateProject project = projectOpt.get();

//           // Get all employees associated with the project
//           Set<Employee> employees = project.getEmployees();

//           // Remove the project from each employee's set of projects
//           for (Employee employee : employees) {
//               employee.getGenerateProjects().remove(project);
//           }

//           // Clear the project’s employee set to remove the association from the join table
//           project.getEmployees().clear();

//           // Save changes to both sides to ensure the join table is updated
//           generateProjectRepository.save(project);
//           for (Employee employee : employees) {
//               employeeService.save(employee);
//           }

//           return ResponseEntity.ok("All employees removed from the project successfully.");
//       } else {
//           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
//       }
//   }
	  

// Remove all employees associated with the given projectId and delete the project
@DeleteMapping("/removeAllEmployeesAndDeleteProject/{projectId}")
public ResponseEntity<String> removeAllEmployeesAndDeleteProject(@PathVariable Long projectId) {
    Optional<GenerateProject> projectOpt = generateProjectRepository.findById(projectId);

    if (projectOpt.isPresent()) {
        GenerateProject project = projectOpt.get();

        // Get all employees associated with the project
        Set<Employee> employees = project.getEmployees();

        // Remove the project from each employee's set of projects
        for (Employee employee : employees) {
            employee.getGenerateProjects().remove(project);
            // Save the employee to reflect the change
            employeeService.save(employee);
        }

        // Clear the project’s employee set to remove the association from the join table
        project.getEmployees().clear();

        // Save the project to reflect the removal of employees
        generateProjectRepository.save(project);

        // Now delete the project itself
        generateProjectRepository.delete(project);

        return ResponseEntity.ok("All employees removed from the project, and the project deleted successfully.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
    }
}

@DeleteMapping("/removeEmployeeFromProject/{projectId}/{employeeId}")
public ResponseEntity<String> removeEmployeeFromProject(@PathVariable Long projectId, @PathVariable Long employeeId) {
    // Find the project by projectId
    Optional<GenerateProject> projectOpt = generateProjectRepository.findById(projectId);
    if (!projectOpt.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
    }

    // Find the employee by employeeId
    Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
    if (!employeeOpt.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
    }

    GenerateProject project = projectOpt.get();
    Employee employee = employeeOpt.get();

    // Check if the employee is part of the project
    if (project.getEmployees().contains(employee)) {
        // Remove the project from the employee's set of projects
        employee.getGenerateProjects().remove(project);
        employeeRepository.save(employee); // Save the updated employee

        // Remove the employee from the project's employee set
        project.getEmployees().remove(employee);
        generateProjectRepository.save(project); // Save the updated project

        return ResponseEntity.ok("Employee removed from the project successfully.");
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee is not part of this project.");
    }
}

}
