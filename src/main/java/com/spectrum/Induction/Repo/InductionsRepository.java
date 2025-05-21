package com.spectrum.Induction.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.Induction.Model.Inductions;

import jakarta.transaction.Transactional;

public interface InductionsRepository extends JpaRepository<Inductions, Long>  {
    
     @Query("SELECT i FROM Inductions i WHERE YEAR(i.date) = :year AND i.company.id = :companyId")
    List<Inductions> findByYearAndCompany(@Param("year") int year, @Param("companyId") Long companyId);

    List<Inductions> findByCompanyIdAndStatus(Long companyId, boolean status);

//     @Query("SELECT i FROM Inductions i WHERE YEAR(i.date) = :year AND i.region = :region")
//     List<Inductions> findByYearAndRegion(@Param("year") int year, @Param("region") String region);



//      @Query("SELECT i FROM Inductions i WHERE i.status = true " +
//            "AND (" +
//            "(:year IS NULL OR YEAR(i.date) = :year) " +
//            "OR (:description IS NULL OR LOWER(i.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
//            "OR (:heading IS NULL OR LOWER(i.heading) LIKE LOWER(CONCAT('%', :heading, '%'))) " +
//            ")")
//     Page<Inductions> searchInductions(
//             @Param("year") Integer year, 
//             @Param("description") String description, 
//             @Param("heading") String heading,
//             Pageable pageable);



// @Query("SELECT i FROM Inductions i WHERE i.status = true " +
// "AND i.company.id = :companyId " +
// "AND (" +
// "(:year IS NULL OR YEAR(i.date) = :year) " +
// "OR (:description IS NULL OR LOWER(i.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
// "OR (:heading IS NULL OR LOWER(i.heading) LIKE LOWER(CONCAT('%', :heading, '%'))) " +
// ")")
// Page<Inductions> searchInductions(
//  @Param("year") Integer year, 
//  @Param("description") String description, 
//  @Param("heading") String heading,
//  @Param("companyId") Long companyId,
//  Pageable pageable);



// @Query("SELECT i FROM Inductions i WHERE i.status = true " +
//        "AND (:companyId IS NULL OR i.company.id = :companyId OR i.company.id IS NULL) " +
//        "AND (:year IS NULL OR FUNCTION('YEAR', i.date) = :year) " +
//        "AND (:description IS NULL OR LOWER(i.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
//        "AND (:heading IS NULL OR LOWER(i.heading) LIKE LOWER(CONCAT('%', :heading, '%')))")
// Page<Inductions> searchInductions(
//         @Param("year") Integer year, 
//         @Param("description") String description, 
//         @Param("heading") String heading,
//         @Param("companyId") Long companyId,
//         Pageable pageable);



//             @Query("SELECT i FROM Inductions i " +
//            "WHERE i.company.id = :companyId " +
//            "AND YEAR(i.date) = :year " +
//            "AND i.region = :region")
//     List<Inductions> findByYearRegionAndCompany(
//             @Param("year") int year, 
//             @Param("region") String region, 
//             @Param("companyId") Long companyId);


@Query("SELECT i FROM Inductions i " +
       "WHERE i.company.id = :companyId " +
       "AND YEAR(i.date) = :year " +
       "AND i.regionId = :regionId")
List<Inductions> findByYearRegionAndCompany(
        @Param("year") int year, 
        @Param("regionId") Long regionId, 
        @Param("companyId") Long companyId);

        @Query("SELECT i FROM Inductions i WHERE " +
        "(:companyId IS NULL OR i.company.id = :companyId OR i.company.id IS NULL) " +
        "AND (:year IS NULL OR FUNCTION('YEAR', i.date) = :year) " +
        "AND (:description IS NULL OR LOWER(i.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
        "AND (:heading IS NULL OR LOWER(i.heading) LIKE LOWER(CONCAT('%', :heading, '%'))) " +
        "AND (:regionId IS NULL OR i.regionId = :regionId) " +
        "AND (:departmentId IS NULL OR i.departmentId = :departmentId)")
 Page<Inductions> searchInductions(
         @Param("year") Integer year, 
         @Param("description") String description, 
         @Param("heading") String heading,
         @Param("companyId") Long companyId,
         @Param("regionId") Long regionId,
         @Param("departmentId") Long departmentId,
         Pageable pageable);
     

}
