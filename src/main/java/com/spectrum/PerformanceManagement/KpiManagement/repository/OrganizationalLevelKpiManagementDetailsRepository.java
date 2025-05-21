package com.spectrum.PerformanceManagement.KpiManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.PerformanceManagement.KpiManagement.model.OrganizationalLevelKpiManagementDetails;

@Repository
public interface OrganizationalLevelKpiManagementDetailsRepository extends JpaRepository<OrganizationalLevelKpiManagementDetails, Long> {

}
