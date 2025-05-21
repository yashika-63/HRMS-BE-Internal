package com.spectrum.PerformanceManagement.Appraisal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalGoal;
import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalKpi;
import com.spectrum.PerformanceManagement.Appraisal.service.AppraisalGoalService;
import com.spectrum.PerformanceManagement.Appraisal.service.AppraisalKpiService;



@RestController
@RequestMapping("/api/appraisalGoal")
public class AppraisalGoalController {


    @Autowired
    private  AppraisalGoalService appraisalGoalService;
   

    @Autowired
    private AppraisalKpiService appraisalKpiService;


  
    @PutMapping("/updateMultiple")
    public ResponseEntity<String> updateMultipleAppraisalGoals(@RequestBody List<AppraisalGoal> updatedAppraisalGoals) {
        String response = appraisalGoalService.updateMultipleAppraisalGoals(updatedAppraisalGoals);
        return ResponseEntity.ok(response);
    }



        @PutMapping("/updateMultiplekpis")
    public ResponseEntity<String> updateMultipleAppraisalKpis(@RequestBody List<AppraisalKpi> updatedAppraisalKpis) {
        String response = appraisalKpiService.updateMultipleAppraisalKpis(updatedAppraisalKpis);
        return ResponseEntity.ok(response);
    }

}
