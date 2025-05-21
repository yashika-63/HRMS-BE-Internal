package com.spectrum.PerformanceManagement.Appraisal.controller;

import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalGoalNotes;
import com.spectrum.PerformanceManagement.Appraisal.service.AppraisalGoalNotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appraisal-goal-notes")
public class AppraisalGoalNotesController {

    @Autowired
    private AppraisalGoalNotesService notesService;

   
    @PostMapping("/saveMultipleGoals")
    public ResponseEntity<List<AppraisalGoalNotes>> saveMultipleNotesForMultipleGoals(
            @RequestBody List<AppraisalGoalNotes> notesList) {

        List<AppraisalGoalNotes> savedNotes = notesService.saveMultipleNotesForMultipleGoals(notesList);
        return ResponseEntity.ok(savedNotes);
    }
}
