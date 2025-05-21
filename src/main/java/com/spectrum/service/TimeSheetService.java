package com.spectrum.service;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.model.TimeSheet;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.repository.TimeSheetRepository;

@Service
public class TimeSheetService {

	@Autowired
	private TimeSheetRepository timeSheetRepository;

	@Autowired
	private EmployeeRepository employeeRepository; // Add this repository

    @Autowired
    private CompanyRegistrationRepository companyRegistrationRepository;



	public TimeSheet saveTimeSheet(Date fromDate, Date toDate, Long employeeId, Long companyId) {
        // Find Employee by employeeId
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Find CompanyRegistration by companyId
        CompanyRegistration company = companyRegistrationRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Create new TimeSheet object
        TimeSheet timeSheet = new TimeSheet();
        timeSheet.setFromDate(fromDate);
        timeSheet.setToDate(toDate);
        timeSheet.setEmployee(employee);
        timeSheet.setCompanyRegistration(company);

        // Save TimeSheet
        return timeSheetRepository.save(timeSheet);
    }


	// public List<TimeSheet> saveMultiple(Long employeeId, List<TimeSheet>
	// timeSheets) {
	// Employee employee = employeeRepository.findById(employeeId).orElse(null);
	// if (employee == null) {
	// throw new RuntimeException("Employee not found with id: " + employeeId);
	// }

	// for (TimeSheet timeSheet : timeSheets) {
	// timeSheet.setEmployee(employee);
	// }
	// return timeSheetRepository.saveAll(timeSheets);
	// }
	





	// public List<TimeSheet> getTimeSheetsByDate(Date date) {
	// 	return timeSheetRepository.findByDate(date);
	// }

	// public Optional<TimeSheet> getTimeSheetById(Long id) {
	// 	return timeSheetRepository.findById(id);
	// }



	// public List<TimeSheet> getTimeSheetsByEmployeeIdAndDate(Long employeeId, Date date) {
	// 	Employee employee = employeeRepository.findById(employeeId).orElse(null);
	// 	if (employee == null) {
	// 		throw new RuntimeException("Employee not found with id: " + employeeId);
	// 	}

	// 	return timeSheetRepository.findByEmployeeAndDate(employee, date);
	// }


	// public List<TimeSheet> getTimeSheetsByYearMonthWeek(Long employeeId, int year, int month, int week) {
    //     LocalDate startOfMonth = LocalDate.of(year, month, 1);
    //     LocalDate startOfWeek = startOfMonth.withDayOfMonth(1)
    //                                         .plusWeeks(week - 1)
    //                                         .with(DayOfWeek.MONDAY);

    //     // Ensure the startOfWeek is still within the month
    //     if (startOfWeek.getMonthValue() != month) {
    //         startOfWeek = startOfWeek.plusWeeks(1);
    //     }

    //     LocalDate endOfWeek = startOfWeek.plusDays(6); // End of the week (Sunday)

    //     Date start = java.sql.Date.valueOf(startOfWeek);
    //     Date end = java.sql.Date.valueOf(endOfWeek);

    //     return timeSheetRepository.findByEmployeeIdAndDateRange(employeeId, start, end);
    // }
}