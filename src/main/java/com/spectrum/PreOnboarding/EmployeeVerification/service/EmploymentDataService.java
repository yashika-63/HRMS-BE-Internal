package com.spectrum.PreOnboarding.EmployeeVerification.service;

import com.spectrum.PreOnboarding.EmployeeVerification.model.EmploymentData;
import com.spectrum.PreOnboarding.EmployeeVerification.model.VerificationTicket;
import com.spectrum.PreOnboarding.EmployeeVerification.repository.EmploymentDataRepository;
import com.spectrum.PreOnboarding.EmployeeVerification.repository.VerificationTicketRepository;
import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;
import com.spectrum.PreOnboarding.PreRegistration.repository.PreRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmploymentDataService {

    @Autowired
    private EmploymentDataRepository employmentDataRepository;

    @Autowired
    private PreRegistrationRepository preRegistrationRepository;

    @Autowired
    private VerificationTicketRepository verificationTicketRepository;

    public List<EmploymentData> saveAllByVerificationAndToken(List<EmploymentData> dataList, Long verificationId, String preLoginToken) {

        PreRegistration preReg = preRegistrationRepository.findByPreLoginToken(preLoginToken)
                .orElseThrow(() -> new RuntimeException("PreRegistration not found for token: " + preLoginToken));

        VerificationTicket ticket = verificationTicketRepository.findById(verificationId)
                .orElseThrow(() -> new RuntimeException("VerificationTicket not found with id: " + verificationId));

        for (EmploymentData data : dataList) {
            data.setPreRegistration(preReg);
            data.setVerificationTicket(ticket);
        }

        return employmentDataRepository.saveAll(dataList);
    }







    public EmploymentData updateEmploymentDataById(Long id, EmploymentData updatedData) {
        EmploymentData existingData = employmentDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EmploymentData not found with id: " + id));
    
        // Check and update only non-null / non-default fields
        if (updatedData.getCompanyName() != null) existingData.setCompanyName(updatedData.getCompanyName());
        if (updatedData.getJobRole() != null) existingData.setJobRole(updatedData.getJobRole());
        if (updatedData.getResponsibilities() != null) existingData.setResponsibilities(updatedData.getResponsibilities());
        if (updatedData.getStartDate() != null) existingData.setStartDate(updatedData.getStartDate());
        if (updatedData.getEndDate() != null) existingData.setEndDate(updatedData.getEndDate());
        if (updatedData.getJobDuration() != null) existingData.setJobDuration(updatedData.getJobDuration());
        if (updatedData.getLatestCtc() != 0) existingData.setLatestCtc(updatedData.getLatestCtc());
        if (updatedData.getSupervisorContact() != null) existingData.setSupervisorContact(updatedData.getSupervisorContact());
        if (updatedData.getReasonOfLeaving() != null) existingData.setReasonOfLeaving(updatedData.getReasonOfLeaving());
        if (updatedData.getAchievements() != null) existingData.setAchievements(updatedData.getAchievements());
        if (updatedData.getEmployeementType() != null) existingData.setEmployeementType(updatedData.getEmployeementType());
        if (updatedData.getLocation() != null) existingData.setLocation(updatedData.getLocation());
        if (updatedData.getIndustry() != null) existingData.setIndustry(updatedData.getIndustry());
        if (updatedData.getCompanySize() != null) existingData.setCompanySize(updatedData.getCompanySize());
        if (updatedData.getLatestMonthGross() != null) existingData.setLatestMonthGross(updatedData.getLatestMonthGross());
        if (updatedData.getTeamSize() != 0) existingData.setTeamSize(updatedData.getTeamSize());
    
        return employmentDataRepository.save(existingData);
    }
    


    public List<EmploymentData> getByVerificationIdAndPreLoginToken(Long verificationId, String preLoginToken) {
        return employmentDataRepository.findByVerificationTicketIdAndPreRegistrationPreLoginToken(verificationId, preLoginToken);
    }


    public void updateVerificationStatus(Long id, boolean status) {
        employmentDataRepository.updateVerificationStatusById(id, status);
    }
}
