package com.spectrum.AssetManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssetManagementDetails {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String lable;

    @Column
    private boolean status;

    @Column
    private String description;

    @Column
    private boolean submitted;

    @Column
    private boolean occupied;
   
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "asset_management_id", referencedColumnName = "id", nullable = false)
    private AssetManagement assetManagement;

}
