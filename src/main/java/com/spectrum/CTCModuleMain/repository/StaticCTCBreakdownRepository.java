package com.spectrum.CTCModuleMain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.CTCModuleMain.model.StaticCTCBreakdown;

public interface StaticCTCBreakdownRepository extends JpaRepository<StaticCTCBreakdown, Long> {

    // Custom query method to find static breakdowns by CTC Breakdown Header ID
    List<StaticCTCBreakdown> findByCtcBreakdownHeaderId(Long ctcBreakdownHeaderId);
}
