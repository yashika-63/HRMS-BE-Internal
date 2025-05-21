package com.spectrum.PerformanceManagement.Appraisal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalKpi;
import com.spectrum.PerformanceManagement.Appraisal.repository.AppraisalGoalRepository;
import com.spectrum.PerformanceManagement.Appraisal.repository.AppraisalKpiRepository;
import com.spectrum.PerformanceManagement.Appraisal.service.AppraisalKpiNewService;
import com.spectrum.PerformanceManagement.Appraisal.service.AppraisalService;
import com.spectrum.PerformanceManagement.KpiManagement.model.EmployeeKpiSetting;
import com.spectrum.PerformanceManagement.KpiManagement.repository.EmployeeKpiSettingRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalGoal;

@RestController
@RequestMapping("/api/appraisal")
public class AppraisalController {

   
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeKpiSettingRepository employeeKpiSettingRepository;

 
    @Autowired
    private AppraisalKpiNewService appraisalKpiService;



    @Autowired
    private AppraisalGoalRepository appraisalGoalRepository;

    @Autowired
    private AppraisalKpiRepository appraisalKpiRepository;

    @Autowired
    private AppraisalService appraisalService;

    @PostMapping("/save-multiple/{reportingManagerId}/{employeeId}")
public ResponseEntity<?> saveMultipleAppraisalKpi(
        @PathVariable Long reportingManagerId,
        @PathVariable Long employeeId,
        @RequestBody List<AppraisalKpi> appraisalKpiList) {

    Optional<Employee> reportingManager = employeeRepository.findById(reportingManagerId);
    Optional<Employee> employee = employeeRepository.findById(employeeId);

    if (reportingManager.isEmpty() || employee.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid reportingManagerId or employeeId");
    }

    for (AppraisalKpi appraisalKpi : appraisalKpiList) {
        Optional<EmployeeKpiSetting> employeeKpiSetting = employeeKpiSettingRepository.findById(
                appraisalKpi.getEmployeeKpiSetting().getId());

        if (employeeKpiSetting.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invalid employeeKpiSettingId: " + appraisalKpi.getEmployeeKpiSetting().getId());
        }

        appraisalKpi.setEmployees(reportingManager.get());
        appraisalKpi.setEmployee(employee.get());
        appraisalKpi.setEmployeeKpiSetting(employeeKpiSetting.get());
    }

    List<AppraisalKpi> savedAppraisalKpiList = appraisalKpiService.saveMultipleAppraisalKpi(appraisalKpiList);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedAppraisalKpiList);
}


// @PostMapping("/save-multipleGoal/{reportingManagerId}/{employeeId}")
// public ResponseEntity<?> saveMultipleAppraisalGoal(
// @PathVariable Long reportingManagerId,
// @PathVariable Long employeeId,
// @RequestBody List<AppraisalGoal> appraisalGoalList) {

// Optional<Employee> reportingManager =
// employeeRepository.findById(reportingManagerId);
// Optional<Employee> employee = employeeRepository.findById(employeeId);

// if (reportingManager.isEmpty() || employee.isEmpty()) {
// return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid
// reportingManagerId or employeeId");
// }

// for (AppraisalGoal appraisalGoal : appraisalGoalList) {
// Optional<EmployeeGoalSetting> employeeGoalSetting =
// employeeGoalSettingRepository.findById(
// appraisalGoal.getEmployeeGoalSetting().getId());

// if (employeeGoalSetting.isEmpty()) {
// return ResponseEntity.status(HttpStatus.NOT_FOUND)
// .body("Invalid employeeGoalSettingId: " +
// appraisalGoal.getEmployeeGoalSetting().getId());
// }

// appraisalGoal.setEmployees(reportingManager.get());
// appraisalGoal.setEmployee(employee.get());
// appraisalGoal.setEmployeeGoalSetting(employeeGoalSetting.get());
// }

// List<AppraisalGoal> savedAppraisalGoalList =
// appraisalGoalService.saveMultipleAppraisalGoal(appraisalGoalList);
// return
// ResponseEntity.status(HttpStatus.CREATED).body(savedAppraisalGoalList);
// }




