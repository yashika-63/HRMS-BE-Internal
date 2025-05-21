package com.spectrum.PerformanceManagement.GoalSetting.model;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.spectrum.model.CompanyRegistration;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Data
@AllArgsConstructor
@NoArgsConstructor


public class OrganizationGoalSettingHeader {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private int levelCode;
    
    @Column
    private String level;

    @Column
    private int regionId;

    @Column
    private String region;

    @Column
    private int typeId;
    // type will be technical or non technical
    @Column
    private String type;

    @Column
    private int departmentId;

    @Column
    private String department;

    // for all or personal
    @Column
    private boolean goalType;

    @Column
    private boolean status;

    @Column
    @CreationTimestamp
    private LocalDate date;




    @OneToMany(mappedBy = "organizationGoalSettingHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrganizationGoalSettingDetail> orgenizOrganizationGoalSettingDetails;


    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private CompanyRegistration company;

}
