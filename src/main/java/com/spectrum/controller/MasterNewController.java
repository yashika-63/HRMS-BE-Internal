package com.spectrum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.model.Master;
import com.spectrum.repository.MasterRepository;

@RestController
// @CrossOrigin("http://localhost:3000")
@RequestMapping("/api/masternew")
public class MasterNewController {

	
	@Autowired
	private MasterRepository MasterRepository;
	
	
	@PostMapping ("/MasterDataSave")
	public ResponseEntity<String> saveMaster(@RequestBody Master Master ){
		try {
			MasterRepository.save(Master);
		}
		catch(Exception e) {
			return new ResponseEntity<String> ("not inserted", HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		return new ResponseEntity<String> (Master+"sucessfully inserted", HttpStatus.OK); 
		
	}
}
