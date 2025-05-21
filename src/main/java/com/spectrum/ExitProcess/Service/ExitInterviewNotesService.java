package com.spectrum.ExitProcess.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.ExitProcess.Model.ExitInterview;
import com.spectrum.ExitProcess.Model.ExitInterviewNotes;
import com.spectrum.ExitProcess.Repo.ExitIntervieRepo;
import com.spectrum.ExitProcess.Repo.ExitInterviewNoteRepo;

import jakarta.transaction.Transactional;

@Service
public class ExitInterviewNotesService {
    @Autowired
    private ExitInterviewNoteRepo exitNoteRepo;

    @Autowired
    private ExitIntervieRepo exitRepo;

    public ExitInterviewNotes saveNote(Long id,ExitInterviewNotes note) {
        

        // Validate and attach managed ExitInterview entity
        ExitInterview exitInterview = exitRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("ExitInterview with id " + id + " not found"));

        note.setExitInterview(exitInterview); // Attach managed entity
       return exitNoteRepo.save(note);
    }

    public List<ExitInterviewNotes>  getAllNotes(){
        return exitNoteRepo.findAll();
        


    }


    public ExitInterviewNotes getExInterviewNotesByID(Long id){
        return exitNoteRepo.findById(id).orElseThrow(()-> new RuntimeException("Id Not found"));
    }

}
