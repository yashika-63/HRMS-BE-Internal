package com.spectrum.ExitProcess.Model;

import org.springframework.stereotype.Service;

import com.spectrum.Training.model.TrainingHRMS;
import com.spectrum.model.CompanyRegistration;

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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
@Entity
public class OffboardingDocument {
      @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;
    private String fileName;
    


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

     public OffboardingDocument(String filePath, OffBoarding offBoarding) {
    this.filePath = filePath;
    this.offBoarding = offBoarding;
    }

    @ManyToOne
@JoinColumn(name = "offBoarding_id", referencedColumnName = "id") 
private OffBoarding offBoarding;
}

