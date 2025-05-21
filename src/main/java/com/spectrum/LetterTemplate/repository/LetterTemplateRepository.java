package com.spectrum.LetterTemplate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spectrum.LetterTemplate.model.LetterTemplate;
import com.spectrum.model.CompanyRegistration;

public interface LetterTemplateRepository extends JpaRepository<LetterTemplate, Long> {




       Optional<LetterTemplate> findByStatusTrueAndCompanyAndTemplateIdentityKey(
        CompanyRegistration company, String templateIdentityKey);


        // Optional<LetterTemplate> findByCompanyIdAndTemplateIdentityKeyAndStatusTrue(Long companyId, String templateIdentityKey);
        LetterTemplate findByCompanyIdAndTemplateIdentityKeyAndStatusTrue(Long companyId, String templateIdentityKey);

        
        List<LetterTemplate> findByCompanyIdAndStatus(Long companyId, boolean status);

}
