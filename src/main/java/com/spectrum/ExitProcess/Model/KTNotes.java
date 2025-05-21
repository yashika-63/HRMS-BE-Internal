package com.spectrum.ExitProcess.Model;

import com.spectrum.model.Employee;

import jakarta.persistence.Column;
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
@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class KTNotes {

     @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1500)
    private String notes;

    @Column
    private String createdBy;


    @ManyToOne
@JoinColumn(name = "knowledge_transfer_id",  referencedColumnName = "id")
private KnowledgeTransfer knowledgeTransfer;

}
