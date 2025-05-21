package com.spectrum.workflow.controller;

import java.util.Date;
import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.workflow.model.ExpenseManagement;
import com.spectrum.workflow.repository.ExpenseManagementRepository;
import com.spectrum.workflow.service.ExpenseManagementService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/expense")
public class ExpenseManagementController {

    @Autowired
    private ExpenseManagementService expenseManagementService;

    @Autowired
    private ExpenseManagementRepository expenseManagementRepository;

    @PostMapping("/saveExpense/{employeeId}/{companyId}/{workflowMainId}")
    public ResponseEntity<ExpenseManagement> saveExpenseManagement(
            @PathVariable long employeeId,
            @PathVariable long companyId,
            @PathVariable long workflowMainId,
            @RequestBody ExpenseManagement expenseManagement) {
        try {
            ExpenseManagement savedExpenseManagement = expenseManagementService.saveExpenseManagement(
                    employeeId, companyId, workflowMainId, expenseManagement);
            return ResponseEntity.ok(savedExpenseManagement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @GetMapping("/GetAllRequest/{workflowDivision}/{workflowDepartment}/{workflowRole}")
    public ResponseEntity<List<ExpenseManagement>> getDataByCriteria(
            @RequestParam long companyId,
            @PathVariable String workflowDivision,
            @PathVariable String workflowDepartment,
            @PathVariable String workflowRole) {
        try {
            List<ExpenseManagement> data = expenseManagementService.findByCriteria(companyId, workflowDivision,
                    workflowDepartment, workflowRole);

            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    


   


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteExpenseManagement(@PathVariable Long id) {
        try {
            expenseManagementService.deleteExpenseManagementById(id);
            return ResponseEntity.ok("ExpenseManagement record deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the record.");
        }
    }
    

    @PutMapping("/updateWorkflowDetails/{expenseManagementId}/{companyId}/{workflowMainId}/{workflowDivision}/{workflowDepartment}/{workflowRole}")
    public ResponseEntity<ExpenseManagement> updateWorkflowDetails(
            @PathVariable long expenseManagementId,
            @PathVariable long companyId,
            @PathVariable long workflowMainId,
            @PathVariable String workflowDivision,
            @PathVariable String workflowDepartment,
            @PathVariable String workflowRole) {
        try {
            ExpenseManagement updatedExpenseManagement = expenseManagementService.updateWorkflowDetails(
                    expenseManagementId, companyId, workflowMainId, workflowDivision, workflowDepartment, workflowRole);
            return ResponseEntity.ok(updatedExpenseManagement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




    @PostMapping("/decline/{expenseManagementId}/{companyId}/{employeeId}")
    public ResponseEntity<ExpenseManagement> declineRequest(
            @PathVariable long expenseManagementId,
            @PathVariable long companyId,
            @PathVariable long employeeId) {
        try {
            ExpenseManagement declinedExpenseManagement = expenseManagementService.declineWorkflowDetails(expenseManagementId, companyId, employeeId);
            return ResponseEntity.ok(declinedExpenseManagement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @GetMapping("/getByEmployeeId/{employeeId}")
    public ResponseEntity<List<ExpenseManagement>> getByEmployeeId(@PathVariable Long employeeId) {
        List<ExpenseManagement> expenses = expenseManagementService.findByEmployeeId(employeeId);
        if (expenses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/latest/{employeeId}")
    public ResponseEntity<List<ExpenseManagement>> getLatestRecordsByEmployeeId(@PathVariable Long employeeId) {
        List<ExpenseManagement> latestExpenses = expenseManagementService
                .findTop10ByEmployeeIdOrderByIdDesc(employeeId);
        if (latestExpenses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(latestExpenses);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ExpenseManagement> updateExpenseManagement(
            @PathVariable Long id,
            @RequestBody ExpenseManagement updatedExpenseManagement) {

        ExpenseManagement updatedExpense = expenseManagementService.updateExpenseManagement(id,
                updatedExpenseManagement);
        return ResponseEntity.ok(updatedExpense);
    }

    @GetMapping("/GetExpenseById")
    public ResponseEntity<ExpenseManagement> getExpenseManagementId(@RequestParam long id) {
        Optional<ExpenseManagement> expense = expenseManagementRepository.findById(id);
        if (expense.isPresent()) {
            return new ResponseEntity<>(expense.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

     @GetMapping("/pending/count/{companyId}/{employeeId}")
    public long getPendingLeaveCount(@PathVariable long companyId, @PathVariable long employeeId) {
        return expenseManagementService.getPendingExpenseCount(companyId, employeeId);
    }
 
    @GetMapping("/GetPendingRequestCount/{companyId}/{workflowDivision}/{workflowDepartment}/{workflowRole}")
    public ResponseEntity<Long> getPendingRequestCount(
            @PathVariable long companyId,
            @PathVariable String workflowDivision,
            @PathVariable String workflowDepartment,
            @PathVariable String workflowRole) {
        try {
            // Call the service method to get the count
            long pendingCount = expenseManagementService.countPendingRequestsByCriteria(
                    companyId, workflowDivision, workflowDepartment, workflowRole);
 
            // Return the count as the response body
            return ResponseEntity.ok(pendingCount);
        } catch (Exception e) {
            // If any error occurs, return INTERNAL_SERVER_ERROR with null as body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
 
    @GetMapping("/getByDateRange/{companyId}/{employeeId}")
    public ResponseEntity<List<ExpenseManagement>> getExpensesByDateRange(
            @PathVariable long companyId,
            @PathVariable long employeeId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
       
        List<ExpenseManagement> expenses = expenseManagementService.getExpensesByDateRange(companyId, employeeId, startDate, endDate);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }





    @GetMapping("/GetAllRequestList/{workflowDivision}/{workflowDepartment}/{workflowRole}")
public ResponseEntity<List<ExpenseManagement>> getDataByCriteria(
        @RequestParam long companyId,
        @RequestParam List<Long> projectIds, // Accept multiple project IDs
        @PathVariable String workflowDivision,
        @PathVariable String workflowDepartment,
        @PathVariable String workflowRole) {
    try {
        List<ExpenseManagement> data = expenseManagementService.findByCriteriaList(companyId, projectIds, workflowDivision, workflowDepartment, workflowRole);
        return ResponseEntity.ok(data);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}





@GetMapping("/getApprovedByEmployeeAndDate")
public List<ExpenseManagement> getApprovedExpenses(
    @RequestParam Long employeeId,
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate
) {
    return expenseManagementService.getApprovedExpensesByEmployeeAndDateRange(employeeId, fromDate, toDate);
}
}
