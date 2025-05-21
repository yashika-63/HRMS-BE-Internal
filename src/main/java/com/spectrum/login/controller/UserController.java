package com.spectrum.login.controller;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.controller.ResourceNotFoundException;
import com.spectrum.login.model.User;
import com.spectrum.login.service.impl.UserServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserServiceImpl userService;
    @GetMapping()
    public ResponseEntity<String> sayHi() {
        return ResponseEntity.ok("HI USER");
    }

    // Update user by employeeId
    @PutMapping("/update-by-employee/{employeeId}")
    public ResponseEntity<User> updateUserByEmployeeId(@PathVariable Long employeeId, @RequestBody User updatedUser) {
        User user = userService.updateUserByEmployeeId(employeeId, updatedUser);
        return ResponseEntity.ok(user);
    }





    /////
    /// 
    /// 
    /// 
    /// 
    /// 
    



      @PostMapping("/send-otp/{employeeId}")
    public ResponseEntity<String> sendOtp(@PathVariable Long employeeId) {
        userService.sendOtpForPasswordReset(employeeId);
        return ResponseEntity.ok("OTP sent to the registered email address.");
    }


    @PostMapping("/send-otp")
public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    if (email == null || email.isEmpty()) {
        return ResponseEntity.badRequest().body("Email is required.");
    }
    boolean isSent = userService.sendOtpToEmail(email);
    if (isSent) {
        return ResponseEntity.ok("OTP sent to the provided email address.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found in the system.");
    }
}


    @PutMapping("/update-password-by-otp")
    public ResponseEntity<String> updatePasswordByOtp(
            @RequestParam Long employeeId,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        userService.updatePasswordByOtp(employeeId, otp, newPassword);
        return ResponseEntity.ok("Password updated successfully.");
    }






    @PutMapping("/update-password-by-otps")
    public ResponseEntity<String> updatePasswordByOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");
    
        if (email == null || otp == null || newPassword == null) {
            return ResponseEntity.badRequest().body("Email, OTP, and new password are required.");
        }
    
        try {
            // Call the service method to update the password
            userService.updatePasswordWithOtp(email, otp, newPassword);
            return ResponseEntity.ok("Password updated successfully.");
        } catch (IllegalArgumentException e) {
            // Handle invalid OTP or expired OTP
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            // Handle user not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
}
