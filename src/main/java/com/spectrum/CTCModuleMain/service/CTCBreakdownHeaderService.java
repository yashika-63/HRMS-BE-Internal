package com.spectrum.CTCModuleMain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spectrum.CTCModuleMain.model.CTCBreakdownHeader;
import com.spectrum.CTCModuleMain.model.CTCBreakdownRecord;
import com.spectrum.CTCModuleMain.model.StaticCTCBreakdown;
import com.spectrum.CTCModuleMain.model.VariableCTCBreakdown;
import com.spectrum.CTCModuleMain.repository.CTCBreakdownHeaderRepository;
import com.spectrum.CTCModuleMain.repository.CTCBreakdownRecordRepository;
import com.spectrum.CTCModuleMain.repository.StaticCTCBreakdownRepository;
import com.spectrum.CTCModuleMain.repository.VariableCTCBreakdownRepository;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.EmployeeConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.EmployeeConfigRepository;
import com.spectrum.Payroll.HoliDayCalender.model.EmployeeHolidayCalendar;
import com.spectrum.Payroll.HoliDayCalender.repository.CompanyDayOffConfigRepository;
import com.spectrum.Payroll.HoliDayCalender.repository.CompanyHolidayCalenderRepository;
import com.spectrum.Payroll.HoliDayCalender.repository.EmployeeHolidayCalendarRepository;
import com.spectrum.Payroll.HoliDayCalender.service.EmployeeHolidayCalendarService;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CTCBreakdownHeaderService {

    @Autowired
    private CTCBreakdownHeaderRepository ctcBreakdownHeaderRepository;

    @Autowired
    private StaticCTCBreakdownRepository staticCTCBreakdownRepository;

    @Autowired
    private VariableCTCBreakdownRepository variableCTCBreakdownRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CTCBreakdownRecordRepository ctcBreakdownRecordRepository;
    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;

    @Autowired
    private EmployeeHolidayCalendarRepository employeeHolidayCalendarRepository;
    

    @Autowired
    private EmployeeConfigRepository employeeConfigRepository;



    @Autowired
    private EmployeeHolidayCalendarService employeeHolidayCalendarService; 

    @Transactional
    public CTCBreakdownHeader saveCTCBreakdown(Long employeeId, Long companyId, CTCBreakdownHeader header,
            List<StaticCTCBreakdown> staticBreakdowns,
            List<VariableCTCBreakdown> variableBreakdowns) {

        // Find employee by ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + employeeId));

        // Find company by ID
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + companyId));

        // Set employee and company in header and save header
        header.setEmployee(employee);
        header.setCompany(company);
        CTCBreakdownHeader savedHeader = ctcBreakdownHeaderRepository.save(header);

        // Set the saved header reference for each static breakdown and save
        for (StaticCTCBreakdown staticBreakdown : staticBreakdowns) {
            staticBreakdown.setCtcBreakdownHeader(savedHeader);
            staticCTCBreakdownRepository.save(staticBreakdown);
        }

        // Set the saved header reference for each variable breakdown and save
        for (VariableCTCBreakdown variableBreakdown : variableBreakdowns) {
            variableBreakdown.setCtcBreakdownHeader(savedHeader);
            variableCTCBreakdownRepository.save(variableBreakdown);
        }

        return savedHeader;
    }




