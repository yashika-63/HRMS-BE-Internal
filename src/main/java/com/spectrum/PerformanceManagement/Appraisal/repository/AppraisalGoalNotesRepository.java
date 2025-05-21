package com.spectrum.PerformanceManagement.Appraisal.repository;

import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalGoalNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppraisalGoalNotesRepository extends JpaRepository<AppraisalGoalNotes, Long> {
    List<AppraisalGoalNotes> findByAppraisalGoalId(Long appraisalGoalId);
}
