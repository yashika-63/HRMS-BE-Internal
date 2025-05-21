package com.spectrum.workflow.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spectrum.workflow.model.ExpenseDetails;
import com.spectrum.workflow.model.ExpenseManagement;
import com.spectrum.workflow.repository.ExpenseDetailsRepository;
import com.spectrum.workflow.repository.ExpenseManagementRepository;

@Service
public class ExpenseDetailsService {

      @Autowired
    private ExpenseDetailsRepository expenseDetailsRepository;

    @Autowired
    private ExpenseManagementRepository expenseManagementRepository;

    // Method to save ExpenseDetails for a given ExpenseManagement ID
    public ExpenseDetails saveForExpenseManagement(Long expenseManagementId, ExpenseDetails expenseDetails) {
        ExpenseManagement expenseManagement = expenseManagementRepository.findById(expenseManagementId)
            .orElseThrow(() -> new RuntimeException("ExpenseManagement not found for this id :: " + expenseManagementId));

        expenseDetails.setExpenseManagement(expenseManagement); // Set the fetched ExpenseManagement
        return expenseDetailsRepository.save(expenseDetails);
    }

    // Method to update an existing ExpenseDetails by ID
    public ResponseEntity<ExpenseDetails> updateExpenseDetails(Long id, ExpenseDetails expenseDetails) {
        Optional<ExpenseDetails> existingExpenseDetails = expenseDetailsRepository.findById(id);
        
        if (existingExpenseDetails.isPresent()) {
            ExpenseDetails updatedExpenseDetails = existingExpenseDetails.get();
            updatedExpenseDetails.setExpenseDate(expenseDetails.getExpenseDate());
            updatedExpenseDetails.setExpenseDescription(expenseDetails.getExpenseDescription());
            updatedExpenseDetails.setExpenseCategory(expenseDetails.getExpenseCategory());
            updatedExpenseDetails.setExpenseCost(expenseDetails.getExpenseCost());
            updatedExpenseDetails.setExpenseTransectionType(expenseDetails.getExpenseTransectionType());

            // No need to change expenseManagement here, keep it as is
            expenseDetailsRepository.save(updatedExpenseDetails);
            return ResponseEntity.ok(updatedExpenseDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
