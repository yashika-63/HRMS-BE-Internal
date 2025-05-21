package com.spectrum.ExitProcess.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.ExitProcess.Model.ExitInterviewNotes;

public interface ExitInterviewNoteRepo extends JpaRepository<ExitInterviewNotes,Long>{
    

}
