package com.spectrum.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.model.CompanyRegistration;

@Repository

public interface CompanyRegistrationRepository extends JpaRepository<CompanyRegistration, Long> {

    List<CompanyRegistration> findByCompanyAssignIdAndAccountMaster_AccountId(String companyAssignId, long accountId);

    // Optional<CompanyRegistration>
    // findByCompanyIdAndAccountMaster_AccountId(String companyId, long accountId);

    Optional<CompanyRegistration> findByCompanyName(String companyName);

    Optional<CompanyRegistration> findByCompanyAssignId(String companyAssignId);
    // Optional<CompanyRegistration> findByCompanyName(String companyName);
    // CompanyRegistration findByCompanyId(String companyId);

    Optional<CompanyRegistration> findByAccountMaster_AccountId(Long accountId);

    List<CompanyRegistration> findByAccountMaster_AccountId(long accountId);

}
