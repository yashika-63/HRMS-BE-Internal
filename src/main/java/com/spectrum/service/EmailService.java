package com.spectrum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
    }


    public void sendOtpEmail(String to, String otp) {
        String subject = "Your OTP for Knowledge Transfer";
        String message = "Your One-Time Password (OTP) is: " + otp + "\n\n"
                       + "This OTP is valid for 5 minutes.\n\n"
                       + "Please do not share this OTP with anyone.";
       
        sendEmail(to, subject, message);
    }
 
    public void sendManagerResignationNotification(String managerEmail, String managerSubject, String managerContent) {
        sendEmail(managerEmail, managerSubject, managerContent);
    }
}