public void calculateAmountsAndCheckStatusForAllEmployees(Long companyId) {
        // Get all active CTC Breakdown headers for the specified company ID
        List<CTCBreakdownHeader> headers = ctcBreakdownHeaderRepository
                .findByCtcStatusAndEmployee_CompanyRegistration_Id(true, companyId);
    
        for (CTCBreakdownHeader header : headers) {
            // Get the static and variable breakdowns associated with this header
            List<StaticCTCBreakdown> staticBreakdowns = staticCTCBreakdownRepository
                            .findByCtcBreakdownHeaderId(header.getId());
            List<VariableCTCBreakdown> variableBreakdowns = variableCTCBreakdownRepository
                    .findByCtcBreakdownHeaderId(header.getId());
    
            // Calculate static and variable totals
            double staticTotal = staticBreakdowns.stream()
                            .mapToDouble(StaticCTCBreakdown::getAmount)
                            .sum();
            double variableTotal = variableBreakdowns.stream()
                    .mapToDouble(VariableCTCBreakdown::getAmount)
                    .sum();
    
            // Retrieve the latest EmployeeHolidayCalendar record for the employee
            EmployeeHolidayCalendar latestHolidayCalendar = employeeHolidayCalendarRepository
                    .findTopByEmployeeOrderByHolidayDateDesc(header.getEmployee());
    
            // Get the working days value from the latest record, default to 266 if no record exists
            int workingDays = (latestHolidayCalendar != null && latestHolidayCalendar.getWorkingDays() != null)
                    ? latestHolidayCalendar.getWorkingDays()
                    : 266;
    
            // Retrieve the working hours from EmployeeConfig
            EmployeeConfig employeeConfig = employeeConfigRepository
                    .findByEmployee_Id(header.getEmployee().getId())
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "No EmployeeConfig found for employee ID " + header.getEmployee().getId()));
    
            int workingHours = employeeConfig.getWorkingHours();
    
            // Ensure workingHours is valid (not zero to prevent division by zero)
            if (workingHours <= 0) {
                throw new RuntimeException("Invalid working hours for employee ID " + header.getEmployee().getId());
            }
    
            // Calculate amounts per month and per hour
            double staticAmountPerMonth = staticTotal / 12;
            double variableAmountPerMonth = variableTotal / workingDays;
            double variableAmountPerHour = variableTotal / (workingDays * workingHours);
    
            // Mark existing records for the same employee as inactive
            List<CTCBreakdownRecord> existingRecords = ctcBreakdownRecordRepository.findByEmployee_Id(header.getEmployee().getId());
            for (CTCBreakdownRecord existingRecord : existingRecords) {
                existingRecord.setStatus(false);
                ctcBreakdownRecordRepository.save(existingRecord); // Save updated status
            }
    
            // Create a new CTC Breakdown Record for the employee
            CTCBreakdownRecord record = new CTCBreakdownRecord();
            record.setStaticAmount(staticAmountPerMonth);
            record.setVariableAmount(variableAmountPerMonth);
            record.setVariableAmountPerHour(variableAmountPerHour);
            record.setDate(LocalDate.now());
            record.setEmployee(header.getEmployee());
            record.setStatus(true); // Mark the new record as active
    
            // Save the new record to the database
            ctcBreakdownRecordRepository.save(record);
    
            // Optional: Log the calculated breakdown amounts
            System.out.println("CTC Breakdown for Employee ID " + header.getEmployee().getId() + ":");
            System.out.println("Total Static Breakdown (divided by 12): " + staticAmountPerMonth);
            System.out.println("Total Variable Breakdown (divided by working days: " + workingDays + "): "
                    + variableAmountPerMonth);
            System.out.println("Variable Breakdown Per Hour (divided by working days and hours): "
                    + variableAmountPerHour);
        }
    }
    


// }

public List<CTCBreakdownHeader> findExpiringCTCs(LocalDate currentDate) {
        LocalDate thresholdDate = currentDate.plusDays(7); // For example, CTCs expiring in the next 7 days
        return ctcBreakdownHeaderRepository.findByEffectiveToDateLessThanEqual(thresholdDate);
    }

    public CTCBreakdownHeader save(CTCBreakdownHeader ctc) {
            return ctcBreakdownHeaderRepository.save(ctc);
    }















////
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 




//////////////////
/// 
/// 
/// 




// @Transactional
// public CTCBreakdownHeader saveCTCBreakdownAndGenerateRecords(Long employeeId, Long companyId, CTCBreakdownHeader header,
//         List<StaticCTCBreakdown> staticBreakdowns, List<VariableCTCBreakdown> variableBreakdowns) {

