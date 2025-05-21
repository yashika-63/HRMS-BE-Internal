package com.spectrum.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.model.UndertakingSap;

public interface UndertakingSapRepository extends JpaRepository<UndertakingSap, String> {
	Page<UndertakingSap> findByEmployeeId(Long employeeId, Pageable pageable);

	  // Method to find undertakings by employeeId
    List<UndertakingSap> findByEmployeeId(Long employeeId);

    // Method to delete undertakings by employeeId
    void deleteByEmployeeId(Long employeeId);
    
    // Method to find undertakings by name
    List<UndertakingSap> findByName(String name);

    // Method to delete undertakings by name
    void deleteByName(String name);
}
