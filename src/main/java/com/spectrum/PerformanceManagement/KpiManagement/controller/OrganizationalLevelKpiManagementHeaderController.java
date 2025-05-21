package com.spectrum.PerformanceManagement.KpiManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.spectrum.PerformanceManagement.KpiManagement.model.OrganizationalLevelKpiManagementHeader;
import com.spectrum.PerformanceManagement.KpiManagement.service.OrganizationalLevelKpiManagementHeaderService;

@RestController
@RequestMapping("/api/KpiSetting/organization/")

public class OrganizationalLevelKpiManagementHeaderController {

   
    @Autowired
    private OrganizationalLevelKpiManagementHeaderService organizationalLevelKpiManagementHeaderService;






    @PostMapping("/save/{companyId}")
public ResponseEntity<OrganizationalLevelKpiManagementHeader> saveKpiManagementHeader(
        @RequestBody  OrganizationalLevelKpiManagementHeader organizationalLevelKpiManagementHeader,
        @PathVariable Long companyId) {  // Change @RequestParam to @PathVariable

            OrganizationalLevelKpiManagementHeader savedHeader = organizationalLevelKpiManagementHeaderService.saveOrganizationalLevelKpiManagementHeader(organizationalLevelKpiManagementHeader, companyId);

    return ResponseEntity.ok(savedHeader);
}



@GetMapping("/getByFilters/{companyId}/{kpiType}/{departmentId}/{levelCode}/{regionId}/{typeId}")
public ResponseEntity<List<OrganizationalLevelKpiManagementHeader>> getKpiByFilters(
        @PathVariable Long companyId,
        @PathVariable boolean kpiType,
        @PathVariable int departmentId,
        @PathVariable int levelCode,
        @PathVariable int regionId,
        @PathVariable int typeId) {

    List<OrganizationalLevelKpiManagementHeader> kpiHeaders = organizationalLevelKpiManagementHeaderService.getKpiByFilters(
            companyId, kpiType, departmentId, levelCode, regionId, typeId);

    return ResponseEntity.ok(kpiHeaders);
}






@GetMapping("/getByFiltersNew/{companyId}/{departmentId}/{levelCode}/{regionId}/{typeId}")
public ResponseEntity<List<OrganizationalLevelKpiManagementHeader>> getKpiByFiltersNew(
        @PathVariable Long companyId,
        @PathVariable int departmentId,
        @PathVariable int levelCode,
        @PathVariable int regionId,
        @PathVariable int typeId) {

    List<OrganizationalLevelKpiManagementHeader> kpiHeaders = organizationalLevelKpiManagementHeaderService.getKpiByFiltersNew(
            companyId,  departmentId, levelCode, regionId, typeId);

    return ResponseEntity.ok(kpiHeaders);
}




@GetMapping("/getByCompanyAndYear/{companyId}/{year}")
    public ResponseEntity<List<OrganizationalLevelKpiManagementHeader>> getKpiByCompanyIdAndYear(
            @PathVariable Long companyId, @PathVariable int year) {
        
        List<OrganizationalLevelKpiManagementHeader> kpiHeaders = organizationalLevelKpiManagementHeaderService.getKpiByCompanyIdAndYear(companyId, year);
        
        return ResponseEntity.ok(kpiHeaders);
    }




    @PutMapping("/update/{id}")
    public ResponseEntity<OrganizationalLevelKpiManagementHeader> updateKpiManagementHeader(
            @PathVariable Long id, @RequestBody OrganizationalLevelKpiManagementHeader updatedHeader) {
    
        OrganizationalLevelKpiManagementHeader updatedEntity = organizationalLevelKpiManagementHeaderService.updateKpiManagementHeader(id, updatedHeader);
        return ResponseEntity.ok(updatedEntity);
    }
    

// @GetMapping("/getKpiDetail")
//     public ResponseEntity<List<EmployeeGoalSetting>> getKpiForAssign(
//             @RequestParam int regionId,
//             @RequestParam Long employeeId,
//             @RequestParam int departmentId,
//             @RequestParam int typeId,
//             @RequestParam boolean status,
//             @RequestParam boolean kpiType) {
//         try {
//             List<OrganizationalLevelKpiManagementHeaderController> kpiManagement =
//             organizationalLevelKpiManagementHeaderService.getGoalsByEmployeeAndDepartmentAndRegionAndGoalTypeAndStatus(
//                     regionId, employeeId, departmentId, typeId, status, kpiType);
//             return ResponseEntity.ok(organizationalLevelKpiManagementHeaderService);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//         }
//     }

@GetMapping("/getKpiDetail")
public ResponseEntity<List<OrganizationalLevelKpiManagementHeader>> getKpiForAssign(
        @RequestParam int regionId,
        @RequestParam Long companyId,
        @RequestParam int departmentId,
        @RequestParam int typeId,
        @RequestParam boolean status,
        @RequestParam boolean kpiType) {
    try {
        List<OrganizationalLevelKpiManagementHeader> kpiManagement =
                organizationalLevelKpiManagementHeaderService.getKpisByCompanyAndDepartmentAndRegionAndKpiTypeAndStatus(
                        regionId, companyId, departmentId, typeId, status, kpiType);
        return ResponseEntity.ok(kpiManagement);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

    


@GetMapping("/getKpiDetailNew")
public ResponseEntity<List<OrganizationalLevelKpiManagementHeader>> getKpiForAssignNew(
        @RequestParam int regionId,
        @RequestParam Long companyId,
        @RequestParam int departmentId,
        @RequestParam int typeId,
        @RequestParam boolean status) {
    try {
        List<OrganizationalLevelKpiManagementHeader> kpiManagement =
                organizationalLevelKpiManagementHeaderService.getKpisByCompanyAndDepartmentAndRegionAndStatus(
                        regionId, companyId, departmentId, typeId, status);
        return ResponseEntity.ok(kpiManagement);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

}