/////////////////
/// 




@GetMapping("/get-appraisal/{employeeId}/{year}")
public ResponseEntity<?> getAppraisalByEmployeeAndYear(@PathVariable Long employeeId, @PathVariable int year) {
    List<AppraisalKpi> kpiList = appraisalKpiRepository.findByEmployeeIdAndAppraisalYear(employeeId, year);
    List<AppraisalGoal> goalList = appraisalGoalRepository.findByEmployeeIdAndAppraisalYear(employeeId, year);

    if (kpiList.isEmpty() && goalList.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No records found for employeeId " + employeeId + " in year " + year);
    }

    double avgKpiRating = kpiList.stream().mapToDouble(AppraisalKpi::getEmployeeSelfRating).average().orElse(0.0);
    double avgGoalRating = goalList.stream().mapToDouble(AppraisalGoal::getEmployeeSelfRating).average().orElse(0.0);
    double totalAvgRating = (avgKpiRating + avgGoalRating) / 2;

    Map<String, Object> response = new HashMap<>();
    response.put("averageKpiRating", avgKpiRating);
    response.put("averageGoalRating", avgGoalRating);
    response.put("totalAverageRating", totalAvgRating);

    List<Map<String, Object>> kpiData = kpiList.stream().map(kpi -> {
        Map<String, Object> kpiMap = new HashMap<>();
        kpiMap.put("id", kpi.getId());
        kpiMap.put("employeeSelfRating", kpi.getEmployeeSelfRating());
        kpiMap.put("kpiSetting", kpi.getEmployeeKpiSetting());
        return kpiMap;
    }).collect(Collectors.toList());

    List<Map<String, Object>> goalData = goalList.stream().map(goal -> {
        Map<String, Object> goalMap = new HashMap<>();
        goalMap.put("id", goal.getId());
        goalMap.put("employeeSelfRating", goal.getEmployeeSelfRating());
        goalMap.put("goalSetting", goal.getEmployeeGoalSetting());
        return goalMap;
    }).collect(Collectors.toList());

    response.put("kpiData", kpiData);
    response.put("goalData", goalData);

    return ResponseEntity.ok(response);
}



