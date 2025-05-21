package com.spectrum.workflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.workflow.model.ExpenseApproval;

public interface ExpenseApprovalRepository extends JpaRepository<ExpenseApproval, Long> {
    List<ExpenseApproval> findByExpenseManagementId(Long expenseManagementId);

}
