package com.spectrum.PerformanceManagement.Appraisal.service;

import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalGoal;
import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalGoalNotes;
import com.spectrum.PerformanceManagement.Appraisal.repository.AppraisalGoalNotesRepository;
import com.spectrum.PerformanceManagement.Appraisal.repository.AppraisalGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppraisalGoalNotesService {

    @Autowired
    private AppraisalGoalNotesRepository notesRepository;

    @Autowired
    private AppraisalGoalRepository appraisalGoalRepository;

    public List<AppraisalGoalNotes> saveMultipleNotesForMultipleGoals(List<AppraisalGoalNotes> notesList) {
        for (AppraisalGoalNotes note : notesList) {
            Long goalId = note.getAppraisalGoal().getId();
            AppraisalGoal appraisalGoal = appraisalGoalRepository.findById(goalId)
                    .orElseThrow(() -> new RuntimeException("Appraisal Goal not found: " + goalId));

            note.setAppraisalGoal(appraisalGoal);
        }
        return notesRepository.saveAll(notesList);
    }
}
