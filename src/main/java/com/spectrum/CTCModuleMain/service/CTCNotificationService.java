package com.spectrum.CTCModuleMain.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spectrum.CTCModuleMain.model.CTCBreakdownHeader;
import com.spectrum.CTCModuleMain.repository.CTCBreakdownHeaderRepository;

import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;

@Service
public class CTCNotificationService {

        private static final Logger logger = LoggerFactory.getLogger(CTCNotificationService.class);

        @Autowired
        private CTCBreakdownHeaderService ctcBreakdownHeaderService;

    // @Autowired
    // private CTCBreakdown ctcBreakdown;

    
    @Autowired
    private CTCBreakdownHeaderRepository ctcBreakdownHeaderRepository;


    // This method will run once a day at midnight (00:00 AM)
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkCTCExpiry() {
        LocalDate today = LocalDate.now();
        List<CTCBreakdownHeader> expiringCTCs = ctcBreakdownHeaderRepository.findAllByEffectiveToDateBefore(today);
 
        for (CTCBreakdownHeader ctc : expiringCTCs) {
            sendNotification(ctc);
        }
    }

    // Method to send notification
    
    // public void sendNotification(CTCBreakdownHeader ctc) {
    // String employeeEmail = ctc.getEmployee().getEmail();

    // SimpleMailMessage msg = new SimpleMailMessage();
    // msg.setTo(employeeEmail);
    // msg.setSubject("CTC Period Over Notification");
    // msg.setText("Dear " + ctc.getEmployee().getFirstName() + ",\n\n" +
    // "Your CTC breakdown period has ended on " + ctc.getEffectiveToDate() +
    // ". Please review your CTC details.\n\n" +
    // "Best regards,\nYour HR Team");
    // // ctcBreakdown.setCtcStatus(false);

    // // Log email details
    // // logger.info("Sending email to: {}", employeeEmail);
    // // logger.info("Subject: {}", msg.getSubject());
    // // logger.info("Email Body:\n{}", msg.getText());

    // // javaMailSender.send(msg);
    // }

    public void sendNotification(CTCBreakdownHeader ctc) {
        // Get employee email and send notification
        String employeeEmail = ctc.getEmployee().getEmail();

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(employeeEmail);
        msg.setSubject("CTC Period Over Notification");
        msg.setText("Dear " + ctc.getEmployee().getFirstName() + ",\n\n" +
                "Your CTC breakdown period has ended on " + ctc.getEffectiveToDate() +
                ". Please review your CTC details.\n\n" +
                "Best regards,\nYour HR Team");

        // Update the status of the CTC
        ctc.setCtcStatus(false); // Set the status to false
        ctcBreakdownHeaderService.save(ctc); // Save the updated CTC object

        // Log email details (optional for debugging)
        // logger.info("Sending email to: {}", employeeEmail);
        // logger.info("Subject: {}", msg.getSubject());
        // logger.info("Email Body:\n{}", msg.getText());

        // Uncomment the following line to actually send the email
        // javaMailSender.send(msg);
    }
}
