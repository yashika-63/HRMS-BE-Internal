package com.spectrum.CTCModuleMain.model;

import com.spectrum.model.CompanyRegistration;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ctc_master")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CTCMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String label;
    @Column
    private String category;
    @Column
    private String type;
    @Column
    private Double percentValue;



    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private CompanyRegistration company;

   
}
