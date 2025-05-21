package com.spectrum.PerformanceManagement.ModeratedRating.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.PerformanceManagement.ModeratedRating.model.ModerateRating;
import com.spectrum.PerformanceManagement.ModeratedRating.service.ModerateRatingService;

@RestController
@RequestMapping("/api/moderateRating")

public class ModerateRatingController {

        @Autowired
        private ModerateRatingService moderateRatingService;

    public ModerateRatingController(ModerateRatingService moderateRatingService) {
        this.moderateRatingService = moderateRatingService;
    }

    @PostMapping("/save/{reportingManagerId}")
    public List<ModerateRating> saveMultipleRatings(@PathVariable Long reportingManagerId, @RequestBody List<ModerateRating> ratings) {
        return moderateRatingService.saveMultipleRatings(reportingManagerId, ratings);
    }


        @GetMapping("/byManagerAndYear/{reportingManagerId}/{year}")
    public List<ModerateRating> getRatingsByManagerAndYear(@PathVariable Long reportingManagerId, @PathVariable int year) {
        return moderateRatingService.getRatingsByManagerAndYear(reportingManagerId, year);
    }

}
