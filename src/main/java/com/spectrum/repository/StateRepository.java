package com.spectrum.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.model.State;

import java.util.List;


public interface StateRepository extends JpaRepository<State, Integer> {
    List<State> findByCountryId(int countryId);
}