//     // Find employee by ID
//     Employee employee = employeeRepository.findById(employeeId)
//             .orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + employeeId));

//     // Find company by ID
//     CompanyRegistration company = companyRegistrationRepository.findById(companyId)
//             .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + companyId));

//     // Set employee and company in header and save header
//     header.setEmployee(employee);
//     header.setCompany(company);
//     CTCBreakdownHeader savedHeader = ctcBreakdownHeaderRepository.save(header);

//     // Set the saved header reference for each static breakdown and save
//     for (StaticCTCBreakdown staticBreakdown : staticBreakdowns) {
//         staticBreakdown.setCtcBreakdownHeader(savedHeader);
//         staticCTCBreakdownRepository.save(staticBreakdown);
//     }

//     // Set the saved header reference for each variable breakdown and save
//     for (VariableCTCBreakdown variableBreakdown : variableBreakdowns) {
//         variableBreakdown.setCtcBreakdownHeader(savedHeader);
//         variableCTCBreakdownRepository.save(variableBreakdown);
//     }

//     // Calculate static and variable totals
//     double staticTotal = staticBreakdowns.stream()
//             .mapToDouble(StaticCTCBreakdown::getAmount)
//             .sum();
//     double variableTotal = variableBreakdowns.stream()
//             .mapToDouble(VariableCTCBreakdown::getAmount)
//             .sum();

//     // Retrieve actual working days and hours
//     int actualWorkingDays;
//     int totalWorkingHours;
//     try {
//         LocalDate today = LocalDate.now();
//         int year = today.getYear();
//         int month = today.getMonthValue();

//         Map<String, Object> workingDaysAndHours = employeeHolidayCalendarService
//                 .calculateActualWorkingDaysAndHoursNews(employee, year, month);

//         actualWorkingDays = (int) workingDaysAndHours.getOrDefault("actualWorkingDays", 0);
//         totalWorkingHours = (int) workingDaysAndHours.getOrDefault("totalWorkingHours", 0);

//         if (actualWorkingDays <= 0 || totalWorkingHours <= 0) {
//             throw new RuntimeException("Invalid actual working days or total working hours");
//         }
//     } catch (Exception e) {
//         // logger.error("Error calculating working days and hours for employee ID {}: {}",
//                 // employee.getId(), e.getMessage(), e);
//         actualWorkingDays = 1; // Default to avoid division by zero
//         totalWorkingHours = 1; // Default to avoid division by zero
//     }

//     // Calculate amounts per month and per hour using updated logic
//     double staticAmountPerMonth = staticTotal / 12;
//     double variableAmountPerMonth = (variableTotal / 12) / actualWorkingDays;
//     double variableAmountPerHour = (variableTotal / 12) / (totalWorkingHours);
// System.out.println("totalWorkingHours = "+ totalWorkingHours);
//     // Create a new CTC Breakdown Record for the employee
//     CTCBreakdownRecord record = new CTCBreakdownRecord();
//     record.setStaticAmount(staticAmountPerMonth);
//     record.setVariableAmount(variableAmountPerMonth);
//     record.setVariableAmountPerHour(variableAmountPerHour);
//     record.setDate(LocalDate.now());
//     record.setEmployee(employee);

//     // Save the record to the database
//     ctcBreakdownRecordRepository.save(record);

