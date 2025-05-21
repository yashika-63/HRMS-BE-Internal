package com.spectrum.Training.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.Training.model.TrainingAcknowledge;
import com.spectrum.Training.service.TrainingServiceAcknowledgement;


@RestController
@RequestMapping("/api/acknowledges")
public class TrainingControllerAcknowledge {
     @Autowired
    private TrainingServiceAcknowledgement service;

    // Get all
    @GetMapping
    public List<TrainingAcknowledge> getAllAcknowledges() {
        return service.getAll();
    }

    // Get by ID
    @GetMapping("/{id}")
    public TrainingAcknowledge getAcknowledgeById(@PathVariable Long id) {
        return service.getById(id); 
    }

    // Create new
    @PostMapping
    public TrainingAcknowledge createAcknowledge(@RequestBody TrainingAcknowledge acknowledge) {
        return service.save(acknowledge);
    }

    // Update existing
    @PutMapping("/{id}")
    public TrainingAcknowledge updateAcknowledge(@PathVariable Long id,
                                                 @RequestBody TrainingAcknowledge updatedAcknowledge) {
        return service.update(id, updatedAcknowledge);
    }

    // Delete
    @DeleteMapping("/{id}")
    public String deleteAcknowledge(@PathVariable Long id) {
        service.delete(id);
        return "Acknowledgment with ID " + id + " has been deleted successfully.";
    }
    //////////
    @PostMapping("/bulk/{trainingId}")
    public List<TrainingAcknowledge> saveMultipleAcknowledges(@PathVariable Long trainingId,
                                                              @RequestBody List<TrainingAcknowledge> acknowledgeList) {
        return service.saveAll(trainingId, acknowledgeList);
    }

    @GetMapping("/training/{trainingId}")
    public ResponseEntity<List<TrainingAcknowledge>> getAllAcknowledgementsByTrainingId(
            @PathVariable Long trainingId
    ) {
        List<TrainingAcknowledge> acknowledges = service.getAllAcknowledgementsByTrainingId(trainingId);
        return ResponseEntity.ok(acknowledges);
    }
}
