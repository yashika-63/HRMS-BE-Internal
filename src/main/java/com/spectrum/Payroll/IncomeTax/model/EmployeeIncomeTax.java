package com.spectrum.Payroll.IncomeTax.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.spectrum.CTCModuleMain.model.CTCBreakdownHeader;
import com.spectrum.Payroll.PayrollHours.model.EncryptedDoubleConverter;
import com.spectrum.Payroll.PayrollRecord.model.PayrollRecord;
import com.spectrum.model.Employee;

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
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeIncomeTax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Convert(converter = EncryptedDoubleConverter.class)
    @Column
    private double incomeTaxDeduction;

    @Column
    @CreationTimestamp
    private LocalDate date;

    @Column
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "ctc_breakdownHeader", referencedColumnName = "id", nullable = false)
    private CTCBreakdownHeader ctcBreakdownHeader;
}
