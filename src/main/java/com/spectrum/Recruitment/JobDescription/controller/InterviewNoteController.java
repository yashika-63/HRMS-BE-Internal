package com.spectrum.Recruitment.JobDescription.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.spectrum.Recruitment.JobDescription.model.InterviewNote;
import com.spectrum.Recruitment.JobDescription.service.InterviewNoteService;

@RestController
@RequestMapping("/api/interview-notes")
public class InterviewNoteController {

    @Autowired
    private InterviewNoteService interviewNoteService;

    @PostMapping("/save/{scheduleInterviewId}")
    public InterviewNote saveInterviewNote(
            @PathVariable Long scheduleInterviewId,
            @RequestBody InterviewNote interviewNote) {
        return interviewNoteService.saveInterviewNote(scheduleInterviewId, interviewNote);
    }
    
    @GetMapping("/get-by-schedule/{scheduleInterviewId}")
    public List<InterviewNote> getNotesByScheduleInterview(@PathVariable Long scheduleInterviewId) {
    return interviewNoteService.getNotesByScheduleInterviewId(scheduleInterviewId);
}



@GetMapping("/by-jobdesc/{jobDescriptionId}")
public List<InterviewNote> getNotesByJobDesc(@PathVariable Long jobDescriptionId) {
    return interviewNoteService.getInterviewNotesByJobDescriptionIdSorted(jobDescriptionId);
}



}

