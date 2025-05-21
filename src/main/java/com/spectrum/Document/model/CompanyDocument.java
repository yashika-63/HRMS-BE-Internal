package com.spectrum.Document.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.spectrum.model.CompanyRegistration;

import jakarta.persistence.Column;
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

@AllArgsConstructor

@NoArgsConstructor

@Getter
@Data
@Setter
@Table
public class CompanyDocument {


     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String filePath;
 
    @Column
    private String documentIdentityKey;

    @Column
    private boolean status;

    @Column
    @CreationTimestamp
    private LocalDate date;

      @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private CompanyRegistration company;


    public CompanyDocument(String filePath) {

        this.filePath = filePath;

    }
 
 
    public Document orElseThrow(Object object) {

        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");

    }
 

 
}
