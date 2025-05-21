package com.spectrum.PreOnboarding.EmployeeVerification.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.PreOnboarding.EmployeeVerification.model.Employeedata;
import com.spectrum.PreOnboarding.EmployeeVerification.repository.EmployeedataRepository;
import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;
import com.spectrum.PreOnboarding.PreRegistration.repository.PreRegistrationRepository;
import com.spectrum.PreOnboarding.EmployeeVerification.model.VerificationTicket;
import com.spectrum.PreOnboarding.EmployeeVerification.repository.VerificationTicketRepository;

@Service
public class EmployeedataService {

    @Autowired
    private EmployeedataRepository employeedataRepository;

    @Autowired
    private PreRegistrationRepository preRegistrationRepository;

    @Autowired
    private VerificationTicketRepository verificationTicketRepository;

    public Employeedata saveEmployeedata(Employeedata employeedata, Long preRegistrationId, Long verificationTicketId, String preLoginToken) {
        PreRegistration preRegistration = preRegistrationRepository.findById(preRegistrationId)
                .orElseThrow(() -> new RuntimeException("PreRegistration not found"));
    
        if (!preRegistration.getPreLoginToken().equals(preLoginToken)) {
            throw new RuntimeException("Invalid preLoginToken");
        }
    
        VerificationTicket verificationTicket = verificationTicketRepository.findById(verificationTicketId)
                .orElseThrow(() -> new RuntimeException("VerificationTicket not found"));
    
        // ✅ Check if already exists
        Optional<Employeedata> existingData = employeedataRepository
                .findByPreRegistrationIdAndVerificationTicketId(preRegistrationId, verificationTicketId);
    
        if (existingData.isPresent()) {
            throw new RuntimeException("Employee data already saved for this PreRegistration and VerificationTicket.");
        }
    
        employeedata.setPreRegistration(preRegistration);
        employeedata.setVerificationTicket(verificationTicket);
    
        return employeedataRepository.save(employeedata);
    }
    


    public Employeedata getEmployeeDataById(Long id) {
        return employeedataRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee data not found for ID: " + id));
    }


    public Employeedata getByPreRegistrationIdAndToken(Long preRegistrationId, String preLoginToken) {
        return employeedataRepository.findByPreRegistrationIdAndPreRegistrationPreLoginToken(preRegistrationId, preLoginToken)
            .orElseThrow(() -> new RuntimeException("Employee data not found for the given preRegistrationId and token"));
    }
    
    



    public Employeedata updateEmployeedata(Long id, Employeedata updatedData) {
        return employeedataRepository.findById(id).map(existingData -> {

        if (updatedData.getFirstName() != null)
        existingData.setFirstName(updatedData.getFirstName());

        if (updatedData.getLastName() != null)
        existingData.setLastName(updatedData.getLastName());

        if (updatedData.getMiddleName() != null)
        existingData.setMiddleName(updatedData.getMiddleName());

        if (updatedData.getMotherName() != null)
        existingData.setMotherName(updatedData.getMotherName());

        if (updatedData.getContactNo() != null)
        existingData.setContactNo(updatedData.getContactNo());

        if (updatedData.getContactNoCountryCode() != null)
        existingData.setContactNoCountryCode(updatedData.getContactNoCountryCode());

        if (updatedData.getGender() != null)
        existingData.setGender(updatedData.getGender());

        if (updatedData.getNationality() != null)
        existingData.setNationality(updatedData.getNationality());

        if (updatedData.getDesignation() != null)
        existingData.setDesignation(updatedData.getDesignation());

        if (updatedData.getDepartment() != null)
        existingData.setDepartment(updatedData.getDepartment());

        if (updatedData.getExperience() != 0)
        existingData.setExperience(updatedData.getExperience());

        if (updatedData.getPanNo() != null)
        existingData.setPanNo(updatedData.getPanNo());

        if (updatedData.getAdhaarNo() != null)
        existingData.setAdhaarNo(updatedData.getAdhaarNo());

        if (updatedData.getJoiningDate() != null)
        existingData.setJoiningDate(updatedData.getJoiningDate());

        existingData.setStatus(updatedData.isStatus()); // boolean — always updated

        if (updatedData.getPriorId() != null)
        existingData.setPriorId(updatedData.getPriorId());

        if (updatedData.getEmployeeType() != null)
        existingData.setEmployeeType(updatedData.getEmployeeType());

        if (updatedData.getCurrentHouseNo() != null)
        existingData.setCurrentHouseNo(updatedData.getCurrentHouseNo());

        if (updatedData.getCurrentStreet() != null)
        existingData.setCurrentStreet(updatedData.getCurrentStreet());

        if (updatedData.getCurrentCity() != null)
        existingData.setCurrentCity(updatedData.getCurrentCity());

        if (updatedData.getCurrentState() != null)
        existingData.setCurrentState(updatedData.getCurrentState());

        if (updatedData.getCurrentPostelcode() != null)
        existingData.setCurrentPostelcode(updatedData.getCurrentPostelcode());

        if (updatedData.getCurrentCountry() != null)
        existingData.setCurrentCountry(updatedData.getCurrentCountry());

        if (updatedData.getPermanentHouseNo() != null)
        existingData.setPermanentHouseNo(updatedData.getPermanentHouseNo());

        if (updatedData.getPermanentStreet() != null)
        existingData.setPermanentStreet(updatedData.getPermanentStreet());

        if (updatedData.getPermanentCity() != null)
        existingData.setPermanentCity(updatedData.getPermanentCity());

        if (updatedData.getPermanentState() != null)
        existingData.setPermanentState(updatedData.getPermanentState());

        if (updatedData.getPermanentPostelcode() != null)
        existingData.setPermanentPostelcode(updatedData.getPermanentPostelcode());

        if (updatedData.getPermanentCountry() != null)
        existingData.setPermanentCountry(updatedData.getPermanentCountry());

        if (updatedData.getAge() != 0)
        existingData.setAge(updatedData.getAge());

        if (updatedData.getAlternateEmail() != null)
        existingData.setAlternateEmail(updatedData.getAlternateEmail());

        if (updatedData.getAlternateContactNo() != null)
        existingData.setAlternateContactNo(updatedData.getAlternateContactNo());

        if (updatedData.getAlternateContactNoCountryCode() != null)
        existingData.setAlternateContactNoCountryCode(updatedData.getAlternateContactNoCountryCode());

        if (updatedData.getMaritalStatus() != null)
        existingData.setMaritalStatus(updatedData.getMaritalStatus());

        if (updatedData.getDivision() != null)
        existingData.setDivision(updatedData.getDivision());

        if (updatedData.getRole() != null)
        existingData.setRole(updatedData.getRole());

        if (updatedData.getDateOfBirth() != null)
        existingData.setDateOfBirth(updatedData.getDateOfBirth());
        // Use wrapper class Boolean to check for null
            if (updatedData.isVerificationStatus() != existingData.isVerificationStatus())
                existingData.setVerificationStatus(updatedData.isVerificationStatus());

            // Add more fields below following same pattern...

            return employeedataRepository.save(existingData);

        }).orElseThrow(() -> new RuntimeException("Employee data not found with ID: " + id));
    }


    public Employeedata updateVerificationStatusToTrue(Long id) {
        return employeedataRepository.findById(id).map(existingData -> {
            existingData.setVerificationStatus(true);
            return employeedataRepository.save(existingData);
        }).orElseThrow(() -> new RuntimeException("Employee data not found with ID: " + id));
    }

}
