package com.spectrum.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.model.City;
import com.spectrum.model.State;
import com.spectrum.service.StateCityService;

import lombok.AllArgsConstructor;

// @CrossOrigin("http://localhost:3000")

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class StateCityController {

    StateCityService stateCityService;

    @PostMapping("/states")
    public ResponseEntity<State> saveState(@RequestBody State state) {
        State savedState = stateCityService.saveState(state);
        return new ResponseEntity<>(savedState, HttpStatus.CREATED);
    }

    @GetMapping("/states")
    public ResponseEntity<List<State>> getAllStates() {
        List<State> states = stateCityService.getAllStates();
        return new ResponseEntity<>(states, HttpStatus.OK);
    }

    @GetMapping("/states/{id}")
    public ResponseEntity<State> getStateById(@PathVariable int id) {
        Optional<State> stateOptional = stateCityService.getStateById(id);
        return stateOptional.map(state -> new ResponseEntity<>(state, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/states/{stateId}/cities")
    public ResponseEntity<City> saveCity(@PathVariable int stateId, @RequestBody City city) {
        City savedCity = stateCityService.saveCity(stateId, city);
        return new ResponseEntity<>(savedCity, HttpStatus.CREATED);
    }

    @GetMapping("/states/{stateId}/cities")
    public ResponseEntity<List<City>> getCitiesByStateId(@PathVariable int stateId) {
        List<City> cities = stateCityService.getCitiesByStateId(stateId);
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
}
