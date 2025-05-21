package com.spectrum.Induction.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.Induction.Model.InductionAck;
import com.spectrum.Induction.Model.Inductions;
import com.spectrum.Induction.Repo.InductionRepoAck;
import com.spectrum.Induction.Repo.InductionsRepository;

import jakarta.transaction.Transactional;

@Service
public class InductionServiceAck {
@Autowired
    private InductionRepoAck inductionRepoAck;

      @Autowired
    private InductionsRepository inductionsRepository;

    // Fetch all induction acknowledgments
    public List<InductionAck> getAllInductions() {
        return inductionRepoAck.findAll();
    }

    // Get a single acknowledgment by ID
    public InductionAck getInductionById(Long id) {
        return inductionRepoAck.findById(id).orElseThrow(()-> new RuntimeException("Id Not Found"));
    }

    // Save a new induction acknowledgment
    public InductionAck saveInduction(InductionAck inductionAck) {
        return inductionRepoAck.save(inductionAck);
    }

    // Update an existing induction acknowledgment
    @Transactional
    public InductionAck updateInduction(Long id, InductionAck updatedInductionAck) {
        return inductionRepoAck.findById(id)
                .map(existingInduction -> {
                    existingInduction.setQuestion(updatedInductionAck.getQuestion());
                    existingInduction.setRating(updatedInductionAck.getRating());
                    return inductionRepoAck.save(existingInduction);
                }).orElseThrow(() -> new RuntimeException("Induction not found with id: " + id));
    }

    // Delete an induction acknowledgment by ID
    public void deleteInduction(Long id) {
     InductionAck var1 =inductionRepoAck.findById(id).orElseThrow(()-> new RuntimeException("Id not found"));
  inductionRepoAck.deleteById(id);    }

  //////////////////////////////////////////////////////////////////////
  public InductionAck addInductionAckToInduction(Long inductionId, InductionAck inductionAck) {
    Inductions induction = inductionsRepository.findById(inductionId)
        .orElseThrow(() -> new RuntimeException("Induction not found with ID: " + inductionId));

    inductionAck.setInduction(induction);
    
    return inductionRepoAck.save(inductionAck);
}

public List<InductionAck> getByInductionId(Long inductionId) {
    Inductions induction = inductionsRepository.findById(inductionId)
    .orElseThrow(() -> new RuntimeException("Induction not found with ID: " + inductionId));

    return inductionRepoAck.findByInductionId(inductionId);
}

}
