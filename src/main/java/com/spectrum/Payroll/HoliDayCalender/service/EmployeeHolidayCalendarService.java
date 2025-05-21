package com.spectrum.Payroll.HoliDayCalender.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.model.EmployeeConfig;
import com.spectrum.CompanyEmployeeConfig.EmployeeConfig.repository.EmployeeConfigRepository;
import com.spectrum.Payroll.HoliDayCalender.model.CompanyDayOffConfig;

import com.spectrum.Payroll.HoliDayCalender.model.CompanyHolidayCalender;
import com.spectrum.Payroll.HoliDayCalender.model.EmployeeHolidayCalendar;
import com.spectrum.Payroll.HoliDayCalender.repository.CompanyDayOffConfigRepository;
import com.spectrum.Payroll.HoliDayCalender.repository.CompanyHolidayCalenderRepository;
import com.spectrum.Payroll.HoliDayCalender.repository.EmployeeHolidayCalendarRepository;
import com.spectrum.model.Employee;
import com.spectrum.workflow.model.LeaveApplication;
import com.spectrum.workflow.repository.LeaveApplicationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.HashMap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.Set;

@Service
public class EmployeeHolidayCalendarService {


        private static final Logger logger = LoggerFactory.getLogger(EmployeeHolidayCalendarService.class);

    @Autowired
    private EmployeeHolidayCalendarRepository holidayCalendarRepository;

    @Autowired
    private EmployeeConfigRepository employeeConfigRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;


     @Autowired
    private CompanyHolidayCalenderRepository companyHolidayCalenderRepository;

    @Autowired
    private CompanyDayOffConfigRepository companyDayOffConfigRepository;
    


    
    // Save all holidays in bulk
    public List<EmployeeHolidayCalendar> saveAllHolidays(List<EmployeeHolidayCalendar> holidayList) {
        return holidayCalendarRepository.saveAll(holidayList);
    }

    // Get holidays by Employee ID
    public List<EmployeeHolidayCalendar> getHolidaysByEmployeeId(Long employeeId) {
        return holidayCalendarRepository.findByEmployeeId(employeeId);
    }

    // Delete holiday by ID
    public void deleteHolidayById(Long id) {
        holidayCalendarRepository.deleteById(id);
    }




