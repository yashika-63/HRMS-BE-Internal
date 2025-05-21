package com.spectrum.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spectrum.model.HiringChecklist;
import com.spectrum.repository.HiringChecklistRepository;

@Service
public class HiringChecklistService {

	
	
	
	
	@Autowired
	private HiringChecklistRepository hiringChecklistRepository;
	
	
	
	public ResponseEntity<String> updateHiringChecklistByEmployeeId(long employeeId, HiringChecklist updateHiringChecklist) {
        Optional<HiringChecklist> existingChecklist = hiringChecklistRepository.findByEmployeeId(employeeId);
        
        if (existingChecklist.isPresent()) {
            HiringChecklist checklist = existingChecklist.get();
            checklist.setEmployeeDataInfoSheet(updateHiringChecklist.isEmployeeDataInfoSheet());
            checklist.setEmployeeCheckAcquiredPassed(updateHiringChecklist.isEmployeeCheckAcquiredPassed());
            checklist.setEmployeeAppointmentLetterSign(updateHiringChecklist.isEmployeeAppointmentLetterSign());
            checklist.setEmployeePayrollBenifits(updateHiringChecklist.isEmployeePayrollBenifits());
            checklist.setEmployeePersonalFile(updateHiringChecklist.isEmployeePersonalFile());
            checklist.setEmployeeStateNewHireReporting(updateHiringChecklist.isEmployeeStateNewHireReporting());
            checklist.setEmployeeHandbookSign(updateHiringChecklist.isEmployeeHandbookSign());
            checklist.setEmployeePolicyDocumentReviewedSign(updateHiringChecklist.isEmployeePolicyDocumentReviewedSign());
            checklist.setEmployeeEmployeeBenifitsEnrollmentFormSign(updateHiringChecklist.isEmployeeEmployeeBenifitsEnrollmentFormSign());
            checklist.setEmployeeWorkSpaceSetup(updateHiringChecklist.isEmployeeWorkSpaceSetup());
            checklist.setEmployeeTimeCardAndEntryCard(updateHiringChecklist.isEmployeeTimeCardAndEntryCard());
            checklist.setEmployeeLoginsAndComputerApplicationAcess(updateHiringChecklist.isEmployeeLoginsAndComputerApplicationAcess());
            checklist.setEmployeemeetGreet(updateHiringChecklist.isEmployeemeetGreet());
            checklist.setEmployeeOrientationShedule(updateHiringChecklist.isEmployeeOrientationShedule());
            checklist.setEmployeeNewHireTreaningShedule(updateHiringChecklist.isEmployeeNewHireTreaningShedule());
            checklist.setEmployeeIdCard(updateHiringChecklist.isEmployeeIdCard());
            
            hiringChecklistRepository.save(checklist);
            
            return ResponseEntity.ok("Hiring checklist updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	
	
	
}
