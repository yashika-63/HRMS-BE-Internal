package com.spectrum.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spectrum.model.Education;
import com.spectrum.repository.EducationRepository;
@Service
public class EducationService {

	
	
	 @Autowired
	    private EducationRepository educationRepository;

	    public void saveEducation(Education education) {
	        educationRepository.save(education);
	    }

	    public Page<Education> getAllEducation(int pageNumber, int pageSize) {
	        Pageable pageable = PageRequest.of(pageNumber, pageSize);
	        return educationRepository.findAll(pageable);
	    }
	     
	    
//	    public Education getByEmployeeId(long employeeId) {
//	        return educationRepository.findByEmployeeId(employeeId);
//	    }
	    
public Optional<Education> updateEducationById(int id, Education updatedEducation) {
	return educationRepository.findById(id)
			.map(existingEducation -> {
				existingEducation.setInstitute(updatedEducation.getInstitute());
				existingEducation.setUniversity(updatedEducation.getUniversity());
				existingEducation.setTypeOfStudy(updatedEducation.getTypeOfStudy());
				existingEducation.setYearOfAddmisstion(updatedEducation.getYearOfAddmisstion());
				existingEducation.setYearOfPassing(updatedEducation.getYearOfPassing());
				existingEducation.setBranch(updatedEducation.getBranch());
				existingEducation.setScore(updatedEducation.getScore());
				existingEducation.setScoreType(updatedEducation.getScoreType());
				// Preserve existing employee if not provided in the updatedEducation
				if (updatedEducation.getEmployee() != null) {
					existingEducation.setEmployee(updatedEducation.getEmployee());
				}

				return educationRepository.save(existingEducation);
			});
}

// Method to delete Education by ID
public boolean deleteEducationById(int id) {
	if (educationRepository.existsById(id)) {
		educationRepository.deleteById(id);
		return true;
	} else {
		return false;
	}
}

}