//     return savedHeader;
// }
@Transactional
public CTCBreakdownHeader saveCTCBreakdownAndGenerateRecords(Long employeeId, Long companyId, CTCBreakdownHeader header,
        List<StaticCTCBreakdown> staticBreakdowns, List<VariableCTCBreakdown> variableBreakdowns) {

    // Find employee by ID
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + employeeId));

    // Find company by ID
    CompanyRegistration company = companyRegistrationRepository.findById(companyId)
            .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + companyId));

    // Check if a CTCBreakdownHeader exists for this employee and set ctcStatus to false
    List<CTCBreakdownHeader> existingHeaders = ctcBreakdownHeaderRepository.findByEmployeeId(employeeId);
    if (!existingHeaders.isEmpty()) {
        for (CTCBreakdownHeader existingHeader : existingHeaders) {
            existingHeader.setCtcStatus(false);
            ctcBreakdownHeaderRepository.save(existingHeader);
        }
    }

    // Set employee and company in header and save header
    header.setEmployee(employee);
    header.setCompany(company);
    CTCBreakdownHeader savedHeader = ctcBreakdownHeaderRepository.save(header);

    // Set the saved header reference for each static breakdown and save
    for (StaticCTCBreakdown staticBreakdown : staticBreakdowns) {
        staticBreakdown.setCtcBreakdownHeader(savedHeader);
        staticCTCBreakdownRepository.save(staticBreakdown);
    }

    // Set the saved header reference for each variable breakdown and save
    for (VariableCTCBreakdown variableBreakdown : variableBreakdowns) {
        variableBreakdown.setCtcBreakdownHeader(savedHeader);
        variableCTCBreakdownRepository.save(variableBreakdown);
    }

    // Calculate static and variable totals
    double staticTotal = staticBreakdowns.stream()
            .mapToDouble(StaticCTCBreakdown::getAmount)
            .sum();
    double variableTotal = variableBreakdowns.stream()
            .mapToDouble(VariableCTCBreakdown::getAmount)
            .sum();

    // Retrieve actual working days and hours
    int actualWorkingDays;
    int totalWorkingHours;
    try {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();

        Map<String, Object> workingDaysAndHours = employeeHolidayCalendarService
                .calculateActualWorkingDaysAndHoursNews(employee, year, month);

        actualWorkingDays = (int) workingDaysAndHours.getOrDefault("actualWorkingDays", 0);
        totalWorkingHours = (int) workingDaysAndHours.getOrDefault("totalWorkingHours", 0);

        if (actualWorkingDays <= 0 || totalWorkingHours <= 0) {
            throw new RuntimeException("Invalid actual working days or total working hours");
        }
    } catch (Exception e) {
        actualWorkingDays = 1; // Default to avoid division by zero
        totalWorkingHours = 1; // Default to avoid division by zero
    }

    // Calculate amounts per month and per hour using updated logic
    double staticAmountPerMonth = staticTotal / 12;
    double variableAmountPerMonth = (variableTotal / 12) / actualWorkingDays;
    double variableAmountPerHour = (variableTotal / 12) / totalWorkingHours;

    // Create a new CTC Breakdown Record for the employee
    CTCBreakdownRecord record = new CTCBreakdownRecord();
    record.setStaticAmount(staticAmountPerMonth);
    record.setVariableAmount(variableAmountPerMonth);
    record.setVariableAmountPerHour(variableAmountPerHour);
    record.setDate(LocalDate.now());
    record.setEmployee(employee);

    // Save the record to the database
    ctcBreakdownRecordRepository.save(record);

    return savedHeader;
}


