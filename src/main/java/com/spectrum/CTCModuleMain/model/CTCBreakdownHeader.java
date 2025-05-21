package com.spectrum.CTCModuleMain.model;

import java.time.LocalDate;
import java.util.List;

import com.spectrum.Payroll.PayrollHours.model.EncryptedLongConverter;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CTCBreakdownHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate effectiveFromDate;
        
    @Column
    private LocalDate effectiveToDate;

    @Column
    private boolean ctcStatus = true;

    @Convert(converter = EncryptedLongConverter.class)
    @Column(columnDefinition = "TEXT")
    private long ctcAmount;

    @Convert(converter = EncryptedLongConverter.class)
    @Column(columnDefinition = "TEXT")
    private long basicAmount;




    @OneToMany(mappedBy = "ctcBreakdownHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VariableCTCBreakdown> variableCTCBreakdowns;



    @OneToMany(mappedBy = "ctcBreakdownHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaticCTCBreakdown> staticCTCBreakdowns;


    
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private CompanyRegistration company;


}
