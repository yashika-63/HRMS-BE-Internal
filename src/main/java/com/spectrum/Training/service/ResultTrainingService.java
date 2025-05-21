package com.spectrum.Training.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spectrum.Training.model.AssignTraining;
import com.spectrum.Training.model.ResultTraining;
import com.spectrum.Training.model.TrainingAcknowledge;
import com.spectrum.Training.model.TrainingHRMS;
import com.spectrum.Training.repository.AssignTrainingRepo;
import com.spectrum.Training.repository.CertificationRepositoary;
import com.spectrum.Training.repository.ResultTrainingRepo;
import com.spectrum.Training.repository.TrainingAcknowledegeRepo;
import com.spectrum.Training.repository.TrainingRepositoary;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;

@Service
public class ResultTrainingService {
    @Autowired
    private ResultTrainingRepo resultTrainingRepo;

    @Autowired
    private AssignTrainingRepo assignTrainingRepository;

    @Autowired
    private TrainingRepositoary trainingHRMSRepository;
    @Autowired
    private TrainingAcknowledegeRepo trainAckRepo;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CertificationRepositoary certificateRepo;

    @Autowired
    private EmailService emailService;

    public ResultTraining createResult(ResultTraining resultTraining) {
        return resultTrainingRepo.save(resultTraining);
    }

    public List<ResultTraining> getAllResults() {
        return resultTrainingRepo.findAll();
    }

    public ResultTraining getResultById(Long id) {
        ResultTraining result = resultTrainingRepo.findById(id).orElse(null);
        if (result == null) {
            throw new RuntimeException("ResultTraining not found for ID: " + id);
        }
        return result;
    }

    public ResultTraining updateResult(Long id, ResultTraining updatedResult) {
        ResultTraining existingResult = resultTrainingRepo.findById(id).orElse(null);
        if (existingResult == null) {
            throw new RuntimeException("ResultTraining not found for ID: " + id);
        }
        existingResult.setNote(updatedResult.getNote());
        existingResult.setTermsAndCondition(updatedResult.isTermsAndCondition());
        existingResult.setEmployee(updatedResult.getEmployee());
        existingResult.setTrainingHRMS(updatedResult.getTrainingHRMS());
        existingResult.setTrainingAcknowledge(updatedResult.getTrainingAcknowledge());
        existingResult.setAssignTraining(updatedResult.getAssignTraining());

        return resultTrainingRepo.save(existingResult);
    }

    public void deleteResult(Long id) {
        ResultTraining existingResult = resultTrainingRepo.findById(id).orElse(null);
        if (existingResult == null) {
            throw new RuntimeException("ResultTraining not found for ID: " + id);
        }
        resultTrainingRepo.deleteById(id);
    }

    // public ResultTraining saveResultTraining(ResultTraining result,
    // Long employeeId,
    // Long trainingId,
    // Long assignId,
    // Long AckId) {

    // if (employeeId != null) {
    // Employee employee = employeeRepository.findById(employeeId)
    // .orElseThrow(() -> new RuntimeException("Employee not found with id: " +
    // employeeId));
    // result.setEmployee(employee);
    // }

    // if (trainingId != null) {
    // TrainingHRMS trainingHRMS = trainingHRMSRepository.findById(trainingId)
    // .orElseThrow(() -> new RuntimeException("TrainingHRMS not found with id: " +
    // trainingId));
    // result.setTrainingHRMS(trainingHRMS);
    // }

    // if (assignId != null) {
    // AssignTraining assignTraining = assignTrainingRepository.findById(assignId)
    // .orElseThrow(() -> new RuntimeException("AssignTraining not found with id: "
    // + assignId));
    // assignTraining.setCompletionStatus(true); // example: mark as complete
    // result.setAssignTraining(assignTraining);
    // }

    // if (AckId != null) {
    // TrainingAcknowledge trainAck = trainAckRepo.findById(AckId)
    // .orElseThrow(() -> new RuntimeException("AssignTraining not found with id: "
    // + AckId));
    // // example: mark as complete
    // result.setTrainingAcknowledge(trainAck);
    // }

    // return resultTrainingRepo.save(result);
    // }

    // public List<ResultTraining> saveResultTrainingList(List<ResultTraining> results,
    //         Long employeeId,
    //         Long trainingId,
    //         Long assignId) {

