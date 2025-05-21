package com.spectrum.workflow.service;

import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.workflow.model.LeaveBucket;
import com.spectrum.workflow.repository.LeaveBucketRepositoary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveBucketService {

    @Autowired
    private LeaveBucketRepositoary leaveBucketRepository;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    int holiday=0;

    // public LeaveBucket saveLeaveBucket(LeaveBucket leaveBucket, Long companyId) {
    //     // Step 1: Validate companyId
    //     if (companyId == null) {
    //         throw new RuntimeException("Company ID cannot be null");
    //     }
    
    //     // Step 2: Fetch the company entity using companyId
    //     CompanyRegistration company = companyRegistrationRepository.findById(companyId)
    //             .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
    
    //     // Step 3: Check if a LeaveBucket with same employeeType and status = true exists for this company
    //     boolean isActiveLeaveBucketExists = leaveBucketRepository
    //             .existsByCompanyRegistrationAndEmployeeTypeAndStatus(company, leaveBucket.getEmployeeType(), true);
    
    //     // Step 4: Set status based on whether an active one exists
    //     if (isActiveLeaveBucketExists) {
    //         // An active record already exists for this employeeType → Set new one to false (inactive)
    //         leaveBucket.setStatus(false);
    //     } else {
    //         // No active record exists → Set this new one to true (active)
    //         leaveBucket.setStatus(true);
    //     }
    
    //     // Step 5: Associate the company with the leave bucket
    //     leaveBucket.setCompanyRegistration(company);
    
    //     // Step 6: Save the leave bucket to the database
    //     return leaveBucketRepository.save(leaveBucket);
    // }
    // public LeaveBucket saveLeaveBucket(LeaveBucket leaveBucket, Long companyId) {
    //     // Step 1: Validate input
    //     if (companyId == null) {
    //         throw new RuntimeException("Company ID cannot be null");
    //     }
    
    //     // Step 2: Fetch company
    //     CompanyRegistration company = companyRegistrationRepository.findById(companyId)
    //             .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
    
    //     // Step 3: Check for existing active LeaveBucket for the same employeeType
    //     Optional<LeaveBucket> existingActiveBucket = leaveBucketRepository
    //             .findByCompanyRegistrationAndEmployeeTypeAndStatus(company, leaveBucket.getEmployeeType(), true);
    
    //     // Step 4: Deactivate the old record if it exists
    //     if (existingActiveBucket.isPresent()) {
    //         LeaveBucket oldBucket = existingActiveBucket.get();
    //         oldBucket.setStatus(false);
    //         leaveBucketRepository.save(oldBucket);
    //     }
    
    //     // Step 5: Set details on the new leave bucket
    //     leaveBucket.setStatus(true); // Mark new one as active
    //     leaveBucket.setCompanyRegistration(company);
    
    //     // Step 6: Save and return the new leave bucket
    //     return leaveBucketRepository.save(leaveBucket);
    // }

    public List<LeaveBucket> saveLeaveBuckets(List<LeaveBucket> leaveBuckets, Long companyId) {
        // Step 1: Validate input
        if (companyId == null) {
            throw new RuntimeException("Company ID cannot be null");
        }
        if (leaveBuckets == null || leaveBuckets.isEmpty()) {
            return Collections.emptyList();
        }
    
        // Step 2: Fetch company
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
    
        // Step 3: Process each leave bucket individually
        List<LeaveBucket> savedBuckets = new ArrayList<>();
        for (LeaveBucket bucket : leaveBuckets) {
            // Check for existing active bucket for this employee type
            Optional<LeaveBucket> existingActiveBucket = leaveBucketRepository
                    .findByCompanyRegistrationAndEmployeeTypeAndStatus(
                        company, 
                        bucket.getEmployeeType(), 
                        true);
    
            // Deactivate old record if it exists
            if (existingActiveBucket.isPresent()) {
                LeaveBucket oldBucket = existingActiveBucket.get();
                oldBucket.setStatus(false);
                leaveBucketRepository.save(oldBucket);
            }
    
            // Set details on the new leave bucket
            bucket.setStatus(true); // Mark as active
            bucket.setCompanyRegistration(company);
    
            // Save and add to result list
            savedBuckets.add(leaveBucketRepository.save(bucket));
        }
    
        return savedBuckets;
    }
    



    // public List<LeaveBucket> getByLeaveBucket(boolean completionStatus, Long companyID, boolean status,String employee_type) {
    //     if (companyID == null) {
    //         throw new RuntimeException("Company Id is null");
    //     }

    //     // Fetch LeaveBucket records based on status and company
    //     List<LeaveBucket> leaveBuckets = leaveBucketRepository.findByStatusAndCompanyRegistration_Id(status, companyID);

    //     if (completionStatus) {
    //         // Filter for probation leave only if completionStatus = true
    //         return leaveBuckets.stream()
    //                 .filter(leave -> leave.getProbrationAllowedHolidays() != null && leave.getProbrationAllowedHolidays() > 0)
    //                 .toList();
    //     } else {
    //         return leaveBuckets.stream()
    //         .filter(leave -> leave.getProbrationAllowedHolidays() == null || leave.getProbrationAllowedHolidays() == 0)
    //         .toList();        }
    // }    


    // public List<LeaveBucket> getByLeaveBucket(boolean completionStatus, Long companyID, boolean status, String employee_type) {
    //     if (companyID == null) {
    //         throw new RuntimeException("Company Id is null");
    //     }
    
    //     // Fetch LeaveBucket records based on status and company
    //     List<LeaveBucket> leaveBuckets = leaveBucketRepository.findByStatusAndCompanyRegistration_Id(status, companyID);
    
    //     // Normalize employee_type to lower case for comparison
    //     String normalizedType = employee_type != null ? employee_type.toLowerCase() : "";
    
    //     // Case 1: For interns and probationary employees, return records with probation holidays > 0
    //     if (normalizedType.contains("intern") || normalizedType.contains("probation")||normalizedType.contains("trainee")) {
    //         return leaveBuckets.stream()
    //                 .filter(leave -> leave.getProbrationAllowedHolidays() != null && leave.getProbrationAllowedHolidays() > 0)
    //                 .toList();
    //     }
    
    //     // Case 2: For all other employees (permanent, full time, contract), exclude probation-related records
    //     return leaveBuckets.stream()
    //             .filter(leave -> leave.getProbrationAllowedHolidays() == null || leave.getProbrationAllowedHolidays() == 0)
    //             .toList();
    // }
    // public List<LeaveBucket> getByLeaveBucket(boolean completionStatus, Long companyID, boolean status, String employee_type) {
    //     if (companyID == null) {
    //         throw new RuntimeException("Company Id is null");
    //     }
    
    //     // Validate employee_type and completionStatus combination
    //     List<String> probationaryTypes = List.of("intern", "probation", "trainee");
    
    //     if (probationaryTypes.contains(employee_type.toLowerCase()) && !completionStatus) {
    //         throw new RuntimeException("Invalid combination: completionStatus must be true for intern/probation/trainee.");
    //     } else if (!probationaryTypes.contains(employee_type.toLowerCase()) && completionStatus) {
    //         throw new RuntimeException("Invalid combination: completionStatus must be false for permanent/fulltime/contract employees.");
    //     }
    
    //     // Fetch LeaveBucket records based on status and company
    //     List<LeaveBucket> leaveBuckets = leaveBucketRepository.findByStatusAndCompanyRegistration_Id(status, companyID);
    
    //     if (completionStatus) {
    //         // Return probationary data (only selected fields)
    //         return leaveBuckets.stream()
    //                 .filter(leave -> leave.getProbrationAllowedHolidays() != null && leave.getProbrationAllowedHolidays() > 0)
    //                 .map(leave -> Map.of(
    //                         "id", leave.getId(),
    //                         "status", leave.getStatus(),
    //                         "probrationAllowedHolidays", leave.getProbrationAllowedHolidays()
    //                 ))
    //                 .toList();
    //     } else {
    //         // Return full data for permanent/fulltime/contract
    //         return leaveBuckets.stream()
    //                 .filter(leave -> leave.getProbrationAllowedHolidays() == null || leave.getProbrationAllowedHolidays() == 0)
    //                 .toList();
    //     }
    // }

public List<LeaveBucket> getByLeaveBucket(boolean completionStatus, Long companyID, boolean status, String employee_type) {
    if (companyID == null) {
        throw new RuntimeException("Company Id is null");
    }

    List<LeaveBucket> leaveBuckets = leaveBucketRepository.findByStatusAndCompanyRegistration_Id(status, companyID);

    if (employee_type.equalsIgnoreCase("intern") || employee_type.equalsIgnoreCase("probation") || employee_type.equalsIgnoreCase("trainee")) {
        return leaveBuckets.stream()
                .filter(lb -> lb.getProbrationAllowedHolidays() != null && lb.getProbrationAllowedHolidays() > 0)
                .collect(Collectors.toList());
    } else {
        return leaveBuckets.stream()
                .filter(lb -> lb.getProbrationAllowedHolidays() == null || lb.getProbrationAllowedHolidays() == 0)
                .collect(Collectors.toList());
    }
}

    
public List<LeaveBucket> getByCriteria(Long companyID, String employeeType, boolean status, boolean fullData) {
    // Fetch LeaveBucket records based on company ID, employee type, and status
    List<LeaveBucket> leaveBuckets = leaveBucketRepository
            .findByCompanyRegistration_IdAndEmployeeTypeAndStatus(companyID, employeeType, status);

    // If fullData is true, return the complete data
    if (fullData) {
        return leaveBuckets;
    } else {
        // Otherwise, return the data with partial fields (only ID, status, and probrationAllowedHolidays)
        return leaveBuckets.stream()
                .map(leave -> {
                    LeaveBucket partial = new LeaveBucket();
                    partial.setId(leave.getId());
                    partial.setStatus(leave.getStatus());
                    partial.setProbrationAllowedHolidays(leave.getProbrationAllowedHolidays());
                    return partial;
                })
                .collect(Collectors.toList());
    }
}

public List<LeaveBucket> getActiveLeaveBuckets(Long companyId) {
    if (companyId == null) {
        // You can throw a custom exception or return an empty list
        throw new IllegalArgumentException("Company ID must not be null");
        // Or alternatively: return Collections.emptyList();
    }
 
    List<LeaveBucket> leaveBuckets = leaveBucketRepository.findActiveLeaveBucketsByCompanyId(companyId);
 
    if (leaveBuckets == null || leaveBuckets.isEmpty()) {
        // Log or handle the case when no data is found
        // For example:
        System.out.println("No active leave buckets found for company ID: " + companyId);
        return Collections.emptyList();
    }
 
    return leaveBuckets;
}   
    
}
