package com.spectrum.Payroll.HoliDayCalender.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spectrum.Payroll.HoliDayCalender.model.CompanyDayOffConfig;
import com.spectrum.Payroll.HoliDayCalender.service.CompanyDayOffConfigService;
import com.spectrum.repository.CompanyRegistrationRepository;

@RestController
@RequestMapping("/api/companyDayOffConfig")
public class CompanyDayOffConfigController {

    @Autowired
    private CompanyDayOffConfigService companyDayOffConfigService;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    

    @Autowired
    public CompanyDayOffConfigController(CompanyDayOffConfigService companyDayOffConfigService) {
        this.companyDayOffConfigService = companyDayOffConfigService;
    }

    @PostMapping("/save-multiple/{companyId}")
    public ResponseEntity<String> saveMultipleCompanyDayOffConfigs(
            @PathVariable Long companyId,
            @RequestBody List<CompanyDayOffConfig> companyDayOffConfigs) {
        try {
            companyDayOffConfigService.saveMultiple(companyId, companyDayOffConfigs);
            return ResponseEntity.ok("Company Day Off Configurations saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error saving configurations: " + e.getMessage());
        }
    }




    // Get all Day Off Configs by Company ID
    @GetMapping("/getByCompanyId/{companyId}")
    public ResponseEntity<List<CompanyDayOffConfig>> getByCompanyId(@PathVariable Long companyId) {
        List<CompanyDayOffConfig> dayOffConfigs = companyDayOffConfigService.findByCompanyId(companyId);
        return ResponseEntity.ok(dayOffConfigs);
    }

    // Delete Day Off Config by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            companyDayOffConfigService.deleteById(id);
            return ResponseEntity.ok("Day Off Configuration deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error deleting configuration: " + e.getMessage());
        }
    }




    @GetMapping("/getUniqueHolidayDates/{companyId}")
public ResponseEntity<List<LocalDate>> getUniqueHolidayDates(@PathVariable Long companyId) {
    List<LocalDate> uniqueHolidayDates = companyDayOffConfigService.findDistinctHolidayDatesByCompanyId(companyId);
    return ResponseEntity.ok(uniqueHolidayDates);
}



@DeleteMapping("/deleteByCategoryAndCompany/{companyId}/{workCategoryCode}")
public ResponseEntity<String> deleteByCategoryAndCompany(
        @PathVariable Long companyId,
        @PathVariable int workCategoryCode) {
    try {
        companyDayOffConfigService.deleteByCategoryCodeAndCompanyId(workCategoryCode, companyId);
        return ResponseEntity.ok("Day Off Configurations deleted successfully for the given category and company!");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Error deleting configurations: " + e.getMessage());
    }
}


@GetMapping("/getByCompanyAndCategory/{companyId}/{workCategoryCode}")
public ResponseEntity<List<CompanyDayOffConfig>> getByCompanyAndCategory(
        @PathVariable Long companyId,
        @PathVariable int workCategoryCode) {
    try {
        List<CompanyDayOffConfig> configs =
                companyDayOffConfigService.findByCompanyIdAndCategoryCode(companyId, workCategoryCode);
        return ResponseEntity.ok(configs);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(null);
    }
}

}
