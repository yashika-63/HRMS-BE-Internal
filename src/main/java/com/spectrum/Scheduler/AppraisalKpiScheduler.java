package com.spectrum.Scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.spectrum.PerformanceManagement.Appraisal.service.AppraisalKpiService;
import com.spectrum.PerformanceManagement.Appraisal.service.AppraisalService;

@Component
public class AppraisalKpiScheduler {

    @Autowired
    private AppraisalKpiService appraisalKpiService;


    @Autowired
    private AppraisalService appraisalService;

    // @Scheduled(cron = "0 33 17 * * ?") // Runs every day at midnight
    // public void triggerAppraisalGeneration() {
    //     appraisalKpiService.generateAppraisalKpis();
    // }

    @Scheduled(cron = "0 5 18 * * ?") // Runs every day at midnight
    public void triggerAppraisalGeneration() {
        appraisalService.generateAppraisalData();
    }
    
}
