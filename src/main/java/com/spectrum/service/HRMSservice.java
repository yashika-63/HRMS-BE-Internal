package com.spectrum.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.spectrum.model.HRMSmaster;
import com.spectrum.model.Master;
import com.spectrum.repository.HRMSrepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HRMSservice {
    HRMSrepo masterRepo;
    public List<HRMSmaster> getAllMasters() {
    return masterRepo.findAll();
}

public Optional<HRMSmaster> getMasterById(Long id) {
    return masterRepo.findById(id);
}

public HRMSmaster createMaster(HRMSmaster master) {
    return masterRepo.save(master);
}

// public HRMSmaster updateMaster(Long id, HRMSmaster masterDetails) {
//     Optional<HRMSmaster> optionalMaster = masterRepo.findById(id);
//     if (optionalMaster.isPresent()) {
//         Master master = optionalMaster.get();
//         master.setKeyvalue(masterDetails.getKeyvalue());
//         master.setData(masterDetails.getData());
//         return masterRepo.saveAll(master);
//     } else {
//         throw new RuntimeException("Master not found with id " + id);
//     }
// }

public void deleteMaster(Long id) {
    masterRepo.deleteById(id);
}

public List<HRMSmaster> callByKey(String val1){
  return  masterRepo.findBykeyvalue(val1);
}

public void deleteMastersByKey(String keyvalue) {
    List<HRMSmaster> mastersToDelete = masterRepo.findBykeyvalue(keyvalue);
    masterRepo.deleteAll(mastersToDelete);
}
}
