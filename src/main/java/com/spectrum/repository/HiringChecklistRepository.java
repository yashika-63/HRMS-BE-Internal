package com.spectrum.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.model.HiringChecklist;

public interface HiringChecklistRepository extends JpaRepository<HiringChecklist, Long> {
	 Optional<HiringChecklist> findByEmployeeId(Long employeeId);
}
