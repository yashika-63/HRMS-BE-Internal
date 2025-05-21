package com.spectrum.workflow.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.spectrum.timesheet.modal.TimesheetMain;
import com.spectrum.timesheet.service.TimesheetMainService;
import com.spectrum.workflow.model.ExpenseApproval;

import com.spectrum.workflow.repository.ExpenseApprovalRepository;
import com.spectrum.workflow.service.ExpenseApprovalService;

@RestController
@RequestMapping("/api/ExpenseApproval")
public class ExpenseApprovalController {

    @Autowired
    private ExpenseApprovalService expenseApprovalService;

    @Autowired
    private ExpenseApprovalRepository expenseApprovalRepository;

    @PostMapping("/save/{ExpenseApprovalId}")
    public ResponseEntity<?> saveExpenseApproval(
            @PathVariable Long ExpenseApprovalId,
            @RequestBody ExpenseApproval expenseApproval) {
        try {
            ExpenseApproval savedExpenseApproval = expenseApprovalService.saveAndNotify(ExpenseApprovalId,
                    expenseApproval);
            return new ResponseEntity<>(savedExpenseApproval, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/ExpenseApproval/{id}")
    public ResponseEntity<ExpenseApproval> getExpenseApprovalById(@PathVariable long id) {
        Optional<ExpenseApproval> expense = expenseApprovalRepository.findById(id);
        if (expense.isPresent()) {
            return new ResponseEntity<>(expense.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-expense-management/{expenseManagementId}")
    public ResponseEntity<List<ExpenseApproval>> getAllByExpenseManagementId(@PathVariable Long expenseManagementId) {
        List<ExpenseApproval> approvals = expenseApprovalService.getAllByExpenseManagementId(expenseManagementId);
        return ResponseEntity.ok(approvals);
    }



    @Autowired
    private TimesheetMainService timesheetMainService;

    @PostMapping("/saveTimesheetNew/{employeeId}/{companyId}/{workflowMainId}")
    public ResponseEntity<?> saveTimesheetMain(
            @PathVariable long employeeId,
            @PathVariable long companyId,
            @PathVariable long workflowMainId,
            @RequestBody TimesheetMain timesheetMain) {
        try {
            TimesheetMain savedTimesheetMain = timesheetMainService.saveTimesheetMain(
                    employeeId, companyId, workflowMainId, timesheetMain);
            return ResponseEntity.ok(savedTimesheetMain);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
