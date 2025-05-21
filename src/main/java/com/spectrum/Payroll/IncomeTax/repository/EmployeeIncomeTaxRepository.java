package com.spectrum.Payroll.IncomeTax.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.CTCModuleMain.model.CTCBreakdownHeader;
import com.spectrum.Payroll.IncomeTax.model.EmployeeIncomeTax;
import com.spectrum.model.Employee;

@Repository
public interface EmployeeIncomeTaxRepository extends JpaRepository<EmployeeIncomeTax,Long> {

    List<EmployeeIncomeTax> findByEmployeeIdAndStatusTrue(Long employeeId);


Optional<EmployeeIncomeTax> findByEmployeeAndCtcBreakdownHeaderAndStatusTrue(Employee employee, CTCBreakdownHeader ctcBreakdownHeader);

}
