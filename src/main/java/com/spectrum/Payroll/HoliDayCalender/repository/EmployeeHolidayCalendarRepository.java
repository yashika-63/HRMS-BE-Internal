package com.spectrum.Payroll.HoliDayCalender.repository;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.Payroll.HoliDayCalender.model.EmployeeHolidayCalendar;
import com.spectrum.model.Employee;

import jakarta.transaction.Transactional;



@Repository
public interface EmployeeHolidayCalendarRepository extends JpaRepository<EmployeeHolidayCalendar, Long> {
    List<EmployeeHolidayCalendar> findByEmployeeId(Long employeeId);

    EmployeeHolidayCalendar findTopByEmployeeOrderByHolidayDateDesc(Employee employee);

List<EmployeeHolidayCalendar> findByEmployeeIdAndHolidayDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
@Query("SELECT e FROM EmployeeHolidayCalendar e WHERE e.employee.id = :employeeId " +
       "AND YEAR(e.holidayDate) = :year AND MONTH(e.holidayDate) = :month")
       
List<EmployeeHolidayCalendar> findByEmployeeIdAndMonth(
        @Param("employeeId") Long employeeId,
        @Param("year") int year,
        @Param("month") int month);

        @Query("SELECT e FROM EmployeeHolidayCalendar e WHERE e.employee.id = :employeeId AND e.holidayDate BETWEEN :fromDate AND :toDate")
        List<EmployeeHolidayCalendar> findLeavesBetweenDates(Long employeeId, LocalDate fromDate, LocalDate toDate);
        
@Modifying
    @Transactional
    @Query("DELETE FROM EmployeeHolidayCalendar e WHERE e.employee.id = :employeeId")
    void deleteByEmployeeId(@Param("employeeId") Long employeeId);
}
