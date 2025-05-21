package com.spectrum.Induction.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.Induction.Model.Inductions;
import com.spectrum.model.Employee;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DocumentInduction {
        @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;
    private String fileName;
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
 
   

      

    

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "induction_id", referencedColumnName = "id", nullable = true)
    private Inductions induction;
  // Constructor
     public DocumentInduction(String filePath, Inductions induction) {
    this.filePath = filePath;
    this.induction = induction;
    }

}
