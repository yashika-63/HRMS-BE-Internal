package com.spectrum.PerformanceManagement.ModeratedRating.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.PerformanceManagement.ModeratedRating.model.ModerateRating;
import com.spectrum.PerformanceManagement.ModeratedRating.repository.ModerateRatingRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class ModerateRatingService {

    @Autowired
    private ModerateRatingRepository moderateRatingRepository;
    @Autowired
    private EmployeeRepository employeeRepository;



      public ModerateRatingService(ModerateRatingRepository moderateRatingRepository, EmployeeRepository employeeRepository) {
        this.moderateRatingRepository = moderateRatingRepository;
        this.employeeRepository = employeeRepository;
    }
     @Transactional
    public List<ModerateRating> saveMultipleRatings(Long reportingManagerId, List<ModerateRating> ratings) {
        Employee reportingManager = employeeRepository.findById(reportingManagerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Reporting Manager ID"));

        int currentYear = LocalDate.now().getYear(); // Get the current year

        List<ModerateRating> processedRatings = ratings.stream().map(rating -> {
            Employee employee = employeeRepository.findById(rating.getEmployee().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Employee ID: " + rating.getEmployee().getId()));

            // Check if an existing record is present for the current year
            ModerateRating existingRating = moderateRatingRepository.findByEmployeeIdAndYear(employee.getId(), currentYear);

            if (existingRating != null) {
                // Update the existing record
                existingRating.setModeratedRating(rating.getModeratedRating());
                existingRating.setEmployees(reportingManager);
                return existingRating;
            } else {
                // Create a new record if no existing record is found
                rating.setEmployee(employee);
                rating.setEmployees(reportingManager);
                rating.setDate(LocalDate.now()); // Ensure the current date is set
                return rating;
            }
        }).collect(Collectors.toList());

        return moderateRatingRepository.saveAll(processedRatings);
    }


    public List<ModerateRating> getRatingsByManagerAndYear(Long reportingManagerId, int year) {
        return moderateRatingRepository.findByReportingManagerIdAndYear(reportingManagerId, year);
    }
}
