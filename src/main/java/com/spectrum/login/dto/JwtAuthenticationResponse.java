package com.spectrum.login.dto;


import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String token;
    //newely added
    private String firstName;
    private String role;
    private String department;
    private String division;
    private long companyId;
    private long accountId;
    private String companyRole;
    private long employeeId;
    // public void setFirstName(String firstName) {
    //
    //     throw new UnsupportedOperationException("Unimplemented method 'setFirstName'");
    // }
}
