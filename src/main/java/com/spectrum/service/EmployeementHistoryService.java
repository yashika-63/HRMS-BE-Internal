package com.spectrum.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.model.Employee;
import com.spectrum.model.EmployeementHistory;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.repository.EmployeementHistoryRepository;

@Service

public class EmployeementHistoryService {

    @Autowired
    private EmployeementHistoryRepository employeementHistoryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeementHistory saveEmployeementHistory(EmployeementHistory employeementHistory, String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee != null) {
            employeementHistory.setEmployee(employee);
            return employeementHistoryRepository.save(employeementHistory);
        } else {
            throw new RuntimeException("Employee not found with ID: " + employeeId);
        }
    }

    public void saveEmployeementHistory(EmployeementHistory employeementHistory) {
        employeementHistoryRepository.save(employeementHistory);
    }






public Optional<EmployeementHistory> getById(Long id) {
    return employeementHistoryRepository.findById(id);
}

public void deleteEmployeementHistory(EmployeementHistory employeementHistory) {
    employeementHistoryRepository.delete(employeementHistory);



}

public List<EmployeementHistory> getByEmployeeId(Long employeeId) {
    return employeementHistoryRepository.findByEmployeeId(employeeId);
}




}
