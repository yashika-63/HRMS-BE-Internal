package com.spectrum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.model.City;

import java.util.*;

public interface CityRepository extends JpaRepository<City,Integer> {
    List<City> findByStateId(int stateId);
}
