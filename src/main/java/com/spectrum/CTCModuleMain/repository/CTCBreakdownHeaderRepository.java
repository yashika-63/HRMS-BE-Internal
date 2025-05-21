package com.spectrum.CTCModuleMain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.spectrum.CTCModuleMain.model.CTCBreakdownHeader;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;

public interface CTCBreakdownHeaderRepository extends JpaRepository<CTCBreakdownHeader, Long> {

    // Method to fetch active CTC headers
    List<CTCBreakdownHeader> findByCtcStatus(boolean ctcStatus);

    // Custom query to fetch active CTC headers by status and company ID
    @Query("SELECT h FROM CTCBreakdownHeader h WHERE h.ctcStatus = :ctcStatus AND h.employee.companyRegistration.id = :companyId")
    List<CTCBreakdownHeader> findByCtcStatusAndEmployee_CompanyRegistration_Id(
            @Param("ctcStatus") boolean ctcStatus,
            @Param("companyId") Long companyId);

           // Custom query to fetch active CTC headers by status and employee ID
    @Query("SELECT h FROM CTCBreakdownHeader h WHERE h.ctcStatus = :ctcStatus AND h.employee.id = :employeeId")
    List<CTCBreakdownHeader> findByCtcStatusAndEmployee_Id(
            @Param("ctcStatus") boolean ctcStatus,
            @Param("employeeId") Long employeeId);

    List<CTCBreakdownHeader> findAllByEffectiveToDateBefore(LocalDate date);
    List<CTCBreakdownHeader> findByEffectiveToDateLessThanEqual(LocalDate date);


  // Custom query method to check if a record exists with ctcStatus=false for a specific ID
  boolean existsByIdAndCtcStatusFalse(Long ctcBreakdownHeaderId);

  // Standard delete method provided by JpaRepository
  void deleteById(Long ctcBreakdownHeaderId);




  // 1. Get record by companyId when ctcStatus = true
  List<CTCBreakdownHeader> findByCompanyIdAndCtcStatusTrue(Long companyId);

  // 2. Get record by employeeId when ctcStatus = true
//   List<CTCBreakdownHeader> findByEmployeeIdAndCtcStatusTrue(Long employeeId);

  // 3. Get record by employeeId when ctcStatus = false
  List<CTCBreakdownHeader> findByEmployeeIdAndCtcStatusFalse(Long employeeId);


      List<CTCBreakdownHeader> findByCompany(CompanyRegistration company);
      CTCBreakdownHeader findCurrentCTCByEmployeeId(Long employeeId);
      Optional<CTCBreakdownHeader> findByEmployeeIdAndCtcStatusTrue(Long employeeId);
          Optional<CTCBreakdownHeader> findByEmployeeAndCompanyAndCtcStatusTrue(Employee employee, CompanyRegistration company);

          List<CTCBreakdownHeader> findByEmployeeId(Long employeeId);

}
