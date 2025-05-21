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

import com.spectrum.model.AccountMaster;
import com.spectrum.repository.AccountMasterRepository;
import com.spectrum.service.AccountMasterService;

@Controller
@RequestMapping("/api/AccountMaster")
    
public class AccountMasterController {

    @Autowired
    private AccountMasterRepository accountMasterRepository;

    @Autowired
    private AccountMasterService accountMasterService;

    @PostMapping("/CreateNewAccount")
    public ResponseEntity<String> saveAccount(@RequestBody AccountMaster accountMaster) {
        try {
            accountMasterRepository.save(accountMaster);
            return new ResponseEntity<>("Account successfully inserted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Not inserted: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/GetAllAccounts")
    public ResponseEntity<List<AccountMaster>> getAllAccounts() {
        try {
            List<AccountMaster> accounts = accountMasterRepository.findAll();
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/GetAccountById/{accountId}")
    public ResponseEntity<?> getById(@PathVariable long accountId) {
        try {
            Optional<AccountMaster> accountMaster = accountMasterService.getById(accountId);
            if (accountMaster.isPresent()) {
                return ResponseEntity.ok(accountMaster.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for the given ID.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching Account by ID: " + e.getMessage());
        }
    }

    @PutMapping("/UpdateAccountById/{accountId}")
    public ResponseEntity<String> updateAccountById(@PathVariable long accountId, @RequestBody AccountMaster accountDetails) {
        try {
            Optional<AccountMaster> accountMasterOptional = accountMasterService.getById(accountId);
            if (accountMasterOptional.isPresent()) {
                AccountMaster accountMaster = accountMasterOptional.get();
                accountMaster.setAccountName(accountDetails.getAccountName());
                accountMaster.setAccountDetails(accountDetails.getAccountDetails());
                accountMasterRepository.save(accountMaster);
                return new ResponseEntity<>("Account successfully updated", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Account not found for the given ID", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/DeleteAccountById/{accountId}")
    public ResponseEntity<String> deleteAccountById(@PathVariable long accountId) {
        try {
            Optional<AccountMaster> accountMasterOptional = accountMasterService.getById(accountId);
            if (accountMasterOptional.isPresent()) {
                accountMasterRepository.deleteById(accountId);
                return new ResponseEntity<>("Account successfully deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Account not found for the given ID", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
