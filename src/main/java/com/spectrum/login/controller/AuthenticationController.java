package com.spectrum.login.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.login.dto.JwtAuthenticationResponse;
import com.spectrum.login.dto.LoginRequest;
import com.spectrum.login.dto.RegisterRequest;
import com.spectrum.login.model.User;
import com.spectrum.login.service.IAuthenticationService;
import com.spectrum.login.service.IUserService;

@RestController
@RequiredArgsConstructor
//@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    @Autowired
    private IUserService iUserService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        User user = iUserService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }













    
}
