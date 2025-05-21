package com.spectrum.login.service;


import com.spectrum.login.dto.JwtAuthenticationResponse;
import com.spectrum.login.dto.LoginRequest;
import com.spectrum.login.dto.RegisterRequest;
import com.spectrum.login.model.User;

public interface IAuthenticationService {
    User register(RegisterRequest registerRequest);
    public JwtAuthenticationResponse login(LoginRequest loginRequest);
}
