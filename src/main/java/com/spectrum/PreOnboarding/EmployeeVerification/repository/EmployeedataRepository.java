package com.spectrum.PreOnboarding.EmployeeVerification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.PreOnboarding.EmployeeVerification.model.Employeedata;

@Repository
public interface EmployeedataRepository extends JpaRepository<Employeedata, Long> {

    Optional<Employeedata> findByPreRegistrationIdAndPreRegistrationPreLoginToken(Long preRegistrationId, String preLoginToken);
    Optional<Employeedata> findByPreRegistrationIdAndVerificationTicketId(Long preRegistrationId, Long verificationTicketId);

}
