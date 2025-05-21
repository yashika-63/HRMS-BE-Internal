package com.spectrum.timesheet.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.timesheet.modal.TimesheetMain;

@Repository
public interface TimesheetMainRepository extends JpaRepository<TimesheetMain, Long> {

    // List<TimesheetMain> findByFromDateBetween(Date fromDate, Date toDate);
    // @Query("SELECT t FROM TimesheetMain t WHERE t.employee.id = :employeeId AND t.companyRegistration.id = :companyId AND t.fromDate >= :fromDate AND t.toDate <= :toDate")
    // List<TimesheetMain> findTimesheetsBetweenDates(
    //         @Param("employeeId") long employeeId,
    //         @Param("companyId") long companyId,
    //         @Param("fromDate") Date fromDate,
    //         @Param("toDate") Date toDate);

    List<TimesheetMain> findByFromDateBetween(Date fromDate, Date toDate);

    @Query("SELECT t FROM TimesheetMain t WHERE t.employee.id = :employeeId AND t.companyRegistration.id = :companyId AND t.fromDate >= :fromDate AND t.toDate <= :toDate")
    List<TimesheetMain> findTimesheetsBetweenDates(
            @Param("employeeId") long employeeId,
            @Param("companyId") long companyId,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate);

    @Query("SELECT t FROM TimesheetMain t WHERE :date BETWEEN t.fromDate AND t.toDate")
    List<TimesheetMain> findByDateBetween(@Param("date") Date date);

    @Query("SELECT t FROM TimesheetMain t WHERE :date BETWEEN t.fromDate AND t.toDate AND t.employee.id = :employeeId")
    List<TimesheetMain> findByDateAndEmployeeId(@Param("date") Date date, @Param("employeeId") long employeeId);

    // Custom query to find timesheets that conflict with the provided date range
    @Query("SELECT t FROM TimesheetMain t WHERE t.employee.id = :employeeId AND t.companyRegistration.id = :companyId "
            + "AND (t.fromDate <= :toDate AND t.toDate >= :fromDate)")
    List<TimesheetMain> findConflictingTimesheets(@Param("employeeId") long employeeId,
            @Param("companyId") long companyId,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate);





            List<TimesheetMain> findByReportingManagerIdAndSendForApprovalTrue(int reportingManagerId);


            List<TimesheetMain> findByEmployeeIdAndManagerRejectedTrue(Long employeeId);

}

