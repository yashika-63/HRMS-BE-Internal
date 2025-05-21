package com.spectrum.PreOnboarding.PreRegistration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.PreOnboarding.PreRegistration.model.OfferCTCBreakdownHeader;
import com.spectrum.PreOnboarding.PreRegistration.model.OfferGeneration;


@Repository
public interface OfferCTCBreakdownHeaderRepository extends JpaRepository<OfferCTCBreakdownHeader, Long>{


 @Query("SELECT o FROM OfferCTCBreakdownHeader o WHERE o.OfferGeneration.id = :offerGenerationId")
    List<OfferCTCBreakdownHeader> findByOfferGenerationId(@Param("offerGenerationId") Long offerGenerationId);

 

}
