package com.spectrum.workflow.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.workflow.model.*;
import com.spectrum.workflow.repository.LeaveApplicationRepository;
import com.spectrum.workflow.service.LeaveApplicationService;

@RestController
@RequestMapping("/api/leaveApplications")
@CrossOrigin(origins = "http://localhost:3000")
public class LeaveApplicationController {
    // LeaveApplication leaveApplication;
    
    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @PostMapping("/SaveLeaveApplicationForEmployee")
    public ResponseEntity<LeaveApplication> saveLeaveApplication(@RequestParam long companyId,
            @RequestParam long employeeId, @RequestBody LeaveApplication leaveApplication) {
        LeaveApplication savedLeaveApplication = leaveApplicationService
                .saveLeaveApplicationWithCompanyAndEmployee(companyId, employeeId, leaveApplication);
        return ResponseEntity.ok(savedLeaveApplication);
    }

    // Updated method with companyId and employeeId as PathVariable
    @GetMapping("/{id}/company/{companyId}/employee/{employeeId}")
    public ResponseEntity<LeaveApplication> getLeaveApplication(
            @PathVariable long id,
            @PathVariable long companyId,
            @PathVariable long employeeId) {

        Optional<LeaveApplication> leaveApplication = leaveApplicationService.getLeaveApplication(id, employeeId,
                companyId);
        return leaveApplication.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Updated method with companyId and employeeId as PathVariable
    @GetMapping("/company/{companyId}/employee/{employeeId}")
    public ResponseEntity<List<LeaveApplication>> getAllLeaveApplications(
            @PathVariable long companyId,
            @PathVariable long employeeId) {

        List<LeaveApplication> leaveApplications = leaveApplicationService.getAllLeaveApplications(employeeId,
                companyId);
        return ResponseEntity.ok(leaveApplications);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveApplication> updateLeaveApplication(@PathVariable long id, @RequestParam long companyId,
            @RequestParam long employeeId, @RequestBody LeaveApplication leaveApplicationDetails) {
        LeaveApplication updatedLeaveApplication = leaveApplicationService.updateLeaveApplication(id, employeeId,
                companyId, leaveApplicationDetails);
        return ResponseEntity.ok(updatedLeaveApplication);
    }

    @DeleteMapping("/{id}/{companyId}/{employeeId}")
    public ResponseEntity<Void> deleteLeaveApplication(@PathVariable long id, 
                                                       @PathVariable long companyId,
                                                       @PathVariable long employeeId) {
        leaveApplicationService.deleteLeaveApplication(id, employeeId, companyId);
        // Return an empty OK (200) response to avoid the "no content found" issue
        return ResponseEntity.ok().build();
    }

    // Now the following method gets the leave application by employeeid companyid
    // division department and role
    @GetMapping("/GetAllRequest/{workflowDivision}/{workflowDepartment}/{workflowRole}")
    public ResponseEntity<List<LeaveApplication>> getDataByCriteria(
            @RequestParam long companyId,
            @PathVariable String workflowDivision,
            @PathVariable String workflowDepartment,
            @PathVariable String workflowRole) {
        try {
            List<LeaveApplication> data = leaveApplicationService.findByCriteria(companyId, workflowDivision,
                    workflowDepartment, workflowRole);

            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // // these are new methods we required for workflow

    // @PostMapping("/saveLeaveRequestForWorkflowMain/{workflowMainId}")
    // public ResponseEntity<LeaveApplication> saveLeaveRequestForWorkflowMain(
    // @RequestParam long companyId,
    // @RequestParam long employeeId,
    // @PathVariable long workflowMainId,
    // @RequestBody LeaveApplication leaveApplication) {
    // try {
    // LeaveApplication savedLeaveApplication =
    // leaveApplicationService.saveLeaveRequestForWorkflowMain(employeeId,
    // companyId, workflowMainId, leaveApplication);
    // return ResponseEntity.ok(savedLeaveApplication);
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    // }
    // }

    // // @PutMapping("/updateWorkflowDetails/{id}")
    // // public ResponseEntity<LeaveApplication> updateWorkflowDetails(
    // // @PathVariable long id,
    // // @RequestParam long companyId,
    // // @RequestParam long employeeId,
    // // @RequestParam String workflowDivision,
    // // @RequestParam String workflowDepartment,
    // // @RequestParam String workflowRole) {
    // // LeaveApplication updatedLeaveApplication =
    // leaveApplicationService.updateWorkflowDetails(id, companyId, employeeId,
    // workflowDivision, workflowDepartment, workflowRole);
    // // return ResponseEntity.ok(updatedLeaveApplication);
    // // }

    // @PutMapping("/updateWorkflowDetailss/{leaveApplicationId}")
    // public ResponseEntity<LeaveApplication> updateWorkflowDetailss(
    // @PathVariable long leaveApplicationId,
    // @RequestParam long companyId,
    // @RequestParam long employeeId,
    // @RequestParam String workflowDivision,
    // @RequestParam String workflowDepartment,
    // @RequestParam String workflowRole) {

    // LeaveApplication updatedLeaveApplication =
    // leaveApplicationService.updatedWorkflowDetailss(
    // leaveApplicationId, companyId, employeeId, workflowDivision,
    // workflowDepartment, workflowRole);

    // return ResponseEntity.ok(updatedLeaveApplication);
    // }

    // @PostMapping("/decline")
    // public LeaveApplication declineRequest(
    // @RequestParam long leaveAppli cationId,
    // @RequestParam long companyId,
    // @RequestParam long employeeId) {
    // return leaveApplicationService.declineWorkflowDetails(leaveApplicationId,
    // companyId, employeeId);
    // }

    // @PostMapping("/saveLeaveRequestForWorkflowMain/{companyId}/{employeeId}/{workflowMainId}")
    // public ResponseEntity<LeaveApplication> saveLeaveRequestForWorkflowMain(
    // @PathVariable long companyId,
    // @PathVariable long employeeId,
    // @PathVariable long workflowMainId,
    // @RequestBody LeaveApplication leaveApplication) {
    // try {
    // LeaveApplication savedLeaveApplication =
    // leaveApplicationService.saveLeaveRequestForWorkflowMain(employeeId,
    // companyId, workflowMainId, leaveApplication);
    // return ResponseEntity.ok(savedLeaveApplication);
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    // }
    // }

    @PostMapping("/saveLeaveRequestForWorkflowMain/{companyId}/{employeeId}/{workflowMainId}")
    public ResponseEntity<?> saveLeaveRequestForWorkflowMain(
            @PathVariable long companyId,
            @PathVariable long employeeId,
            @PathVariable long workflowMainId,
            @RequestBody LeaveApplication leaveApplication) {
        try {
            LeaveApplication savedLeaveApplication = leaveApplicationService.saveLeaveRequestForWorkflowMain(employeeId,
                    companyId, workflowMainId, leaveApplication);
            return ResponseEntity.ok(savedLeaveApplication);
        } catch (RuntimeException e) {
            // Handle custom runtime exceptions and return their message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Log the exception to help with debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An internal server error occurred: " + e.getMessage());
        }
    }


    @PutMapping("/updateWorkflowDetails/{leaveApplicationId}/{companyId}/{workflowMainId}/{employeeId}/{workflowDivision}/{workflowDepartment}/{workflowRole}")
    public ResponseEntity<LeaveApplication> updateWorkflowDetails(
            @PathVariable long leaveApplicationId,
            @PathVariable long companyId,
            @PathVariable long employeeId,
            @PathVariable long workflowMainId,
            @PathVariable String workflowDivision,
            @PathVariable String workflowDepartment,
            @PathVariable String workflowRole) {
        try {
            LeaveApplication updatedLeaveApplication = leaveApplicationService.updatedWorkflowDetailss(
                    leaveApplicationId, companyId, employeeId,workflowMainId, workflowDivision, workflowDepartment, workflowRole);
            return ResponseEntity.ok(updatedLeaveApplication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/decline/{leaveApplicationId}/{companyId}/{employeeId}")
    public ResponseEntity<LeaveApplication> declineRequest(
            @PathVariable long leaveApplicationId,
            @PathVariable long companyId,
            @PathVariable long employeeId) {
        try {
            LeaveApplication declinedLeaveApplication = leaveApplicationService
                    .declineWorkflowDetails(leaveApplicationId, companyId, employeeId);
            return ResponseEntity.ok(declinedLeaveApplication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/totalLeavesForCurrentYear")
    public ResponseEntity<Integer> getTotalLeavesForCurrentYear(
            @RequestParam long companyId,
            @RequestParam long employeeId) {
        try {
            int totalLeaves = leaveApplicationService.getTotalLeaveDaysForCurrentYear(companyId, employeeId);
            return ResponseEntity.ok(totalLeaves);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/totalApprovedLeaves")
    public ResponseEntity<Integer> getTotalApprovedLeaves(
            @RequestParam("companyId") long companyId,
            @RequestParam("employeeId") long employeeId) {
        int totalApprovedLeaveDays = leaveApplicationService.getTotalApprovedLeaveDays(companyId, employeeId);
        return ResponseEntity.ok(totalApprovedLeaveDays);
    }

    @GetMapping("/leaveCounts")
    public ResponseEntity<Map<String, Integer>> getLeaveCounts(
            @RequestParam("companyId") long companyId,
            @RequestParam("employeeId") long employeeId) {
        Map<String, Integer> leaveCounts = leaveApplicationService.getLeaveCountsByStatus(companyId, employeeId);
        return ResponseEntity.ok(leaveCounts);
    }

    @GetMapping("/leaveCountsByStatusAndType")
    public ResponseEntity<Map<String, Integer>> getLeaveCountsByStatusAndType(
            @RequestParam("companyId") long companyId,
            @RequestParam("employeeId") long employeeId) {
        Map<String, Integer> leaveCounts = leaveApplicationService.getLeaveCountsByStatusAndType(companyId, employeeId);
        return ResponseEntity.ok(leaveCounts);
    }

    // Endpoint to get the latest 10 leave applications by employee ID
    @GetMapping("/latest/{employeeId}")
    public ResponseEntity<List<LeaveApplication>> getLatestLeaveApplications(
            @PathVariable long employeeId) {
        try {
            List<LeaveApplication> leaveApplications = leaveApplicationService.getLatestLeaveApplicationsByEmployeeId(employeeId);
            return ResponseEntity.ok(leaveApplications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/leaveDayCounts")
    public ResponseEntity<Map<String, Integer>> getLeaveDayCounts(
            @RequestParam("companyId") long companyId,
            @RequestParam("employeeId") long employeeId) { 
        Map<String, Integer> leaveDayCounts = leaveApplicationService.getLeaveDaysByStatus(companyId, employeeId);
        return ResponseEntity.ok(leaveDayCounts);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<LeaveApplication> updateLeaveApplicationById(
            @PathVariable long id,
            @RequestBody LeaveApplication leaveApplicationDetails) {
        LeaveApplication updatedLeaveApplication = leaveApplicationService.updateLeaveApplicationById(id,
                leaveApplicationDetails);
        return ResponseEntity.ok(updatedLeaveApplication);
    }

    @GetMapping("/leaves/{id}")
    public ResponseEntity<LeaveApplication> getLeaveApplicationById(@PathVariable long id) {
        Optional<LeaveApplication> leave = leaveApplicationRepository.findById(id);
        if (leave.isPresent()) {
            return new ResponseEntity<>(leave.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

      @GetMapping("/pending/count/{companyId}/{employeeId}")
    public long getPendingLeaveCount(
            @PathVariable long companyId,
            @PathVariable long employeeId) {
        return leaveApplicationService.getPendingLeaveCount(companyId, employeeId);
    }
 
    @GetMapping("/GetPendingRequestCount/{companyId}/{workflowDivision}/{workflowDepartment}/{workflowRole}")
    public ResponseEntity<Long> getPendingRequestCount(
            @PathVariable long companyId,
            @PathVariable String workflowDivision,
            @PathVariable String workflowDepartment,
            @PathVariable String workflowRole) {
        try {
            // Call the service method to get the count of pending leave requests by
            // criteria
            long pendingCount = leaveApplicationService.countPendingRequestsByCriteria(
                    companyId, workflowDivision, workflowDepartment, workflowRole);
 
            // Return the pending count as the response body
            return ResponseEntity.ok(pendingCount);
        } catch (Exception e) {
            // If any error occurs, return INTERNAL_SERVER_ERROR with null as body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
 
    @GetMapping("/GetLeaveApplicationsBetweenDates/{companyId}/{employeeId}")
    public ResponseEntity<List<LeaveApplication>> getLeaveApplicationsBetweenDates(
            @PathVariable long companyId,
            @PathVariable long employeeId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        try {
            // Call the service method to fetch leave applications between the given dates
            List<LeaveApplication> leaveApplications = leaveApplicationService.getLeaveApplicationsBetweenDates(
                    companyId, employeeId, fromDate, toDate);
            return ResponseEntity.ok(leaveApplications);
        } catch (Exception e) {
            // Handle any exceptions
            return ResponseEntity.status(500).body(null);
        }
    }









    //////////////////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// 
    /// 
    /// 
    







    @GetMapping("/GetAllRequestNew/{projectId}/{workflowDivision}/{workflowDepartment}/{workflowRole}")
public ResponseEntity<List<LeaveApplication>> getDataByCriteria(
        @RequestParam long companyId,
        @PathVariable long projectId,
        @PathVariable String workflowDivision,
        @PathVariable String workflowDepartment,
        @PathVariable String workflowRole)
      { // New parameter for projectId
    try {
        List<LeaveApplication> data = leaveApplicationService.findByCriteriaNew(companyId, workflowDivision, workflowDepartment, workflowRole, projectId);
        return ResponseEntity.ok(data);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}





@GetMapping("/GetAllRequestList/{workflowDivision}/{workflowDepartment}/{workflowRole}")
public ResponseEntity<List<LeaveApplication>> getDataByCriteria(
        @RequestParam long companyId,
        @RequestParam List<Long> projectIds, // Accept multiple project IDs
        @PathVariable String workflowDivision,
        @PathVariable String workflowDepartment,
        @PathVariable String workflowRole) {
    try {
        List<LeaveApplication> data = leaveApplicationService.findByCriteriaList(companyId, projectIds, workflowDivision, workflowDepartment, workflowRole);
        return ResponseEntity.ok(data);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

}
