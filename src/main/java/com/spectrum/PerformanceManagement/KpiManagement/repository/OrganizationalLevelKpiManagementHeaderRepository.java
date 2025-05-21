package com.spectrum.PerformanceManagement.KpiManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.PerformanceManagement.GoalSetting.model.OrganizationGoalSettingHeader;
import com.spectrum.PerformanceManagement.KpiManagement.model.OrganizationalLevelKpiManagementHeader;

import jakarta.transaction.Transactional;

@Repository
public interface OrganizationalLevelKpiManagementHeaderRepository extends JpaRepository<OrganizationalLevelKpiManagementHeader, Long> {


       @Query("SELECT k FROM OrganizationalLevelKpiManagementHeader k " +
           "WHERE k.company.id = :companyId " +
           "AND k.kpiType = :kpiType " +
           "AND k.departmentId = :departmentId " +
           "AND k.levelCode = :levelCode " +
           "AND k.regionId = :regionId " +
           "AND k.typeId = :typeId")
    List<OrganizationalLevelKpiManagementHeader> findByFilters(
            @Param("companyId") Long companyId,
            @Param("kpiType") boolean kpiType,
            @Param("departmentId") int departmentId,
            @Param("levelCode") int levelCode,
            @Param("regionId") int regionId,
            @Param("typeId") int typeId);



            @Query("SELECT k FROM OrganizationalLevelKpiManagementHeader k " +
            "WHERE k.company.id = :companyId " +
            "AND k.departmentId = :departmentId " +
            "AND k.levelCode = :levelCode " +
            "AND k.regionId = :regionId " +
            "AND k.typeId = :typeId")
     List<OrganizationalLevelKpiManagementHeader> findByFiltersNew(
             @Param("companyId") Long companyId,
             @Param("departmentId") int departmentId,
             @Param("levelCode") int levelCode,
             @Param("regionId") int regionId,
             @Param("typeId") int typeId);
 




          
                @Query("SELECT k FROM OrganizationalLevelKpiManagementHeader k WHERE k.company.id = :companyId AND YEAR(k.date) = :year")
                List<OrganizationalLevelKpiManagementHeader> findByCompanyIdAndYear(@Param("companyId") Long companyId, @Param("year") int year);
                        


        @Modifying
        @Transactional
        @Query("DELETE FROM OrganizationalLevelKpiManagementDetails d WHERE d.organizationalLevelKpiManagementHeader = :header")
        void deleteByOrganizationalLevelKpiManagementHeader(@Param("header") OrganizationalLevelKpiManagementHeader header);
                

        @Query("SELECT e FROM OrganizationalLevelKpiManagementHeader e " +
        "WHERE (e.regionId = :regionId OR e.regionId = 0) " +
        "AND e.company.id = :companyId " +
        "AND e.kpiType = :kpiType " +
        "AND (e.departmentId = :departmentId OR e.departmentId = 0) " +
        "AND (e.typeId = :typeId OR e.typeId = 0) " +
        "AND e.status = :status")
 List<OrganizationalLevelKpiManagementHeader> findGoalsWithDepartmentOrZeroAndRegionIdOrZeroAndTypeIdOrZero(
        @Param("regionId") int regionId,
        @Param("companyId") Long companyId,  // Corrected parameter
        @Param("kpiType") boolean kpiType,
        @Param("departmentId") int departmentId,
        @Param("typeId") int typeId,
        @Param("status") boolean status
 );
 


 @Query("SELECT e FROM OrganizationalLevelKpiManagementHeader e " +
        "WHERE (e.regionId = :regionId OR e.regionId = 0) " +
        "AND e.company.id = :companyId " +
        "AND (e.departmentId = :departmentId OR e.departmentId = 0) " +
        "AND (e.typeId = :typeId OR e.typeId = 0) " +
        "AND e.status = :status")
List<OrganizationalLevelKpiManagementHeader> findGoalsWithDepartmentOrZeroAndRegionIdOrZeroAndTypeIdOrZero(
        @Param("regionId") int regionId,
        @Param("companyId") Long companyId,
        @Param("departmentId") int departmentId,
        @Param("typeId") int typeId,
        @Param("status") boolean status
);


}
