package com.spectrum.PreOnboarding.PreRegistration.service;

import com.spectrum.PreOnboarding.PreRegistration.model.OfferGeneration;
import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;
import com.spectrum.PreOnboarding.PreRegistration.repository.OfferGenerationRepository;
import com.spectrum.PreOnboarding.PreRegistration.repository.PreRegistrationRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferGenerationService {


    @Autowired
    private OfferGenerationRepository offerGenerationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private PreRegistrationRepository preRegistrationRepository;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private EmailService emailService;
   

    

    // Save OfferGeneration
    public OfferGeneration saveOfferGeneration(OfferGeneration offerGeneration) {
        return offerGenerationRepository.save(offerGeneration);
    }

    // Get OfferGeneration by ID
    public Optional<OfferGeneration> getOfferGenerationById(Long id) {
        return offerGenerationRepository.findById(id);
    }



    public OfferGeneration saveOfferGeneration(Long reportingPersonId, Long companyId, Long preRegistrationId, OfferGeneration offerGeneration) {
    Employee employee = employeeRepository.findById(reportingPersonId)
            .orElseThrow(() -> new RuntimeException("Candidate not found"));

    CompanyRegistration company = companyRegistrationRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));

    PreRegistration preRegistration = preRegistrationRepository.findById(preRegistrationId)
            .orElseThrow(() -> new RuntimeException("Pre-registration not found"));

    offerGeneration.setEmployees(employee);
    offerGeneration.setCompany(company);
    offerGeneration.setPreRegistration(preRegistration);

    return offerGenerationRepository.save(offerGeneration);
}

public List<OfferGeneration> getOffersByFilters(Long companyId, Long reportingPersonId, Boolean status,
        Boolean sendForApproval) {
    return offerGenerationRepository.findByFilters(companyId, reportingPersonId, status, sendForApproval);
}

public List<OfferGeneration> getOfferByCompanyAndPreLoginTokenAndPreRegistration(Long companyId, String preLoginToken,
        Long preRegistrationId) {
    return offerGenerationRepository.findByCompanyIdAndPreLoginTokenAndPreRegistrationId(companyId, preLoginToken,
            preRegistrationId);
}

public Optional<OfferGeneration> findById(Long id) {
    return offerGenerationRepository.findById(id);
}

public OfferGeneration save(OfferGeneration offerGeneration) {
    return offerGenerationRepository.save(offerGeneration);
}

// @Transactional
// public OfferGeneration updateOfferGenerationStatus(Long id) {
//     Optional<OfferGeneration> offerOptional = offerGenerationRepository.findById(id);

//     if (offerOptional.isEmpty()) {
//         throw new IllegalStateException("Offer not found with ID: " + id);
//     }

//     OfferGeneration offerGeneration = offerOptional.get();
//     offerGeneration.setSendForApproval(true);

//     // Get the candidate email from PreRegistration
//     PreRegistration preRegistration = offerGeneration.getPreRegistration();
//     if (preRegistration == null) {
//         throw new IllegalStateException("PreRegistration details not found for this offer.");
//     }

//     String candidateEmail = preRegistration.getEmail();
//     String loginToken = preRegistration.getPreLoginToken();
//     String loginUrl = "http://localhost:5173/Prelogin";

//     // Send Email Notification
//     sendApprovalEmail(candidateEmail, loginToken, loginUrl);

//         return offerGenerationRepository.save(offerGeneration);
//     }

   

@Transactional
public OfferGeneration updateOfferGenerationStatus(Long id) {
    Optional<OfferGeneration> offerOptional = offerGenerationRepository.findById(id);

    if (offerOptional.isEmpty()) {
        throw new IllegalStateException("Offer not found with ID: " + id);
    }

    OfferGeneration currentOffer = offerOptional.get();
    PreRegistration preRegistration = currentOffer.getPreRegistration();

    if (preRegistration == null) {
        throw new IllegalStateException("PreRegistration details not found for this offer.");
    }

    // Deactivate other offers for the same PreRegistration
    List<OfferGeneration> existingOffers = offerGenerationRepository.findByPreRegistration_Id(preRegistration.getId());
    for (OfferGeneration offer : existingOffers) {
        if (!offer.getId().equals(id)) {
            offer.setStatus(false); // set all other offers to inactive
        }
    }

    // Set current offer to active and mark it as sent for approval
    currentOffer.setSendForApproval(true);
    currentOffer.setStatus(true); // mark this one as active

    // ✅ Update PreRegistration to mark offer as generated
    preRegistration.setOfferGenerated(true);
    preRegistration.setLoginStatus(true);
    preRegistration.setStatus(true);
    // Send Email Notification
    String candidateEmail = preRegistration.getEmail();
    String loginToken = preRegistration.getPreLoginToken();
    // String loginUrl = "http://localhost:5173/Prelogin";
    String loginUrl = "http://15.207.163.30:3002/Prelogin";

    sendApprovalEmail(candidateEmail, loginToken, loginUrl);

    // Save all offers (current + modified ones)
    existingOffers.add(currentOffer);
    offerGenerationRepository.saveAll(existingOffers);

    return currentOffer;
}

