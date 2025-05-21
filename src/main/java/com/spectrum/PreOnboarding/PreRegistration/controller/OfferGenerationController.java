// Controller Class
package com.spectrum.PreOnboarding.PreRegistration.controller;

import com.spectrum.PreOnboarding.PreRegistration.model.OfferGeneration;
import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;
import com.spectrum.PreOnboarding.PreRegistration.repository.PreRegistrationRepository;
import com.spectrum.PreOnboarding.PreRegistration.service.OfferGenerationService;
import com.spectrum.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/offerGeneration")
public class OfferGenerationController {

    @Autowired
    private OfferGenerationService offerGenerationService;

    @Autowired
    private EmailService emailService;

    // Save OfferGeneration
    @PostMapping("/save")
    public ResponseEntity<?> saveOfferGeneration(@RequestBody OfferGeneration offerGeneration) {
        return ResponseEntity.ok(offerGenerationService.saveOfferGeneration(offerGeneration));
    }

    // Get OfferGeneration by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOfferGenerationById(@PathVariable Long id) {
        Optional<OfferGeneration> offerGeneration = offerGenerationService.getOfferGenerationById(id);
        return offerGeneration.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/saveMain/{reportingPersonId}/{companyId}/{preRegistrationId}")
public ResponseEntity<?> saveOfferGeneration(
        @PathVariable Long reportingPersonId,
        @PathVariable Long companyId,
        @PathVariable Long preRegistrationId,
        @RequestBody OfferGeneration offerGeneration
) {
    try {
        OfferGeneration savedOffer = offerGenerationService.saveOfferGeneration(
                reportingPersonId, companyId, preRegistrationId, offerGeneration);
        return ResponseEntity.ok(savedOffer);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving offer generation: " + e.getMessage());
    }
}

@GetMapping("/filter")
public List<OfferGeneration> getOfferDetails(
        @RequestParam(required = false) Long companyId,
        @RequestParam(required = false) Long reportingPersonId,
        @RequestParam(required = false) Boolean status,
        @RequestParam(required = false) Boolean sendForApproval) {
    return offerGenerationService.getOffersByFilters(companyId, reportingPersonId, status, sendForApproval);
}

@GetMapping("/filterWithoutReporting")
public List<OfferGeneration> getOffersWithoutReporting(
        @RequestParam(required = false) Long companyId,
        @RequestParam(required = false) Boolean status,
        @RequestParam(required = false) Boolean sendForApproval) {
    return offerGenerationService.getOffersWithoutReportingPerson(companyId, status, sendForApproval);
}

@GetMapping("/byCompanyPreLoginTokenPreRegistration")
public ResponseEntity<List<OfferGeneration>> getOfferByCompanyAndPreLoginTokenAndPreRegistration(
        @RequestParam Long companyId,
        @RequestParam String preLoginToken,
        @RequestParam Long preRegistrationId) {

    List<OfferGeneration> offerGenerations = offerGenerationService
            .getOfferByCompanyAndPreLoginTokenAndPreRegistration(companyId, preLoginToken, preRegistrationId);
    return ResponseEntity.ok(offerGenerations);
}




@PutMapping("/sendOffer/update/{id}")
public ResponseEntity<?> updateOfferGeneration(@PathVariable Long id, @RequestParam String preLoginToken,
        @RequestBody OfferGeneration updateRequest) {
    Optional<OfferGeneration> existingOfferOpt = offerGenerationService.findById(id);
    if (!existingOfferOpt.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OfferGeneration record not found");
    }

    OfferGeneration existingOffer = existingOfferOpt.get();

    // Validate PreRegistration preLoginToken
    PreRegistration preRegistration = existingOffer.getPreRegistration();
    if (!preRegistration.getPreLoginToken().equals(preLoginToken)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid preLoginToken");
    }

    // Update only allowed fields
    existingOffer.setRejectedByCandidate(updateRequest.isRejectedByCandidate());
    existingOffer.setAcceptedByCandidate(updateRequest.isAcceptedByCandidate());
    existingOffer.setRemarkedByCandidate(updateRequest.isRemarkedByCandidate());
    existingOffer.setActionTakenByCandidate(true);

    // Save the updated entity
    offerGenerationService.save(existingOffer);

    // Send email notification to reporting person
    String reportingEmail = existingOffer.getEmployees().getEmail();
    String subject = "Offer Status Update";
    String message = "The offer has been " + (existingOffer.isAcceptedByCandidate() ? "Accepted"
            : existingOffer.isRejectedByCandidate() ? "Rejected" : "Remarked") + " by the candidate.";

    emailService.sendEmail(reportingEmail, subject, message);

    return ResponseEntity.ok("OfferGeneration updated successfully and email sent");
}


@PutMapping("/update-status/{id}")
public ResponseEntity<?> updateOfferStatus(@PathVariable Long id) {
    try {
        OfferGeneration updatedOffer = offerGenerationService.updateOfferGenerationStatus(id);
        return ResponseEntity.ok(updatedOffer);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}

@GetMapping("/getByPreRegAndPreLoginToken")
public ResponseEntity<?> getByPreRegIdAndPreLoginToken(
        @RequestParam Long preRegistrationId,
        @RequestParam String preLoginToken) {
    try {
        List<OfferGeneration> offers = offerGenerationService.getOfferByPreRegIdAndPreLoginToken(preRegistrationId,
                preLoginToken);
        return ResponseEntity.ok(offers);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}

@GetMapping("/getByPreRegId")
public ResponseEntity<?> getByPreRegistrationId(@RequestParam Long preRegistrationId) {
    try {
        List<OfferGeneration> offers = offerGenerationService.getOfferByPreRegistrationId(preRegistrationId);
        return ResponseEntity.ok(offers);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}





@GetMapping("/getApprovedAndSentByPreReg")
public ResponseEntity<?> getApprovedAndSentOffers(
        @RequestParam Long preRegistrationId,
        @RequestParam String preLoginToken) {
    try {
        List<OfferGeneration> offers = offerGenerationService.getApprovedAndSentOffersByPreRegistration(preRegistrationId, preLoginToken);
        return ResponseEntity.ok(offers);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error fetching data: " + e.getMessage());
    }
}



        @GetMapping("/getApprovedOffers")
        public ResponseEntity<?> getApprovedOffers(
                @RequestParam Long preRegistrationId,
                @RequestParam String preLoginToken) {
            try {
                List<OfferGeneration> offers = offerGenerationService
                    .getApprovedOffers(preRegistrationId, preLoginToken);
                return ResponseEntity.ok(offers);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error: " + e.getMessage());
            }
        }



        @GetMapping("/getOffersWithActionTaken")
public ResponseEntity<?> getOffersWithActionTaken(
        @RequestParam Long preRegistrationId,
        @RequestParam String preLoginToken) {
    try {
        List<OfferGeneration> offers = offerGenerationService
            .getOffersWithActionTaken(preRegistrationId, preLoginToken);
        return ResponseEntity.ok(offers);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}

























@GetMapping("/accept")
    public ResponseEntity<String> acceptOffer(
            @RequestParam Long offerId,
            @RequestParam String token) {

        String result = offerGenerationService.acceptOffer(offerId, token);
        if (result.equals("Offer accepted successfully.")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }


    @PostMapping("/reject-offer")
public ResponseEntity<String> rejectOffer(
        @RequestParam Long offerId,
        @RequestParam String token) {
    String response = offerGenerationService.rejectOffer(offerId, token);
    return ResponseEntity.ok(response);
}

}