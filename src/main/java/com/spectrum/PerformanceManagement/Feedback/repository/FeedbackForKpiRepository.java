package com.spectrum.PerformanceManagement.Feedback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.PerformanceManagement.Feedback.model.FeedbackForKpi;
import com.spectrum.PerformanceManagement.KpiManagement.model.EmployeeKpiSetting;

@Repository
public interface FeedbackForKpiRepository extends JpaRepository<FeedbackForKpi, Long> {


  

    // @Query("SELECT f FROM FeedbackForKpi f WHERE f.employee.id = :employeeId AND YEAR(f.date) = :year")
    // List<FeedbackForKpi> findByEmployeeIdAndYear(@Param("employeeId") Long employeeId, @Param("year") int year);

    

   

    @Query("SELECT k FROM EmployeeKpiSetting k WHERE k.employee.id = :employeeId AND YEAR(k.date) = :year")
    List<EmployeeKpiSetting> findByEmployeeIdAndYear(@Param("employeeId") Long employeeId, @Param("year") int year);



//     @Query("SELECT f.employeeKpiSetting.id, AVG(f.ratingForKpi) AS avgRating, COUNT(f.id) AS totalRatings " +
//     "FROM FeedbackForKpi f WHERE f.employeeKpiSetting.id IN :kpiIds GROUP BY f.employeeKpiSetting.id")
// List<Object[]> findAverageRatingForKpi(@Param("kpiIds") List<Long> kpiIds);



@Query("SELECT f.employeeKpiSetting.id, f.ratingForKpi, f.note, f.date, f.employees.firstName, f.employees.lastName " +
           "FROM FeedbackForKpi f WHERE f.employeeKpiSetting.id IN :kpiIds")
    List<Object[]> findFeedbackDetailsForKpi(@Param("kpiIds") List<Long> kpiIds);

    @Query("SELECT f.employeeKpiSetting.id, AVG(f.ratingForKpi) AS avgRating, COUNT(f.id) AS totalRatings " +
           "FROM FeedbackForKpi f WHERE f.employeeKpiSetting.id IN :kpiIds GROUP BY f.employeeKpiSetting.id")
    List<Object[]> findAverageRatingForKpi(@Param("kpiIds") List<Long> kpiIds);
}