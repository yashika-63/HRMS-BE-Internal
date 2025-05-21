package com.spectrum.PerformanceManagement.GoalSetting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.spectrum.PerformanceManagement.GoalSetting.model.EmployeeGoalSetting;
import com.spectrum.PerformanceManagement.GoalSetting.model.OrganizationGoalSettingHeader;
import com.spectrum.PerformanceManagement.GoalSetting.service.OrganizationGoalSettingHeaderService;

@RestController
@RequestMapping("/api/goalSetting/organization/")

public class OrganizationGoalSettingHeaderController {


@Autowired
private OrganizationGoalSettingHeaderService organizationGoalSettingHeaderService;


@PostMapping("/save/{companyId}")
public ResponseEntity<OrganizationGoalSettingHeader> saveGoalSettingHeader(
        @RequestBody OrganizationGoalSettingHeader organizationGoalSettingHeader,
        @PathVariable Long companyId) {  // Change @RequestParam to @PathVariable

    OrganizationGoalSettingHeader savedHeader = organizationGoalSettingHeaderService
            .saveOrganizationGoalSettingHeader(organizationGoalSettingHeader, companyId);

    return ResponseEntity.ok(savedHeader);
}

// @GetMapping("/getByFilters/{companyId}/{goalType}/{departmentId}/{levelId}/{regionId}/{typeCode}")
// public ResponseEntity<List<OrganizationGoalSettingHeader>> getGoalsByFilters(
//         @PathVariable Long companyId,
//         @PathVariable boolean goalType,
//         @PathVariable int departmentId,
//         @PathVariable int levelId,
//         @PathVariable int regionId,
//         @PathVariable int typeCode) {

//     List<OrganizationGoalSettingHeader> goals = organizationGoalSettingHeaderService.getGoalsByFilters(
//             companyId, goalType, departmentId, levelId, regionId, typeCode);

//     return ResponseEntity.ok(goals);
// }

@GetMapping("/getByFilters")
public ResponseEntity<List<Map<String, Object>>> getGoalsByFilters(
        @RequestParam Long companyId,
        @RequestParam boolean goalType, 
        @RequestParam boolean status,
        @RequestParam int departmentId,
        @RequestParam int levelId,
        @RequestParam int regionId,
        @RequestParam int typeCode) {

    List<OrganizationGoalSettingHeader> goals = organizationGoalSettingHeaderService.getGoalsByFilters(
            companyId, goalType, status, departmentId, levelId, regionId, typeCode);

    // Transform the data
    List<Map<String, Object>> transformedGoals = goals.stream().flatMap(goalHeader ->
            goalHeader.getOrgenizOrganizationGoalSettingDetails().stream().map(detail -> {
                Map<String, Object> goalMap = new HashMap<>();
                goalMap.put("goal", detail.getGoal());
                goalMap.put("regionId", goalHeader.getRegionId());
                goalMap.put("region", goalHeader.getRegion());
                goalMap.put("typeId", goalHeader.getTypeId());
                goalMap.put("type", goalHeader.getType());
                goalMap.put("departmentId", goalHeader.getDepartmentId());
                goalMap.put("department", goalHeader.getDepartment());
                goalMap.put("goalType", goalHeader.isGoalType());

                return goalMap;
            })
    ).collect(Collectors.toList());

    return ResponseEntity.ok(transformedGoals);
}



@GetMapping("/getByFiltersNew")
public ResponseEntity<List<Map<String, Object>>> getGoalsByFiltersNew(
        @RequestParam Long companyId,
        @RequestParam boolean status,
        @RequestParam int departmentId,
        @RequestParam int levelId,
        @RequestParam int regionId,
        @RequestParam int typeCode) {

    List<OrganizationGoalSettingHeader> goals = organizationGoalSettingHeaderService.getGoalsByFiltersNew(
            companyId, status, departmentId, levelId, regionId, typeCode);

    // Transform the data
    List<Map<String, Object>> transformedGoals = goals.stream().flatMap(goalHeader ->
            goalHeader.getOrgenizOrganizationGoalSettingDetails().stream().map(detail -> {
                Map<String, Object> goalMap = new HashMap<>();
                goalMap.put("goal", detail.getGoal());
                goalMap.put("regionId", goalHeader.getRegionId());
                goalMap.put("region", goalHeader.getRegion());
                goalMap.put("typeId", goalHeader.getTypeId());
                goalMap.put("type", goalHeader.getType());
                goalMap.put("departmentId", goalHeader.getDepartmentId());
                goalMap.put("department", goalHeader.getDepartment());
                goalMap.put("goalType", goalHeader.isGoalType());

                return goalMap;
            })
    ).collect(Collectors.toList());

    return ResponseEntity.ok(transformedGoals);
}





@GetMapping("/getByCompany/{companyId}")
    public ResponseEntity<List<OrganizationGoalSettingHeader>> getByCompanyId(@PathVariable Long companyId) {
        List<OrganizationGoalSettingHeader> goalSettings = organizationGoalSettingHeaderService.getByCompanyIdAndGoalType(companyId);
        
        if (goalSettings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(goalSettings);
        }
        return ResponseEntity.ok(goalSettings);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<OrganizationGoalSettingHeader> updateGoalSettingHeader(
            @PathVariable Long id, @RequestBody OrganizationGoalSettingHeader updatedHeader) {

        OrganizationGoalSettingHeader updatedEntity = organizationGoalSettingHeaderService.updateGoalSettingHeader(id, updatedHeader);
        return ResponseEntity.ok(updatedEntity);
    }



    @GetMapping("/getByCompany/{companyId}/{year}")
    public ResponseEntity<List<OrganizationGoalSettingHeader>> getByCompanyIdAndYear(
            @PathVariable Long companyId, @PathVariable int year) {
        
        List<OrganizationGoalSettingHeader> goalSettings = organizationGoalSettingHeaderService.getByCompanyIdAndYear(companyId, year);
        
        if (goalSettings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(goalSettings);
    }



    










    

}
