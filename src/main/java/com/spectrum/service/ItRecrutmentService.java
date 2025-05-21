package com.spectrum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.model.ItRecrutment;
import com.spectrum.repository.ItRecrutmentRepository;

@Service
public class ItRecrutmentService {

	
	
	
	@Autowired
	private ItRecrutmentRepository ItRecrutmentRepository;
	
	 public ItRecrutment saveItRecrutmentForEmployee(ItRecrutment ItRecrutment) {
	        return ItRecrutmentRepository.save(ItRecrutment);
	    }
	    
	
}
