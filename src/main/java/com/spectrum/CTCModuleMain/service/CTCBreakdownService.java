package com.spectrum.CTCModuleMain.service;

import com.spectrum.CTCModuleMain.model.CTCBreakdown;
import com.spectrum.CTCModuleMain.repository.CTCBreakdownRepository;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CTCBreakdownService {

    @Autowired
    private CTCBreakdownRepository ctcBreakdownRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public CTCBreakdown saveCTCData(Long employeeId, CTCBreakdown ctcBreakdown) throws Exception {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();

            ctcBreakdown.setEmployee(employee);
            ctcBreakdown.setCtcStatus(true);
            return ctcBreakdownRepository.save(ctcBreakdown);
        } else {
            throw new Exception("Employee not found with ID: " + employeeId);
        }
    }

    // Get CTCBreakdown by ID
    public Optional<CTCBreakdown> getCTCBreakdownById(Long id) {
        return ctcBreakdownRepository.findById(id);
    }

    // Update CTCBreakdown by ID
    public CTCBreakdown updateCTCBreakdown(Long id, CTCBreakdown updatedCTC) {
        return ctcBreakdownRepository.findById(id).map(ctcBreakdown -> {
            ctcBreakdown.setBasicSalary(updatedCTC.getBasicSalary());
            ctcBreakdown.setHouseRentAllowance(updatedCTC.getHouseRentAllowance());
            ctcBreakdown.setTotalCtcAmount(updatedCTC.getTotalCtcAmount());
            ctcBreakdown.setProvidentFund(updatedCTC.getProvidentFund());
            ctcBreakdown.setConveyanceAllowance(updatedCTC.getConveyanceAllowance());
            ctcBreakdown.setPerformanceBonus(updatedCTC.getPerformanceBonus());
            ctcBreakdown.setGratuity(updatedCTC.getGratuity());
            ctcBreakdown.setSpecialAllowances(updatedCTC.getSpecialAllowances());
            ctcBreakdown.setCtcDate(updatedCTC.getCtcDate());
            ctcBreakdown.setPerformanceBonusDate(updatedCTC.getPerformanceBonusDate());
            return ctcBreakdownRepository.save(ctcBreakdown);
        }).orElseThrow(() -> new RuntimeException("CTC not found with id " + id));
    }

    // Get CTCBreakdown by Employee ID
    public Optional<CTCBreakdown> getCTCBreakdownByEmployeeId(Long employeeId) {
        return ctcBreakdownRepository.findByEmployee_Id(employeeId);
    }

    // Delete CTCBreakdown by ID
    public void deleteCTCBreakdown(Long id) {
        ctcBreakdownRepository.deleteById(id);
    }

    public List<CTCBreakdown> findExpiringCTCs(LocalDate currentDate) {
        LocalDate thresholdDate = currentDate.plusDays(7); // For example, CTCs expiring in the next 7 days
        return ctcBreakdownRepository.findByEffectiveToDateLessThanEqual(thresholdDate);
    }
}
