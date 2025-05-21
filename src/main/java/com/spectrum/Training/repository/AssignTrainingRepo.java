package com.spectrum.Training.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.Training.model.AssignTraining;

public interface AssignTrainingRepo extends JpaRepository<AssignTraining,Long>  {

Page<AssignTraining> findByEmployeeId(Long employeeId, Pageable pageable);


@Query("SELECT a FROM AssignTraining a WHERE a.companyId = :companyId AND FUNCTION('YEAR', a.assignDate) = :year")
Page<AssignTraining> findByCompanyIdAndYear(@Param("companyId") Long companyId, @Param("year") int year, Pageable pageable);

@Query("SELECT a FROM AssignTraining a WHERE a.companyId = :companyId AND a.assignDate BETWEEN :startDate AND :endDate")
    Page<AssignTraining> findByCompanyIdAndAssignDateBetween(
        @Param("companyId") Long companyId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable
    );



    @Query("FROM AssignTraining at " +
    "WHERE at.employee.id = :employeeId " +
    "AND at.completionStatus = :completionStatus " +
    "AND at.expiryStatus = :expiryStatus")
Page<AssignTraining> findByEmployeeAndStatus(
     @Param("employeeId") Long employeeId,
     @Param("completionStatus") Boolean completionStatus,
     @Param("expiryStatus") Boolean expiryStatus,
     Pageable pageable);


     List<AssignTraining> findByCompletionStatusFalseAndExpiryStatusFalse();

     @Query("SELECT a FROM AssignTraining a WHERE a.companyId = :companyId")
Page<AssignTraining> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);



@Query("SELECT a FROM AssignTraining a " +
           "JOIN FETCH a.employee e " +
           "JOIN FETCH a.trainingHRMS t " +
           "WHERE e.id = :employeeId")
    List<AssignTraining> findAllByEmployeeIdWithDetails(@Param("employeeId") Long employeeId);

    
    ////////////////////////////////////////////////////////////////////// reports 
    @Query("SELECT " +
    "t.id, t.heading, t.type, t.date, t.department, t.departmentId, t.description, t.time, t.status, " +
    "a.id, a.assignDate, a.expiryStatus, a.completionStatus, a.expiryDays, a.assignedBy, " +
    "e.id, e.employeeId, e.firstName, e.lastName, e.email " +
    "FROM AssignTraining a " +
    "LEFT JOIN a.trainingHRMS t " +
    "LEFT JOIN a.employee e")
Page<Object[]> fetchTrainingReport(Pageable pageable);
    
    
}
