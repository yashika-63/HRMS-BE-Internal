package com.spectrum.Payroll.PayrollProcessed.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.Payroll.PayrollHours.model.EncryptedDoubleConverter;
import com.spectrum.Payroll.PayrollHours.model.EncryptedIntegerConverter;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayrollProcessed {

 @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
// this column's carries data for 
    @Column
    private boolean paymentPaidStatus = false;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double idealAmountForEmployeeToPaidForDays;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double idealAmountForEmployeeToPaidForHours;

    @Convert(converter = EncryptedIntegerConverter.class)
    @Column(columnDefinition = "TEXT")
    private int idealWorkingdaysForThisPayroll;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double idealWorkinghoursForThisPayroll ;
   
    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double actualAmountForEmployeeToPaidForDays;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double actualAmountForEmployeeToPaidForHours;

    @Convert(converter = EncryptedIntegerConverter.class)
    @Column(columnDefinition = "TEXT")
    private int actualWorkingdaysForThisPayroll;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double  actualWorkinghoursForThisPayroll ;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double  overtimeAmount ;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double totalDiduction;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double totalInhand;

    @Convert(converter = EncryptedDoubleConverter.class)
    @Column(columnDefinition = "TEXT")
    private double incomeTaxDeduction;

    //////////////////
        
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDate date;



    // here we establish required relation 
     @OneToOne
	 @JoinColumn(name = "payrollRecord_id", referencedColumnName = "id")
	 private PayrollRecord payrollRecordayrollRecord;

     @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

}
