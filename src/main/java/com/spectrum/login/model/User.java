package com.spectrum.login.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;



@Data
@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    //new added
    private String middleName;
    private String lastName;
    private String email;
    private String password;

    private Role role;
    // new items
    private String division;
    private String department;
    private String module;
    // items me added for workflow
    private long companyId;
    private long accountId;
    private String companyRole;
    private long employeeId;
   



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public void setRole(String roleName) {
        this.role = Role.valueOf(roleName.toUpperCase());
    }
    // public void setRole(String role) {
    //     this.role = role; // Implemented setRole
    // }
    // @Override
    // public String toString() {
    // return "JwtAuthenticationResponse{" +
               
    // ", firstName='" + firstName + '\'' +
    // ", division='" + division + '\'' +
    // ", department='" + department + '\'' +
    // ", companyId=" + companyId +
    // ", accountId=" + accountId +
    // ", companyRole='" + companyRole + '\'' +
    // ", employeeId=" + employeeId +
    // ", role=" + role + // Include role in the output
    // '}';
    // }
}
