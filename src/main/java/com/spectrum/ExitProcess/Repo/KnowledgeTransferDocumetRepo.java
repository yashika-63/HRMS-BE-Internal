package com.spectrum.ExitProcess.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.ExitProcess.Model.ExitInterviewNotes;
import com.spectrum.ExitProcess.Model.KnowledgeTransferDocument;

public interface KnowledgeTransferDocumetRepo extends JpaRepository<KnowledgeTransferDocument,Long>{
    List<KnowledgeTransferDocument> findByKTtransfer_Id(Long ktId);
    
}
