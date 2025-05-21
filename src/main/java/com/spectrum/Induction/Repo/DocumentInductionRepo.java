package com.spectrum.Induction.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.Induction.Model.DocumentInduction;


public interface DocumentInductionRepo extends  JpaRepository <DocumentInduction,Long> {
    List<DocumentInduction> findByInductionId(Long inductionId);
}
