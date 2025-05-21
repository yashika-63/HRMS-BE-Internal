package com.spectrum.PreOnboarding.PreRegistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.PreOnboarding.PreRegistration.model.OfferStaticCTCBreakdown;


@Repository
public interface OfferStaticCTCBreakdownRepository extends JpaRepository<OfferStaticCTCBreakdown, Long>{

}
