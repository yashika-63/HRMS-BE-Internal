package com.spectrum.ExitProcess.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.ExitProcess.Model.ExitInterview;
import com.spectrum.ExitProcess.Model.ExitInterviewNotes;
import com.spectrum.ExitProcess.Repo.ExitInterviewNoteRepo;
import com.spectrum.ExitProcess.Service.ExitInterviewNotesService;
import com.spectrum.ExitProcess.Service.ExitInterviewService;
import com.spectrum.ExitProcess.Service.KTService;


@RestController
@RequestMapping("/api/exit-interviewsNotes")
public class ExitInterviewNotesController {

    @Autowired 
    private ExitInterviewNotesService exitNoteService;

    @PostMapping("/save/{exitInterviewId}")
    public ResponseEntity<ExitInterviewNotes> saveNote(@PathVariable Long exitInterviewId, 
                                           @RequestBody ExitInterviewNotes note) {
                                            ExitInterviewNotes        var1= exitNoteService.saveNote(exitInterviewId, note);
        return ResponseEntity.ok(var1);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<ExitInterviewNotes>> getAllNotes() {
        List<ExitInterviewNotes> notes = exitNoteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }

    // Get a single note by ID
    @GetMapping("/{id}")
    public ResponseEntity<ExitInterviewNotes> getNoteById(@PathVariable Long id) {
        ExitInterviewNotes note = exitNoteService.getExInterviewNotesByID(id);
        return ResponseEntity.ok(note);
    }
    
}