    public List<EmployeeHolidayCalendar> saveHolidaysAndCalculateWorkingDays(
        Employee employee, List<EmployeeHolidayCalendar> holidayList, int year) {

    logger.info("Calculating working days for employee ID: {} in year: {}", employee.getId(), year);
    
    // Total days in the specified year
    int totalDaysInYear = Year.isLeap(year) ? 366 : 365;
    logger.debug("Total days in year {}: {}", year, totalDaysInYear);

    // Get the count of employee-specific holidays
    int employeeHolidayCount = holidayList.size();
    logger.info("Employee-specific holiday count: {}", employeeHolidayCount);

    // Get the count of company holidays for the specified year and company
    List<CompanyHolidayCalender> companyHolidays = companyHolidayCalenderRepository
            .findByCompanyId(employee.getCompanyRegistration().getId());
    int companyHolidayCount = (int) companyHolidays.stream()
            .filter(holiday -> holiday.getHolidayDate().getYear() == year)
            .count();
    logger.info("Company holiday count for year {}: {}", year, companyHolidayCount);

    // Merge the employee-specific holidays and company holidays, avoiding duplicates
    Set<LocalDate> allHolidayDates = new HashSet<>();
    
    // Add employee-specific holidays
    for (EmployeeHolidayCalendar holiday : holidayList) {
        allHolidayDates.add(holiday.getHolidayDate());
    }

    // Add company holidays (only those in the specified year)
    for (CompanyHolidayCalender companyHoliday : companyHolidays) {
        if (companyHoliday.getHolidayDate().getYear() == year) {
            allHolidayDates.add(companyHoliday.getHolidayDate());
        }
    }

    // Calculate the total working days
    int totalHolidayCount = allHolidayDates.size();
    int workingDays = totalDaysInYear - totalHolidayCount;
    logger.info("Calculated working days for employee ID {} in year {}: {}", employee.getId(), year, workingDays);

    // Set the calculated working days in each EmployeeHolidayCalendar record
    for (EmployeeHolidayCalendar holiday : holidayList) {
        holiday.setWorkingDays(workingDays);
    }

    // Save the holiday records with the updated working days
    logger.info("Saving updated holiday records with calculated working days.");
    return holidayCalendarRepository.saveAll(holidayList);


}

// public int calculateWorkingDaysForMonth(Long employeeId, Long companyId, int
// year, int month) {
// // Get the first and last days of the specified month
// YearMonth yearMonth = YearMonth.of(year, month);
// LocalDate startDate = yearMonth.atDay(1);
// LocalDate endDate = yearMonth.atEndOfMonth();

// // Fetch employee-specific holidays in the given month
// List<EmployeeHolidayCalendar> employeeHolidays =
// holidayCalendarRepository.findByEmployeeIdAndHolidayDateBetween(employeeId,
// startDate, endDate);

// // Fetch company holidays for the given month
// List<CompanyHolidayCalender> companyHolidays =
// companyHolidayCalenderRepository.findByCompanyIdAndHolidayDateBetween(companyId,
// startDate, endDate);

// // Merge holidays into a Set to eliminate duplicates
// Set<LocalDate> uniqueHolidays = new HashSet<>();
// employeeHolidays.forEach(holiday ->
// uniqueHolidays.add(holiday.getHolidayDate()));
// companyHolidays.forEach(holiday ->
// uniqueHolidays.add(holiday.getHolidayDate()));

// // Count total weekdays in the specified month
// int totalWeekdays = 0;
// for (LocalDate date = startDate; !date.isAfter(endDate); date =
// date.plusDays(1)) {
// if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() !=
// DayOfWeek.SUNDAY) {
// totalWeekdays++;
// }
// }

// // Calculate working days by subtracting unique holidays from total weekdays
// int workingDays = totalWeekdays - uniqueHolidays.size();

// return workingDays;

public int calculateActualWorkingDays(Employee employee, int year, int month) {
    logger.info("Calculating actual working days for employee ID: {} for {}/{}",
            employee.getId(), month, year);

    // Get total days in the month
    YearMonth yearMonth = YearMonth.of(year, month);
    int totalDaysInMonth = yearMonth.lengthOfMonth();

    // Get all holidays for the employee in the month
    List<EmployeeHolidayCalendar> employeeHolidays = holidayCalendarRepository
            .findByEmployeeIdAndMonth(employee.getId(), year, month);

    // Get all company holidays for the employee's company in the month
    List<CompanyHolidayCalender> companyHolidays = companyHolidayCalenderRepository
            .findByCompanyIdAndMonth(employee.getCompanyRegistration().getId(), year, month);

    // Merge all holidays, avoiding duplicates
    Set<LocalDate> allHolidays = new HashSet<>();
    for (EmployeeHolidayCalendar holiday : employeeHolidays) {
        allHolidays.add(holiday.getHolidayDate());
    }
    for (CompanyHolidayCalender holiday : companyHolidays) {
        allHolidays.add(holiday.getHolidayDate());
    }

    // Calculate actual working days
    int actualWorkingDays = totalDaysInMonth - allHolidays.size();
    logger.info("Total working days for {}/{}: {}", month, year, actualWorkingDays);

    return actualWorkingDays;
}

public Map<String, Object> calculateActualWorkingDaysAndHours(Employee employee, int year, int month) {
    logger.info("Calculating actual working days and hours for employee ID: {} for {}/{}",
            employee.getId(), month, year);

    // Get total days in the month
    YearMonth yearMonth = YearMonth.of(year, month);
    int totalDaysInMonth = yearMonth.lengthOfMonth();

    // Get all holidays for the employee in the month
    List<EmployeeHolidayCalendar> employeeHolidays = holidayCalendarRepository
            .findByEmployeeIdAndMonth(employee.getId(), year, month);

    // Get all company holidays for the employee's company in the month
    List<CompanyHolidayCalender> companyHolidays = companyHolidayCalenderRepository
            .findByCompanyIdAndMonth(employee.getCompanyRegistration().getId(), year, month);

    // Merge all holidays, avoiding duplicates
    Set<LocalDate> allHolidays = new HashSet<>();
    for (EmployeeHolidayCalendar holiday : employeeHolidays) {
        allHolidays.add(holiday.getHolidayDate());
    }
    for (CompanyHolidayCalender holiday : companyHolidays) {
        allHolidays.add(holiday.getHolidayDate());
    }

    // Calculate actual working days
    int actualWorkingDays = totalDaysInMonth - allHolidays.size();
    logger.info("Total working days for {}/{}: {}", month, year, actualWorkingDays);

    // Fetch working hours from EmployeeConfig
    Optional<EmployeeConfig> employeeConfig = employeeConfigRepository.findByEmployee(employee);
    if (employeeConfig.isEmpty()) {
        throw new IllegalStateException("Employee configuration not found for employee ID: " + employee.getId());
    }
    int dailyWorkingHours = employeeConfig.get().getWorkingHours();
    int totalWorkingHours = actualWorkingDays * dailyWorkingHours;

    // Prepare the response
    Map<String, Object> result = new HashMap<>();
    result.put("employeeId", employee.getId());
    result.put("year", year);
    result.put("month", month);
    result.put("actualWorkingDays", actualWorkingDays);
    result.put("dailyWorkingHours", dailyWorkingHours);
    result.put("totalWorkingHours", totalWorkingHours);

    return result;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
public List<EmployeeHolidayCalendar> saveHolidaysAndCalculateWorkingDaysNew(
        Employee employee, List<EmployeeHolidayCalendar> holidayList, int year) {

    logger.info("Calculating working days for employee ID: {} in year: {}", employee.getId(), year);

    // Validate and fetch employee configuration
    Optional<EmployeeConfig> employeeConfigOptional = employeeConfigRepository.findByEmployee(employee);
    if (employeeConfigOptional.isEmpty()) {
        throw new IllegalStateException("Employee configuration not found for employee ID: " + employee.getId());
    }
    EmployeeConfig employeeConfig = employeeConfigOptional.get();
    int employeeWorkStateCode = employeeConfig.getWorkStateCode();

    // Total days in the specified year
    int totalDaysInYear = Year.isLeap(year) ? 366 : 365;
    logger.debug("Total days in year {}: {}", year, totalDaysInYear);

    // Get the count of employee-specific holidays
    int employeeHolidayCount = holidayList.size();
    logger.info("Employee-specific holiday count: {}", employeeHolidayCount);

    // Get the count of company holidays for the specified year and workStateCode
    List<CompanyHolidayCalender> companyHolidays = companyHolidayCalenderRepository
            .findByCompanyId(employee.getCompanyRegistration().getId());
    List<CompanyHolidayCalender> filteredCompanyHolidays = companyHolidays.stream()
            .filter(holiday -> holiday.getHolidayDate().getYear() == year 
                    && holiday.getWorkStateCode() == employeeWorkStateCode)
            .toList();
    logger.info("Filtered company holiday count for year {} and workStateCode {}: {}", 
            year, employeeWorkStateCode, filteredCompanyHolidays.size());

    // Merge the employee-specific holidays and filtered company holidays, avoiding duplicates
    Set<LocalDate> allHolidayDates = new HashSet<>();

    // Add employee-specific holidays
    for (EmployeeHolidayCalendar holiday : holidayList) {
        allHolidayDates.add(holiday.getHolidayDate());
    }

    // Add filtered company holidays
    for (CompanyHolidayCalender companyHoliday : filteredCompanyHolidays) {
        allHolidayDates.add(companyHoliday.getHolidayDate());
    }

    // Calculate the total working days
    int totalHolidayCount = allHolidayDates.size();
    int workingDays = totalDaysInYear - totalHolidayCount;
    logger.info("Calculated working days for employee ID {} in year {}: {}", employee.getId(), year, workingDays);

    // Set the calculated working days in each EmployeeHolidayCalendar record
    for (EmployeeHolidayCalendar holiday : holidayList) {
        holiday.setWorkingDays(workingDays);
    }

    // Save the holiday records with the updated working days
    logger.info("Saving updated holiday records with calculated working days.");
    return holidayCalendarRepository.saveAll(holidayList);
}




public List<EmployeeHolidayCalendar> saveHolidaysAndCalculateWorkingDaysNewn(
        Employee employee, List<EmployeeHolidayCalendar> holidayList, int year) {

    logger.info("Calculating working days for employee ID: {} in year: {}", employee.getId(), year);

    // Validate and fetch employee configuration
    Optional<EmployeeConfig> employeeConfigOptional = employeeConfigRepository.findByEmployee(employee);
    if (employeeConfigOptional.isEmpty()) {
        throw new IllegalStateException("Employee configuration not found for employee ID: " + employee.getId());
    }
    EmployeeConfig employeeConfig = employeeConfigOptional.get();
    int employeeWorkStateCode = employeeConfig.getWorkStateCode();
    int employeeWorkCategoryCode = employeeConfig.getWorkCategoryCode();

    // Total days in the specified year
    int totalDaysInYear = Year.isLeap(year) ? 366 : 365;
    logger.debug("Total days in year {}: {}", year, totalDaysInYear);

    // Get company holidays based on `workStateCode`
    List<CompanyHolidayCalender> companyHolidays = companyHolidayCalenderRepository
            .findByCompanyId(employee.getCompanyRegistration().getId());
    List<CompanyHolidayCalender> filteredCompanyHolidays = companyHolidays.stream()
            .filter(holiday -> holiday.getHolidayDate().getYear() == year 
                    && holiday.getWorkStateCode() == employeeWorkStateCode)
            .toList();

    // Get company day-offs based on `workCategoryCode`
    List<CompanyDayOffConfig> companyDayOffs = companyDayOffConfigRepository
            .findByCompanyId(employee.getCompanyRegistration().getId());
    List<CompanyDayOffConfig> filteredCompanyDayOffs = companyDayOffs.stream()
            .filter(dayOff -> dayOff.getHolidayDate().getYear() == year 
                    && dayOff.getWorkCategoryCode() == employeeWorkCategoryCode)
            .toList();

    // Merge all holidays into a set to avoid duplicates
    Set<LocalDate> allHolidayDates = new HashSet<>();

    // Add employee-specific holidays
    for (EmployeeHolidayCalendar holiday : holidayList) {
        allHolidayDates.add(holiday.getHolidayDate());
    }

    // Add filtered company holidays
    for (CompanyHolidayCalender companyHoliday : filteredCompanyHolidays) {
        allHolidayDates.add(companyHoliday.getHolidayDate());
    }

    // Add filtered company day-offs
    for (CompanyDayOffConfig companyDayOff : filteredCompanyDayOffs) {
        allHolidayDates.add(companyDayOff.getHolidayDate());
    }

    // Calculate the total working days
    int totalHolidayCount = allHolidayDates.size();
    int workingDays = totalDaysInYear - totalHolidayCount;
    logger.info("Calculated working days for employee ID {} in year {}: {}", employee.getId(), year, workingDays);

    // Set the calculated working days in each EmployeeHolidayCalendar record
    for (EmployeeHolidayCalendar holiday : holidayList) {
        holiday.setWorkingDays(workingDays);
    }

    // Save the holiday records with the updated working days
    logger.info("Saving updated holiday records with calculated working days.");
    return holidayCalendarRepository.saveAll(holidayList);
}



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// ////////
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 






public Map<String, Object> calculateActualWorkingDaysAndHoursnew(Employee employee, int year, int month) {
    logger.info("Calculating actual working days and hours for employee ID: {} for {}/{}", 
                employee.getId(), month, year);

    // Get total days in the month
    YearMonth yearMonth = YearMonth.of(year, month);
    int totalDaysInMonth = yearMonth.lengthOfMonth();

    // Fetch the employee's configuration to validate `workStateCode`
    Optional<EmployeeConfig> employeeConfigOptional = employeeConfigRepository.findByEmployee(employee);
    if (employeeConfigOptional.isEmpty()) {
        throw new IllegalStateException("Employee configuration not found for employee ID: " + employee.getId());
    }
    EmployeeConfig employeeConfig = employeeConfigOptional.get();
    int employeeWorkStateCode = employeeConfig.getWorkStateCode();
    int dailyWorkingHours = employeeConfig.getWorkingHours();

    // Get all holidays for the employee in the month
    List<EmployeeHolidayCalendar> employeeHolidays = holidayCalendarRepository
            .findByEmployeeIdAndMonth(employee.getId(), year, month);

    // Filter company holidays based on the employee's `workStateCode`
    List<CompanyHolidayCalender> companyHolidays = companyHolidayCalenderRepository
            .findByCompanyIdAndMonth(employee.getCompanyRegistration().getId(), year, month);
    List<CompanyHolidayCalender> filteredCompanyHolidays = companyHolidays.stream()
            .filter(holiday -> holiday.getWorkStateCode() == employeeWorkStateCode)
            .toList();

    // Merge all holidays, avoiding duplicates
    Set<LocalDate> allHolidays = new HashSet<>();
    for (EmployeeHolidayCalendar holiday : employeeHolidays) {
        allHolidays.add(holiday.getHolidayDate());
    }
    for (CompanyHolidayCalender holiday : filteredCompanyHolidays) {
        allHolidays.add(holiday.getHolidayDate());
    }

    // Calculate actual working days
    int actualWorkingDays = totalDaysInMonth - allHolidays.size();
    logger.info("Total working days for {}/{}: {}", month, year, actualWorkingDays);

    // Calculate total working hours
    int totalWorkingHours = actualWorkingDays * dailyWorkingHours;

    // Prepare the response
    Map<String, Object> result = new HashMap<>();
    result.put("employeeId", employee.getId());
    result.put("year", year);
    result.put("month", month);
    result.put("actualWorkingDays", actualWorkingDays);
    result.put("dailyWorkingHours", dailyWorkingHours);
    result.put("totalWorkingHours", totalWorkingHours);

    return result;
}



































public Map<String, Object> calculateActualWorkingDaysAndHoursNew(Employee employee, int year, int month) {
    logger.info("Calculating actual working days and hours for employee ID: {} for {}/{}", 
                employee.getId(), month, year);

    // Get total days in the month
    YearMonth yearMonth = YearMonth.of(year, month);
    int totalDaysInMonth = yearMonth.lengthOfMonth();

    // Fetch the employee's configuration to validate `workStateCode` and `workCategoryCode`
    Optional<EmployeeConfig> employeeConfigOptional = employeeConfigRepository.findByEmployee(employee);
    if (employeeConfigOptional.isEmpty()) {
        throw new IllegalStateException("Employee configuration not found for employee ID: " + employee.getId());
    }
    EmployeeConfig employeeConfig = employeeConfigOptional.get();
    int employeeWorkStateCode = employeeConfig.getWorkStateCode();
    int employeeWorkCategoryCode = employeeConfig.getWorkCategoryCode();
    int dailyWorkingHours = employeeConfig.getWorkingHours();

    // Get all holidays for the employee in the month
    List<EmployeeHolidayCalendar> employeeHolidays = holidayCalendarRepository
            .findByEmployeeIdAndMonth(employee.getId(), year, month);

    // Fetch and filter company holidays by `workStateCode`
    List<CompanyHolidayCalender> companyHolidays = companyHolidayCalenderRepository
            .findByCompanyIdAndMonth(employee.getCompanyRegistration().getId(), year, month);
    List<CompanyHolidayCalender> filteredCompanyHolidays = companyHolidays.stream()
            .filter(holiday -> holiday.getWorkStateCode() == employeeWorkStateCode)
            .toList();

    // Fetch and filter company day-offs by `workCategoryCode`
    List<CompanyDayOffConfig> companyDayOffs = companyDayOffConfigRepository
            .findByCompanyIdAndMonth(employee.getCompanyRegistration().getId(), year, month);
    List<CompanyDayOffConfig> filteredCompanyDayOffs = companyDayOffs.stream()
            .filter(dayOff -> dayOff.getWorkCategoryCode() == employeeWorkCategoryCode)
            .toList();

    // Merge all holidays and day-offs, avoiding duplicates
    Set<LocalDate> allHolidays = new HashSet<>();
    for (EmployeeHolidayCalendar holiday : employeeHolidays) {
        allHolidays.add(holiday.getHolidayDate());
    }
    for (CompanyHolidayCalender holiday : filteredCompanyHolidays) {
        allHolidays.add(holiday.getHolidayDate());
    }
    for (CompanyDayOffConfig dayOff : filteredCompanyDayOffs) {
        allHolidays.add(dayOff.getHolidayDate());
    }

    // Calculate actual working days
    int actualWorkingDays = totalDaysInMonth - allHolidays.size();
    logger.info("Total working days for {}/{}: {}", month, year, actualWorkingDays);

    // Calculate total working hours
    int totalWorkingHours = actualWorkingDays * dailyWorkingHours;

    // Prepare the response
    Map<String, Object> result = new HashMap<>();
    result.put("employeeId", employee.getId());
    result.put("year", year);
    result.put("month", month);
    result.put("actualWorkingDays", actualWorkingDays);
    result.put("dailyWorkingHours", dailyWorkingHours);
    result.put("totalWorkingHours", totalWorkingHours);

    return result;
}










public Map<String, Object> calculateActualWorkingDaysAndHoursNews(Employee employee, int year, int month) {
    logger.info("Calculating actual working days and hours for employee ID: {} for {}/{}", 
                employee.getId(), month, year);

    // Get total days in the month
    YearMonth yearMonth = YearMonth.of(year, month);
    int totalDaysInMonth = yearMonth.lengthOfMonth();

    // Fetch the employee's configuration to validate `workStateCode`, `workCategoryCode`, and `employeeSpecialHolidayAccess`
    Optional<EmployeeConfig> employeeConfigOptional = employeeConfigRepository.findByEmployee(employee);
    if (employeeConfigOptional.isEmpty()) {
        throw new IllegalStateException("Employee configuration not found for employee ID: " + employee.getId());
    }
    EmployeeConfig employeeConfig = employeeConfigOptional.get();
    int employeeWorkStateCode = employeeConfig.getWorkStateCode();
    int employeeWorkCategoryCode = employeeConfig.getWorkCategoryCode();
    boolean specialHolidayAccess = employeeConfig.isEmployeeSpecialHolidayAccess();
    int dailyWorkingHours = employeeConfig.getWorkingHours();

    // Get all holidays for the employee in the month
    List<EmployeeHolidayCalendar> employeeHolidays = holidayCalendarRepository
            .findByEmployeeIdAndMonth(employee.getId(), year, month);

    // Fetch and filter company holidays by `workStateCode`
    List<CompanyHolidayCalender> companyHolidays = companyHolidayCalenderRepository
            .findByCompanyIdAndMonth(employee.getCompanyRegistration().getId(), year, month);
    List<CompanyHolidayCalender> filteredCompanyHolidays = companyHolidays.stream()
            .filter(holiday -> holiday.getWorkStateCode() == employeeWorkStateCode)
            .toList();

    // Fetch company day-offs by `workCategoryCode` if `employeeSpecialHolidayAccess` is false
    Set<LocalDate> companyOffDays = new HashSet<>();
    if (!specialHolidayAccess) {
        List<CompanyDayOffConfig> companyDayOffs = companyDayOffConfigRepository
                .findByCompanyIdAndMonth(employee.getCompanyRegistration().getId(), year, month);
        List<CompanyDayOffConfig> filteredCompanyDayOffs = companyDayOffs.stream()
                .filter(dayOff -> dayOff.getWorkCategoryCode() == employeeWorkCategoryCode)
                .toList();
        filteredCompanyDayOffs.forEach(dayOff -> companyOffDays.add(dayOff.getHolidayDate()));
    }

    // Merge all holidays and company off days, avoiding duplicates
    Set<LocalDate> allHolidays = new HashSet<>();
    for (EmployeeHolidayCalendar holiday : employeeHolidays) {
        allHolidays.add(holiday.getHolidayDate());
    }
    for (CompanyHolidayCalender holiday : filteredCompanyHolidays) {
        allHolidays.add(holiday.getHolidayDate());
    }
    allHolidays.addAll(companyOffDays);

    // Calculate actual working days
    int actualWorkingDays = totalDaysInMonth - allHolidays.size();
    logger.info("Total working days for {}/{}: {}", month, year, actualWorkingDays);

    // Calculate total working hours
    int totalWorkingHours = actualWorkingDays * dailyWorkingHours;

    // Prepare the response
    Map<String, Object> result = new HashMap<>();
    result.put("employeeId", employee.getId());
    result.put("year", year);
    result.put("month", month);
    result.put("actualWorkingDays", actualWorkingDays);
    result.put("dailyWorkingHours", dailyWorkingHours);
    result.put("totalWorkingHours", totalWorkingHours);

    return result;
}



public Map<String, Object> calculateWorkingDaysAndHoursBetweenDates(Employee employee,
                                                                    LocalDate startDate,
                                                                    LocalDate endDate) {
    logger.info("Calculating working days between {} and {} for employee ID: {}",
                startDate, endDate, employee.getId());

    if (endDate.isBefore(startDate)) {
        throw new IllegalArgumentException("End date cannot be before start date");
    }

    Optional<EmployeeConfig> employeeConfigOptional = employeeConfigRepository.findByEmployee(employee);
    if (employeeConfigOptional.isEmpty()) {
        throw new IllegalStateException("Employee configuration not found for employee ID: " + employee.getId());
    }

    EmployeeConfig config = employeeConfigOptional.get();
    int dailyWorkingHours = config.getWorkingHours();

    Set<LocalDate> allHolidays = new HashSet<>();

    // Gather all holiday and off-day data in the given date range
    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
        int year = date.getYear();
        int month = date.getMonthValue();

        List<EmployeeHolidayCalendar> empHolidays = holidayCalendarRepository
                .findByEmployeeIdAndMonth(employee.getId(), year, month);
        List<CompanyHolidayCalender> compHolidays = companyHolidayCalenderRepository
                .findByCompanyIdAndMonth(employee.getCompanyRegistration().getId(), year, month).stream()
                .filter(h -> h.getWorkStateCode() == config.getWorkStateCode())
                .toList();
        List<CompanyDayOffConfig> dayOffs = config.isEmployeeSpecialHolidayAccess() ? List.of() :
                companyDayOffConfigRepository
                        .findByCompanyIdAndMonth(employee.getCompanyRegistration().getId(), year, month).stream()
                        .filter(d -> d.getWorkCategoryCode() == config.getWorkCategoryCode())
                        .toList();

        empHolidays.forEach(h -> allHolidays.add(h.getHolidayDate()));
        compHolidays.forEach(h -> allHolidays.add(h.getHolidayDate()));
        dayOffs.forEach(d -> allHolidays.add(d.getHolidayDate()));
    }

    // Calculate working days (excluding holidays in date range)
    int actualWorkingDays = 0;
    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
        if (!allHolidays.contains(date)) {
            actualWorkingDays++;
        }
    }

