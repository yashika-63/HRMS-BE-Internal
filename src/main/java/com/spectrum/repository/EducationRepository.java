package com.spectrum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.model.Education;
import com.spectrum.model.Employee;

public interface EducationRepository extends JpaRepository<Education, Integer> {

	List<Education> findByEmployee(Employee employee);
	 
}
