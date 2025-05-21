package com.spectrum.Training.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spectrum.Training.model.TrainingAcknowledge;
import com.spectrum.Training.model.TrainingHRMS;
import com.spectrum.Training.repository.TrainingAcknowledegeRepo;
import com.spectrum.Training.repository.TrainingRepositoary;

@Service
public class TrainingServiceAcknowledgement {
    
    @Autowired
    private TrainingAcknowledegeRepo repo;

    @Autowired
    private TrainingRepositoary trainingRepo;

    public List<TrainingAcknowledge> getAll() {
        return repo.findAll();
    }

    public TrainingAcknowledge getById(Long id) {
        return repo.findById(id).orElseThrow(()-> new RuntimeException("Id not found"));
    }

    public TrainingAcknowledge save(TrainingAcknowledge acknowledge) {
        return repo.save(acknowledge);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
    public TrainingAcknowledge update(Long id, TrainingAcknowledge updatedAcknowledge) {
        TrainingAcknowledge existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Id not found: " + id));

        existing.setQuestion(updatedAcknowledge.getQuestion());
        existing.setRating(updatedAcknowledge.getRating());
        existing.setTermAndCondition(updatedAcknowledge.isTermAndCondition());

        return repo.save(existing);
    }
    /////////
    
    public List<TrainingAcknowledge> saveAll(Long trainingId, List<TrainingAcknowledge> acknowledgeList) {
        TrainingHRMS training = trainingRepo.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found with ID: " + trainingId));

        acknowledgeList.forEach(ack -> ack.setTrainingHRMS(training));
        return repo.saveAll(acknowledgeList);
    }
    public List<TrainingAcknowledge> getAllAcknowledgementsByTrainingId(Long trainingId) {
        TrainingHRMS training = trainingRepo.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found with ID: " + trainingId));
       
        return repo.findAllByTrainingHRMSId(trainingId);
    }
}
