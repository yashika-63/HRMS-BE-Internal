package com.spectrum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.model.Country;


public interface CountryRepository extends JpaRepository<Country, Integer> {

}
