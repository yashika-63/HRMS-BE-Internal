package com.spectrum.Scheduler;

import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spectrum.Recruitment.JobDescription.service.ScheduleInterviewService;

@Service
public class ScheduleInterviewScheduler {


    @Autowired
    private ScheduleInterviewService scheduleInterviewService;

    @Scheduled(cron = "0 18 18 * * ?") // Runs every day at midnight
    public void triggerAppraisalGeneration() {
    scheduleInterviewService.sendTodayInterviewRemindersToInterviewer();
    }


}
