package com.spectrum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.model.ItAsset;
import com.spectrum.repository.ItAssetRepository;

@Service
public class ItAssetService {

	
	@Autowired
	private ItAssetRepository ItAssetRepository;
	
	 public ItAsset saveItAssettForEmployee(ItAsset ItAsset) {
	        return ItAssetRepository.save(ItAsset);
	    }
	    
	
	    
}
