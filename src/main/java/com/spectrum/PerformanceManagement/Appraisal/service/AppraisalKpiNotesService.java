package com.spectrum.PerformanceManagement.Appraisal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalKpi;
import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalKpiNotes;
import com.spectrum.PerformanceManagement.Appraisal.repository.AppraisalKpiNotesRepository;
import com.spectrum.PerformanceManagement.Appraisal.repository.AppraisalKpiRepository;

@Service
public class AppraisalKpiNotesService {


     @Autowired
    private AppraisalKpiNotesRepository kpiNotesRepository;

    @Autowired
    private AppraisalKpiRepository appraisalKpiRepository;

    public List<AppraisalKpiNotes> saveMultipleKpiNotes(List<AppraisalKpiNotes> notesList) {
        for (AppraisalKpiNotes note : notesList) {
            Long kpiId = note.getAppraisalKpi().getId();
            AppraisalKpi appraisalKpi = appraisalKpiRepository.findById(kpiId)
                    .orElseThrow(() -> new RuntimeException("Appraisal KPI not found: " + kpiId));

            note.setAppraisalKpi(appraisalKpi);
        }
        return kpiNotesRepository.saveAll(notesList);
    }
}
