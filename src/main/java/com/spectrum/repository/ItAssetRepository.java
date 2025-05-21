package com.spectrum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.model.ItAsset;

public interface ItAssetRepository extends JpaRepository<ItAsset, Long> {
	
	Page<ItAsset> findByEmployeeId(Long employeeId, Pageable pageable);

}
