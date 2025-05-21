package com.spectrum.PreOnboarding.EmployeeVerification.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.PreOnboarding.PreRegistration.repository.PreRegistrationRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;
import com.spectrum.PreOnboarding.EmployeeVerification.model.VerificationTicket;
import com.spectrum.PreOnboarding.EmployeeVerification.model.VerificationTicketNote;
import com.spectrum.PreOnboarding.EmployeeVerification.repository.VerificationTicketNoteRepository;
import com.spectrum.PreOnboarding.EmployeeVerification.repository.VerificationTicketRepository;
import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;

@Service
public class VerificationTicketService {

    @Autowired
    private VerificationTicketRepository verificationTicketRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PreRegistrationRepository preRegistrationRepository;

    @Autowired
    private EmailService emailService;


    @Autowired
    private VerificationTicketNoteRepository verificationTicketNoteRepository;

    public VerificationTicket createVerificationTicket(Long companyId, Long reportingPersonId, Long preRegistrationId) {
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    
        Employee reportingPerson = employeeRepository.findById(reportingPersonId)
                .orElseThrow(() -> new RuntimeException("Reporting person not found"));
    
        PreRegistration preRegistration = preRegistrationRepository.findById(preRegistrationId)
                .orElseThrow(() -> new RuntimeException("Pre-registration not found"));
    
        // ✅ Check if a ticket already exists for this PreRegistration
        Optional<VerificationTicket> existingTicket = verificationTicketRepository.findByPreRegistrationId(preRegistrationId);
        if (existingTicket.isPresent()) {
            throw new RuntimeException("Verification ticket already exists for this pre-registration");
        }
    
        // 🚀 Create new ticket if not exists
        VerificationTicket ticket = new VerificationTicket();
        ticket.setCompany(company);
        ticket.setEmployees(reportingPerson);
        ticket.setPreRegistration(preRegistration);
    
        // Set default flags
        ticket.setStatus(true);
        ticket.setSendForVerification(false);
        ticket.setVerificationStatus(false);
        ticket.setEmployeeAction(false);
        ticket.setSentForEmployeeAction(false);
        ticket.setEmployeeDataUpdateAccess(false);
        ticket.setEducationDataUpdateAccess(false);
        ticket.setEmployeementDataUpdateAccess(false);
        ticket.setSentBack(false);
        ticket.setReportingPersonAction(false);
    
        return verificationTicketRepository.save(ticket);
    }
    
    


    public VerificationTicket updateAccessAndNotify(Long preRegistrationId) {
        Optional<VerificationTicket> optionalTicket = verificationTicketRepository
                .findByPreRegistrationId(preRegistrationId);

        if (optionalTicket.isEmpty()) {
            throw new RuntimeException("Verification ticket not found for preRegistrationId: " + preRegistrationId);
        }

        VerificationTicket ticket = optionalTicket.get();

        // Set required fields to true
        ticket.setEmployeementDataUpdateAccess(true);
        ticket.setEducationDataUpdateAccess(true);
        ticket.setEmployeeDataUpdateAccess(true);
        ticket.setSentForEmployeeAction(true);

        VerificationTicket updatedTicket = verificationTicketRepository.save(ticket);

        // Send email
        PreRegistration preRegistration = ticket.getPreRegistration();
        String candidateEmail = preRegistration.getEmail();
        String loginToken = preRegistration.getPreLoginToken();
        // String loginUrl = "http://localhost:5173/Prelogin";
        String loginUrl = "http://15.207.163.30:3002/Prelogin";

        String subject = "Complete Your Onboarding Process";
        String message = "Dear " + preRegistration.getFirstName() + ",\n\n"
                + "You are requested to complete your onboarding process within 7 days.\n"
                + "Please visit the following link and login using your token:\n\n"
                + "Url : "+ loginUrl + "\n\n"
                + "User Name: " + candidateEmail + "\n\n"
                + "Password: " + loginToken + "\n\n"
                + "Thank you,\nHR Team";

        emailService.sendEmail(candidateEmail, subject, message);

        return updatedTicket;
    }


    public VerificationTicket getActiveTicketByTokenAndPreRegId(String token, Long preRegId) {
        return verificationTicketRepository.findActiveTicketByTokenAndPreRegId(token, preRegId)
                .orElseThrow(() -> new RuntimeException("No active verification ticket found for provided details."));
    }
    

