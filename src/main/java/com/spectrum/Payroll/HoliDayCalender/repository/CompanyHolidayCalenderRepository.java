package com.spectrum.Payroll.HoliDayCalender.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.Payroll.HoliDayCalender.model.CompanyHolidayCalender;
import com.spectrum.Payroll.HoliDayCalender.model.EmployeeHolidayCalendar;

@Repository
public interface CompanyHolidayCalenderRepository extends JpaRepository<CompanyHolidayCalender, Long> {

    List<CompanyHolidayCalender> findByCompanyId(Long companyId);
List<CompanyHolidayCalender> findByCompanyIdAndHolidayDateBetween(Long companyId, LocalDate startDate, LocalDate endDate);
@Query("SELECT c FROM CompanyHolidayCalender c WHERE c.company.id = :companyId " +
       "AND YEAR(c.holidayDate) = :year AND MONTH(c.holidayDate) = :month")
List<CompanyHolidayCalender> findByCompanyIdAndMonth(
        @Param("companyId") Long companyId,
        @Param("year") int year,
        @Param("month") int month);

        List<CompanyHolidayCalender> findByCompanyIdAndWorkStateCodeAndHolidayDateBetween(
        Long companyId, int workStateCode, LocalDate startDate, LocalDate endDate);



        @Query("SELECT e FROM EmployeeHolidayCalendar e WHERE e.employee.id = :employeeId AND e.holidayDate BETWEEN :fromDate AND :toDate")
    List<EmployeeHolidayCalendar> findLeavesBetweenDates(Long employeeId, LocalDate fromDate, LocalDate toDate);




    @Query("SELECT c FROM CompanyHolidayCalender c WHERE c.company.id = :companyId AND c.workStateCode = :workStateCode AND c.holidayDate BETWEEN :fromDate AND :toDate")
    List<CompanyHolidayCalender> findHolidaysByCompanyAndStateCode(
            @Param("companyId") Long companyId,
            @Param("workStateCode") int workStateCode,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);


            @Query("SELECT c FROM CompanyHolidayCalender c WHERE c.company.id = :companyId AND c.workStateCode = :workStateCode")
            List<CompanyHolidayCalender> findByCompanyIdAndWorkStateCode(
                    @Param("companyId") Long companyId,
                    @Param("workStateCode") int workStateCode);
    
}
