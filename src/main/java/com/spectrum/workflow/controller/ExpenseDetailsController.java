package com.spectrum.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.workflow.model.ExpenseDetails;
import com.spectrum.workflow.service.ExpenseDetailsService;

@RestController
@RequestMapping("/api/expensedetails")
public class ExpenseDetailsController {
    @Autowired
    private ExpenseDetailsService expenseDetailsService;


    // Endpoint to save ExpenseDetails for a given ExpenseManagement ID
    @PostMapping("/save/{expenseManagementId}")
    public ExpenseDetails saveForExpenseManagement(@PathVariable Long expenseManagementId,
            @RequestBody ExpenseDetails expenseDetails) {
        return expenseDetailsService.saveForExpenseManagement(expenseManagementId, expenseDetails);
    }

    // Endpoint to update an ExpenseDetails by ID
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDetails> updateExpenseDetails(@PathVariable Long id,
            @RequestBody ExpenseDetails expenseDetails) {
        return expenseDetailsService.updateExpenseDetails(id, expenseDetails);
    }
    
}
