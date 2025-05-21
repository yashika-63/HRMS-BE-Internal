package com.spectrum.PreOnboarding.PreRegistration.repository;

import com.spectrum.PreOnboarding.PreRegistration.model.OfferNotes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferNotesRepository extends JpaRepository<OfferNotes, Long> {

    List<OfferNotes> findByOfferGeneration_Id(Long offerId);

}
