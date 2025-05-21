package com.spectrum.Training.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.spectrum.Training.model.ResultTraining;
import com.spectrum.Training.model.TrainingHRMS;
import com.spectrum.Training.repository.TrainingRepositoary;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;

@Service
public class TrainingService {

    @Autowired
    private TrainingRepositoary trainingRepository;

    @Autowired
    private CompanyRegistrationRepository companyRepository;

public TrainingHRMS getByID(Long id){

    return trainingRepository.findById(id).orElseThrow(()-> new RuntimeException("ID was not found"));
}

    public TrainingHRMS saveTraining(TrainingHRMS training, Long companyId) throws Exception {
        if (training == null) {
            throw new IllegalArgumentException("Training data cannot be null.");
        }

        if (companyId != null) {
            Optional<CompanyRegistration> optionalCompany = companyRepository.findById(companyId);
            if (optionalCompany.isEmpty()) {
                throw new Exception("Company with ID " + companyId + " not found.");
            }
            training.setCompany(optionalCompany.get());
        }
        training.setStatus(true);
        return trainingRepository.save(training);
    }

    public TrainingHRMS saveTrainingWithEmployee(TrainingHRMS training, Long companyId, Long employeeId) throws Exception {
        if (training == null) {
            throw new IllegalArgumentException("Training data cannot be null.");
        }
    
        // Set employee ID from request parameter
        if (employeeId == null) {
            throw new IllegalArgumentException("Employee ID is required.");
        }
        training.setCreatedByEmpId(employeeId);
    
        // Set company if companyId is passed
        if (companyId != null) {
            Optional<CompanyRegistration> optionalCompany = companyRepository.findById(companyId);
            if (optionalCompany.isEmpty()) {
                throw new Exception("Company with ID " + companyId + " not found.");
            }
            training.setCompany(optionalCompany.get());
        }
        training.setStatus(true);
        return trainingRepository.save(training);
    }
    public List<TrainingHRMS> getTrainingByYearAndCompanyId(int year, Long companyId) throws Exception {
        if (companyId == null || year <= 0) {
            throw new IllegalArgumentException("Invalid year or company ID.");
        }
    
        List<TrainingHRMS> trainings = trainingRepository.findByYearAndCompanyId(year, companyId);
        if (trainings.isEmpty()) {
            throw new Exception("No trainings found for year " + year + " and company ID " + companyId);
        }
    
        return trainings;
    }

    public List<TrainingHRMS> getTrainingByStatusYearAndCompanyId(boolean status, int year, Long companyId) throws Exception {
        if (companyId == null || year <= 0) {
            throw new IllegalArgumentException("Invalid input: companyId or year is missing/invalid.");
        }
    
        List<TrainingHRMS> trainings = trainingRepository.findByStatusYearAndCompanyId(status, year, companyId);
        if (trainings.isEmpty()) {
            throw new Exception("No training records found for the given filters.");
        }
    
        return trainings;
    }
    public List<TrainingHRMS> getByYearRegionIdAndCompanyId(int year, Long regionId, Long companyId) throws Exception {
        if (year <= 0 || regionId == null || companyId == null) {
            throw new IllegalArgumentException("Year, regionId, or companyId is invalid or missing.");
        }
    
        List<TrainingHRMS> list = trainingRepository.findByYearRegionIdAndCompanyId(year, regionId, companyId);
    
        if (list.isEmpty()) {
            throw new Exception("No data found for the provided year, region ID, and company ID.");
        }
    
        return list;
    }


   
  public Page<TrainingHRMS> getActiveTrainingByCompanyId(Long companyId, Pageable pageable) {
    CompanyRegistration var1=companyRepository.findById(companyId).orElseThrow(()-> new RuntimeException("Company Not Found:"+companyId));

        return trainingRepository.findByStatusTrueAndCompanyId(companyId, pageable);
    }
    
     
    // public Optional<TrainingHRMS> updateTraining(Long id, TrainingHRMS updatedTraining) {
    //     return trainingRepository.findById(id).map(existingTraining -> {

    //         existingTraining.setRegion(updatedTraining.getRegion());
    //         existingTraining.setRegionId(updatedTraining.getRegionId());
    //         existingTraining.setType(updatedTraining.getType());
    //         existingTraining.setStatus(updatedTraining.isStatus());
    //         existingTraining.setDate(updatedTraining.getDate());
    //         existingTraining.setCreatedByEmpId(updatedTraining.getCreatedByEmpId());
    //         existingTraining.setDepartment(updatedTraining.getDepartment());
    //         existingTraining.setDescription(updatedTraining.getDescription());
    //         existingTraining.setHeading(updatedTraining.getHeading());
    //         existingTraining.setCompany(updatedTraining.getCompany());

    //         return trainingRepository.save(existingTraining);
    //     });
    // }

    public Optional<TrainingHRMS> updateTraining(Long id, TrainingHRMS updatedTraining) {
        trainingRepository.findById(id).orElseThrow(()-> new RuntimeException("ID was not found"));
 
        return trainingRepository.findById(id).map(existingTraining -> {
 
            existingTraining.setRegion(updatedTraining.getRegion());
            existingTraining.setRegionId(updatedTraining.getRegionId());
            existingTraining.setType(updatedTraining.getType());
            existingTraining.setStatus(updatedTraining.isStatus());
            existingTraining.setDate(updatedTraining.getDate());
            existingTraining.setCreatedByEmpId(updatedTraining.getCreatedByEmpId());
            existingTraining.setDepartment(updatedTraining.getDepartment());
            existingTraining.setDescription(updatedTraining.getDescription());
            existingTraining.setHeading(updatedTraining.getHeading());
            existingTraining.setCompany(updatedTraining.getCompany());
            existingTraining.setTime(updatedTraining.getTime());
 
            return trainingRepository.save(existingTraining);
        });
    }
    public void deleteTrainingById(Long id) {
        if (!trainingRepository.existsById(id)) {
            throw new RuntimeException("TrainingHRMS with ID " + id + " not found");
        }
        trainingRepository.deleteById(id);
    }


    public Page<TrainingHRMS> getByFilters(int year, Long regionId, Long companyId, Long deptId, Pageable pageable) {
        if (year <= 0 || regionId == null || companyId == null || deptId == null) {
            throw new IllegalArgumentException("Year, regionId, companyId, or deptId is invalid or missing.");
        }

        return trainingRepository.findByDateYearAndRegionIdAndCompanyIdAndDepartmentId(year, regionId, companyId, deptId, pageable);
    }


    // public Page<TrainingHRMS> searchTrainingRecords(String description, String heading, Integer year, Long companyId, Pageable pageable) {
    //     return trainingRepository.search(description, heading, year, companyId, pageable);
    // }

    public Page<TrainingHRMS> searchTrainingRecords(String description, String heading, Integer year, 
    Long companyId, Long departmentId, Long regionId, 
    Pageable pageable) {
        
return trainingRepository.search(description, heading, year, companyId, departmentId, regionId, pageable);
}



}