@GetMapping("/get-appraisal-manager/{employeeId}/{year}/{reportingManagerId}")
public ResponseEntity<?> getAppraisalByEmployeeYearManager(
        @PathVariable Long employeeId,
        @PathVariable int year,
        @PathVariable Long reportingManagerId) {

    List<AppraisalKpi> kpiList = appraisalKpiRepository.findByEmployeeIdAndAppraisalYearAndEmployeesId(employeeId, year, reportingManagerId);
    List<AppraisalGoal> goalList = appraisalGoalRepository.findByEmployeeIdAndAppraisalYearAndEmployeesId(employeeId, year, reportingManagerId);

    if (kpiList.isEmpty() && goalList.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No records found for employeeId " + employeeId + " in year " + year + " with reporting manager " + reportingManagerId);
    }

    // Separate Average Ratings Calculation for Employee, Reporting Manager, and Moderated
    double avgEmployeeKpiRating = kpiList.stream().mapToDouble(AppraisalKpi::getEmployeeSelfRating).average().orElse(0.0);
    double avgManagerKpiRating =  kpiList.stream().mapToDouble(AppraisalKpi::getEmployeeManagerRating).average().orElse(0.0);
    double avgModeratedKpiRating =kpiList.stream().mapToDouble(AppraisalKpi::getEmployeeModeratedFinalRating).average().orElse(0.0);

    double avgEmployeeGoalRating = goalList.stream().mapToDouble(AppraisalGoal::getEmployeeSelfRating).average().orElse(0.0);
    double avgManagerGoalRating = goalList.stream().mapToDouble(AppraisalGoal::getEmployeeManagerRating).average().orElse(0.0);
    double avgModeratedGoalRating = goalList.stream().mapToDouble(AppraisalGoal::getEmployeeModeratedFinalRating).average().orElse(0.0);

    // Total Average Ratings Calculation
    double totalAvgEmployeeRating = (avgEmployeeKpiRating + avgEmployeeGoalRating) / 2;
    double totalAvgManagerRating = (avgManagerKpiRating + avgManagerGoalRating) / 2;
    double totalAvgModeratedRating = (avgModeratedKpiRating + avgModeratedGoalRating) / 2;

    Map<String, Object> response = new HashMap<>();
    response.put("averageEmployeeKpiRating", avgEmployeeKpiRating);
    response.put("averageManagerKpiRating", avgManagerKpiRating);
    response.put("averageModeratedKpiRating", avgModeratedKpiRating);

    response.put("averageEmployeeGoalRating", avgEmployeeGoalRating);
    response.put("averageManagerGoalRating", avgManagerGoalRating);
    response.put("averageModeratedGoalRating", avgModeratedGoalRating);

    response.put("totalAverageEmployeeRating", totalAvgEmployeeRating);
    response.put("totalAverageManagerRating", totalAvgManagerRating);
    response.put("totalAverageModeratedRating", totalAvgModeratedRating);

    // KPI Details
    List<Map<String, Object>> kpiData = kpiList.stream().map(kpi -> {
        Map<String, Object> kpiMap = new HashMap<>();
        kpiMap.put("id", kpi.getId());
        kpiMap.put("employeeSelfRating", kpi.getEmployeeSelfRating());
        kpiMap.put("reportingManagerRating", kpi.getEmployeeManagerRating());
        kpiMap.put("moderatedRating", kpi.getEmployeeModeratedFinalRating());
        kpiMap.put("kpiSetting", kpi.getEmployeeKpiSetting());
        return kpiMap;
    }).collect(Collectors.toList());

    // Goal Details
    List<Map<String, Object>> goalData = goalList.stream().map(goal -> {
        Map<String, Object> goalMap = new HashMap<>();
        goalMap.put("id", goal.getId());
        goalMap.put("employeeSelfRating", goal.getEmployeeSelfRating());
        goalMap.put("reportingManagerRating", goal.getEmployeeManagerRating());
        goalMap.put("moderatedRating", goal.getEmployeeModeratedFinalRating());
        goalMap.put("goalSetting", goal.getEmployeeGoalSetting());
        return goalMap;
    }).collect(Collectors.toList());

    response.put("kpiData", kpiData);
    response.put("goalData", goalData);

    return ResponseEntity.ok(response);
}


@PutMapping("/update-appraisal/{reportingManagerId}")
public ResponseEntity<?> updateAppraisalByManager(
        @PathVariable Long reportingManagerId,
        @RequestBody Map<String, Object> requestData) {

    List<Map<String, Object>> kpiUpdates = (List<Map<String, Object>>) requestData.get("kpiRatings");
    List<Map<String, Object>> goalUpdates = (List<Map<String, Object>>) requestData.get("goalRatings");

    // Update KPI Ratings
    for (Map<String, Object> kpiData : kpiUpdates) {
        Long kpiId = Long.valueOf(kpiData.get("id").toString());
        Double newManagerRating = Double.valueOf(kpiData.get("reportingManagerRating").toString());

        Optional<AppraisalKpi> optionalKpi = appraisalKpiRepository.findById(kpiId);
        optionalKpi.ifPresent(kpi -> {
            if (kpi.getEmployees().getId().equals(reportingManagerId)) {
                kpi.setEmployeeManagerRating(newManagerRating);
                kpi.setEmployeeModeratedFinalRating(newManagerRating);
                appraisalKpiRepository.save(kpi);
            }
        });
    }

    // Update Goal Ratings
    for (Map<String, Object> goalData : goalUpdates) {
        Long goalId = Long.valueOf(goalData.get("id").toString());
        Double newManagerRating = Double.valueOf(goalData.get("reportingManagerRating").toString());

        Optional<AppraisalGoal> optionalGoal = appraisalGoalRepository.findById(goalId);
        optionalGoal.ifPresent(goal -> {
            if (goal.getEmployees().getId().equals(reportingManagerId)) {
                goal.setEmployeeManagerRating(newManagerRating);
                goal.setEmployeeModeratedFinalRating(newManagerRating);
                appraisalGoalRepository.save(goal);
            }
        });
    }

    return ResponseEntity.ok("Appraisal ratings updated successfully by Reporting Manager ID " + reportingManagerId);
}