    int totalWorkingHours = actualWorkingDays * dailyWorkingHours;

    Map<String, Object> result = new HashMap<>();
    result.put("employeeId", employee.getId());
    result.put("startDate", startDate);
    result.put("endDate", endDate);
    result.put("actualWorkingDays", actualWorkingDays);
    result.put("dailyWorkingHours", dailyWorkingHours);
    result.put("totalWorkingHours", totalWorkingHours);

    return result;
}




//////////////////////////////////////////////////
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 





public int calculateTotalLeaves(Long employeeId, LocalDate fromDate, LocalDate toDate) {
    List<EmployeeHolidayCalendar> leaveRecords = holidayCalendarRepository.findLeavesBetweenDates(employeeId, fromDate, toDate);
    return leaveRecords.size(); // Return the count of leave records
}

// public int calculateTotalHolidays(Long employeeId, LocalDate fromDate, LocalDate toDate) {
//     // Fetch employee-specific holidays
//     List<EmployeeHolidayCalendar> employeeHolidays = holidayCalendarRepository.findLeavesBetweenDates(employeeId, fromDate, toDate);

//     // Fetch employee configuration
//     EmployeeConfig employeeConfig = employeeConfigRepository.findByEmployeeId(employeeId)
//             .orElseThrow(() -> new IllegalArgumentException("Employee configuration not found for employeeId: " + employeeId));

