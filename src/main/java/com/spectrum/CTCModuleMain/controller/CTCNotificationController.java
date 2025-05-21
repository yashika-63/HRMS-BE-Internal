package com.spectrum.CTCModuleMain.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.CTCModuleMain.model.CTCBreakdownHeader;
import com.spectrum.CTCModuleMain.service.CTCBreakdownHeaderService;
import com.spectrum.CTCModuleMain.service.CTCNotificationService;
import com.spectrum.service.EmailService;

@RestController
@RequestMapping("/api/notifications")
public class CTCNotificationController {

    @Autowired
    private CTCNotificationService ctcNotificationService;


     @Autowired
    private CTCBreakdownHeaderService ctcBreakdownHeaderService;

    @Autowired
    private EmailService emailService;

    // This endpoint can be called to manually trigger the CTC expiry check
    @GetMapping("/check-ctc-expiry")
    public String checkCTCExpiryNow() {
        ctcNotificationService.checkCTCExpiry();
        return "CTC expiry check completed!";
    }

    
    @GetMapping("/check-ctc-expiryone")
    public String checkCTCExpiry() {
        // Get the list of CTCs that are expiring
        List<CTCBreakdownHeader> expiringCTCs = ctcBreakdownHeaderService.findExpiringCTCs(LocalDate.now());
        
        // Iterate through the expiring CTCs
        for (CTCBreakdownHeader ctc : expiringCTCs) {
            // Get the employee's email and send a notification
            String email = ctc.getEmployee().getEmail();
            String subject = "CTC Expiry Notification";
            String text = "Dear " + ctc.getEmployee().getFirstName() + ",\n\n" +
                          "Your CTC breakdown is nearing expiry on " + ctc.getEffectiveToDate() + ".\n" +
                          "Please take the necessary actions.\n\n" +
                          "Best regards,\nYour Company";
            
            // Send the email
            emailService.sendEmail(email, subject, text);
            
            // Update the CTC status to false
            ctc.setCtcStatus(false);
            
            // Save the updated CTC object
            ctcBreakdownHeaderService.save(ctc);
        }
        
        // Return a message indicating the process is complete
        return "Notifications sent and statuses updated for expiring CTCs.";
    }
    

    // @GetMapping("/check-ctc-expiryone")
    // public String checkCTCExpiry() {
    //     List<CTCBreakdownHeader> expiringCTCs = ctcBreakdownHeaderService.findExpiringCTCs(LocalDate.now());

    //     for (CTCBreakdownHeader ctc : expiringCTCs) {
    //         String email = ctc.getEmployee().getEmail(); // Get the employee's email
    //         String subject = "CTC Expiry Notification";
    //         String text = "Dear " + ctc.getEmployee().getFirstName() + ",\n\n" +
    //                       "Your CTC breakdown is nearing expiry on " + ctc.getEffectiveToDate() + ".\n" +
    //                       "Please take the necessary actions.\n\n" +
    //                       "Best regards,\n" +
    //                       "Your Company";

    //         emailService.sendEmail(email, subject, text);
    //     }

    //     return "Notifications sent for expiring CTCs.";
    // }

    // @GetMapping("/check-ctc-expiryone")
    // public String checkCTCExpiry() {
    //     List<CTCBreakdownHeader> expiringCTCs = ctcBreakdownHeaderService.findExpiringCTCs(LocalDate.now());

    //     for (CTCBreakdownHeader ctc : expiringCTCs) {
    //         String email = ctc.getEmployee().getEmail(); // Get the employee's email
    //         String subject = "CTC Expiry Notification";
    //         String text = "Dear " + ctc.getEmployee().getFirstName() + ",\n\n" +
    //                       "Your CTC breakdown is nearing expiry on " + ctc.getEffectiveToDate() + ".\n" +
    //                       "Please take the necessary actions.\n\n" +
    //                       "Best regards,\n" +
    //                       "Your Company";

    //         emailService.sendEmail(email, subject, text);
    //     }

    //     return "Notifications sent for expiring CTCs.";
    // }





}
