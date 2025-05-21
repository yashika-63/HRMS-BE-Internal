package com.spectrum.Payroll.HoliDayCalender.controller;

import com.spectrum.Payroll.HoliDayCalender.model.EmployeeHolidayCalendar;
import com.spectrum.Payroll.HoliDayCalender.service.EmployeeHolidayCalendarService;
import com.spectrum.Payroll.PayrollHours.model.PayrollHours;
import com.spectrum.Payroll.PayrollHours.repository.PayrollHoursRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/holidayCalendar")
public class EmployeeHolidayCalendarController {

    @Autowired
    private EmployeeHolidayCalendarService holidayCalendarService;

    @Autowired
    private EmployeeRepository employeeRepository; // Inject the Employee repository
@Autowired
private PayrollHoursRepository payrollHoursRepository;
    
    // Save multiple EmployeeHolidayCalendar records by employeeId passed in the URL
    @PostMapping("/saveMultiple/{employeeId}")
    public List<EmployeeHolidayCalendar> saveMultipleHolidays(
            @PathVariable Long employeeId, // Get employeeId from the URL
            @RequestBody List<EmployeeHolidayCalendar> holidayList) {
    
        // Retrieve the employee from the database using the employeeId from the URL
        Optional<Employee> employee = employeeRepository.findById(employeeId);
    
        // Check if the employee exists, else throw an exception
        if (!employee.isPresent()) {
            throw new RuntimeException("Employee with id " + employeeId + " not found.");
        }
    
        // Set the employee for each holiday record
        holidayList.forEach(holiday -> holiday.setEmployee(employee.get()));
    
        // Bulk save holidays and return the saved list
        return holidayCalendarService.saveAllHolidays(holidayList);
    }
    
    // Get holidays by Employee ID
    @GetMapping("/getByEmployee/{employeeId}")
    public List<EmployeeHolidayCalendar> getHolidaysByEmployeeId(@PathVariable Long employeeId) {
        return holidayCalendarService.getHolidaysByEmployeeId(employeeId);
    }

    // Delete holiday by ID
    @DeleteMapping("/delete/{id}")
    public void deleteHolidayById(@PathVariable Long id) {
        holidayCalendarService.deleteHolidayById(id);
    }


    // @PostMapping("/saveAndCalculate/{employeeId}/{year}")
    // public List<EmployeeHolidayCalendar> saveAndCalculateWorkingDays(
    //         @PathVariable Long employeeId,
    //         @PathVariable int year,
    //         @RequestBody List<EmployeeHolidayCalendar> holidayList) {
        
    //     Optional<Employee> employee = employeeRepository.findById(employeeId);
    //     if (!employee.isPresent()) {
    //         throw new RuntimeException("Employee with id " + employeeId + " not found.");
    //     }

    //     // Set employee in each holiday record
    //     for (EmployeeHolidayCalendar holiday : holidayList) {
    //         holiday.setEmployee(employee.get());
    //     }
    //     // Calculate and save working days
    //     return holidayCalendarService.saveHolidaysAndCalculateWorkingDays(employee.get(), holidayList, year);
        
    // }

//     @GetMapping("/calculateWorkingDays/{employeeId}")
// public ResponseEntity<?> calculateActualWorkingDays(@PathVariable Long employeeId) {
//     // Get the current date
//     LocalDate currentDate = LocalDate.now();
//     int year = currentDate.getYear();
//     int month = currentDate.getMonthValue();

//     Optional<Employee> employee = employeeRepository.findById(employeeId);
//     if (!employee.isPresent()) {
//         return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                 .body("Employee with ID " + employeeId + " not found.");
//     }

//     int workingDays = holidayCalendarService.calculateActualWorkingDays(employee.get(), year, month);
//     return ResponseEntity.ok(Map.of(
//             "employeeId", employeeId,
//             "year", year,
//             "month", month,
//             "workingDays", workingDays
//     ));
// }


// @GetMapping("/calculateWorkingDaysAndHours/{employeeId}")
// public ResponseEntity<?> calculateActualWorkingDaysAndHours(@PathVariable Long employeeId) {
//     LocalDate currentDate = LocalDate.now();
//     int year = currentDate.getYear();
//     int month = currentDate.getMonthValue();

//     Optional<Employee> employee = employeeRepository.findById(employeeId);
//     if (!employee.isPresent()) {
//         return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                 .body("Employee with ID " + employeeId + " not found.");
//     }

//     Map<String, Object> result = holidayCalendarService.calculateActualWorkingDaysAndHours(employee.get(), year, month);
//     return ResponseEntity.ok(result);
// }




////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ///////
    // @PostMapping("/saveAndCalculate/{employeeId}/{year}")
    // public List<EmployeeHolidayCalendar> saveAndCalculateWorkingDays(
    //         @PathVariable Long employeeId,
    //         @PathVariable int year,
    //         @RequestBody List<EmployeeHolidayCalendar> holidayList) {
        
