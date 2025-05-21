package com.spectrum.PreOnboarding.PreRegistration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.PreOnboarding.PreRegistration.model.OfferCTCBreakdownHeader;
import com.spectrum.PreOnboarding.PreRegistration.service.OfferCTCBreakdownHeaderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/offerCTC")
public class OfferCTCBreakdownHeaderController {


    @Autowired
    private OfferCTCBreakdownHeaderService offerCTCBreakdownHeaderService;

    @PostMapping("/save/{companyId}/{offerGenerationId}")
    public ResponseEntity<OfferCTCBreakdownHeader> saveOfferCTCBreakdownHeader(
            @PathVariable Long companyId,
            @PathVariable Long offerGenerationId,
            @RequestBody OfferCTCBreakdownHeader request) {
        
        OfferCTCBreakdownHeader savedOfferCTC = offerCTCBreakdownHeaderService
                .saveOfferCTCBreakdownHeader(request, companyId, offerGenerationId);
        return ResponseEntity.ok(savedOfferCTC);
    }

    @GetMapping("/byOfferGeneration/{offerGenerationId}")
    public ResponseEntity<List<OfferCTCBreakdownHeader>> getByOfferGenerationId(@PathVariable Long offerGenerationId) {
        return ResponseEntity.ok(offerCTCBreakdownHeaderService.getByOfferGenerationId(offerGenerationId));
    }
}

