package com.spectrum.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.model.BankDetails;
import com.spectrum.repository.BankDetailsRepository;
import com.spectrum.repository.EmployeeRepository;

@Service
public class BankDetailsService {

    @Autowired
    private BankDetailsRepository bankDetailsRepository;

    public BankDetails saveBankDetails(BankDetails bankDetails) {
        if (bankDetails.getEmployee() != null) {
            return bankDetailsRepository.save(bankDetails);
        } else {
            throw new IllegalArgumentException("Bank details must be associated with an employee.");
        }
    }

    public Optional<BankDetails> getBankDetailsById(Long id) {
        return bankDetailsRepository.findById(id);
    }

    public BankDetails updateBankDetails(Long id, BankDetails updatedBankDetails) {
        BankDetails bankDetails = bankDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bank details not found with ID: " + id));

        bankDetails.setBankName(updatedBankDetails.getBankName());
        bankDetails.setBranch(updatedBankDetails.getBranch());
        bankDetails.setAccountHolderName(updatedBankDetails.getAccountHolderName());
        bankDetails.setAccountNumber(updatedBankDetails.getAccountNumber());
        bankDetails.setAccountifscCode(updatedBankDetails.getAccountifscCode());
        bankDetails.setBranchCode(updatedBankDetails.getBranchCode());
        bankDetails.setBranchAdress(updatedBankDetails.getBranchAdress());
        bankDetails.setAccountType(updatedBankDetails.getAccountType());
        bankDetails.setLinkedContactNo(updatedBankDetails.getLinkedContactNo());
        bankDetails.setLinkedEmail(updatedBankDetails.getLinkedEmail());
        // Update other fields as needed

        return bankDetailsRepository.save(bankDetails);
    }

    public void deleteBankDetails(Long id) {
        if (bankDetailsRepository.existsById(id)) {
            bankDetailsRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Bank details not found with ID: " + id);
        }
    }




    // New method to get bank details by employee ID
    public List<BankDetails> getBankDetailsByEmployeeId(Long employeeId) {
        return bankDetailsRepository.findByEmployeeId(employeeId);
    }

}







