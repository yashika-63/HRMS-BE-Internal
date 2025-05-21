package com.spectrum.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spectrum.model.AccountMaster;
import com.spectrum.model.Education;
import com.spectrum.model.Master;
import com.spectrum.repository.AccountMasterRepository;
import com.spectrum.repository.MasterRepository;
@Service
public class MasterService {

	
	
	
	@Autowired
	private MasterRepository repo;
    @Autowired
    private AccountMasterRepository accountMasterRepository;
	
	public List<Master> searchByAnyField(String searchText) {
        return repo.searchByAnyField(searchText);
    }
	
	
    public void saveMasterWithCompany(Long companyId, Master master) throws Exception {
        AccountMaster accountMaster = accountMasterRepository.findById(companyId)
                .orElseThrow(() -> new Exception("Company with ID " + companyId + " not found"));

        master.setAccountMaster(accountMaster);
        repo.save(master);
    }
	

    public ResponseEntity<String> updateDataByKeyValue(String keyvalue, Master updatedData) {
        try {
            Master existingData = repo.findByKeyvalue(keyvalue);
            if (existingData != null) {
                // Update the fields of the existing data with the values from updatedData
                existingData.setKeyvalue(updatedData.getKeyvalue());
                existingData.setData(updatedData.getData());
                // Update other fields as needed

                // Save the updated data
                repo.save(existingData);

                return ResponseEntity.ok("Data updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update data");
        }
    }
    

    // public ResponseEntity<?> updateById(long masterId, Master masterData) {
    // try {
    // Optional<Master> optionalMaster = repo.findById(masterId);
    // if (optionalMaster.isPresent()) {
    // Master existingMaster = optionalMaster.get();

    // // Update only the fields that should be updated
    // existingMaster.setKeyvalue(masterData.getKeyvalue());
    // existingMaster.setData(masterData.getData());
    // existingMaster.setCategory(masterData.getCategory());

    // // Preserve existing employee if not provided in the updatedEducation

    // // Do not update the accountMaster field
    // // existingMaster.setAccountMaster(masterData.getAccountMaster()); // This
    // line
    // // is intentionally commented out

    // repo.save(existingMaster);

    // return ResponseEntity.ok(existingMaster);
    // } else {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Master not found for
    // the given ID.");
    // }
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error
    // occurred while updating Master by ID.");
    // }
    // }

    public Optional<Master> updateById(long masterId, Master masterData) {
        return repo.findById(masterId)
                .map(existingMaster -> {
                    // Update only the fields that should be updated
                    existingMaster.setKeyvalue(masterData.getKeyvalue());
                    existingMaster.setData(masterData.getData());
                    existingMaster.setCategory(masterData.getCategory());
    
                    // Preserve existing accountMaster if not provided in the updatedMasterData
                    if (masterData.getAccountMaster() != null) {
                        existingMaster.setAccountMaster(masterData.getAccountMaster());
                    }

                    return repo.save(existingMaster);
                });
    }
}