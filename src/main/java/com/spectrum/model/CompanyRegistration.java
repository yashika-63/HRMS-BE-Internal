package com.spectrum.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data 
public class CompanyRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length=50)
    private String companyAssignId;

    @Column(length=100)
    private String companyName;

    @Column(length=100)
    private String companyType;

    @Column(length=200)
    private String companyAddress;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String country;

    @Column
    private String postalCode;

    @Column
    private String landmark;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private String website;


    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private AccountMaster accountMaster;

}
