package com.spectrum.PreOnboarding.PreRegistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.PreOnboarding.PreRegistration.model.OfferVariableCTCBreakdown;


@Repository
public interface OfferVariableCTCBreakdownRepository extends JpaRepository<OfferVariableCTCBreakdown, Long> {

}
