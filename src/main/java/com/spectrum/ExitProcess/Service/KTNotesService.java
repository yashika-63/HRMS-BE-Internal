package com.spectrum.ExitProcess.Service;

import org.springframework.stereotype.Service;

import com.spectrum.ExitProcess.Model.KTNotes;
import com.spectrum.ExitProcess.Model.KnowledgeTransfer;
import com.spectrum.ExitProcess.Repo.KTRepo;
import com.spectrum.ExitProcess.Repo.KtRepoNotes;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KTNotesService {

    @Autowired
    private  KtRepoNotes ktRepoNotes;

   @Autowired
   private KTRepo knowledgeTransferRepository;

    // Create or Update a note
    public KTNotes saveNote(KTNotes note) {
        return ktRepoNotes.save(note);
    }

    // Get all notes
    public List<KTNotes> getAllNotes() {
        return ktRepoNotes.findAll();
    }


    // Get note by ID
    public KTNotes getNoteById(Long id) {
        return ktRepoNotes.findById(id).orElseThrow(()-> new RuntimeException("Not Found the Particular ID"));
    }

    // Delete note by ID
    public void deleteNote(Long id) {
        ktRepoNotes.deleteById(id);
    }
        
    @Transactional
    public KTNotes saveNote(KTNotes ktnotes, Long knowledgeTransferId) {
        if(knowledgeTransferId==null){
            throw new RuntimeException(" knowledgeTransferId Parameter is Null");
        }
        KnowledgeTransfer kt = knowledgeTransferRepository.findById(knowledgeTransferId)
                .orElseThrow(() -> new RuntimeException("KnowledgeTransfer not found with id: " + knowledgeTransferId));
    
        ktnotes.setKnowledgeTransfer(kt);
        return ktRepoNotes.save(ktnotes);
    }
    

    

   
}


