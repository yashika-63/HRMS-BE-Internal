package com.spectrum.login.service.impl;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spectrum.controller.ResourceNotFoundException;
import com.spectrum.login.model.OtpEntry;
import com.spectrum.login.model.User;
import com.spectrum.login.repository.IUserRepository;
import com.spectrum.login.repository.OtpRepository;
import com.spectrum.login.service.IUserService;
import com.spectrum.service.EmailService;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    @Transactional
    public User updateUser(Integer id, User updatedUser) {
        // Fetch the existing user by ID
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Update the existing user's fields with values from updatedUser
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setMiddleName(updatedUser.getMiddleName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword()); // Store the raw password temporarily
        existingUser.setRole(updatedUser.getRole());
        existingUser.setDivision(updatedUser.getDivision());
        existingUser.setDepartment(updatedUser.getDepartment());
        existingUser.setModule(updatedUser.getModule());
        existingUser.setCompanyId(updatedUser.getCompanyId());
        existingUser.setAccountId(updatedUser.getAccountId());
        existingUser.setCompanyRole(updatedUser.getCompanyRole());
        existingUser.setEmployeeId(updatedUser.getEmployeeId());

        // Save the updated user (you may want to encode the password here)
        return userRepository.save(existingUser);
    }

    @Override
    public User updateUserByEmployeeId(Long employeeId, User updatedUser) {
        // Fetch the existing user by employeeId
        User existingUser = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with employeeId: " + employeeId));

        // Update the details
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setMiddleName(updatedUser.getMiddleName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setCompanyRole(updatedUser.getCompanyRole());

        // Only update password if it is being changed
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
            existingUser.setPassword(encodedPassword);
        }

        // The following fields remain unchanged
        // existingUser.setRole(updatedUser.getRole()); // Unchanged
        existingUser.setDivision(updatedUser.getDivision());
        existingUser.setDepartment(updatedUser.getDepartment());
        existingUser.setModule(updatedUser.getModule());

        // Save the updated user back to the database
        return userRepository.save(existingUser);
    }

    /////
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///

    // Temporary store for OTPs (in a real-world app, use Redis or a database)
    private final Map<Long, String> otpStore = new HashMap<>();

    public void sendOtpForPasswordReset(Long employeeId) {
        // Fetch the user by employeeId
        User user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with employeeId: " + employeeId));

        // Generate a random OTP
        String otp = String.valueOf(new Random().nextInt(999999)).substring(0, 6);

        // Store OTP against the employeeId
        otpStore.put(employeeId, otp);

        // Simulate sending OTP to user's email
        System.out.println("Sending OTP to " + user.getEmail() + ": " + otp);
        // In production, integrate with an email service like AWS SES, SendGrid, etc.
    }

    public void updatePasswordByOtp(Long employeeId, String otp, String newPassword) {
        // Verify OTP
        String storedOtp = otpStore.get(employeeId);
        if (storedOtp == null || !storedOtp.equals(otp)) {
            throw new IllegalArgumentException("Invalid or expired OTP.");
        }

        // Fetch the user by employeeId
        User user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with employeeId: " + employeeId));

        // Encode the new password
        String encodedPassword = passwordEncoder.encode(newPassword);

        // Update the user's password
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // Remove OTP from store after successful verification
        otpStore.remove(employeeId);
    }

    @Transactional
    @Override
    public boolean sendOtpToEmail(String email) {
        // Check if the email exists in the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Ensure there is only one OTP entry per email
        otpRepository.deleteByEmail(email);  // Delete any existing OTP for the same email

        // Generate a 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Save the OTP in a temporary storage (e.g., a cache or database)
        otpRepository.save(new OtpEntry(email, otp, LocalDateTime.now().plusMinutes(5))); // Expiry after 5 minutes

        // Send the OTP to the user's email
        try {
            emailService.sendEmail(
                    email,
                    "Your OTP for Password Reset",
                    "Your OTP is: " + otp + ". It is valid for 5 minutes.");
        } catch (Exception e) {
            // Log or handle the email sending failure here
            throw new RuntimeException("Failed to send OTP email.", e);
        }

        return true;
    }

    ////
    ///
    ///

    @Override
    public User updatePasswordWithOtp(String email, String otp, String newPassword) {
        // Fetch the OTP entry for the given email
        OtpEntry otpEntry = otpRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No OTP found for the provided email."));

        // Validate the OTP and check if it has expired
        if (!otpEntry.getOtp().equals(otp) || otpEntry.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired OTP.");
        }

        // Fetch the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Encode and update the password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Optionally, delete the OTP entry after successful verification
        otpRepository.delete(otpEntry);

        return user;
    }

}
