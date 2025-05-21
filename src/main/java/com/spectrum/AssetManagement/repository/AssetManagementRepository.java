package com.spectrum.AssetManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.AssetManagement.model.AssetManagement;

public interface AssetManagementRepository extends JpaRepository<AssetManagement ,Long> {

    List<AssetManagement> findByEmployee_IdAndEmployeeAction(Long employeeId, boolean employeeAction);
    List<AssetManagement> findByEmployee_IdAndSentForEmployeeActionTrue(Long employeeId);

@Query("SELECT am FROM AssetManagement am LEFT JOIN FETCH am.details WHERE am.employee.id = :employeeId")
List<AssetManagement> findByEmployeeIdWithDetails(@Param("employeeId") Long employeeId);

    
}
