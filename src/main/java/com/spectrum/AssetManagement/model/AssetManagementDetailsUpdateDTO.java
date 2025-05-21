package com.spectrum.AssetManagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssetManagementDetailsUpdateDTO {
    private Long id;
    private String description;
}