    //     Employee employee = employeeRepository.findById(employeeId)
    //             .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

    //     TrainingHRMS trainingHRMS = trainingHRMSRepository.findById(trainingId)
    //             .orElseThrow(() -> new RuntimeException("TrainingHRMS not found with id: " + trainingId));

    //     AssignTraining assign = assignTrainingRepository.findById(assignId)
    //             .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + assignId));

    //     // Mark assign as completed
    //     assign.setCompletionStatus(true);

    //     for (ResultTraining result : results) {

    //         result.setEmployee(employee);
    //         result.setTrainingHRMS(trainingHRMS);
    //         result.setAssignTraining(assign);

    //         // Acknowledge ID check
    //         if (result.getTrainingAcknowledge() != null && result.getTrainingAcknowledge().getId() != null) {

    //             Long ackId = result.getTrainingAcknowledge().getId();
    //             TrainingAcknowledge ack = trainAckRepo.findById(ackId)
    //                     .orElseThrow(() -> new RuntimeException("Training Acknowledgment not found with id: " + ackId));
    //             result.setTrainingAcknowledge(ack);

    //         } else {
    //             throw new RuntimeException("Acknowledgment ID is missing in one of the ResultTraining objects!");
    //         }
    //     }
    //     List<ResultTraining> savedResults = resultTrainingRepo.saveAll(results);

     
    //     return savedResults;
    // }

    // public List<ResultTraining> getResultsByEmployeeId(Long employeeId) {
    //     if (employeeId != null) {
    //         Employee employee = employeeRepository.findById(employeeId)
    //                 .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
    //     }
    //     return resultTrainingRepo.findByEmployeeId(employeeId);
    // }


    public List<ResultTraining> saveResultTrainingList(List<ResultTraining> results,
            Long employeeId,
            Long trainingId,
            Long assignId) {
 
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
 
        TrainingHRMS trainingHRMS = trainingHRMSRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("TrainingHRMS not found with id: " + trainingId));
 
        AssignTraining assign = assignTrainingRepository.findById(assignId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + assignId));
 
        // Mark assign as completed
        assign.setCompletionStatus(true);
 
        // Fetching the employee who assigned the training
        Long assignedById = assign.getAssignedBy();
        if (assignedById == null) {
            throw new RuntimeException("AssignedBy ID is missing in AssignTraining!");
        }
 
        Employee assignedBy = employeeRepository.findById(assignedById)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + assignedById));
 
        for (ResultTraining result : results) {
            result.setEmployee(employee);
            result.setTrainingHRMS(trainingHRMS);
            result.setAssignTraining(assign);
 
            if (result.getTrainingAcknowledge() != null && result.getTrainingAcknowledge().getId() != null) {
                Long ackId = result.getTrainingAcknowledge().getId();
                TrainingAcknowledge ack = trainAckRepo.findById(ackId)
                        .orElseThrow(() -> new RuntimeException("Training Acknowledgment not found with id: " + ackId));
                result.setTrainingAcknowledge(ack);
            } else {
                throw new RuntimeException("Acknowledgment ID is missing in one of the ResultTraining objects!");
            }
        }
 
        List<ResultTraining> savedResults = resultTrainingRepo.saveAll(results);
 
        // Send email notification to the assigner
        if (assignedBy.getEmail() != null && !assignedBy.getEmail().isEmpty()) {
            String subject = "Training Completed";
            String message = String.format("Dear %s,\n\nEmployee %s has completed the training: %s.",
                    assignedBy.getFirstName(), employee.getFirstName(), trainingHRMS.getHeading());
 
            emailService.sendEmail(assignedBy.getEmail(), subject, message);
        } else {
            throw new RuntimeException("AssignedBy employee email is missing!");
        }
 
        return savedResults;
    }
    public List<ResultTraining> getResultsByEmployeeId(Long employeeId) {
        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        }
        return resultTrainingRepo.findByEmployeeId(employeeId);
    }
 
    public Page<ResultTraining> getResultByEmployeeAndTraining(Long employeeId, Long trainingId, int page, int size) {
        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        }

        // if(trainingId!=null){
        // trainingHRMSRepository.findById(trainingId).orElse
        // }
        Pageable pageable = PageRequest.of(page, size);
        return resultTrainingRepo.findByEmployeeIdAndTrainingHRMSId(employeeId, trainingId, pageable);
    }

}