    public VerificationTicket markTicketAsSentForVerification(Long preRegistrationId) {
        Optional<VerificationTicket> optionalTicket = verificationTicketRepository.findByPreRegistrationId(preRegistrationId);
    
        if (optionalTicket.isEmpty()) {
            throw new RuntimeException("Verification ticket not found for preRegistrationId: " + preRegistrationId);
        }
    
        VerificationTicket ticket = optionalTicket.get();
    
        // Set sendForVerification to true
        ticket.setSendForVerification(true);

        ticket.setEmployeeAction(true);

    
        VerificationTicket updatedTicket = verificationTicketRepository.save(ticket);
    
        // Get the reporting person (Employee)
        Employee reportingPerson = ticket.getEmployees();
        String reportingEmail = reportingPerson.getEmail();
    
        // Get candidate info
        PreRegistration candidate = ticket.getPreRegistration();
        String candidateFullName = candidate.getFirstName() + " " + candidate.getLastName();
        String candidateEmail = candidate.getEmail();
    
        // Compose and send email
        String subject = "Candidate Verification Request";
        String message = "Dear " + reportingPerson.getFirstName() + ",\n\n"
                + "You have a new candidate to verify:\n\n"
                + "Name: " + candidateFullName + "\n"
                + "Email: " + candidateEmail + "\n\n"
                + "Please log in to the portal to review and complete the verification process.\n\n"
                + "Best regards,\nHR Team";
    
        emailService.sendEmail(reportingEmail, subject, message);
    
        return updatedTicket;
    }




    public List<VerificationTicket> getTicketsForVerification(Long companyId, Long reportingPersonId) {
        return verificationTicketRepository.findByCompany_IdAndEmployees_IdAndStatusTrueAndSendForVerificationTrue(
                companyId, reportingPersonId);
    }
    



    public VerificationTicket updateVerificationStatus(Long id, Long reportingPersonId) {
        VerificationTicket ticket = verificationTicketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VerificationTicket not found"));
    
        if (!ticket.getEmployees().getId().equals(reportingPersonId)) {
            throw new RuntimeException("Unauthorized update attempt by reporting person.");
        }
    
        ticket.setVerificationStatus(true);
        ticket.setReportingPersonAction(true);
    
        VerificationTicket updatedTicket = verificationTicketRepository.save(ticket);
    
        // Send email
        String email = ticket.getEmployees().getEmail();  // Make sure Employee has an email field
        String subject = "Verification Completed";
        String body = "Hi " + ticket.getEmployees().getFirstName() + ",\n\nYour data verification process is completed successfully.";
        emailService.sendEmail(email, subject, body);
    
        return updatedTicket;
    }
    
    
    

    public List<VerificationTicket> getPendingEmployeeActionTickets(Long preRegistrationId, String preLoginToken) {
        return verificationTicketRepository.findPendingEmployeeActionTickets(preRegistrationId, preLoginToken);
    }
    
    public List<VerificationTicket> getCompletedEmployeeActionTickets(Long preRegistrationId, String preLoginToken) {
        return verificationTicketRepository.findCompletedEmployeeActionTickets(preRegistrationId, preLoginToken);
    }
    
    public List<VerificationTicket> getPendingVerificationTickets() {
        return verificationTicketRepository.findAllPendingVerification();
    }


    public List<VerificationTicket> getActiveTicketsByCompanyId(Long companyId) {
        return verificationTicketRepository.findByCompanyIdAndStatusTrue(companyId);
    }
    
    

    public List<VerificationTicket> getPendingTicketsByReportingPerson(Long reportingPersonId) {
        return verificationTicketRepository.findByEmployeesIdAndSendForVerificationTrueAndReportingPersonActionFalseAndStatusTrue(reportingPersonId);
    }

    public List<VerificationTicket> getLatestVerifiedTicketsByCompanyId(Long companyId) {
        return verificationTicketRepository.findTop10VerifiedTicketsByCompanyId(companyId);
    }
    
    











     public String sendBackToEmployee(Long ticketId, String noteText) {
        VerificationTicket ticket = verificationTicketRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("VerificationTicket not found"));

        ticket.setSentForEmployeeAction(true);
        ticket.setReportingPersonAction(false);
        ticket.setEmployeeAction(false);
        ticket.setSentBack(true);
        verificationTicketRepository.save(ticket);

        VerificationTicketNote note = new VerificationTicketNote();
        note.setVerificationTicket(ticket);
        note.setNote(noteText);
        verificationTicketNoteRepository.save(note);

        // Send email to candidate
        String email = ticket.getPreRegistration().getEmail();
        String subject = "Application Sent Back for Update";
        String message = "Dear " + ticket.getPreRegistration().getFirstName() + ",\n\n" +
                         "Your application has been sent back for update. Please check and update the required details.\n\n" +
                         "Note: " + noteText;

        emailService.sendEmail(email, subject, message);

        return "Verification ticket updated and notification sent.";
    }
}



