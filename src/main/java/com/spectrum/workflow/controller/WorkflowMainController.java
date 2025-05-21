package com.spectrum.workflow.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.workflow.model.WorkflowMain;
import com.spectrum.workflow.repository.LeaveApplicationRepository;
import com.spectrum.workflow.repository.WorkflowMainRepository;
import com.spectrum.workflow.service.WorkflowMainService;

@RestController
@RequestMapping("/api/workflow")
@CrossOrigin("http://localhost:3000")
public class WorkflowMainController {

    @Autowired
    private WorkflowMainRepository workflowMainRepository;

    @Autowired
    private LeaveApplicationRepository LeaveApplicationRepository;

    @Autowired
    private WorkflowMainService workflowMainService;

    // @PostMapping("/save/{companyId}/{accountId}")
    // public ResponseEntity<WorkflowMain> saveWorkflowMain(
    // @PathVariable long companyId,
    // @PathVariable long accountId,
    // @RequestBody WorkflowMain workflowMain) {
    //
    // workflowMain.setCompanyId(companyId);
    // workflowMain.setAccountId(accountId);
    //
    // WorkflowMain savedWorkflowMain = workflowMainRepository.save(workflowMain);
    // return new ResponseEntity<>(savedWorkflowMain, HttpStatus.CREATED);
    // }
    //
    // @GetMapping("/{companyId}/{accountId}")
    // public ResponseEntity<List<WorkflowMain>> getAllWorkflows(
    // @PathVariable long companyId,
    // @PathVariable long accountId) {
    //
    // List<WorkflowMain> workflows =
    // workflowMainRepository.findByCompanyIdAndAccountId(companyId, accountId);
    // return new ResponseEntity<>(workflows, HttpStatus.OK);
    // }
    //
    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<String> handleException(Exception e) {
    // return new ResponseEntity<>(e.getMessage(),
    // HttpStatus.INTERNAL_SERVER_ERROR);
    // }

    @PostMapping("/save/{companyId}/{accountId}")
    public ResponseEntity<WorkflowMain> saveWorkflowMain(
            @PathVariable long companyId,
            @PathVariable long accountId,
            @RequestBody WorkflowMain workflowMain) {

        workflowMain.setCompanyId(companyId);
        workflowMain.setAccountId(accountId);

        if (workflowMain.getWorkflowDetails() != null) {
            workflowMain.getWorkflowDetails().forEach(detail -> detail.setWorkflowMain(workflowMain));
        }

        WorkflowMain savedWorkflowMain = workflowMainRepository.save(workflowMain);
        return new ResponseEntity<>(savedWorkflowMain, HttpStatus.CREATED);
    }
 
    @GetMapping("/{companyId}/{accountId}")
    public ResponseEntity<List<WorkflowMain>> getAllWorkflows(
            @PathVariable long companyId,
            @PathVariable long accountId) {

        List<WorkflowMain> workflows = workflowMainRepository.findByCompanyIdAndAccountId(companyId, accountId);
        return new ResponseEntity<>(workflows, HttpStatus.OK);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<List<WorkflowMain>> getAllWorkflows(
            @PathVariable long companyId) {

        List<WorkflowMain> workflows = workflowMainRepository.findByCompanyId(companyId);
        return new ResponseEntity<>(workflows, HttpStatus.OK);
    }

    // @PostMapping("/assign/{workflowId}/{leaveApplicationId}")
    // public ResponseEntity<String> assignWorkflowToLeaveApplication(
    // @PathVariable long workflowId,
    // @PathVariable long leaveApplicationId) {
    //
    // Optional<WorkflowMain> workflowMainOptional =
    // workflowMainRepository.findById(workflowId);
    // Optional<LeaveApplication> leaveApplicationOptional =
    // LeaveApplicationRepository.findById(leaveApplicationId);
    //
    // if (workflowMainOptional.isPresent() && leaveApplicationOptional.isPresent())
    // {
    // LeaveApplication leaveApplication = leaveApplicationOptional.get();
    // leaveApplication.setWorkflowMain(workflowMainOptional.get());
    // LeaveApplicationRepository.save(leaveApplication);
    // return new ResponseEntity<>("Workflow assigned to Leave Application
    // successfully", HttpStatus.OK);
    // } else {
    // return new ResponseEntity<>("Workflow or Leave Application not found",
    // HttpStatus.NOT_FOUND);
    // }
    // }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteWorkflowMainById(@PathVariable long id) {
        try {
            if (workflowMainRepository.existsById(id)) {
                // Remove references to the WorkflowMain in the LeaveApplication table
                LeaveApplicationRepository.removeWorkflowMainReferences(id);

                // Now delete the WorkflowMain and its associated WorkflowDetails
                workflowMainRepository.deleteById(id);

                return new ResponseEntity<>("WorkflowMain and associated details deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("WorkflowMain not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting WorkflowMain: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/names/{companyId}")
    public ResponseEntity<List<Map<String, Object>>> getWorkflowNamesAndIdsByCompany(
            @PathVariable long companyId) {

        List<Object[]> results = workflowMainRepository.findWorkflowNamesAndIdsByCompanyId(companyId);
        List<Map<String, Object>> response = results.stream()
                .map(record -> Map.of("id", record[0], "workflowName", record[1]))
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<String> handleException(Exception e) {
    // return new ResponseEntity<>(e.getMessage(),
    // HttpStatus.INTERNAL_SERVER_ERROR);
    // }

    @DeleteMapping("/deleteWorkflowMainById/{id}")  
    public ResponseEntity<?> deleteWorkflowMainById(@PathVariable Long id) {
        try {
            workflowMainService.deleteWorkflowMainById(id);
            return ResponseEntity.ok("Workflow record deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the record.");
        }
    }


    @GetMapping("/getWorkflowById/{id}")
    public ResponseEntity<WorkflowMain> getWorkflowMainById(@PathVariable Long id) {
        try {
            // Find the WorkflowMain by its ID
            WorkflowMain workflowMain = workflowMainRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("WorkflowMain not found with id: " + id));

            return new ResponseEntity<>(workflowMain, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Return a NOT FOUND response if the WorkflowMain is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Handle any other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }






    @PutMapping("/update/{id}")
public ResponseEntity<WorkflowMain> updateWorkflowMainById(
        @PathVariable long id, 
        @RequestBody WorkflowMain updatedWorkflowMain) {
    try {
        // Find the existing WorkflowMain by ID
        WorkflowMain existingWorkflowMain = workflowMainRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkflowMain not found with id: " + id));

        // Update fields of the existing entity
        existingWorkflowMain.setThisWorkflowId(updatedWorkflowMain.getThisWorkflowId());
        existingWorkflowMain.setWorkflowName(updatedWorkflowMain.getWorkflowName());
        existingWorkflowMain.setCompanyId(updatedWorkflowMain.getCompanyId());
        existingWorkflowMain.setAccountId(updatedWorkflowMain.getAccountId());
        existingWorkflowMain.setWorkflowActivityStatus(updatedWorkflowMain.isWorkflowActivityStatus());

        // Handle WorkflowDetails
        if (updatedWorkflowMain.getWorkflowDetails() != null) {
            // Clear existing WorkflowDetails and add the new ones
            existingWorkflowMain.getWorkflowDetails().clear();
            updatedWorkflowMain.getWorkflowDetails().forEach(detail -> {
                detail.setWorkflowMain(existingWorkflowMain); // Set reference to WorkflowMain
                existingWorkflowMain.getWorkflowDetails().add(detail);
            });
        }

        // Save the updated WorkflowMain back to the repository
        WorkflowMain savedWorkflowMain = workflowMainRepository.save(existingWorkflowMain);

        return new ResponseEntity<>(savedWorkflowMain, HttpStatus.OK);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}


}
