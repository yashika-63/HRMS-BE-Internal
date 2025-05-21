package com.spectrum.PreOnboarding.EmployeeVerification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.PreOnboarding.EmployeeVerification.model.VerificationTicketNote;
import com.spectrum.PreOnboarding.EmployeeVerification.service.VerificationTicketNoteService;

@RestController
@RequestMapping("/api/verificationTicketNote")

public class VerificationTicketNoteController {

    @Autowired
    private VerificationTicketNoteService verificationTicketNoteService;

     @GetMapping("/verificationTicketNotes/{verificationTicketId}")
    public List<VerificationTicketNote> getVerificationTicketNotes(@PathVariable Long verificationTicketId) {
        return verificationTicketNoteService.getVerificationTicketNotesByTicketId(verificationTicketId);
    }
}
