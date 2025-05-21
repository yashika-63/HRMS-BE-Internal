package com.spectrum.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.spectrum.model.City;
import com.spectrum.model.State;
import com.spectrum.repository.CityRepository;
import com.spectrum.repository.StateRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StateCityService {

    StateRepository stateRepository;

     CityRepository cityRepository;

    public State saveState(State state) {
        return stateRepository.save(state);
    }

    public List<State> getAllStates() {
        return stateRepository.findAll();
    }

    public Optional<State> getStateById(int id) {
        return stateRepository.findById(id);
    }

    public City saveCity(int stateId, City city) {
        Optional<State> stateOptional = stateRepository.findById(stateId);
        if (stateOptional.isPresent()) {
            State state = stateOptional.get();
            city.setState(state);
            return cityRepository.save(city);
        }
        throw new IllegalArgumentException("State with id " + stateId + " not found");
    }

    public List<City> getCitiesByStateId(int stateId) {
        return cityRepository.findByStateId(stateId);
    }
}
