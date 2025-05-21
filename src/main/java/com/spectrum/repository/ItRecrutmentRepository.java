package com.spectrum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.model.ItRecrutment;
@Repository
public interface ItRecrutmentRepository extends JpaRepository<ItRecrutment, Long> {
	
	Page<ItRecrutment> findByEmployeeId(Long employeeId, Pageable pageable);

}
