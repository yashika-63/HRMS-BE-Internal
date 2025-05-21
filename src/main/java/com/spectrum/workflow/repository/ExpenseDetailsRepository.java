package com.spectrum.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.workflow.model.ExpenseDetails;

public interface ExpenseDetailsRepository extends JpaRepository<ExpenseDetails, Long> {

}
