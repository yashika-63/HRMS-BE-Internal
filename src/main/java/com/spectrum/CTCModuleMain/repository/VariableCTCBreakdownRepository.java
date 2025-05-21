package com.spectrum.CTCModuleMain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.CTCModuleMain.model.VariableCTCBreakdown;

public interface VariableCTCBreakdownRepository extends JpaRepository<VariableCTCBreakdown, Long> {
    // Custom query method to find variable breakdowns by CTC Breakdown Header ID
    List<VariableCTCBreakdown> findByCtcBreakdownHeaderId(Long ctcBreakdownHeaderId);
}
