package com.spectrum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import jakarta.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 100)
    private String stateName;


    @ManyToOne
    @JoinColumn(name = "country_id")
    @JsonIgnore
    private Country country;


    @OneToMany(mappedBy = "state",cascade = CascadeType.ALL)
    private List<City> city;


}
