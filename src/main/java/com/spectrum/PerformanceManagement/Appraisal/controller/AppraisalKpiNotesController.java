package com.spectrum.PerformanceManagement.Appraisal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalKpiNotes;
import com.spectrum.PerformanceManagement.Appraisal.service.AppraisalKpiNotesService;

@RestController
@RequestMapping("/api/appraisal-kpi-notes")
public class AppraisalKpiNotesController {

    @Autowired
    private AppraisalKpiNotesService kpiNotesService;

    @PostMapping("/saveMultipleKpiNotes")
    public ResponseEntity<List<AppraisalKpiNotes>> saveMultipleKpiNotes(
            @RequestBody List<AppraisalKpiNotes> notesList) {

        List<AppraisalKpiNotes> savedNotes = kpiNotesService.saveMultipleKpiNotes(notesList);
        return ResponseEntity.ok(savedNotes);
    }
}
