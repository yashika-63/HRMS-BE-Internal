package com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.EmployeeConfig;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeConfigRepository extends JpaRepository<EmployeeConfig, Long> {
    // Add custom query methods here if needed

      // Find EmployeeConfig by employee ID
          List<EmployeeConfig> findByEmployee_Id(Long employeeId);
          Optional<EmployeeConfig> findByEmployee(Employee employee);
          Optional<EmployeeConfig> findByEmployeeId(Long employeeId);
          


          // Optional<EmployeeConfig> findByReportingManagerId(Long reportingManagerId);
          List<EmployeeConfig> findByReportingManagerId(Long reportingManagerId);
          EmployeeConfig findByEmployeeIdAndReportingManagerId(Long employeeId, Long reportingManagerId);
          List<EmployeeConfig> findByConfirmationStatusFalse();


            @Query("SELECT e FROM EmployeeConfig e WHERE " +
           "(:companyId IS NULL OR e.company.id = :companyId) AND " +
           "(:designationId IS NULL OR e.designation_id = :designationId) AND " +
           "(:departmentId IS NULL OR e.departmentId = :departmentId) AND " +
           "(:regionId IS NULL OR e.regionId = :regionId)")
    List<EmployeeConfig> findFilteredEmployeeConfigs(
            @Param("companyId") Long companyId,
            @Param("designationId") Integer designationId,
            @Param("departmentId") Integer departmentId,
            @Param("regionId") Integer regionId
    );

        Optional<EmployeeConfig> findByEmployeeAndCompany(Employee employee, CompanyRegistration company);


}