//     int workStateCode = employeeConfig.getWorkStateCode();
//     Long companyId = employeeConfig.getEmployee().getCompanyRegistration().getId();

//     // Fetch company-level holidays for the employee's work state
//     List<CompanyHolidayCalender> companyHolidays = companyHolidayCalenderRepository.findHolidaysByCompanyAndStateCode(companyId, workStateCode, fromDate, toDate);

//     // Merge all holidays into a set to avoid duplicates
//     Set<LocalDate> allHolidays = new HashSet<>();
//     employeeHolidays.forEach(holiday -> allHolidays.add(holiday.getHolidayDate()));
//     companyHolidays.forEach(holiday -> allHolidays.add(holiday.getHolidayDate()));

//     // Return the count of unique holidays
//     return allHolidays.size();
// }




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
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 
/// 





public int calculateTotalHolidays(Long employeeId, LocalDate fromDate, LocalDate toDate) {
    // Fetch employee-specific holidays
    List<EmployeeHolidayCalendar> employeeHolidays = holidayCalendarRepository.findLeavesBetweenDates(employeeId, fromDate, toDate);

    // Fetch employee configuration
    EmployeeConfig employeeConfig = employeeConfigRepository.findByEmployeeId(employeeId)
            .orElseThrow(() -> new IllegalArgumentException("Employee configuration not found for employeeId: " + employeeId));

    int workStateCode = employeeConfig.getWorkStateCode();
    int workCategoryCode = employeeConfig.getWorkCategoryCode(); // Extract work category code
    Long companyId = employeeConfig.getEmployee().getCompanyRegistration().getId();

    // Fetch company-level holidays for the employee's work state
    List<CompanyHolidayCalender> companyHolidays = companyHolidayCalenderRepository
            .findHolidaysByCompanyAndStateCode(companyId, workStateCode, fromDate, toDate);

    // Create a set to store unique holidays
    Set<LocalDate> allHolidays = new HashSet<>();

    // Add employee-specific holidays
    employeeHolidays.forEach(holiday -> allHolidays.add(holiday.getHolidayDate()));

    // Add company-level holidays
    companyHolidays.forEach(holiday -> allHolidays.add(holiday.getHolidayDate()));

    // Check the employee's special holiday access
    if (!employeeConfig.isEmployeeSpecialHolidayAccess()) {
        // Fetch day-offs for the employee's work category
        List<CompanyDayOffConfig> companyDayOffs = companyDayOffConfigRepository
                .findDayOffsByCompanyAndCategoryCode(companyId, workCategoryCode, fromDate, toDate);

        // Add day-offs to the set
        companyDayOffs.forEach(dayOff -> allHolidays.add(dayOff.getHolidayDate()));
    }

    // Return the count of unique holidays
    return allHolidays.size();
}





