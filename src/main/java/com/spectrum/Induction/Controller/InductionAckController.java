package com.spectrum.Induction.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.Induction.Model.InductionAck;
import com.spectrum.Induction.Service.InductionServiceAck;

@RestController
@RequestMapping("/api/inductionsACK")
public class InductionAckController {

     @Autowired
    private InductionServiceAck inductionServiceAck;

    // Get all induction acknowledgments
   // Get all induction acknowledgments
    @GetMapping("getall")
    public ResponseEntity<List<InductionAck>> getAllInductions() {
        List<InductionAck> inductions = inductionServiceAck.getAllInductions();
        return new ResponseEntity<>(inductions, HttpStatus.OK);
    }

    // Get a single acknowledgment by ID
    @GetMapping("/{id}")
    public ResponseEntity<InductionAck> getInductionById(@PathVariable Long id) {
        InductionAck inductionAck = inductionServiceAck.getInductionById(id);
        return new ResponseEntity<>(inductionAck, HttpStatus.OK);
    }

    // Create a new induction acknowledgment
    @PostMapping("post")
    public ResponseEntity<InductionAck> createInduction(@RequestBody InductionAck inductionAck) {
        InductionAck savedInduction = inductionServiceAck.saveInduction(inductionAck);
        return new ResponseEntity<>(savedInduction, HttpStatus.CREATED);
    }

    // Update an existing acknowledgment
    @PutMapping("/{id}")
    public ResponseEntity<InductionAck> updateInduction(
            @PathVariable Long id,
            @RequestBody InductionAck updatedInductionAck) {
        InductionAck updatedInduction = inductionServiceAck.updateInduction(id, updatedInductionAck);
        return new ResponseEntity<>(updatedInduction, HttpStatus.OK);
    }

    // Delete an acknowledgment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInduction(@PathVariable Long id) {
        inductionServiceAck.deleteInduction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
/////////////////////////////////////////////////////////////////////////////////
@PostMapping("/add-to-induction/{inductionId}")
public InductionAck addInductionAckToInduction(@PathVariable Long inductionId, @RequestBody InductionAck inductionAck) {
    return inductionServiceAck.addInductionAckToInduction(inductionId, inductionAck);
}
 
@GetMapping("/inductionID/{inductionId}")
    public ResponseEntity<List<InductionAck>> getByInductionId(@PathVariable Long inductionId) {
        List<InductionAck> ackList = inductionServiceAck.getByInductionId(inductionId);

        if (ackList == null || ackList.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(ackList); // 200 OK
    }
}
