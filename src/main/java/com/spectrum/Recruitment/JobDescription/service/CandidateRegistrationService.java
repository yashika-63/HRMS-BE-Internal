package com.spectrum.Recruitment.JobDescription.service;

import com.spectrum.Recruitment.JobDescription.model.CandidateRegistration;
import com.spectrum.Recruitment.JobDescription.model.CandidateUpdateRequest;
import com.spectrum.Recruitment.JobDescription.model.JobDescription;
import com.spectrum.Recruitment.JobDescription.repository.CandidateRegistrationRepository;
import com.spectrum.Recruitment.JobDescription.repository.JobDescriptionRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateRegistrationService {

    @Autowired
    private CandidateRegistrationRepository candidateRegistrationRepository;

    @Autowired
    private JobDescriptionRepository jobDescriptionRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    public List<CandidateRegistration> saveAll(List<CandidateRegistration> candidates, Long jobDescriptionId, Long companyId) {
        JobDescription jobDescription = jobDescriptionRepository.findById(jobDescriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Job Description not found"));
    
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
    
        for (CandidateRegistration candidate : candidates) {
            candidate.setJobDescription(jobDescription);
            candidate.setCompany(company);
            
            // Set default boolean values
            candidate.setStatus(true);
            candidate.setShortListed(false);
            candidate.setFinalizedForInterview(false);
            candidate.setSelectedCandidate(false);
        }
    
        return candidateRegistrationRepository.saveAll(candidates);
    }
    

    public List<CandidateRegistration> getByJobDescriptionAndFilters(
    Long jobDescriptionId,
    boolean status,
    boolean shortListed,
    boolean finalizedForInterview,
    boolean selectedCandidate
) {
    return candidateRegistrationRepository
        .findByJobDescriptionIdAndStatusAndShortListedAndFinalizedForInterviewAndSelectedCandidate(
            jobDescriptionId,
            status,
            shortListed,
            finalizedForInterview,
            selectedCandidate
        );
}




 public void updateCandidates(List<CandidateUpdateRequest> updateRequests) {
        for (CandidateUpdateRequest request : updateRequests) {
            CandidateRegistration candidate = candidateRegistrationRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + request.getId()));

            if (request.getFirstName() != null) candidate.setFirstName(request.getFirstName());
            if (request.getMiddelName() != null) candidate.setMiddelName(request.getMiddelName());
            if (request.getLastName() != null) candidate.setLastName(request.getLastName());
            if (request.getEmail() != null) candidate.setEmail(request.getEmail());
            if (request.getPhoneNumber() != null) candidate.setPhoneNumber(request.getPhoneNumber());
            if (request.getHighestQualification() != null) candidate.setHighestQualification(request.getHighestQualification());
            if (request.getYearsOfExperience() != null) candidate.setYearsOfExperience(request.getYearsOfExperience());
            if (request.getSkills() != null) candidate.setSkills(request.getSkills());
            if (request.getJobTitle() != null) candidate.setJobTitle(request.getJobTitle());
            if (request.getStatus() != null) candidate.setStatus(request.getStatus());
            if (request.getShortListed() != null) candidate.setShortListed(request.getShortListed());
            if (request.getFinalizedForInterview() != null) candidate.setFinalizedForInterview(request.getFinalizedForInterview());
            if (request.getSelectedCandidate() != null) candidate.setSelectedCandidate(request.getSelectedCandidate());
            if (request.getRelevantExperience() != null) candidate.setRelevantExperience(request.getRelevantExperience());

            candidateRegistrationRepository.save(candidate);
        }
    }
}
