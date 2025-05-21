package com.spectrum.login.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.spectrum.login.model.User;

public interface IUserService {
    UserDetailsService userDetailsService();
    User updateUser(Integer id, User updatedUser);

    User updateUserByEmployeeId(Long employeeId, User updatedUser); // Add this method

    boolean sendOtpToEmail(String email);
    
    User updatePasswordWithOtp(String email, String otp, String newPassword);


}
