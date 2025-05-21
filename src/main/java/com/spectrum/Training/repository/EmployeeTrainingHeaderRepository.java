package com.spectrum.Training.repository;

import com.spectrum.Training.model.EmployeeTrainingHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeTrainingHeaderRepository extends JpaRepository<EmployeeTrainingHeader, Long> {


    List<EmployeeTrainingHeader> findByReportingmanagerIdAndManagerApprovalStatusFalse(Long reportingManagerId);


    @Query("SELECT e FROM EmployeeTrainingHeader e WHERE e.employee.id = :employeeId AND YEAR(e.date) = :year")
    List<EmployeeTrainingHeader> findByEmployeeIdAndYear(@Param("employeeId") Long employeeId, @Param("year") int year);


    // List<EmployeeTrainingHeader> findByReportingManagerIdAndManagerApprovalStatusFalse(Long reportingManagerId);

    List<EmployeeTrainingHeader> findByIdInAndReportingmanagerId(List<Long> ids, int reportingManagerId);
}
