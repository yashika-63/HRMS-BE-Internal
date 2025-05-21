package com.spectrum.Recruitment.JobDescription.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spectrum.Recruitment.JobDescription.model.CandidateRegistration;

public interface CandidateRegistrationRepository extends JpaRepository<CandidateRegistration, Long> {

    List<CandidateRegistration> findByJobDescriptionIdAndStatusAndShortListedAndFinalizedForInterviewAndSelectedCandidate(
        Long jobDescriptionId,
        boolean status,
        boolean shortListed,
        boolean finalizedForInterview,
        boolean selectedCandidate
    );
}
