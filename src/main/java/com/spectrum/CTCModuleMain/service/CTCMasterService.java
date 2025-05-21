package com.spectrum.CTCModuleMain.service;

import com.spectrum.CTCModuleMain.model.CTCMaster;
import com.spectrum.CTCModuleMain.repository.CTCMasterRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CTCMasterService {

    @Autowired
    private CTCMasterRepository ctcMasterRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    // Save multiple CTCMaster entries
public List<CTCMaster> saveMultipleCTCMasters(Long companyId, List<CTCMaster> ctcMasters) {
    CompanyRegistration company = companyRegistrationRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));

    // Set the company for each CTCMaster and save them
    ctcMasters.forEach(ctcMaster -> ctcMaster.setCompany(company));
    return ctcMasterRepository.saveAll(ctcMasters);
}


    // Update CTCMaster by ID
    public CTCMaster updateCTCMaster(Long id, String label, String category, double percentValue ) {
        CTCMaster ctcMaster = ctcMasterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CTCMaster not found with id: " + id));
        ctcMaster.setLabel(label);
        ctcMaster.setCategory(category);
        ctcMaster.setPercentValue(percentValue);

        return ctcMasterRepository.save(ctcMaster);
    }

    // Get CTCMaster by companyId and category
    public List<CTCMaster> getCTCMastersByCompanyIdAndCategory(Long companyId, String category) {
        return ctcMasterRepository.findByCompanyIdAndCategory(companyId, category);
    }

    // Get all CTCMaster entries by companyId
    public List<CTCMaster> getCTCMastersByCompanyId(Long companyId) {
        return ctcMasterRepository.findByCompanyId(companyId);
    }




    public void deleteCTCMastersByCompanyIdAndCategory(Long companyId, String category) {
        ctcMasterRepository.deleteByCompanyIdAndCategory(companyId, category);
    }
}
