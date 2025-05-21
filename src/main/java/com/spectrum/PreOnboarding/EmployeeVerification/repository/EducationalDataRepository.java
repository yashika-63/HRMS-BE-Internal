package com.spectrum.PreOnboarding.EmployeeVerification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.PreOnboarding.EmployeeVerification.model.EducationalData;
import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;

public interface EducationalDataRepository extends JpaRepository<EducationalData, Long> {

    @Query("SELECT e FROM EducationalData e WHERE e.verificationTicket.id = :verificationId AND e.preRegistration.preLoginToken = :preLoginToken")
List<EducationalData> findByVerificationIdAndPreLoginToken(@Param("verificationId") Long verificationId, @Param("preLoginToken") String preLoginToken);





@Modifying
@Query("UPDATE EducationalData e SET e.verificationStatus = :status WHERE e.id = :id")
void updateVerificationStatusById(@Param("id") Long id, @Param("status") boolean status);
}
