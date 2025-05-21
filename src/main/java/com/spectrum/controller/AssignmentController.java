package com.spectrum.controller;

import com.spectrum.model.Employee;
import com.spectrum.model.GenerateProject;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.repository.GenerateProjectRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
// @CrossOrigin("http://localhost:3000")
@RequestMapping("/api")
public class AssignmentController {
	
	@Autowired
	private EmployeeRepository EmployeeRepository;
	
	@Autowired
	private GenerateProjectRepository GenerateProjectRepository;
	
	@Autowired
    public AssignmentController(EmployeeRepository employeeRepository, GenerateProjectRepository GenerateProjectRepository) {
        this.EmployeeRepository = employeeRepository;
        this.GenerateProjectRepository = GenerateProjectRepository;
    }
	
	 @PostMapping("/assign-project/{employeeId}/{projectId}")
	    public ResponseEntity<String> assignProjectToEmployee(@PathVariable("employeeId") Long employeeId,
	                                                           @PathVariable("projectId") Long projectId) {
	        Employee employee = EmployeeRepository.findById(employeeId).orElse(null);
	        GenerateProject project = GenerateProjectRepository.findById(projectId).orElse(null);

	        if (employee == null || project == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee or Project not found");
	        }

	        // Add the project to the employee's assigned projects
	        employee.getGenerateProjects().add(project);
	        EmployeeRepository.save(employee);

	        // Add the employee to the project's assigned employees
	        project.getEmployees().add(employee);
	        GenerateProjectRepository.save(project);

	        return ResponseEntity.status(HttpStatus.OK).body("Project assigned to Employee successfully");
	    }	

		@GetMapping("/project/{projectId}/employees")
		public ResponseEntity<Set<Employee>> getEmployeesByProject(@PathVariable("projectId") Long projectId) {
			GenerateProject project = GenerateProjectRepository.findById(projectId).orElse(null);

			if (project == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			Set<Employee> employees = project.getEmployees();
			return ResponseEntity.status(HttpStatus.OK).body(employees);
		}

		@GetMapping("/employee/{employeeId}/projects")
		public ResponseEntity<List<String>> getProjectsByEmployee(@PathVariable("employeeId") Long employeeId) {
			Employee employee = EmployeeRepository.findById(employeeId).orElse(null);

			if (employee == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			List<String> projectNames = employee.getGenerateProjects().stream()
					.map(GenerateProject::getProjectName)
					.collect(Collectors.toList());

			return ResponseEntity.status(HttpStatus.OK).body(projectNames);
		}
}



//@Autowired
//private EmployeeRepository employeeRepository;
//
//@Autowired
//private GenerateProjectRepository projectRepository;
//
//// Assign a project to an employee
//@PostMapping("/assignProjectToEmployee/{employeeId}/{projectId}")
//public ResponseEntity<String> assignProjectToEmployee(@PathVariable long employeeId, @PathVariable long projectId) {
//  try {
//      Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
//      Optional<GenerateProject> optionalProject = projectRepository.findById(projectId);
//
//      if (optionalEmployee.isPresent() && optionalProject.isPresent()) {
//          Employee employee = optionalEmployee.get();
//          GenerateProject GenerateProject = optionalProject.get();
//
//          // Add project to employee
//          employee.getProjects().add(GenerateProject);
//          employeeRepository.save(employee);
//
//          return ResponseEntity.ok("Project assigned to employee successfully.");
//      } else {
//          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee or project not found for the given ID.");
//      }
//  } catch (Exception e) {
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while assigning project to employee.");
//  }
//}
//
//// Assign an employee to a project
//@PostMapping("/assignEmployeeToProject/{projectId}/{employeeId}")
//public ResponseEntity<String> assignEmployeeToProject(@PathVariable long projectId, @PathVariable long employeeId) {
//  try {
//      Optional<GenerateProject> optionalProject = projectRepository.findById(projectId);
//      Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
//
//      if (optionalProject.isPresent() && optionalEmployee.isPresent()) {
//      	GenerateProject project = optionalProject.get();
//          Employee employee = optionalEmployee.get();
//
//          // Add employee to project
//          project.getEmployees().add(employee);
//          projectRepository.save(project);
//
//          return ResponseEntity.ok("Employee assigned to project successfully.");
//      } else {
//          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project or employee not found for the given ID.");
//      }
//  } catch (Exception e) {
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while assigning employee to project.");
//  }
//}

//
//private final EmployeeService employeeService;
//private final GenerateProjectService generateProjectService;
//
//@Autowired
//public AssignmentController(EmployeeService employeeService, GenerateProjectService generateProjectService) {
//  this.employeeService = employeeService;
//  this.generateProjectService = generateProjectService;
//}
//
//@PostMapping("/assign-project/{employeeId}/{projectId}")
//public ResponseEntity<String> assignProjectToEmployee(@PathVariable("employeeId") Long employeeId,
//                                                     @PathVariable("projectId") Long projectId) {
//  Employee employee = employeeService.getEmployeeById(employeeId);
//  GenerateProject project = generateProjectService.getProjectById(projectId);
//
//  if (employee == null || project == null) {
//      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee or Project not found");
//  }
//
//  // Add the project to the employee's assigned projects
//  employee.getProjects().add(project);
//  employeeService.saveOrUpdateEmployee(employee);
//
//  // Add the employee to the project's assigned employees
//  project.getEmployees().add(employee);
//  generateProjectService.saveOrUpdateProject(project);
//
//  return ResponseEntity.status(HttpStatus.OK).body("Project assigned to Employee successfully");
//}
//}
//
//