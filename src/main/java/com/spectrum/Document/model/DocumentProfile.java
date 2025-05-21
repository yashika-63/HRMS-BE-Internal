package com.spectrum.Document.model;



import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spectrum.model.Employee;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import lombok.NoArgsConstructor;

import lombok.Setter;
 
@Entity

@AllArgsConstructor

@NoArgsConstructor

@Getter
@Data
@Setter
@Table
@JsonIgnoreProperties(ignoreUnknown = true)

public class DocumentProfile {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
 
    

    private String filePath;
 
    
    @Column
    private boolean status;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    
   
 
    public DocumentProfile(String filePath) {

        this.filePath = filePath;

    }
 
    public Document orElseThrow(Object object) {

        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");

    }
 
 
    

}
 