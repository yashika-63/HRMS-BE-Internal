package com.spectrum.Payroll.HoliDayCalender.repository;

import com.spectrum.Payroll.HoliDayCalender.model.CompanyDayOffConfig;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDayOffConfigRepository extends JpaRepository<CompanyDayOffConfig, Long> {
    @Query("SELECT c FROM CompanyDayOffConfig c WHERE c.company.id = :companyId AND MONTH(c.holidayDate) = :month AND YEAR(c.holidayDate) = :year")
    List<CompanyDayOffConfig> findByCompanyIdAndMonth(@Param("companyId") Long companyId,
            @Param("year") int year,
            @Param("month") int month);

    @Query("SELECT c FROM CompanyDayOffConfig c WHERE c.company.id = :companyId")
    List<CompanyDayOffConfig> findByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT d FROM CompanyDayOffConfig d WHERE d.company.id = :companyId AND d.workCategoryCode = :workCategoryCode AND d.holidayDate BETWEEN :fromDate AND :toDate")
    List<CompanyDayOffConfig> findDayOffsByCompanyAndCategoryCode(
            @Param("companyId") Long companyId,
            @Param("workCategoryCode") int workCategoryCode,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);




            @Query("SELECT DISTINCT c.holidayDate FROM CompanyDayOffConfig c WHERE c.company.id = :companyId")
List<LocalDate> findDistinctHolidayDatesByCompanyId(@Param("companyId") Long companyId);



@Transactional
    @Modifying
    @Query("DELETE FROM CompanyDayOffConfig c WHERE c.workCategoryCode = :workCategoryCode AND c.company.id = :companyId")
    void deleteByCategoryCodeAndCompanyId(int workCategoryCode, Long companyId);


    List<CompanyDayOffConfig> findByCompanyIdAndWorkCategoryCode(Long companyId, int workCategoryCode);

}
