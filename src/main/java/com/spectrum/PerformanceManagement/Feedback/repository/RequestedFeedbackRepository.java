package com.spectrum.PerformanceManagement.Feedback.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.PerformanceManagement.Feedback.model.RequestedFeedback;

public interface RequestedFeedbackRepository extends JpaRepository <RequestedFeedback,Long> {


        List<RequestedFeedback> findByEndDate(LocalDate endDate);
        List<RequestedFeedback> findByEndDateAndStatus(LocalDate endDate, boolean status);
        List<RequestedFeedback> findByStatusAndRequestedToEmployeeId(boolean status, int requestedToEmployeeId);
 
        @Query("SELECT r FROM RequestedFeedback r WHERE r.employee.id = :employeeId AND YEAR(r.date) = :year")
        List<RequestedFeedback> findByEmployeeIdAndYear(@Param("employeeId") Long employeeId, @Param("year") int year);
}
