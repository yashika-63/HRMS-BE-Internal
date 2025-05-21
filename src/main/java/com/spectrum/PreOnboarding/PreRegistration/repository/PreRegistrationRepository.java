package com.spectrum.PreOnboarding.PreRegistration.repository;

import com.spectrum.PreOnboarding.PreRegistration.model.PreRegistration;
import com.spectrum.model.CompanyRegistration;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PreRegistrationRepository extends JpaRepository<PreRegistration, Long> {

    Optional<PreRegistration> findByEmail(String email);

    Optional<PreRegistration> findByEmailAndCompany(String email, CompanyRegistration company);

    Optional<PreRegistration> findByEmailAndPreLoginTokenAndLoginStatus(String email, String preLoginToken, boolean loginStatus);

    List<PreRegistration> findByCompanyIdAndStatusTrueAndEmployeesIdAndOfferGeneratedFalse(Long companyId, Long reportingPersonId);
    List<PreRegistration> findByCompanyIdAndStatusTrueAndEmployeesIdAndOfferGeneratedTrue(Long companyId, Long reportingPersonId);


    List<PreRegistration> findByCompany_IdAndStatus(Long companyId, boolean status);
    List<PreRegistration> findByCompany_IdAndStatusTrueAndOfferGeneratedFalse(Long companyId);

    List<PreRegistration> findByCompany_IdAndStatusTrueAndOfferGeneratedTrue(Long companyId);
    
    Optional<PreRegistration> findByPreLoginToken(String preLoginToken);

      @Query("SELECT p FROM PreRegistration p WHERE p.company.id = :companyId AND MONTH(p.date) = :month AND YEAR(p.date) = :year ORDER BY p.date DESC")
Page<PreRegistration> findByCompanyAndMonthAndYear(
        @Param("companyId") Long companyId,
        @Param("month") int month,
        @Param("year") int year,
        Pageable pageable);

    

        
}