/////
/// 
/// 
/// 
/// 
/// 
/// 


    public int calculateTotalWorkingDays(Long employeeId, LocalDate fromDate, LocalDate toDate) {
        // Fetch employee-specific holidays
        List<EmployeeHolidayCalendar> employeeHolidays = holidayCalendarRepository.findLeavesBetweenDates(employeeId, fromDate, toDate);

        // Fetch employee configuration
        EmployeeConfig employeeConfig = employeeConfigRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee configuration not found for employeeId: " + employeeId));

        int workStateCode = employeeConfig.getWorkStateCode();
        int workCategoryCode = employeeConfig.getWorkCategoryCode();
        Long companyId = employeeConfig.getEmployee().getCompanyRegistration().getId();

        // Fetch company-level holidays for the employee's work state
        List<CompanyHolidayCalender> companyHolidays = companyHolidayCalenderRepository
                .findHolidaysByCompanyAndStateCode(companyId, workStateCode, fromDate, toDate);

        // Create a set to store unique holidays
        Set<LocalDate> allHolidays = new HashSet<>();

        // Add employee-specific holidays
        employeeHolidays.forEach(holiday -> allHolidays.add(holiday.getHolidayDate()));

        // Add company-level holidays
        companyHolidays.forEach(holiday -> allHolidays.add(holiday.getHolidayDate()));

        // Check the employee's special holiday access
        if (!employeeConfig.isEmployeeSpecialHolidayAccess()) {
            // Fetch day-offs for the employee's work category
            List<CompanyDayOffConfig> companyDayOffs = companyDayOffConfigRepository
                    .findDayOffsByCompanyAndCategoryCode(companyId, workCategoryCode, fromDate, toDate);

            // Add day-offs to the set
            companyDayOffs.forEach(dayOff -> allHolidays.add(dayOff.getHolidayDate()));
        }

        // Calculate the total number of days between fromDate and toDate (inclusive)
        long totalDays = fromDate.datesUntil(toDate.plusDays(1)).count();

        // Subtract the number of holidays from the total days
        int workingDays = (int) totalDays - allHolidays.size();

        // Return the number of working days
        return workingDays;
}





