package com.spectrum.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spectrum.model.AccountMaster;
import com.spectrum.model.Master;
import com.spectrum.repository.AccountMasterRepository;
import com.spectrum.repository.MasterRepository;
import com.spectrum.service.MasterService;

@Controller
// @CrossOrigin("http://localhost:3000")
@RequestMapping("/api/master1")
public class MasterController {

	@Autowired
	private MasterRepository repo;
	
	@Autowired
	private MasterService service;
	
	// @PostMapping ("/MasterDataSave")
	// public ResponseEntity<String> saveInvestorRelationr(@RequestBody Master Master){
	// 	try {
	// 	repo.save(Master);
	// 	}
	// 	catch(Exception e) {
	// 		return new ResponseEntity<String> ("not inserted", HttpStatus.INTERNAL_SERVER_ERROR); 
	// 	}
	// 	return new ResponseEntity<String> (Master+"sucessfully inserted", HttpStatus.OK); 
		
	// }
	
	


	@Autowired
	private AccountMasterRepository accountMasterRepository;

	@PostMapping("/saveMasterData/{accountId}")
	public ResponseEntity<String> saveMasterData(@PathVariable("accountId") long accountId,
			@RequestBody Master masterData) {
		try {
			// Find the AccountMaster by accountId
			AccountMaster accountMaster = accountMasterRepository.findById(accountId)
					.orElseThrow(() -> new RuntimeException("AccountMaster not found with id " + accountId));

			// Set the AccountMaster in the Master entity
			masterData.setAccountMaster(accountMaster);

			// Save the Master entity
			repo.save(masterData);

			return new ResponseEntity<>("Master data successfully saved", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Failed to save Master data", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping ("/GetAllData")
	public ResponseEntity<List<Master>> getMaster(){
		List<Master> list = null;
		try {
		list =   repo.findAll();
		}
		catch(Exception e) {
			return new ResponseEntity<List<Master>> (list, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		return new ResponseEntity<List<Master>> (list, HttpStatus.OK); 
	
}

	@GetMapping("/GetDataByKey/{keyvalue}")
	public ResponseEntity<List<Master>> searchDataBykeyvalue(@PathVariable String keyvalue) {
	    try {
	        List<Master> searchResult = repo.findByKeyvalueContainingIgnoreCase(keyvalue);
	        return ResponseEntity.ok(searchResult);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}


    @PutMapping("/updateMasterDataByKeyValue/{keyvalue}")
    public ResponseEntity<String> updatekeyvalue(
            @PathVariable String keyvalue,
            @RequestBody Master updatedata) {
        return service.updateDataByKeyValue(keyvalue, updatedata);
    }
	

	  
	  @DeleteMapping("/deleteMasterData/{masterId}")
	  public ResponseEntity<String> deleteMaster(@PathVariable("masterId") long masterId) {
	      try {
	          repo.deleteById(masterId);
	          return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
	      } catch (Exception e) {
	          return new ResponseEntity<>("Not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
	      }
	  }
	  

		// @PutMapping("/MasterDataUpadteByID/{masterId}")
		// public ResponseEntity<?> updateById(@PathVariable long masterId, @RequestBody Master masterData) {
		// 	try {
		// 		ResponseEntity<?> response = service.updateById(masterId, masterData);
		// 		return response;
		// 	} catch (Exception e) {
		// 		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		// 				.body("Error occurred while updating Master by ID.");
		// 	}
		// }
	    
		@PutMapping("/MasterDataUpadteByID/{masterId}")
		public ResponseEntity<String> updateById(@PathVariable int masterId, @RequestBody Master masterData) {
			Optional<Master> updated = service.updateById(masterId, masterData);
			if (updated.isPresent()) {
				return ResponseEntity.ok("master data updated successfully.");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Education not found with ID: " + masterId);
			}
		}

	    @GetMapping("/SearchMasterData")
	    public ResponseEntity<List<Master>> searchMaster(@RequestParam("searchText") String searchText) {
	        try {
	            List<Master> searchResult = service.searchByAnyField(searchText);
	            if (searchResult.isEmpty()) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(searchResult);
	            }
	            return ResponseEntity.ok(searchResult);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
	  
	  
	    
	    
	//     @PostMapping ("/MasterDatasaveForUs")
	// public ResponseEntity<String> saveInvestorRelationr(@RequestBody Master Master){
	// 	try {
	// 	repo.save(Master);
	// 	}
	// 	catch(Exception e) {
	// 		return new ResponseEntity<String> ("not inserted", HttpStatus.INTERNAL_SERVER_ERROR); 
	// 	}
	// 	return new ResponseEntity<String> (Master+"sucessfully inserted", HttpStatus.OK); 
		
	// }
	// @PostMapping ("/MasterClassSave")
	// public ResponseEntity<String> saveMasterClass(@RequestBody Master Master){
	// 	try {
	// 	repo.save(Master);
	// 	}
	// 	catch(Exception e) {
	// 		return new ResponseEntity<String> ("not inserted", HttpStatus.INTERNAL_SERVER_ERROR); 
	// 	}
	// 	return new ResponseEntity<String> (Master+"sucessfully inserted", HttpStatus.OK); 
		
	// }
	    
	
}
