package com.spectrum.PerformanceManagement.ModeratedRating.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.PerformanceManagement.ModeratedRating.model.ModerateRating;

@Repository
public interface ModerateRatingRepository  extends JpaRepository< ModerateRating,Long>{

    @Query("SELECT m FROM ModerateRating m WHERE m.employees.id = :reportingManagerId AND YEAR(m.date) = :year")
    List<ModerateRating> findByReportingManagerIdAndYear(@Param("reportingManagerId") Long reportingManagerId, @Param("year") int year);


    @Query("SELECT m FROM ModerateRating m WHERE m.employee.id = :employeeId AND FUNCTION('YEAR', m.date) = :year")
    ModerateRating findByEmployeeIdAndYear(@Param("employeeId") Long employeeId, @Param("year") int year);
}
