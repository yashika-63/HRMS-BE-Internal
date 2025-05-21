package com.spectrum.PreOnboarding.PreRegistration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.PreOnboarding.PreRegistration.model.OfferCTCBreakdownHeader;
import com.spectrum.PreOnboarding.PreRegistration.model.OfferGeneration;
import com.spectrum.PreOnboarding.PreRegistration.model.OfferStaticCTCBreakdown;
import com.spectrum.PreOnboarding.PreRegistration.model.OfferVariableCTCBreakdown;
import com.spectrum.PreOnboarding.PreRegistration.repository.OfferCTCBreakdownHeaderRepository;
import com.spectrum.PreOnboarding.PreRegistration.repository.OfferGenerationRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;


@Service
public class OfferCTCBreakdownHeaderService {

    @Autowired
    private  OfferCTCBreakdownHeaderRepository offerCTCBreakdownHeaderRepository;
    @Autowired

    private  CompanyRegistrationRepository companyRegistrationRepository;
    @Autowired

    private  OfferGenerationRepository offerGenerationRepository;

    public OfferCTCBreakdownHeader saveOfferCTCBreakdownHeader(OfferCTCBreakdownHeader request, Long companyId, Long offerGenerationId) {
        // Fetch company and offer generation using IDs from the URL
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));

        OfferGeneration offerGeneration = offerGenerationRepository.findById(offerGenerationId)
                .orElseThrow(() -> new RuntimeException("Offer Generation not found with id: " + offerGenerationId));

        // Set company and offerGeneration
        request.setCompany(company);
        request.setOfferGeneration(offerGeneration);

        // Set the reference in OfferStaticCTCBreakdown
        if (request.getOfferStaticCTCBreakdowns() != null) {
            for (OfferStaticCTCBreakdown staticBreakdown : request.getOfferStaticCTCBreakdowns()) {
                staticBreakdown.setOfferCTCBreakdownHeader(request);
            }
        }

        // Set the reference in OfferVariableCTCBreakdown
        if (request.getOfferVariableCTCBreakdowns() != null) {
            for (OfferVariableCTCBreakdown variableBreakdown : request.getOfferVariableCTCBreakdowns()) {
                variableBreakdown.setOfferCTCBreakdownHeader(request);
            }
        }

        // Save everything in one transaction
        return offerCTCBreakdownHeaderRepository.save(request);
    }










    public List<OfferCTCBreakdownHeader> getByOfferGenerationId(Long offerGenerationId) {
        return offerCTCBreakdownHeaderRepository.findByOfferGenerationId(offerGenerationId);
    }
}