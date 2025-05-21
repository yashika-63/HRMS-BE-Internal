package com.spectrum.ExitProcess.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.ExitProcess.Model.OffBoarding;
import com.spectrum.ExitProcess.Model.OffboardingDocument;

@Repository
public interface OffboardingDocumentRepo extends JpaRepository<OffboardingDocument,Long> {
    List<OffboardingDocument> findByOffBoardingId(Long id);
}
