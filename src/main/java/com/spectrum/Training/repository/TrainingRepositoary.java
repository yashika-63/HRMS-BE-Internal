package com.spectrum.Training.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.Training.model.TrainingHRMS;

public interface TrainingRepositoary extends JpaRepository<TrainingHRMS,Long>{
     @Query("SELECT t FROM TrainingHRMS t WHERE YEAR(t.date) = :year AND t.company.id = :companyId")
    List<TrainingHRMS> findByYearAndCompanyId(@Param("year") int year, @Param("companyId") Long companyId);


    @Query("SELECT t FROM TrainingHRMS t WHERE YEAR(t.date) = :year AND t.company.id = :companyId AND t.status = :status")
List<TrainingHRMS> findByStatusYearAndCompanyId(
    @Param("status") boolean status,
    @Param("year") int year,
    @Param("companyId") Long companyId);

    @Query("SELECT t FROM TrainingHRMS t WHERE YEAR(t.date) = :year AND t.regionId = :regionId AND t.company.id = :companyId")
List<TrainingHRMS> findByYearRegionIdAndCompanyId(
    @Param("year") int year,
    @Param("regionId") Long regionId,
    @Param("companyId") Long companyId);

Page<TrainingHRMS> findByStatusTrueAndCompanyId(Long companyId, Pageable pageable);

// Page<TrainingHRMS> findByDateYearAndRegionIdAndCompanyIdAndDeptId(int year, Long regionId, Long companyId, Long deptId, Pageable pageable);  


@Query("SELECT t FROM TrainingHRMS t " +
"WHERE YEAR(t.date) = :year " +
"AND t.regionId = :regionId " +
"AND t.company.id = :companyId " +
"AND t.departmentId = :departmentId")
Page<TrainingHRMS> findByDateYearAndRegionIdAndCompanyIdAndDepartmentId(
 int year, Long regionId, Long companyId, Long departmentId, Pageable pageable);



//  @Query("SELECT e FROM TrainingHRMS e " +
//  "WHERE (:description IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
//  "AND (:heading IS NULL OR LOWER(e.heading) LIKE LOWER(CONCAT('%', :heading, '%'))) " +
//  "AND (:year IS NULL OR FUNCTION('YEAR', e.date) = :year) " +
//  "AND (:companyId IS NULL OR e.company.id = :companyId)")
// Page<TrainingHRMS> search(
//   @Param("description") String description,
//   @Param("heading") String heading,
//   @Param("year") Integer year,
//   @Param("companyId") Long companyId,
//   Pageable pageable
// );

@Query("SELECT e FROM TrainingHRMS e " +
       "WHERE (:description IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
       "AND (:heading IS NULL OR LOWER(e.heading) LIKE LOWER(CONCAT('%', :heading, '%'))) " +
       "AND (:year IS NULL OR FUNCTION('YEAR', e.date) = :year) " +
       "AND (:companyId IS NULL OR e.company.id = :companyId) " +
       "AND (:departmentId IS NULL OR e.departmentId = :departmentId) " +
       "AND (:regionId IS NULL OR e.regionId = :regionId)")
Page<TrainingHRMS> search(
    @Param("description") String description,
    @Param("heading") String heading,
    @Param("year") Integer year,
    @Param("companyId") Long companyId,
    @Param("departmentId") Long departmentId,
    @Param("regionId") Long regionId,
    Pageable pageable
);


/////////////////////////////////////////////////////////// from here i am adding reports methods
@Query("SELECT " +
       "t.id, t.heading, t.type, t.date, t.department, t.departmentId, t.description, t.time, t.status, t.region, t.regionId, t.createdByEmpId, " +
       "c.id, c.companyAssignId, c.companyName, c.companyType, c.companyAddress, c.city, c.state, c.country, c.postalCode, " +
       "c.landmark, c.phone, c.email, c.website " +
       "FROM TrainingHRMS t LEFT JOIN t.company c")
List<Object[]> fetchTrainingReportWithCompanyFields();


@Query("SELECT " +
"t.id, t.heading, t.type, t.date, t.department, t.departmentId, t.description, t.time, t.status, t.region, t.regionId, t.createdByEmpId, " +
"c.id, c.companyAssignId, c.companyName, c.companyType, c.companyAddress, c.city, c.state, c.country, c.postalCode, " +
"c.landmark, c.phone, c.email, c.website " +
"FROM TrainingHRMS t LEFT JOIN t.company c")
Page<Object[]> fetchTrainingReportWithCompanyFields(Pageable pageable);




}


