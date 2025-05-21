package com.spectrum.PreOnboarding.EmployeeVerification.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.PreOnboarding.EmployeeVerification.model.VerificationTicketNote;
import com.spectrum.PreOnboarding.EmployeeVerification.repository.VerificationTicketNoteRepository;

@Service
public class VerificationTicketNoteService {




    
    @Autowired
    private VerificationTicketNoteRepository verificationTicketNoteRepository;

    // Method to get VerificationTicketNotes by verificationTicketId
    public List<VerificationTicketNote> getVerificationTicketNotesByTicketId(Long verificationTicketId) {
        return verificationTicketNoteRepository.findByVerificationTicket_Id(verificationTicketId);
    }
}
