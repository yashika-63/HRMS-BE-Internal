package com.spectrum.Document.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spectrum.workflow.model.ExpenseManagement;

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

public class DocumentExpenseManagement {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
 
    

    private String filePath;
 
    

    @JsonIgnore

    

    @ManyToOne
    @JoinColumn(name = "expense_Management_id", referencedColumnName = "id", nullable = false)
    private ExpenseManagement expenseManagement;

    
   
 
    public DocumentExpenseManagement(String filePath) {

        this.filePath = filePath;

    }
 
    public Document orElseThrow(Object object) {



        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");

    }
 
 
    

}
 