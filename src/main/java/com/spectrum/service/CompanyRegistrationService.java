package com.spectrum.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.model.AccountMaster;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.AccountMasterRepository;
import com.spectrum.repository.CompanyRegistrationRepository;

@Service

public class CompanyRegistrationService {

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private AccountMasterRepository accountMasterRepository;

    // public CompanyRegistration saveCompanyWithAccount(long accountId,
    // CompanyRegistration companyRegistration) {
    // AccountMaster accountMaster = accountMasterRepository.findById(accountId)
    // .orElseThrow(() -> new RuntimeException("Account not found"));
    //
    // companyRegistration.setAccountMaster(accountMaster);
    // return companyRegistrationRepository.save(companyRegistration);
    // }
    //

    public Optional<CompanyRegistration> findByCompanyId(String companyAssignId) {
        return companyRegistrationRepository.findByCompanyAssignId(companyAssignId);
    }

    public CompanyRegistration saveCompanyWithAccount(long accountId, CompanyRegistration companyRegistration) {
        // Your logic to associate companyRegistration with accountId
        // This part remains unchanged
        AccountMaster accountMaster = accountMasterRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        companyRegistration.setAccountMaster(accountMaster);
        return companyRegistrationRepository.save(companyRegistration);
    }

    public boolean companyIdExists(String companyAssignId) {
        return companyRegistrationRepository.findByCompanyAssignId(companyAssignId).isPresent();
    }

}
