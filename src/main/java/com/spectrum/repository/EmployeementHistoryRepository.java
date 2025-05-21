package com.spectrum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.model.EmployeementHistory;

@Repository
public interface EmployeementHistoryRepository extends JpaRepository<EmployeementHistory, Long> {

    List<EmployeementHistory> findByEmployeeId(Long employeeId);


}
