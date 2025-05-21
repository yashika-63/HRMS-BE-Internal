package com.spectrum.PreOnboarding.PreRegistration.controller;

import com.spectrum.PreOnboarding.PreRegistration.model.OfferNotes;
import com.spectrum.PreOnboarding.PreRegistration.service.OfferNotesService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/offer-notes")
public class OfferNotesController {

    @Autowired
    private OfferNotesService offerNotesService;
@PostMapping("/save/{offerId}")
    public ResponseEntity<?> saveOfferNote(@PathVariable Long offerId, @RequestBody OfferNotes offerNotes) {
        try {
            OfferNotes savedNote = offerNotesService.saveOfferNote(offerId, offerNotes);
            return ResponseEntity.ok(savedNote);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/getByOfferId/{offerId}")
    public ResponseEntity<?> getNotesByOfferId(@PathVariable Long offerId) {
        List<OfferNotes> notes = offerNotesService.getNotesByOfferId(offerId);
        return ResponseEntity.ok(notes);
    }
}
