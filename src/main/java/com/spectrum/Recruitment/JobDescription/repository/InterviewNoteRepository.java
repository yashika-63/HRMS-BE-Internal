package com.spectrum.Recruitment.JobDescription.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.Recruitment.JobDescription.model.InterviewNote;
import com.spectrum.Recruitment.JobDescription.model.ScheduleInterview;

public interface InterviewNoteRepository extends JpaRepository<InterviewNote, Long> {
    List<InterviewNote> findByScheduleInterview(ScheduleInterview scheduleInterview);
    List<InterviewNote> findByJobDescriptionIdOrderByInterviewScoreDesc(Long jobDescriptionId);

}