//////////////////
/// 
/// 
/// 
/// 
/// 
/// 






   // Service method to delete a specific CTC breakdown record by its ID if ctcStatus is false
   public boolean deleteCTCBreakdownById(Long ctcBreakdownHeaderId) {
        // Check if the record exists and has ctcStatus=false
        boolean exists = ctcBreakdownHeaderRepository.existsByIdAndCtcStatusFalse(ctcBreakdownHeaderId);

        // Proceed with deletion only if such a record exists
        if (exists) {
            ctcBreakdownHeaderRepository.deleteById(ctcBreakdownHeaderId);
            return true;
        }
        // Return false if no matching record was found
        return false;
    }







     // 1. Get records by companyId where ctcStatus = true
     public List<CTCBreakdownHeader> getByCompanyIdAndCtcStatusTrue(Long companyId) {
        return ctcBreakdownHeaderRepository.findByCompanyIdAndCtcStatusTrue(companyId);
    }

    // Get records by employeeId where ctcStatus = true
        public Optional<CTCBreakdownHeader> getByEmployeeIdAndCtcStatusTrue(Long employeeId) {
        return ctcBreakdownHeaderRepository.findByEmployeeIdAndCtcStatusTrue(employeeId);
    }
    
    // 3. Get records by employeeId where ctcStatus = false
    public List<CTCBreakdownHeader> getByEmployeeIdAndCtcStatusFalse(Long employeeId) {
        return ctcBreakdownHeaderRepository.findByEmployeeIdAndCtcStatusFalse(employeeId);
    }

    @Transactional
    public List<CTCBreakdownRecord> createCTCRecordsForAllEmployees(Long companyId) {
        // Find the company by ID
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + companyId));
    
        // Retrieve all CTCBreakdownHeaders for the company with ctcStatus = true
        List<CTCBreakdownHeader> headers = ctcBreakdownHeaderRepository.findByCompany(company).stream()
                .filter(CTCBreakdownHeader::isCtcStatus)
                .toList();
    
        if (headers.isEmpty()) {
            throw new IllegalArgumentException("No active CTC Headers found for the specified company");
        }
    
        List<CTCBreakdownRecord> newRecords = new ArrayList<>();
    
        for (CTCBreakdownHeader header : headers) {
            // Set status to false for all existing records with status = true for the employee
            List<CTCBreakdownRecord> existingRecords = ctcBreakdownRecordRepository.findByEmployeeAndStatusTrue(header.getEmployee());
            for (CTCBreakdownRecord record : existingRecords) {
                record.setStatus(false);
                ctcBreakdownRecordRepository.save(record);
            }
    
            // Calculate static and variable totals
            double staticTotal = header.getStaticCTCBreakdowns().stream()
                    .mapToDouble(StaticCTCBreakdown::getAmount)
                    .sum();
            double variableTotal = header.getVariableCTCBreakdowns().stream()
                    .mapToDouble(VariableCTCBreakdown::getAmount)
                    .sum();
    
            // Retrieve actual working days and hours
            int actualWorkingDays;
            int totalWorkingHours;
            try {
                LocalDate today = LocalDate.now();
                int year = today.getYear();
                int month = today.getMonthValue();
                Map<String, Object> workingDaysAndHours = employeeHolidayCalendarService
                        .calculateActualWorkingDaysAndHoursNews(header.getEmployee(), year, month);
                actualWorkingDays = (int) workingDaysAndHours.getOrDefault("actualWorkingDays", 0);
                totalWorkingHours = (int) workingDaysAndHours.getOrDefault("totalWorkingHours", 0);
    
                if (actualWorkingDays <= 0 || totalWorkingHours <= 0) {
                    throw new RuntimeException("Invalid actual working days or total working hours");
                }
            } catch (Exception e) {
                actualWorkingDays = 1; // Default to avoid division by zero
                totalWorkingHours = 1; // Default to avoid division by zero
            }
    
            // Calculate amounts per month and per hour using updated logic
            double staticAmountPerMonth = staticTotal / 12;
            double variableAmountPerMonth = (variableTotal / 12) / actualWorkingDays;
            double variableAmountPerHour = (variableTotal / 12) / totalWorkingHours;
    
            // Create a new CTC Breakdown Record
            CTCBreakdownRecord newRecord = new CTCBreakdownRecord();
            newRecord.setStaticAmount(staticAmountPerMonth);
            newRecord.setVariableAmount(variableAmountPerMonth);
            newRecord.setVariableAmountPerHour(variableAmountPerHour);
            newRecord.setDate(LocalDate.now());
            newRecord.setEmployee(header.getEmployee());
            newRecord.setStatus(true); // Mark new record as active
    
            // Save the new record to the database
            newRecords.add(ctcBreakdownRecordRepository.save(newRecord));
        }
    
        return newRecords;
    }
    










    @Transactional
