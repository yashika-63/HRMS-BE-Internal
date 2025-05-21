package com.spectrum.PreOnboarding.EmployeeVerification.repository;

import com.spectrum.PreOnboarding.EmployeeVerification.model.EmploymentData;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EmploymentDataRepository extends JpaRepository<EmploymentData, Long> {


    List<EmploymentData> findByVerificationTicketIdAndPreRegistrationPreLoginToken(Long verificationId, String preLoginToken);


    
    @Modifying
    @Transactional
    @Query("UPDATE EmploymentData e SET e.verificationStatus = :status WHERE e.id = :id")
    void updateVerificationStatusById(Long id, boolean status);
}
