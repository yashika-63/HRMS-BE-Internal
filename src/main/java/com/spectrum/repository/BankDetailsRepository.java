package com.spectrum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.model.BankDetails;
@Repository
public interface BankDetailsRepository extends JpaRepository<BankDetails, Long> {
    List<BankDetails> findByEmployeeId(Long employeeId);

}
