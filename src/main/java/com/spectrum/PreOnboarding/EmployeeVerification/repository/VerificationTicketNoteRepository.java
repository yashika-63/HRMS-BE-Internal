package com.spectrum.PreOnboarding.EmployeeVerification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.PreOnboarding.EmployeeVerification.model.VerificationTicketNote;

@Repository

public interface VerificationTicketNoteRepository extends JpaRepository<VerificationTicketNote,Long>{
    // Find all VerificationTicketNote entries by the verification_ticket_id (i.e., VerificationTicket's id)
    List<VerificationTicketNote> findByVerificationTicket_Id(Long verificationTicketId);
}
