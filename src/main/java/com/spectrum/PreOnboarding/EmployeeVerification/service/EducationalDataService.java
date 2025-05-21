package com.spectrum.PreOnboarding.EmployeeVerification.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.PreOnboarding.EmployeeVerification.model.EducationalData;
import com.spectrum.PreOnboarding.EmployeeVerification.model.VerificationTicket;
import com.spectrum.PreOnboarding.EmployeeVerification.repository.EducationalDataRepository;
import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;
import com.spectrum.PreOnboarding.PreRegistration.repository.PreRegistrationRepository;

import jakarta.transaction.Transactional;

import com.spectrum.PreOnboarding.EmployeeVerification.repository.VerificationTicketRepository;

@Service
public class EducationalDataService {

    @Autowired
    private EducationalDataRepository educationalDataRepository;

    @Autowired
    private PreRegistrationRepository preRegistrationRepository;

    @Autowired
    private VerificationTicketRepository verificationTicketRepository;

    public String saveEducationalData(String preLoginToken, Long verificationId, List<EducationalData> educationalDataList) {
        PreRegistration preRegistration = preRegistrationRepository
            .findByPreLoginToken(preLoginToken)
            .orElseThrow(() -> new RuntimeException("PreRegistration not found"));
    
        VerificationTicket ticket = verificationTicketRepository
            .findById(verificationId)
            .orElseThrow(() -> new RuntimeException("VerificationTicket not found"));
    
        for (EducationalData data : educationalDataList) {
            data.setPreRegistration(preRegistration);
            data.setVerificationTicket(ticket);
            data.setVerificationStatus(false); // explicitly set
        }
    
        educationalDataRepository.saveAll(educationalDataList);
        return "Educational Data saved successfully with verificationStatus=false";
    }








    public List<EducationalData> getByVerificationIdAndPreLoginToken(Long verificationId, String preLoginToken) {
        return educationalDataRepository.findByVerificationIdAndPreLoginToken(verificationId, preLoginToken);
    }
    
    @Transactional
    public void updateVerificationStatus(Long id, boolean status) {
    educationalDataRepository.updateVerificationStatusById(id, status);
}

    


public EducationalData updateEducationalDataPartially(Long id, EducationalData updatedData) {
    EducationalData existing = educationalDataRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Educational record not found with id: " + id));

    if (updatedData.getInstitute() != null)
        existing.setInstitute(updatedData.getInstitute());

    if (updatedData.getUniversity() != null)
        existing.setUniversity(updatedData.getUniversity());

    if (updatedData.getTypeOfStudy() != null)
        existing.setTypeOfStudy(updatedData.getTypeOfStudy());

    if (updatedData.getYearOfAddmisstion() != 0)
        existing.setYearOfAddmisstion(updatedData.getYearOfAddmisstion());

    if (updatedData.getYearOfPassing() != 0)
        existing.setYearOfPassing(updatedData.getYearOfPassing());

    if (updatedData.getBranch() != null)
        existing.setBranch(updatedData.getBranch());

    if (updatedData.getScore() != 0)
        existing.setScore(updatedData.getScore());

    if (updatedData.getScoreType() != null)
        existing.setScoreType(updatedData.getScoreType());

    
    return educationalDataRepository.save(existing);
}

}
