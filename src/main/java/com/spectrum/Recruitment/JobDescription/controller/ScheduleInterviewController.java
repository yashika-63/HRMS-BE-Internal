package com.spectrum.Recruitment.JobDescription.controller;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.Recruitment.JobDescription.model.ScheduleInterview;
import com.spectrum.Recruitment.JobDescription.model.ScheduleInterviewRequest;
import com.spectrum.Recruitment.JobDescription.repository.ScheduleInterviewRepository;
import com.spectrum.Recruitment.JobDescription.service.ScheduleInterviewService;

@RestController
@RequestMapping("/api/interview")
public class ScheduleInterviewController {

    @Autowired
    private ScheduleInterviewService scheduleInterviewService;

    @Autowired
    private ScheduleInterviewRepository scheduleInterviewRepository;

    @PostMapping("/save")
    public ResponseEntity<ScheduleInterview> saveInterview(
            @RequestBody ScheduleInterview interview,
            @RequestParam Long companyId,
            @RequestParam Long jobDescriptionId,
            @RequestParam Long candidateId,
            @RequestParam Long interviewerId) {

        ScheduleInterview savedInterview = scheduleInterviewService.saveInterview(interview, companyId, jobDescriptionId, candidateId, interviewerId);
        return ResponseEntity.ok(savedInterview);
    }


    @GetMapping("/byCandidate/{candidateId}")
    public List<ScheduleInterview> getByCandidateIdAndStatus(@PathVariable Long candidateId) {
        return scheduleInterviewService.getInterviewsByCandidateIdAndStatus(candidateId);
    }


    @GetMapping("/checkConflict")
    public List<ScheduleInterview> checkConflict(
            @RequestParam Long interviewerId,
            @RequestParam String date,    // Format: yyyy-MM-dd
            @RequestParam String time     // Format: HH:mm:ss
    ) {
        LocalDate localDate = LocalDate.parse(date);
        Time localTime = Time.valueOf(time);
        return scheduleInterviewService.checkScheduleConflict(interviewerId, localDate, localTime);
    }


    @GetMapping("/upcoming/company/{companyId}")
public ResponseEntity<List<ScheduleInterview>> getUpcomingInterviewsByCompany(@PathVariable Long companyId) {
    List<ScheduleInterview> interviews = scheduleInterviewService.getUpcomingInterviewsByCompany(companyId);
    return new ResponseEntity<>(interviews, HttpStatus.OK);
}



// @GetMapping("/active/interviewer/{interviewerId}")
// public ResponseEntity<List<ScheduleInterview>> getActiveInterviewsByInterviewer(@PathVariable Long interviewerId) {
//     List<ScheduleInterview> interviews = scheduleInterviewService.getActiveInterviewsByInterviewer(interviewerId);
//     return new ResponseEntity<>(interviews, HttpStatus.OK);
// }



@GetMapping("/interviewer")
public ResponseEntity<List<ScheduleInterview>> getInterviewsByStatusAndInterviewer(
        @RequestParam boolean status,
        @RequestParam Long interviewerId) {

    List<ScheduleInterview> interviews = scheduleInterviewService.getInterviewsByStatusAndInterviewer(status, interviewerId);
    return new ResponseEntity<>(interviews, HttpStatus.OK);
}



@GetMapping("/byJobDescription/{jobDescriptionId}")
    public ResponseEntity<List<ScheduleInterview>> getByJobDescriptionAndStatus(@PathVariable Long jobDescriptionId) {
        List<ScheduleInterview> interviews = scheduleInterviewService.getInterviewsByJobDescriptionAndStatus(jobDescriptionId);
        return ResponseEntity.ok(interviews);
    }


    @GetMapping("/today/{interviewerId}")
    public ResponseEntity<List<ScheduleInterview>> getTodayInterviews(@PathVariable Long interviewerId) {
        List<ScheduleInterview> interviews = scheduleInterviewService.getTodayInterviewsByInterviewer(interviewerId);
        return ResponseEntity.ok(interviews);
    }


    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInterview(@PathVariable Long id) {
        scheduleInterviewService.deleteScheduleInterviewById(id);
        return ResponseEntity.ok("Interview deleted successfully with ID: " + id);
    }



    @PutMapping("/updateResult/{id}")
    public ResponseEntity<ScheduleInterview> updateInterviewResult(
            @PathVariable Long id,
            @RequestParam boolean passed) {
        ScheduleInterview updated = scheduleInterviewService.updateInterviewResult(id, passed);
        return ResponseEntity.ok(updated);
    }
    
    
    @GetMapping("/sendTodayInterviewerReminders")
    public ResponseEntity<String> sendTodayReminders() {
        scheduleInterviewService.sendTodayInterviewRemindersToInterviewer();
        return ResponseEntity.ok("Interview reminders sent to all interviewers for today.");
    }



   

 // ✅ More specific mapping comes first
 @GetMapping("/byMonthYear")
 public List<ScheduleInterview> getByMonthYearAndCompany(@RequestParam int month,
                                                          @RequestParam int year,
                                                          @RequestParam Long companyId) {
     return scheduleInterviewRepository.findByMonthYearAndCompany(month, year, companyId);
 }


 @GetMapping("/{id}")
 public ScheduleInterview getScheduleInterviewById(@PathVariable Long id) {
     return scheduleInterviewService.getScheduleInterviewById(id);
 }
    


 @PostMapping("/scheduleMultiple")
 public String scheduleMultipleInterviews(@RequestBody ScheduleInterviewRequest request) {
     return scheduleInterviewService.scheduleMultipleInterviews(request);
 }


 @GetMapping("/getByInterviewIdAndMonthYear")
    public List<ScheduleInterview> getInterviewsByInterviewIdAndMonthYear(
            @RequestParam Long interviewId,
            @RequestParam int month,
            @RequestParam int year) {
        return scheduleInterviewService.getInterviewsByInterviewIdAndMonthYear(interviewId, month, year);
    }

    @GetMapping("/getByInterviewerIdAndMonthYear")
    public List<ScheduleInterview> getInterviewsByInterviewerIdAndMonthYear(
            @RequestParam Long interviewerId,
            @RequestParam int month,
            @RequestParam int year) {
        return scheduleInterviewService.getInterviewsByInterviewerIdAndMonthYear(interviewerId, month, year);
    }
    
}
