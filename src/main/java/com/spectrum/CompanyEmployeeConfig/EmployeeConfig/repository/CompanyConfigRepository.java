package com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.CompanyConfig;

public interface CompanyConfigRepository extends JpaRepository<CompanyConfig, Long> {


        List<CompanyConfig> findByAppraisalStartDate(LocalDate appraisalStartDate);

        

}