public List<CTCBreakdownRecord> createCTCRecordsForSpecificMonthAndYear(Long companyId, int month, int year) {
    // Find the company by ID
    CompanyRegistration company = companyRegistrationRepository.findById(companyId)
            .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + companyId));

    // Retrieve all CTCBreakdownHeaders for the company with ctcStatus = true
    List<CTCBreakdownHeader> headers = ctcBreakdownHeaderRepository.findByCompany(company).stream()
            .filter(header -> header.isCtcStatus() && isHeaderInMonthYear(header, month, year))
            .toList();

    if (headers.isEmpty()) {
        throw new IllegalArgumentException("No active CTC Headers found for the specified company and date range");
    }

    List<CTCBreakdownRecord> newRecords = new ArrayList<>();

    for (CTCBreakdownHeader header : headers) {
        // Set status to false for all existing records with status = true for the employee
        List<CTCBreakdownRecord> existingRecords = ctcBreakdownRecordRepository.findByEmployeeAndStatusTrue(header.getEmployee());
        for (CTCBreakdownRecord record : existingRecords) {
            record.setStatus(false);
            ctcBreakdownRecordRepository.save(record);
        }

        // Calculate static and variable totals
        double staticTotal = header.getStaticCTCBreakdowns().stream()
                .mapToDouble(StaticCTCBreakdown::getAmount)
                .sum();
        double variableTotal = header.getVariableCTCBreakdowns().stream()
                .mapToDouble(VariableCTCBreakdown::getAmount)
                .sum();

        // Retrieve actual working days and hours
        int actualWorkingDays;
        int totalWorkingHours;
        try {
            Map<String, Object> workingDaysAndHours = employeeHolidayCalendarService
                    .calculateActualWorkingDaysAndHoursNews(header.getEmployee(), year, month);
            actualWorkingDays = (int) workingDaysAndHours.getOrDefault("actualWorkingDays", 0);
            totalWorkingHours = (int) workingDaysAndHours.getOrDefault("totalWorkingHours", 0);

            if (actualWorkingDays <= 0 || totalWorkingHours <= 0) {
                throw new RuntimeException("Invalid actual working days or total working hours");
            }
        } catch (Exception e) {
            actualWorkingDays = 1; // Default to avoid division by zero
            totalWorkingHours = 1; // Default to avoid division by zero
        }

        // Calculate amounts per month and per hour using updated logic
        double staticAmountPerMonth = staticTotal / 12;
        double variableAmountPerMonth = (variableTotal / 12) / actualWorkingDays;
        double variableAmountPerHour = (variableTotal / 12) / totalWorkingHours;

        // Create a new CTC Breakdown Record
        CTCBreakdownRecord newRecord = new CTCBreakdownRecord();
        newRecord.setStaticAmount(staticAmountPerMonth);
        newRecord.setVariableAmount(variableAmountPerMonth);
        newRecord.setVariableAmountPerHour(variableAmountPerHour);
        newRecord.setDate(LocalDate.of(year, month, 1)); // Set date to the start of the specific month
        newRecord.setEmployee(header.getEmployee());
        newRecord.setStatus(true); // Mark new record as active

        // Save the new record to the database
        newRecords.add(ctcBreakdownRecordRepository.save(newRecord));
    }

    return newRecords;
}

// Helper method to check if a header falls within a specific month and year
private boolean isHeaderInMonthYear(CTCBreakdownHeader header, int month, int year) {
    LocalDate effectiveStartDate = header.getEffectiveFromDate();
    LocalDate effectiveEndDate = header.getEffectiveToDate();
    LocalDate startOfMonth = LocalDate.of(year, month, 1);
    LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

    // Check if the effective date range overlaps with the specified month and year
    return (effectiveStartDate.isBefore(endOfMonth) || effectiveStartDate.isEqual(endOfMonth)) &&
           (effectiveEndDate.isAfter(startOfMonth) || effectiveEndDate.isEqual(startOfMonth));
}


}
