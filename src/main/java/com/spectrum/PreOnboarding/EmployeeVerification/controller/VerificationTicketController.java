package com.spectrum.PreOnboarding.EmployeeVerification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.PreOnboarding.EmployeeVerification.model.VerificationTicket;
import com.spectrum.PreOnboarding.EmployeeVerification.service.VerificationTicketService;

@RestController
@RequestMapping("/api/verification")
public class VerificationTicketController {

    @Autowired
    private VerificationTicketService verificationTicketService;

    @PostMapping("/create")
    public ResponseEntity<?> createTicket(
            @RequestParam Long companyId,
            @RequestParam Long reportingPersonId,
            @RequestParam Long preRegistrationId) {
        try {
            VerificationTicket ticket = verificationTicketService
                    .createVerificationTicket(companyId, reportingPersonId, preRegistrationId);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



//  this is the method to send complete you onboarding verificaton process 
    @PutMapping("/notify/{preRegistrationId}")
    public ResponseEntity<?> updateAccessAndNotify(@PathVariable Long preRegistrationId) {
        try {
            VerificationTicket updated = verificationTicketService.updateAccessAndNotify(preRegistrationId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/ticket/byToken")
    public ResponseEntity<VerificationTicket> getTicketByTokenAndId(
            @RequestParam String token,
            @RequestParam Long preRegId) {
        VerificationTicket ticket = verificationTicketService.getActiveTicketByTokenAndPreRegId(token, preRegId);
        return ResponseEntity.ok(ticket);
    }






// send back for verification 

@PutMapping("/ticket/sendForVerification/{preRegistrationId}")
public ResponseEntity<VerificationTicket> sendForVerification(
        @PathVariable Long preRegistrationId) {
    VerificationTicket ticket = verificationTicketService.markTicketAsSentForVerification(preRegistrationId);
    return ResponseEntity.ok(ticket);
}

// to get data comme to reporting person for verification

@GetMapping("/ticket/forVerification")
public ResponseEntity<List<VerificationTicket>> getTicketsForVerification(
        @RequestParam Long companyId,
        @RequestParam Long reportingPersonId) {
    List<VerificationTicket> tickets = verificationTicketService.getTicketsForVerification(companyId, reportingPersonId);
    return ResponseEntity.ok(tickets);
}

  

// method to say all data verified 
// complete process 
@PutMapping("/updateStatus")
public ResponseEntity<?> updateVerificationTicketStatus(
        @RequestParam Long id,
        @RequestParam Long reportingPersonId) {
    try {
        VerificationTicket updatedTicket = verificationTicketService.updateVerificationStatus(id, reportingPersonId);
        return ResponseEntity.ok(updatedTicket);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}



// this is for second column where employee side action is pernding  

@GetMapping("/pendingEmployeeActions")
public ResponseEntity<?> getPendingTicketsByPreRegistration(
        @RequestParam Long preRegistrationId,
        @RequestParam String preLoginToken) {
    try {
        List<VerificationTicket> tickets = verificationTicketService
                .getPendingEmployeeActionTickets(preRegistrationId, preLoginToken);
        return ResponseEntity.ok(tickets);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}


@GetMapping("/completedEmployeeActions")
public ResponseEntity<?> getCompletedTicketsByPreRegistration(
        @RequestParam Long preRegistrationId,
        @RequestParam String preLoginToken) {
    try {
        List<VerificationTicket> tickets = verificationTicketService
                .getCompletedEmployeeActionTickets(preRegistrationId, preLoginToken);
        return ResponseEntity.ok(tickets);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}


@GetMapping("/pending-verification")
public ResponseEntity<?> getPendingVerificationTickets() {
    try {
        List<VerificationTicket> pendingTickets = verificationTicketService.getPendingVerificationTickets();
        return ResponseEntity.ok(pendingTickets);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}




@GetMapping("/active-by-company")
public ResponseEntity<?> getActiveTicketsByCompanyId(@RequestParam Long companyId) {
    try {
        List<VerificationTicket> tickets = verificationTicketService.getActiveTicketsByCompanyId(companyId);
        return ResponseEntity.ok(tickets);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}




@GetMapping("/pendingByReportingPerson/{reportingPersonId}")
public List<VerificationTicket> getPendingTickets(@PathVariable Long reportingPersonId) {
    return verificationTicketService.getPendingTicketsByReportingPerson(reportingPersonId);
}


@GetMapping("/latest-verified/{companyId}")
public ResponseEntity<List<VerificationTicket>> getLatestVerifiedTickets(@PathVariable Long companyId) {
    List<VerificationTicket> tickets = verificationTicketService.getLatestVerifiedTicketsByCompanyId(companyId);
    return ResponseEntity.ok(tickets);
}






@PostMapping("/send-back")
public ResponseEntity<String> sendBackToEmployee(
        @RequestParam Long ticketId,
        @RequestParam String note) {
    String response = verificationTicketService.sendBackToEmployee(ticketId, note);
    return ResponseEntity.ok(response);
}



}