    //     Optional<Employee> employee = employeeRepository.findById(employeeId);
    //     if (!employee.isPresent()) {
    //         throw new RuntimeException("Employee with id " + employeeId + " not found.");
    //     }

    //     // Set employee in each holiday record
    //     for (EmployeeHolidayCalendar holiday : holidayList) {
    //         holiday.setEmployee(employee.get());
    //     }
    //     // Calculate and save working days
    //     return holidayCalendarService.saveHolidaysAndCalculateWorkingDays(employee.get(), holidayList, year);
        
    // }

    @GetMapping("/calculateWorkingDays/{employeeId}")
public ResponseEntity<?> calculateActualWorkingDays(@PathVariable Long employeeId) {
    // Get the current date
    LocalDate currentDate = LocalDate.now();
    int year = currentDate.getYear();
    int month = currentDate.getMonthValue();

    Optional<Employee> employee = employeeRepository.findById(employeeId);
    if (!employee.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Employee with ID " + employeeId + " not found.");
    }

    int workingDays = holidayCalendarService.calculateActualWorkingDays(employee.get(), year, month);
    return ResponseEntity.ok(Map.of(
            "employeeId", employeeId,
            "year", year,
            "month", month,
            "workingDays", workingDays
    ));
}


// @GetMapping("/calculateWorkingDaysAndHours/{employeeId}")
// public ResponseEntity<?> calculateActualWorkingDaysAndHours(@PathVariable Long employeeId) {
//     LocalDate currentDate = LocalDate.now();
//     int year = currentDate.getYear();
//     int month = currentDate.getMonthValue();

//     Optional<Employee> employee = employeeRepository.findById(employeeId);
//     if (!employee.isPresent()) {
//         return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                 .body("Employee with ID " + employeeId + " not found.");
//     }

//     Map<String, Object> result = holidayCalendarService.calculateActualWorkingDaysAndHours(employee.get(), year, month);
//     return ResponseEntity.ok(result);
// }




////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ///////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////
/// 
/// 
/// 
/// 


// @PostMapping("/saveAndCalculate/{employeeId}/{year}")
// public List<EmployeeHolidayCalendar> saveAndCalculateWorkingDays(
//         @PathVariable Long employeeId,
//         @PathVariable int year,
//         @RequestBody List<EmployeeHolidayCalendar> holidayList) {
    
//     Optional<Employee> employee = employeeRepository.findById(employeeId);
//     if (!employee.isPresent()) {
//         throw new RuntimeException("Employee with id " + employeeId + " not found.");
//     }
@PostMapping("/saveAndCalculate/{employeeId}/{year}")
public List<EmployeeHolidayCalendar> saveAndCalculateWorkingDays(
        @PathVariable Long employeeId,
        @PathVariable int year,
        @RequestBody List<EmployeeHolidayCalendar> holidayList) {
    
    Optional<Employee> employee = employeeRepository.findById(employeeId);
    if (!employee.isPresent()) {
        throw new RuntimeException("Employee with id " + employeeId + " not found.");
    }

    // Set employee in each holiday record
    for (EmployeeHolidayCalendar holiday : holidayList) {
        holiday.setEmployee(employee.get());
    }
    // Calculate and save working days
    return holidayCalendarService.saveHolidaysAndCalculateWorkingDaysNewn(employee.get(), holidayList, year);
    
}



@GetMapping("/calculateWorkingDaysAndHours/{employeeId}")
public ResponseEntity<?> calculateActualWorkingDaysAndHours(@PathVariable Long employeeId) {
    LocalDate currentDate = LocalDate.now();
    int year = currentDate.getYear();
    int month = currentDate.getMonthValue();

    Optional<Employee> employee = employeeRepository.findById(employeeId);
    if (!employee.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Employee with ID " + employeeId + " not found.");
    }

    Map<String, Object> result = holidayCalendarService.calculateActualWorkingDaysAndHoursNews(employee.get(), year, month);
    return ResponseEntity.ok(result);
}

@GetMapping("/calculateWorkingDaysAndHoursByDates/{employeeId}/{startDate}/{endDate}")
public ResponseEntity<?> calculateWorkingDaysAndHoursBetweenDates(
        @PathVariable Long employeeId,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    Optional<Employee> employee = employeeRepository.findById(employeeId);
    if (!employee.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Employee with ID " + employeeId + " not found.");
    }

    Map<String, Object> result = holidayCalendarService.calculateWorkingDaysAndHoursBetweenDates(
            employee.get(), startDate, endDate);
    return ResponseEntity.ok(result);
}


 @GetMapping("/leaves/count")
    public int getTotalLeaves(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return holidayCalendarService.calculateTotalLeaves(employeeId, fromDate, toDate);
    }





