package com.spectrum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.service.CompanyRegistrationService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/CompanyRegistartion")
public class CompanyRegistrationController {

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private CompanyRegistrationService companyRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCompany(@RequestParam long accountId,
            @RequestBody CompanyRegistration companyRegistration) {
        String companyAssignId = companyRegistration.getCompanyAssignId();

        if (companyRegistrationService.companyIdExists(companyAssignId)) {
            return new ResponseEntity<>("Company ID already exists", HttpStatus.CONFLICT);
        }

        CompanyRegistration savedCompany = companyRegistrationService.saveCompanyWithAccount(accountId,
                companyRegistration);
        return ResponseEntity.ok(savedCompany);
    }

    @GetMapping("/GetAllCompanies")
    public ResponseEntity<List<CompanyRegistration>> getAllCompanies() {
        try {
            List<CompanyRegistration> companies = companyRegistrationRepository.findAll();
            return new ResponseEntity<>(companies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/GetCompanyByIds")
    public ResponseEntity<List<CompanyRegistration>> getCompanyByIds(@RequestParam String companyAssignId,
            @RequestParam long accountId) {
        List<CompanyRegistration> companies = companyRegistrationRepository
                .findByCompanyAssignIdAndAccountMaster_AccountId(companyAssignId, accountId);
        if (companies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(companies, HttpStatus.OK);
        }
    }

    @GetMapping("/GetCompanyById")
    public ResponseEntity<CompanyRegistration> getCompanyById(@RequestParam long id) {
        Optional<CompanyRegistration> company = companyRegistrationRepository.findById(id);
        if (company.isPresent()) {
            return new ResponseEntity<>(company.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/UpdateCompanyById")
    public ResponseEntity<CompanyRegistration> updateCompanyById(@RequestParam long id,
            @RequestBody CompanyRegistration companyDetails) {
        Optional<CompanyRegistration> company = companyRegistrationRepository.findById(id);
        if (company.isPresent()) {
            CompanyRegistration existingCompany = company.get();
            existingCompany.setCompanyAssignId(companyDetails.getCompanyAssignId());
            existingCompany.setCompanyName(companyDetails.getCompanyName());
            existingCompany.setCompanyType(companyDetails.getCompanyType());
            // existingCompany.setAccountMaster(companyDetails.getAccountMaster());
            CompanyRegistration updatedCompany = companyRegistrationRepository.save(existingCompany);
            return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/DeleteCompanyById")
    public ResponseEntity<Void> deleteCompanyById(@RequestParam long id) {
        try {
            companyRegistrationRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // this is the method to gett all compoanies by account id

    @GetMapping("/GetCompaniesByAccountId")
    public ResponseEntity<List<CompanyRegistration>> getCompaniesByAccountId(@RequestParam long accountId) {
        List<CompanyRegistration> companies = companyRegistrationRepository.findByAccountMaster_AccountId(accountId);
        if (companies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(companies, HttpStatus.OK);
        }
    }

}
