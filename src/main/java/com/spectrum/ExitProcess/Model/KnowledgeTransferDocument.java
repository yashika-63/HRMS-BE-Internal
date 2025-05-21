package com.spectrum.ExitProcess.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Table
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgeTransferDocument {
      @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;
    private String fileName;
    


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

     public KnowledgeTransferDocument(String filePath, KnowledgeTransfer KTtransfer) {
    this.filePath = filePath;
    this.KTtransfer = KTtransfer;
    }

    @ManyToOne
@JoinColumn(name = "Knowledge_Transfer_id", referencedColumnName = "id") 
private KnowledgeTransfer KTtransfer;
}
