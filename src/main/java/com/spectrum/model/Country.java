package com.spectrum.model;

//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 50)
    private String countryName;
    @Column(length = 10)
    private String countryCode;
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<State> states;
}
