package com.spectrum.service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.model.GenerateProject;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.repository.GenerateProjectRepository;
import com.spectrum.workflow.controller.ResourceNotFoundException;
 
@Service
public class GenerateProjectService {
 
	@Autowired
    private GenerateProjectRepository generateProjectRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

	public Page<GenerateProject> getAllGenerateProject(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return generateProjectRepository.findAll(pageable);
    }


 
    public ResponseEntity<String> updateProjectByName(String projectName, GenerateProject updatedProject) {
        try {
            GenerateProject existingProject = generateProjectRepository.findByprojectName(projectName);
            if (existingProject != null) {
                // Update the fields of the existing project with the values from updatedProject
                existingProject.setProId(updatedProject.getProId());
                existingProject.setProjectName(updatedProject.getProjectName());
                existingProject.setProjectLead(updatedProject.getProjectLead());
                existingProject.setDeliveryLead(updatedProject.getDeliveryLead());
                existingProject.setProjectType(updatedProject.getProjectType());
                existingProject.setIndustry(updatedProject.getIndustry());
                existingProject.setCurrencyCode(updatedProject.getCurrencyCode());
                
                // Update other fields as needed
 
                // Save the updated project
                generateProjectRepository.save(existingProject);
 
                return ResponseEntity.ok("Project updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update project");
        }





    }




	
    // public List<GenerateProject> searchByAnyField(String searchText) {
    // return repo.searchByAnyField(searchText);
    // }
	
	




    public GenerateProject updateById(long projectId, GenerateProject newGenerateProjectData) {
        Optional<GenerateProject> optionalGenerateProject = generateProjectRepository.findById(projectId);
        if (optionalGenerateProject.isPresent()) {
            GenerateProject existingGenerateProject = optionalGenerateProject.get();
            existingGenerateProject.setProId(newGenerateProjectData.getProId());


            existingGenerateProject.setClientName(newGenerateProjectData.getClientName());
            existingGenerateProject.setProjectName(newGenerateProjectData.getProjectName());
            existingGenerateProject.setProjectLead(newGenerateProjectData.getProjectLead());
            existingGenerateProject.setProjectType(newGenerateProjectData.getProjectType());
            existingGenerateProject.setDeliveryLead(newGenerateProjectData.getDeliveryLead());
           
            existingGenerateProject.setIndustry(newGenerateProjectData.getIndustry());
            existingGenerateProject.setProjectStatus(newGenerateProjectData.getProjectStatus());

            existingGenerateProject.setTechnologies(newGenerateProjectData.getTechnologies());
            existingGenerateProject.setTotalEffort(newGenerateProjectData.getTotalEffort());
            existingGenerateProject.setCurrencyCode(newGenerateProjectData.getCurrencyCode());
            existingGenerateProject.setTotalCost(newGenerateProjectData.getTotalCost());
            existingGenerateProject.setCityLocation(newGenerateProjectData.getCityLocation());
            existingGenerateProject.setCurrentPhase(newGenerateProjectData.getCurrentPhase());
            existingGenerateProject.setWorkType(newGenerateProjectData.getWorkType());
            existingGenerateProject.setShift(newGenerateProjectData.getShift());

            existingGenerateProject.setStartDate(newGenerateProjectData.getStartDate());
            existingGenerateProject.setEndDate(newGenerateProjectData.getEndDate());
            existingGenerateProject.setActualStartDate(newGenerateProjectData.getActualStartDate());
            existingGenerateProject.setExpectedEndDate(newGenerateProjectData.getExpectedEndDate());
            existingGenerateProject.setDescription(newGenerateProjectData.getDescription());
            return generateProjectRepository.save(existingGenerateProject);
        } else {
            return null; // Or you can throw an exception indicating that the project with the given ID was not found
        }
    }

    public GenerateProject saveGenerateProjectForEmployee(GenerateProject generateProject) {
        return generateProjectRepository.save(generateProject);
    }

    public GenerateProject saveProjectByCompanyId(long companyId, GenerateProject project) {
        // Find the company by companyId
        Optional<CompanyRegistration> companyOpt = companyRegistrationRepository.findById(companyId);

        if (companyOpt.isPresent()) {
            CompanyRegistration company = companyOpt.get();
            // Set the company to the project
            project.setCompanyRegistration(company);
            // Save the project
            return generateProjectRepository.save(project);
        } else {
            // Handle the case where the company is not found
            throw new RuntimeException("Company not found with ID: " + companyId);
        }
    }

    public List<GenerateProject> getProjectsByCompanyId(long companyId) {
        return generateProjectRepository.findByCompanyRegistration_Id(companyId);
    }



    public Set<GenerateProject> getProjectsByEmployeeId(String employeeId) {
        return generateProjectRepository.findByEmployees_EmployeeId(employeeId);
    }



    public GenerateProject saveProjectByCompanyIdAndLeads(long companyId, Long projectLeadEmployeeId,
            Long deliveryLeadEmployeeId, GenerateProject project) {
        // Find the company by companyId
        Optional<CompanyRegistration> companyOpt = companyRegistrationRepository.findById(companyId);
        if (!companyOpt.isPresent()) {
            throw new RuntimeException("Company not found with ID: " + companyId);
        }

        // Find the project lead employee
        Optional<Employee> projectLeadOpt = employeeRepository.findById(projectLeadEmployeeId);
        if (!projectLeadOpt.isPresent()) {
            throw new RuntimeException("Project Lead Employee not found with ID: " + projectLeadEmployeeId);
        }

        // Find the delivery lead employee
        Optional<Employee> deliveryLeadOpt = employeeRepository.findById(deliveryLeadEmployeeId);
        if (!deliveryLeadOpt.isPresent()) {
            throw new RuntimeException("Delivery Lead Employee not found with ID: " + deliveryLeadEmployeeId);
        }

        // Set the company and leads to the project
        project.setCompanyRegistration(companyOpt.get());
        project.setEmployee(projectLeadOpt.get()); // Set the project lead employee
        project.setEmployeetwo(deliveryLeadOpt.get()); // Set the delivery lead employee

        // Save the project
        return generateProjectRepository.save(project);
    }






    public ResponseEntity<String> removeEmployeeFromProject(Long projectId, Long employeeId) {
        Optional<GenerateProject> projectOpt = generateProjectRepository.findById(projectId);
        if (!projectOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
        }
    
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        if (!employeeOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
        }
    
        GenerateProject project = projectOpt.get();
        Employee employee = employeeOpt.get();
    
        if (project.getEmployees().contains(employee)) {
            employee.getGenerateProjects().remove(project);
            employeeRepository.save(employee);
    
            project.getEmployees().remove(employee);
            generateProjectRepository.save(project);
    
            return ResponseEntity.ok("Employee removed from the project successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee is not part of this project.");
        }
    }
    
}