private void sendApprovalEmail(String email, String token, String loginUrl) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Offer Letter Generated - Login to View");
    message.setText(
            "Dear Candidate,\n\nYour offer letter has been generated. Please login to the portal to view it.\n\n"
                    + "Login URL: " + loginUrl + "\n"
                    + "Username: " + email + "\n"
                    + "Password: " + token + "\n\n"
                    + "Best regards,\nHR Team");

    mailSender.send(message);
}


    public List<OfferGeneration> getOfferByPreRegIdAndPreLoginToken(Long preRegId, String preLoginToken) {
        List<OfferGeneration> offers = offerGenerationRepository
                .findByPreRegistration_IdAndPreRegistration_PreLoginToken(preRegId, preLoginToken);

        if (offers.isEmpty()) {
            throw new IllegalStateException("No offers found for given pre-registration ID and token");
        }
        return offers;
    }

    public List<OfferGeneration> getOffersWithoutReportingPerson(Long companyId, Boolean status,
            Boolean sendForApproval) {
        return offerGenerationRepository.findByCompanyAndStatusAndApproval(companyId, status, sendForApproval);
    }

    public List<OfferGeneration> getOfferByPreRegistrationId(Long preRegId) {
        List<OfferGeneration> offers = offerGenerationRepository.findByPreRegistration_Id(preRegId);

        if (offers.isEmpty()) {
            throw new IllegalStateException("No offers found for given pre-registration ID");
        }

        return offers;
    }







    public List<OfferGeneration> getApprovedAndSentOffersByPreRegistration(Long preRegId, String preLoginToken) {
        return offerGenerationRepository
                .findByStatusTrueAndSendForApprovalTrueAndActionTakenByCandidateFalseAndPreRegistrationIdAndPreRegistrationPreLoginToken(
                preRegId, preLoginToken);
    }


    public List<OfferGeneration> getApprovedOffers(Long preRegId, String preLoginToken) {
        List<OfferGeneration> offers = offerGenerationRepository
            .findByPreRegistration_IdAndPreRegistration_PreLoginTokenAndSendForApprovalTrue(preRegId, preLoginToken);
    
        if (offers.isEmpty()) {
            throw new IllegalStateException("No approved offers found for given PreRegistration ID and token");
        }
    
        return offers;
    }
    


    public List<OfferGeneration> getOffersWithActionTaken(Long preRegId, String preLoginToken) {
        List<OfferGeneration> offers = offerGenerationRepository
            .findByPreRegistration_IdAndPreRegistration_PreLoginTokenAndActionTakenByCandidateTrue(preRegId, preLoginToken);
    
        if (offers.isEmpty()) {
            throw new IllegalStateException("No offers found with action taken for given PreRegistration ID and token");
        }
    
        return offers;
    }















    ///////////////// 
    /// 
    /// this are the api's for offer accept reject remark
     
    /// 
    ////////////////
    
    public String acceptOffer(Long offerId, String token) {
        Optional<OfferGeneration> optionalOffer = offerGenerationRepository
                .findByIdAndPreRegistration_PreLoginToken(offerId, token);
    
        if (optionalOffer.isPresent()) {
            OfferGeneration offer = optionalOffer.get();
    
            offer.setAcceptedByCandidate(true);
            offer.setActionTakenByCandidate(true);
    
            // Set pre-registration status to false
            PreRegistration preRegistration = offer.getPreRegistration();
            preRegistration.setStatus(false); // <-- this is the improvement
    
            // Save both entities
            preRegistrationRepository.save(preRegistration);  // Save PreRegistration status
            offerGenerationRepository.save(offer);            // Save OfferGeneration
    
            // Send email to manager
            String managerEmail = offer.getEmployees().getEmail();
            String candidateEmail = preRegistration.getEmail();
            String candidateName = offer.getFirstName() + " " + offer.getLastName();
            String companyName = offer.getCompany().getCompanyName();
    
            emailService.sendEmail(
                managerEmail,
                "Offer Accepted Notification",
                "Candidate " + candidateName + " has accepted the offer at " + companyName + "."
            );
    
            // Send email to candidate
            emailService.sendEmail(
                candidateEmail,
                "Congratulations!",
                "Dear " + candidateName + ",\n\nCongratulations on accepting the offer from " + companyName + "."
            );
    
            return "Offer accepted successfully.";
        } else {
            return "Invalid offerId or token.";
        }
    }
    





    public String rejectOffer(Long offerId, String token) {
        Optional<OfferGeneration> optionalOffer = offerGenerationRepository.findByIdAndPreRegistration_PreLoginToken(offerId, token);
    
        if (optionalOffer.isPresent()) {
            OfferGeneration offer = optionalOffer.get();
            offer.setRejectedByCandidate(true);
            offer.setActionTakenByCandidate(true);

            // Set pre-registration status to false
            PreRegistration preRegistration = offer.getPreRegistration();
            preRegistration.setStatus(false); // <-- this is the improvement
            preRegistration.setLoginStatus(false); // <-- this is the improvement

            // Save both entities
            preRegistrationRepository.save(preRegistration);  // Save PreRegistration status
            offerGenerationRepository.save(offer);            // Save OfferGeneration


    
            String managerEmail = offer.getEmployees().getEmail();
            String candidateEmail = offer.getPreRegistration().getEmail();
            String candidateName = offer.getFirstName() + " " + offer.getLastName();
            String companyName = offer.getCompany().getCompanyName();
    
            // Send email to manager
            emailService.sendEmail(
                managerEmail,
                "Offer Rejected Notification",
                "Candidate " + candidateName + " has rejected the offer at " + companyName + "."
            );
    
            // Send email to candidate
            emailService.sendEmail(
                candidateEmail,
                "Offer Rejected",
                "Dear " + candidateName + ",\n\nYou have rejected the offer from " + companyName + "."
            );
    
            return "Offer rejected successfully.";
        } else {
            return "Invalid offerId or token.";
        }
    }
    

    
}
