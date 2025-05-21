package com.spectrum.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.spectrum.model.Country;
import com.spectrum.model.State;
import com.spectrum.repository.CountryRepository;
import com.spectrum.repository.StateRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LocationService {
 CountryRepository countryRepository;

     StateRepository stateRepository;

    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }

    public State saveState(int countryId, State state) {
        Optional<Country> optionalCountry = countryRepository.findById(countryId);
        if (optionalCountry.isPresent()) {
            Country country = optionalCountry.get();
            state.setCountry(country);
            return stateRepository.save(state);
        } else {
            throw new IllegalArgumentException("Country with id " + countryId + " not found");
        }
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public List<State> getAllStatesByCountryId(int countryId) {
        return stateRepository.findByCountryId(countryId);
    }
}
