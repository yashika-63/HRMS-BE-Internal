package com.spectrum.Training.model;

import com.spectrum.Induction.Model.Inductions;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentTraining {
         @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;
    private String fileName;
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
 
    @ManyToOne
    @JoinColumn(name = "training_id", referencedColumnName = "id", nullable = true)  // Foreign Key
    private TrainingHRMS training;  

       public DocumentTraining(String filePath, TrainingHRMS training) {
    this.filePath = filePath;
    this.training = training;
    }

}

