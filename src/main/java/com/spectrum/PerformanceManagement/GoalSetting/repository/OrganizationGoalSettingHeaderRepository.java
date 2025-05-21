package com.spectrum.PerformanceManagement.GoalSetting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.PerformanceManagement.GoalSetting.model.EmployeeGoalSetting;
import com.spectrum.PerformanceManagement.GoalSetting.model.OrganizationGoalSettingHeader;

@Repository
public interface OrganizationGoalSettingHeaderRepository extends JpaRepository<OrganizationGoalSettingHeader, Long> {


        @Query("SELECT h FROM OrganizationGoalSettingHeader h " +
        "WHERE h.company.id = :companyId " +
        "AND h.goalType = :goalType " +
        "AND h.departmentId = :departmentId " +
        "AND h.levelCode = :levelId " +
        "AND h.regionId = :regionId " +
        "AND h.typeId = :typeCode")
    List<OrganizationGoalSettingHeader> findGoalsByExactMatch(
        @Param("companyId") Long companyId,
        @Param("goalType") boolean goalType,
        @Param("departmentId") int departmentId,
        @Param("levelId") int levelId,
        @Param("regionId") int regionId,
        @Param("typeCode") int typeCode
    );



    List<OrganizationGoalSettingHeader> findByCompanyIdAndGoalType(Long companyId, boolean goalType);

    List<OrganizationGoalSettingHeader> findByGoalType(boolean goalType); // To find all active goals


    @Query("SELECT o FROM OrganizationGoalSettingHeader o WHERE o.company.id = :companyId AND YEAR(o.date) = :year AND o.goalType = true")
    List<OrganizationGoalSettingHeader> findByCompanyIdAndYear(@Param("companyId") Long companyId, @Param("year") int year);


    @Query("SELECT h FROM OrganizationGoalSettingHeader h " +
    "WHERE h.company.id = :companyId " +
    "AND h.goalType = :goalType " +
    "AND h.status = :status " +
    "AND (:departmentId = 0 OR h.departmentId = :departmentId) " +
    "AND (:levelId = 0 OR h.levelCode = :levelId) " +
    "AND (:regionId = 0 OR h.regionId = :regionId) " +
    "AND (:typeCode = 0 OR h.typeId = :typeCode)")
List<OrganizationGoalSettingHeader> findGoalsByFilters(
    @Param("companyId") Long companyId,
    @Param("goalType") boolean goalType,
    @Param("status") boolean status,
    @Param("departmentId") Integer departmentId,
    @Param("levelId") Integer levelId,
    @Param("regionId") Integer regionId,
    @Param("typeCode") Integer typeCode
);





@Query("SELECT h FROM OrganizationGoalSettingHeader h " +
"WHERE h.company.id = :companyId " +
"AND h.status = :status " +
"AND (:departmentId = 0 OR h.departmentId = :departmentId) " +
"AND (:levelId = 0 OR h.levelCode = :levelId) " +
"AND (:regionId = 0 OR h.regionId = :regionId) " +
"AND (:typeCode = 0 OR h.typeId = :typeCode)")
List<OrganizationGoalSettingHeader> findGoalsByFiltersNew(
@Param("companyId") Long companyId,
@Param("status") boolean status,
@Param("departmentId") Integer departmentId,
@Param("levelId") Integer levelId,
@Param("regionId") Integer regionId,
@Param("typeCode") Integer typeCode
);

}