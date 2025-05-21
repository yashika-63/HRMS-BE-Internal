package com.spectrum.Recruitment.JobDescription.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spectrum.Recruitment.JobDescription.model.JobDescription;

public interface JobDescriptionRepository extends JpaRepository<JobDescription, Long> {
    // Custom query methods can be added here if needed


    List<JobDescription> findByCompanyIdAndStatus(Long companyRegistrationId, boolean status);
    Optional<JobDescription> findByJobKey(String jobKey);

}   
