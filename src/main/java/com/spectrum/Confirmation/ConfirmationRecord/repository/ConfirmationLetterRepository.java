package com.spectrum.Confirmation.ConfirmationRecord.repository;

import com.spectrum.Confirmation.ConfirmationRecord.model.ConfirmationLetter;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationLetterRepository extends JpaRepository<ConfirmationLetter, Long> {
        Optional<ConfirmationLetter> findByEmployee(Employee employee);


        List<ConfirmationLetter> findByCompanyOrderByIdDesc(CompanyRegistration company);

        List<ConfirmationLetter> findByEmployeeActionFalse();

}
