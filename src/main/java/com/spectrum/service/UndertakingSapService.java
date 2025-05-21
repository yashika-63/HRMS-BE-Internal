package com.spectrum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spectrum.model.UndertakingSap;
import com.spectrum.repository.UndertakingSapRepository;

@Service
public class UndertakingSapService {

	@Autowired
	private UndertakingSapRepository UndertakingSapRepository;
	
	
		public Page<UndertakingSap> getAllUndertakingSap(int pageNumber, int pageSize) {
	        Pageable pageable = PageRequest.of(pageNumber, pageSize);
	        return UndertakingSapRepository.findAll(pageable);
	    }
		

    public UndertakingSap saveUndertakingSapForEmployee(UndertakingSap UndertakingSap) {
        return UndertakingSapRepository.save(UndertakingSap);
    }
    
}
