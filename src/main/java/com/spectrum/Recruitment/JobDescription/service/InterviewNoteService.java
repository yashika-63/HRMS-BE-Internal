package com.spectrum.Recruitment.JobDescription.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.Recruitment.JobDescription.model.InterviewNote;
import com.spectrum.Recruitment.JobDescription.model.JobDescription;
import com.spectrum.Recruitment.JobDescription.model.ScheduleInterview;
import com.spectrum.Recruitment.JobDescription.repository.InterviewNoteRepository;
import com.spectrum.Recruitment.JobDescription.repository.JobDescriptionRepository;
import com.spectrum.Recruitment.JobDescription.repository.ScheduleInterviewRepository;

@Service
public class InterviewNoteService {

    @Autowired
    private InterviewNoteRepository interviewNoteRepository;

    @Autowired
    private ScheduleInterviewRepository scheduleInterviewRepository;

   @Autowired
private JobDescriptionRepository jobDescriptionRepository;

public InterviewNote saveInterviewNote(Long scheduleInterviewId, InterviewNote interviewNote) {
    ScheduleInterview scheduleInterview = scheduleInterviewRepository.findById(scheduleInterviewId)
        .orElseThrow(() -> new RuntimeException("Schedule Interview not found"));

    JobDescription jobDescription = scheduleInterview.getJobDescription();

    interviewNote.setScheduleInterview(scheduleInterview);
    interviewNote.setJobDescription(jobDescription);

    return interviewNoteRepository.save(interviewNote);
}





    public List<InterviewNote> getNotesByScheduleInterviewId(Long scheduleInterviewId) {
        ScheduleInterview scheduleInterview = scheduleInterviewRepository.findById(scheduleInterviewId)
                .orElseThrow(() -> new RuntimeException("ScheduleInterview not found"));
        return interviewNoteRepository.findByScheduleInterview(scheduleInterview);
    }
    

    public List<InterviewNote> getInterviewNotesByJobDescriptionIdSorted(Long jobDescriptionId) {
        return interviewNoteRepository.findByJobDescriptionIdOrderByInterviewScoreDesc(jobDescriptionId);
    }
    
}
