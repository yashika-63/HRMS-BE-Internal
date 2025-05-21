package com.spectrum.Document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.Document.model.CompanyDocument;
import com.spectrum.Document.model.DocumentProfile;
@Repository
public interface DocumentProfileRepository extends JpaRepository<DocumentProfile, Long> {



    List<DocumentProfile> findByEmployeeIdAndStatusTrue(Long employeeId);
    List<DocumentProfile> findAllByEmployeeIdAndStatus(Long employeeId, boolean status);


}