public void deleteAllByEmployeeId(Long employeeId) {
    holidayCalendarRepository.deleteByEmployeeId(employeeId);
}

public int calculateApprovedLeaves(Employee employee, int year, int month) {
    logger.info("Calculating approved leaves for employee ID: {} for {}/{}", employee.getId(), month, year);

    // Fetch approved leave applications for the employee
    List<LeaveApplication> approvedLeaves = leaveApplicationRepository.findByEmployeeAndRequestStatus(employee, "APPROVED");

    // Calculate working days for the employee
    Map<String, Object> workingDaysData = calculateActualWorkingDaysAndHoursNews(employee, year, month);
    Set<LocalDate> workingDays = (Set<LocalDate>) workingDaysData.get("workingDays");

    if (workingDays == null) {
        logger.warn("Working days are null for employee ID: {} for {}/{}", employee.getId(), month, year);
        workingDays = new HashSet<>(); // Default to an empty set
    }

    // Filter leave applications for the specified month and year
    int approvedLeavesCount = 0;
    for (LeaveApplication leave : approvedLeaves) {
        LocalDate leaveFromDate = leave.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate leaveToDate = leave.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Check overlap with working days
        for (LocalDate date = leaveFromDate; !date.isAfter(leaveToDate); date = date.plusDays(1)) {
            if (workingDays.contains(date)) {
                approvedLeavesCount++;
            }
        }
    }

    logger.info("Total approved leaves for {}/{}: {}", month, year, approvedLeavesCount);
    return approvedLeavesCount;
}

}
