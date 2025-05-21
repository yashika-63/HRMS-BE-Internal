package com.spectrum.AssetManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.AssetManagement.model.AssetManagementDetails;

@Repository

public interface AssetManagementDetailsRepository extends JpaRepository<AssetManagementDetails ,Long> {

    Optional<AssetManagementDetails> findById(Long id);

@Query("SELECT d FROM AssetManagementDetails d WHERE d.assetManagement.employee.id = :employeeId")
List<AssetManagementDetails> findByEmployeeId(@Param("employeeId") Long employeeId);



}
