package com.spectrum.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.model.AccountMaster;
import com.spectrum.repository.AccountMasterRepository;

@Service

public class AccountMasterService {

    @Autowired
    private AccountMasterRepository AccountMasterRepository;

    public Optional<AccountMaster> getById(long id) {
        return AccountMasterRepository.findById(id);
    }

}
