package com.spectrum.ExitProcess.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.ExitProcess.Model.OffBoarding;

public interface OffBoardingRepo extends JpaRepository<OffBoarding,Long>{
    boolean existsByEmployeeIdAndAppliedTrue(Long employeeId);

    List<OffBoarding> findByCompletionStatusAndCompanyId(boolean completionStatus, Long compId);

    OffBoarding findBynameAndEmployee_id(String name , Long employeeId);

    // @Query("SELECT o FROM OffBoarding o JOIN o.employee e WHERE e.employeeId = :employeeId")
    // List<OffBoarding> findByEmployeeEmployeeId(@Param("employeeId") String employeeId);

    @Query("SELECT o FROM OffBoarding o JOIN o.employee e WHERE e.employeeId LIKE %:employeeId%")
List<OffBoarding> findByEmployeeEmployeeId(@Param("employeeId") String employeeId);
    
    // Search by employee name (case insensitive)
    @Query("SELECT o FROM OffBoarding o JOIN o.employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<OffBoarding> findByEmployeeNameContainingIgnoreCase(@Param("name") String name);
    
    // Search by both employee ID and name
    @Query("SELECT o FROM OffBoarding o JOIN o.employee e WHERE " +
           "e.employeeId = :employeeId AND " +
           "(LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<OffBoarding> findByEmployeeEmployeeIdAndNameContainingIgnoreCase(
            @Param("employeeId") String employeeId, 
            @Param("name") String name);   
            
            List<OffBoarding> getByStatusAndCompanyId(Boolean status, Long companyId);

            // List<OffBoarding> findByStatusAndCompanyIdAndEmployees_Id(Boolean status, Long companyId, Long managerId);

            
    // @Query("SELECT o FROM OffBoarding o WHERE "
    // + "(:deptId IS NULL OR o.deptId = :deptId) AND "
    // + "(:completionStatus IS NULL OR o.completionStatus = :completionStatus) AND "
    // + "(:year IS NULL OR FUNCTION('YEAR', o.dateField) = :year)")
    //  Page<OffBoarding> searchOffBoarding(
    //      @Param("deptId") Long deptId,
        
    //      @Param("completionStatus") Boolean completionStatus,
    //      @Param("year") Integer year,
    // Pageable pageable
    //  );

// Page<OffBoarding> searchOffBoarding(
//     @Param("deptId") Long deptId,
//     @Param("completionStatus") Boolean completionStatus,
//     @Param("year") Integer year,
//     Pageable pageable
// );


     Page<OffBoarding> findByStatusAndCompanyId(Boolean status, Long companyId, Pageable pageable);

     Page<OffBoarding> findByStatusAndCompanyIdAndEmployees_Id(Boolean status, Long companyId, Long managerId, Pageable pageable);

//      @Query("SELECT o FROM OffBoarding o WHERE "
//      + "(:deptId IS NULL OR o.deptId = :deptId) AND "
//      + "(:completionStatus IS NULL OR o.completionStatus = :completionStatus) AND "
//      + "(:year IS NULL OR YEAR(o.date) = :year)")
//  Page<OffBoarding> searchOffBoardings(
//      @Param("deptId") Long deptId,
//      @Param("completionStatus") Boolean completionStatus,
//      @Param("year") Integer year,
//      Pageable pageable
//  );
 Page<OffBoarding> findByStatusOrCompanyId(Boolean status, Long companyId, Pageable pageable);

 @Query("SELECT o FROM OffBoarding o WHERE "
 + "(:deptId IS NULL OR o.deptId = :deptId) AND "
 + "(:completionStatus IS NULL OR o.completionStatus = :completionStatus) AND "
 + "(:year IS NULL OR YEAR(o.date) = :year)")
Page<OffBoarding> searchOffBoardings(
 @Param("deptId") Long deptId,
 @Param("completionStatus") Boolean completionStatus,
 @Param("year") Integer year,
 Pageable pageable);
}
