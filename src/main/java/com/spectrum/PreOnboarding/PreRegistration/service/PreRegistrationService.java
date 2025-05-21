package com.spectrum.PreOnboarding.PreRegistration.service;


import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;
import com.spectrum.PreOnboarding.PreRegistration.repository.PreRegistrationRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class PreRegistrationService {

    @Autowired
    private PreRegistrationRepository preRegistrationRepository;
    @Autowired
    private JavaMailSender mailSender;

      @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;



     @Transactional
    public PreRegistration savePreRegistration(PreRegistration preRegistration, Long companyId, Long reportingPersonId) {
        // Validate Company
        Optional<CompanyRegistration> companyOptional = companyRegistrationRepository.findById(companyId);
        if (companyOptional.isEmpty()) {
            throw new IllegalStateException("Company not found with ID: " + companyId);
        }

        // Validate Employee (Reporting Person)
        Optional<Employee> employeeOptional = employeeRepository.findById(reportingPersonId);
        if (employeeOptional.isEmpty()) {
            throw new IllegalStateException("Reporting person (Employee) not found with ID: " + reportingPersonId);
        }

        // Check if email already exists for the same company
        Optional<PreRegistration> existingRegistration = preRegistrationRepository.findByEmailAndCompany(
                preRegistration.getEmail(), companyOptional.get());
        if (existingRegistration.isPresent()) {
            throw new IllegalStateException("Employee with this email is already registered for this company.");
        }

        // Set required fields
        preRegistration.setCompany(companyOptional.get());
        preRegistration.setEmployees(employeeOptional.get());
        preRegistration.setStatus(true);
        preRegistration.setLoginStatus(true);
        preRegistration.setOfferGenerated(false);
        preRegistration.setPreLoginToken(UUID.randomUUID().toString()); // Generate unique preLoginToken

        return preRegistrationRepository.save(preRegistration);
    }

    public PreRegistration login(String email, String preLoginToken) {
        return preRegistrationRepository.findByEmailAndPreLoginTokenAndLoginStatus(email, preLoginToken, true)
            .orElseThrow(() -> new IllegalArgumentException("Invalid email, preLoginToken, or inactive status."));
    }




    public List<PreRegistration> getPreRegistrations(Long companyId, Long reportingPersonId) {
        return preRegistrationRepository.findByCompanyIdAndStatusTrueAndEmployeesIdAndOfferGeneratedFalse(companyId, reportingPersonId);
    }


    public List<PreRegistration> getPreRegistrationsOfferMade(Long companyId, Long reportingPersonId) {
        return preRegistrationRepository.findByCompanyIdAndStatusTrueAndEmployeesIdAndOfferGeneratedTrue(companyId, reportingPersonId);
    }
    
    public List<PreRegistration> getByCompanyIdAndStatus(Long companyId, boolean status) {
        return preRegistrationRepository.findByCompany_IdAndStatus(companyId, status);
    }


    public List<PreRegistration> getPreRegistrations(Long companyId) {
        return preRegistrationRepository.findByCompany_IdAndStatusTrueAndOfferGeneratedFalse(companyId);
    }
    
    public List<PreRegistration> getPreRegistrationsOfferMade(Long companyId) {
        return preRegistrationRepository.findByCompany_IdAndStatusTrueAndOfferGeneratedTrue(companyId);
    }



    
    public Page<PreRegistration> getByCompanyMonthYear(Long companyId, int month, int year, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return preRegistrationRepository.findByCompanyAndMonthAndYear(companyId, month, year, pageable);
    }
    


    
}   




