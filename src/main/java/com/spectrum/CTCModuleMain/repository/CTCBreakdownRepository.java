package com.spectrum.CTCModuleMain.repository;

import com.spectrum.CTCModuleMain.model.CTCBreakdown;
import com.spectrum.CTCModuleMain.model.CTCBreakdownRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
 
@Repository
public interface CTCBreakdownRepository extends JpaRepository<CTCBreakdown, Long> {
    Optional<CTCBreakdown> findByEmployee_Id(Long employeeId);

    List<CTCBreakdown> findAllByEffectiveToDateBefore(LocalDate date);

    List<CTCBreakdown> findByEffectiveToDateLessThanEqual(LocalDate date);

            // List<CTCBreakdownRecord> findByEmployee_CompanyRegistration_IdAndStatus(Long companyId, boolean status);



            @Query("SELECT c FROM CTCBreakdownRecord c WHERE c.employee.companyRegistration.id = :companyId AND c.status = true")
    List<CTCBreakdownRecord> findActiveRecordsByCompanyId(@Param("companyId") Long companyId);
}