@PostMapping("/{employeeId}")
public String sendOtp(@PathVariable Long employeeId) {
    return appraisalService.sendOtpToEmployee(employeeId);
}

@PostMapping("/verifyOtp/{employeeId}")
public String verifyOtp(@PathVariable Long employeeId, @RequestParam String otp) {
    return appraisalService.verifyOtpAndUpdateStatus(employeeId, otp);
}


@PostMapping("/verifyOtpGoal/{employeeId}")
public String verifyOtpGoal(@PathVariable Long employeeId, @RequestParam String otp) {
    return appraisalService.verifyOtpAndUpdateGoalStatus(employeeId, otp);
}


@PostMapping("/verifyOtpAndUpdate/{employeeId}")
    public ResponseEntity<String> verifyOtpAndUpdate(@PathVariable Long employeeId, @RequestParam String otp) {
        String response = appraisalService.verifyOtpAndUpdateAppraisalStatus(employeeId, otp);
        
        if (response.contains("successfully")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }





    ///////
    /// 
    /// 
   

    
    @GetMapping("/goalbyReportingManager/{reportingManagerId}")
    public List<AppraisalGoal> getAppraisalGoalsByReportingManager(
            @PathVariable Long reportingManagerId) {
        return appraisalService.getAppraisalGoalsByReportingManager(reportingManagerId);
    }




    @GetMapping("/kpibyReportingManager/{reportingManagerId}")
    public List<AppraisalKpi> getAppraisalKpisByReportingManager(
            @PathVariable Long reportingManagerId) {
        return appraisalService.getAppraisalKpisByReportingManager(reportingManagerId);
    }


    @GetMapping("/{type}/byReportingManager/{reportingManagerId}")
    public List<?> getAppraisalsByReportingManager(
            @PathVariable String type,
            @PathVariable Long reportingManagerId) {

        if ("goal".equalsIgnoreCase(type)) {
            return appraisalService.getAppraisalsByReportingManager(reportingManagerId, AppraisalGoal.class);
        } else if ("kpi".equalsIgnoreCase(type)) {
            return appraisalService.getAppraisalsByReportingManager(reportingManagerId, AppraisalKpi.class);
        } else {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
    }





    @GetMapping("/goals/byEmployee/{employeeId}")
    public List<AppraisalGoal> getAppraisalGoalsByEmployeeId(@PathVariable Long employeeId) {
        return appraisalService.getAppraisalGoalsByEmployeeId(employeeId);
    }

    @GetMapping("/kpis/byEmployee/{employeeId}")
    public List<AppraisalKpi> getAppraisalKpisByEmployeeId(@PathVariable Long employeeId) {
        return appraisalService.getAppraisalKpisByEmployeeId(employeeId);
    }







    @PostMapping("/verifyOtpAndUpdateStatus/{employeeId}/{reportingManagerId}")
    public ResponseEntity<String> verifyOtpAndUpdateStatus(
            @PathVariable Long employeeId,
            @PathVariable Long reportingManagerId,
            @RequestParam String otp) {

        String response = appraisalService.verifyOtpAndUpdateReportingStatus(employeeId, reportingManagerId, otp);

        if (response.contains("successfully")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    

    


    @PostMapping("/sendOtp/{employeeId}/{reportingManagerId}")
public ResponseEntity<String> sendOtp(@PathVariable Long employeeId, @PathVariable Long reportingManagerId) {
    String response = appraisalService.sendOtpToManager(employeeId, reportingManagerId);
    return ResponseEntity.ok(response);
}






        @GetMapping("/trigger")
        public ResponseEntity<?> triggerAppraisalGeneration() {
            try {
                appraisalService.generateAppraisalData();
                return ResponseEntity.ok("Appraisal generation triggered successfully.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Error triggering appraisal generation: " + e.getMessage());
            }
        }

}
