package com.spectrum.login.dto;




import com.spectrum.login.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
    //new added 
    private String division;
    private String department;
    private String module;// if needed change data type
    private String middleName;
    private long companyId;
    private long accountId;
    private String companyRole;
    private long employeeId;
   //  @Default private String role = "user";
}
