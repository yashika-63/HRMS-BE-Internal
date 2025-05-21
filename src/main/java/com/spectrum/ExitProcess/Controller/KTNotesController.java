package com.spectrum.ExitProcess.Controller;


import com.spectrum.ExitProcess.Model.KTNotes;
import com.spectrum.ExitProcess.Model.KnowledgeTransfer;
import com.spectrum.ExitProcess.Service.KTNotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kt-notes")
public class KTNotesController {
    @Autowired
    private  KTNotesService ktNotesService;

    
   

    // Create a new KT Note
    @PostMapping
    public ResponseEntity<KTNotes> createNote(@RequestBody KTNotes note) {
        KTNotes savedNote = ktNotesService.saveNote(note);
        return new ResponseEntity<>(savedNote, HttpStatus.CREATED);
    }

    // Get all KT Notes
    @GetMapping
    public ResponseEntity<List<KTNotes>> getAllNotes() {
        List<KTNotes> notes = ktNotesService.getAllNotes();
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    // Get a single KT Note by ID
    @GetMapping("/{id}")
    public ResponseEntity<KTNotes> getNoteById(@PathVariable Long id) {
        KTNotes note = ktNotesService.getNoteById(id);
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    // Update an existing KT Note
    @PutMapping("/{id}")
    public ResponseEntity<KTNotes> updateNote(@PathVariable Long id, @RequestBody KTNotes noteDetails) {
        // First get the existing note to ensure it exists
        KTNotes existingNote = ktNotesService.getNoteById(id);
        
        // Update the fields
        existingNote.setNotes(noteDetails.getNotes());
        existingNote.setCreatedBy(noteDetails.getCreatedBy());
        
        // Save the updated note
        KTNotes updatedNote = ktNotesService.saveNote(existingNote);
        return new ResponseEntity<>(updatedNote, HttpStatus.OK);
    }

    // Delete a KT Note
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        // First verify the note exists
        ktNotesService.getNoteById(id); // Will throw exception if not found
        ktNotesService.deleteNote(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/save")
    public ResponseEntity<KTNotes> saveKTNote(
            @RequestBody KTNotes ktNotes,
            @RequestParam(required = false) Long knowledgeTransferId) {

        KTNotes savedNote = ktNotesService.saveNote(ktNotes, knowledgeTransferId);
        return ResponseEntity.ok(savedNote);
    }

    



}

