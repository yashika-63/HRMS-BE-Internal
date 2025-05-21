package com.spectrum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.model.Country;
import com.spectrum.model.State;
import com.spectrum.service.LocationService;

import lombok.AllArgsConstructor;
@RestController
// @CrossOrigin("http://localhost:3000")


@RequestMapping("/api/location")
@AllArgsConstructor
public class LocationController {

    LocationService locationService;

    @PostMapping("/countries")
    public Country addCountry(@RequestBody Country country) {
        return locationService.saveCountry(country);
    }

    @PostMapping("/countries/{countryId}/states")
    public State addState(@PathVariable int countryId, @RequestBody State state) {
        return locationService.saveState(countryId, state);
    }

    @GetMapping
    public List<Country> getAllCountries() {
        return locationService.getAllCountries();
    }

    @GetMapping("/countries/{countryId}/states")
    public List<State> getAllStatesByCountryId(@PathVariable int countryId) {
        return locationService.getAllStatesByCountryId(countryId);
    }
}
