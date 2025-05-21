package com.spectrum.CTCModuleMain.repository;

import com.spectrum.CTCModuleMain.model.CTCBreakdownRecord;
import com.spectrum.model.Employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CTCBreakdownRecordRepository extends JpaRepository<CTCBreakdownRecord, Long> {
   // Custom query to fetch CTC Breakdown Records by companyId
    @Query("SELECT c FROM CTCBreakdownRecord c WHERE c.employee.companyRegistration.id = :companyId")
    List<CTCBreakdownRecord> findByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT c FROM CTCBreakdownRecord c WHERE c.employee.companyRegistration.id = :companyId AND c.status = true")
    List<CTCBreakdownRecord> findActiveRecordsByCompanyId(@Param("companyId") Long companyId);


        List<CTCBreakdownRecord> findByEmployeeAndStatusTrue(Employee employee);
        List<CTCBreakdownRecord> findByEmployee_Id(Long employeeId);

}
