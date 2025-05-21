package com.spectrum.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.model.TimeSheet;

public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {


    // // Custom query to find timesheets by date
    // @Query("SELECT t FROM TimeSheet t WHERE FUNCTION('DATE', t.date) = FUNCTION('DATE', :date)")
    // List<TimeSheet> findByDate(@Param("date") Date date);

    // @Query("SELECT t FROM TimeSheet t WHERE t.employee = :employee AND t.date = :date")
    // List<TimeSheet> findByEmployeeAndDate(@Param("employee") Employee employee, @Param("date") Date date);




    // @Query("SELECT t FROM TimeSheet t WHERE t.employee.id = :employeeId AND t.date BETWEEN :startDate AND :endDate")
    // List<TimeSheet> findByEmployeeIdAndDateRange(@Param("employeeId") Long employeeId,
    //         @Param("startDate") Date startDate,
    //         @Param("endDate") Date endDate);
}
