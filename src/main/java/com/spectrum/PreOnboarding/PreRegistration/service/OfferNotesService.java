package com.spectrum.PreOnboarding.PreRegistration.service;

import com.spectrum.PreOnboarding.PreRegistration.model.OfferGeneration;
import com.spectrum.PreOnboarding.PreRegistration.model.OfferNotes;
import com.spectrum.PreOnboarding.PreRegistration.repository.OfferGenerationRepository;
import com.spectrum.PreOnboarding.PreRegistration.repository.OfferNotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferNotesService {

    @Autowired
    private OfferNotesRepository offerNotesRepository;

    @Autowired
    private OfferGenerationRepository offerGenerationRepository;

   
    public OfferNotes saveOfferNote(Long offerId, OfferNotes offerNotes) {
        OfferGeneration offerGeneration = offerGenerationRepository.findById(offerId)
            .orElseThrow(() -> new RuntimeException("OfferGeneration not found with id: " + offerId));

        offerNotes.setOfferGeneration(offerGeneration);
        return offerNotesRepository.save(offerNotes);
    }
 
    



    public List<OfferNotes> getNotesByOfferId(Long offerId) {
        return offerNotesRepository.findByOfferGeneration_Id(offerId);
    }
}
