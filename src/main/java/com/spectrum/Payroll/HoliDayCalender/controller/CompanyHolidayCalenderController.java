package com.spectrum.Payroll.HoliDayCalender.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.Payroll.HoliDayCalender.model.CompanyHolidayCalender;
import com.spectrum.Payroll.HoliDayCalender.service.CompanyHolidayCalenderService;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;

@RestController
@RequestMapping("/api/companyHolidayCalendar")
public class CompanyHolidayCalenderController {

    @Autowired
    private CompanyHolidayCalenderService companyHolidayCalenderService;

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    // Save multiple CompanyHolidayCalender records by companyId passed in the URL
    @PostMapping("/saveMultiple/{companyId}")
    public ResponseEntity<?> saveMultipleHolidays(
            @PathVariable Long companyId,
            @RequestBody List<CompanyHolidayCalender> holidayList) {
        
        // Retrieve the company from the database using the companyId from the URL
        Optional<CompanyRegistration> company = companyRegistrationRepository.findById(companyId);

        if (!company.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company with id " + companyId + " not found.");
        }

        // Set the company for each holiday record
        for (CompanyHolidayCalender holiday : holidayList) {
            holiday.setCompany(company.get()); // Set the company in each holiday record
        }

        // Bulk save holidays
        List<CompanyHolidayCalender> savedHolidays = companyHolidayCalenderService.saveAllHolidays(holidayList);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHolidays);
    }

    // Get all holidays by companyId
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CompanyHolidayCalender>> getHolidaysByCompanyId(@PathVariable Long companyId) {
        List<CompanyHolidayCalender> holidays = companyHolidayCalenderService.getHolidaysByCompanyId(companyId);
        return ResponseEntity.ok(holidays);
    }

    // Delete holiday by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHolidayById(@PathVariable Long id) {
        boolean deleted = companyHolidayCalenderService.deleteHolidayById(id);
        if (deleted) {
            return ResponseEntity.ok("Holiday deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Holiday not found.");
        }
    }



    @GetMapping("/company/{companyId}/workStateCode/{workStateCode}")
    public ResponseEntity<?> getHolidaysByCompanyIdAndWorkStateCode(
            @PathVariable Long companyId, 
            @PathVariable int workStateCode) {
        List<CompanyHolidayCalender> holidays = companyHolidayCalenderService.getHolidaysByCompanyIdAndWorkStateCode(companyId, workStateCode);
        if (holidays.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No holidays found for companyId " + companyId + " and workStateCode " + workStateCode);
        }
        return ResponseEntity.ok(holidays);
    }
}
