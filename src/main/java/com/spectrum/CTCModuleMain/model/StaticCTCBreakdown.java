package com.spectrum.CTCModuleMain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.Payroll.PayrollHours.model.EncryptedLongConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StaticCTCBreakdown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String label;

     @Convert(converter = EncryptedLongConverter.class)
    @Column(columnDefinition = "TEXT") 
    private long amount;

    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ctc_breakdown_header_id", referencedColumnName = "id", nullable = false)
    private CTCBreakdownHeader ctcBreakdownHeader;
}
