package com.spectrum.workflow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.model.CompanyRegistration;
import com.spectrum.workflow.model.LeaveBucket;

public interface LeaveBucketRepositoary extends JpaRepository<LeaveBucket,Long> {
   

    List<LeaveBucket> findByStatusAndCompanyRegistration_Id(Boolean status, Long companyId);

    public List<LeaveBucket> findByCompanyRegistration_IdAndEmployeeTypeAndStatus(Long companyId, String employeeType, boolean status);

     boolean existsByCompanyRegistrationAndEmployeeTypeAndStatus(CompanyRegistration company, String employeeType, Boolean status);

     Optional<LeaveBucket> findByCompanyRegistrationAndEmployeeTypeAndStatus(
    CompanyRegistration companyRegistration, String employeeType, Boolean status);

    LeaveBucket findByEmployeeType(String employeeType);

@Query("SELECT lb FROM LeaveBucket lb WHERE lb.companyRegistration.id = :companyId AND lb.status = true")
    List<LeaveBucket> findActiveLeaveBucketsByCompanyId(@Param("companyId") Long companyId);
}
