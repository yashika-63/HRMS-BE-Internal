package com.spectrum.PerformanceManagement.KpiManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spectrum.PerformanceManagement.GoalSetting.model.OrganizationGoalSettingHeader;

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
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationalLevelKpiManagementDetails {



    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String kpi;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organization_level_kpi_management_header_id", referencedColumnName = "id", nullable = false)
    private OrganizationalLevelKpiManagementHeader organizationalLevelKpiManagementHeader;



}