    @GetMapping("/holidays/{employeeId}")
public int getTotalHolidays(
    @PathVariable Long employeeId,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
    return holidayCalendarService.calculateTotalHolidays(employeeId, fromDate, toDate);
}


@GetMapping("/working-days/total/{employeeId}")
    public ResponseEntity<Integer> getTotalWorkingDays(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        try {
            // Call the service method to calculate total working days
            int totalWorkingDays = holidayCalendarService.calculateTotalWorkingDays(employeeId, fromDate, toDate);

            // Return the response
            return ResponseEntity.ok(totalWorkingDays);
        } catch (IllegalArgumentException ex) {
            // Handle cases where employee configuration or data is missing
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        } catch (Exception ex) {
            // Handle other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }
    }




    @DeleteMapping("/deleteByEmployee/{employeeId}")
    public ResponseEntity<String> deleteByEmployeeId(@PathVariable Long employeeId) {
        try {
            holidayCalendarService.deleteAllByEmployeeId(employeeId);
            return ResponseEntity.ok("Employee holiday records deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting employee holiday records: " + e.getMessage());
        }
    }



    @GetMapping("/calculateWorkingDaysAndHours/{employeeId}/{year}/{month}")
    public ResponseEntity<?> calculateActualWorkingDaysAndHoursByYearAndMonth(
            @PathVariable Long employeeId,
            @PathVariable int year,
            @PathVariable int month) {
        // Validate the month and year
        if (month < 1 || month > 12) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid month value: " + month + ". Month should be between 1 and 12.");
        }
    
        if (year < 1900 || year > LocalDate.now().getYear()) { // Adjust year range as needed
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid year value: " + year + ". Year should be a valid past or current year.");
        }
    
        // Check if the employee exists
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (!employee.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee with ID " + employeeId + " not found.");
        }
    
        // Calculate working days and hours
        Map<String, Object> result = holidayCalendarService.calculateActualWorkingDaysAndHoursNews(employee.get(), year, month);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/calculateApprovedLeaves/{employeeId}/{year}/{month}")
public ResponseEntity<?> calculateApprovedLeaves(
        @PathVariable Long employeeId,
        @PathVariable int year,
        @PathVariable int month) {
    try {
        // Validate the month and year
        if (month < 1 || month > 12) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid month value: " + month + ". Month should be between 1 and 12.");
        }

        if (year < 1900 || year > LocalDate.now().getYear()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid year value: " + year + ". Year should be a valid past or current year.");
        }

        // Check if the employee exists
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (!employee.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee with ID " + employeeId + " not found.");
        }

        // Calculate approved leaves
        int approvedLeavesCount = holidayCalendarService.calculateApprovedLeaves(employee.get(), year, month);
        Map<String, Object> result = new HashMap<>();
        result.put("employeeId", employeeId);
        result.put("year", year);
        result.put("month", month);
        result.put("approvedLeavesCount", approvedLeavesCount);

        return ResponseEntity.ok(result);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An internal server error occurred: " + e.getMessage());
    }
}

    













//////////////
/// 
/// 
/// 
/// 
/// 



@PostMapping("/savePayrollHours/{companyId}/{year}/{month}")
public ResponseEntity<?> calculateAndSavePayrollHours(
        @PathVariable Long companyId,
        @PathVariable int year,
        @PathVariable int month) {
    // Validate the month and year
    if (month < 1 || month > 12) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid month value: " + month + ". Month should be between 1 and 12.");
    }

    if (year < 1900 || year > LocalDate.now().getYear()) { // Adjust year range as needed
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid year value: " + year + ". Year should be a valid past or current year.");
    }

    // Fetch all employees for the given company
    Optional<List<Employee>> optionalEmployees = employeeRepository.findByCompanyRegistration_Id(companyId);
    if (optionalEmployees.isEmpty() || optionalEmployees.get().isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No employees found for company ID: " + companyId);
    }
    List<Employee> employees = optionalEmployees.get();

    List<PayrollHours> payrollHoursList = new ArrayList<>();
    for (Employee employee : employees) {
        try {
            // Calculate actual working days and hours for each employee
            Map<String, Object> result = holidayCalendarService.calculateActualWorkingDaysAndHoursNews(employee, year, month);

            int assignHours = (int) result.get("totalWorkingHours");

            // Create and save PayrollHours entry for the employee
            PayrollHours payrollHours = new PayrollHours();
            payrollHours.setEmployee(employee);
            payrollHours.setAssignHours(assignHours);
            payrollHours.setExpectedHours(0); // Initialize to 0
            payrollHours.setActualHours(0); // Initialize to 0
            payrollHours.setStatus(true); // Default status
            payrollHours.setDate(LocalDate.now()); // Current date

            payrollHoursList.add(payrollHours);
        } catch (Exception e) {
            // Log the exception for the specific employee but continue processing others
            // logger.error("Error calculating payroll hours for employee ID: {}", employee.getId(), e);
        }
    }

    // Save all payroll hours to the database
    payrollHoursRepository.saveAll(payrollHoursList);

    return ResponseEntity.ok("Payroll hours saved successfully for " + payrollHoursList.size() + " employees.");
}


}              
