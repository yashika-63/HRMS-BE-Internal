package com.spectrum.ExitProcess.Service;

import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPService {
    private final Map<Long, OTPData> otpMap = new HashMap<>();
    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALID_MINUTES = 5;

      @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmailService emailService;

    // public String generateOTP(Long employeeId) {
    //     String otp = generateRandomOTP();
    //     LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES);
    //     otpMap.put(employeeId, new OTPData(otp, expiryTime));
    //     return otp;
    // }


    public String generateAndSendOTP(Long employeeToId) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeToId);
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }
        
        Employee employee = employeeOpt.get();
        String otp = generateRandomOTP();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES);
        
        // Store OTP only against employee_to
        otpMap.put(employeeToId, new OTPData(otp, expiryTime));
        
        emailService.sendOtpEmail(employee.getEmail(), otp);
        return otp;
    }
  
    // Changed to accept employeeId directly
    public boolean verifyOTP(Long employeeToId, String otp) {
        // Only check against employee_to
        OTPData otpData = otpMap.get(employeeToId);
        
        if (otpData == null) {
            System.out.println("[OTP Verification] No OTP found for employee_to: " + employeeToId);
            return false;
        }
        
        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            otpMap.remove(employeeToId);
            System.out.println("[OTP Verification] OTP expired for employee_to: " + employeeToId);
            return false;
        }
        
        boolean isValid = otpData.getOtp().equals(otp.trim());
        if (isValid) {
            otpMap.remove(employeeToId);
            System.out.println("[OTP Verification] Valid OTP for employee_to: " + employeeToId);
        } else {
            System.out.println("[OTP Verification] Invalid OTP for employee_to: " + employeeToId);
        }
        
        return isValid;
    }   

    private String generateRandomOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    private static class OTPData {
        private final String otp;
        private final LocalDateTime expiryTime;

        public OTPData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() { return otp; }
        public LocalDateTime getExpiryTime() { return expiryTime; }
    }
}