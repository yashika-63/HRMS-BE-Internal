package com.spectrum.Induction.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.Induction.Model.AssignInduction;
import com.spectrum.Induction.Model.InductionAck;
import com.spectrum.Induction.Model.Inductions;
import com.spectrum.Induction.Model.ResultInduction;
import com.spectrum.Induction.Repo.AssignInductionRepo;
import com.spectrum.Induction.Repo.InductionRepoAck;
import com.spectrum.Induction.Repo.InductionsRepository;
import com.spectrum.Induction.Repo.ResultRepo;
import com.spectrum.model.Employee;
import com.spectrum.repository.EmployeeRepository;
import com.spectrum.service.EmailService;



@Service

public class ResultService {
    @Autowired
    private ResultRepo resultRepo;

    @Autowired
    private InductionRepoAck inductionAckRepo;
    @Autowired
    private InductionsRepository inductionsRepo;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AssignInductionRepo assignRepository;
    @Autowired
    private EmailService emailService;

    // Create or Update
    public ResultInduction saveResult(ResultInduction resultInduction) {
        return resultRepo.save(resultInduction);
    }

    // Get all results
    public List<ResultInduction> getAllResults() {
        return resultRepo.findAll();
    }

    // Get result by ID
    public ResultInduction getResultById(Long id) {
        return resultRepo.findById(id).orElseThrow(() -> new RuntimeException("Id not found"));
    }

    // Delete result by ID
    public void deleteResult(Long id) {
        resultRepo.deleteById(id);
    }
    /////////////////////////////////////////////////////////////////////////////////////

    // public ResultInduction saveResult(Long inductionId, Long ackId, Long empId) {

    // // Fetch the induction
    // Inductions induction = inductionsRepo.findById(inductionId)
    // .orElseThrow(() -> new RuntimeException("Induction not found with id: " +
    // inductionId));
    // System.out.println("Induction"+induction);
    // System.out.println("HIiii"+induction.getCreatedByEmployeeId());
    // // Validate the employee ID
    // if (induction.getCreatedByEmployeeId() == 0 ) {
    // throw new EntityNotFoundException("Induction does not belong to the given
    // employee");
    // }

    // // Fetch the ack
    // InductionAck ack = inductionAckRepo.findById(ackId)
    // .orElseThrow(() -> new RuntimeException("InductionAck not found with id: " +
    // ackId));

    // // Create ResultInduction and set both FK relationships
    // ResultInduction result = new ResultInduction();
    // result.setInduction(induction);
    // result.setInductionAck(ack);

    // // Save and return
    // return resultRepo.save(result);
    // }

    // @Transactional
    // public ResultInduction saveResult(Long inductionId, Long ackId, Long empId) {
    // // Fetch the induction
    // Inductions induction = inductionsRepo.findById(inductionId)
    // .orElseThrow(() -> new RuntimeException("Induction not found with id: " +
    // inductionId));
    // System.out.println("Induction: " + induction);
    // System.out.println("Created By Employee ID: " +
    // induction.getCreatedByEmployeeId());

    // // Validate the employee ID (Check if the provided employeeId matches the one
    // that created the induction)
    // if (induction.getCreatedByEmployeeId() != empId) {
    // throw new EntityNotFoundException("Induction does not belong to the given
    // employee");
    // }

    // // Fetch the ack
    // InductionAck ack = inductionAckRepo.findById(ackId)
    // .orElseThrow(() -> new RuntimeException("InductionAck not found with id: " +
    // ackId));

    // // Create ResultInduction and set both FK relationships
    // ResultInduction result = new ResultInduction();
    // result.setInduction(induction);
    // result.setInductionAck(ack);

    // // Save and return
    // return resultRepo.save(result);
    // }

    // public ResultInduction saveResultInduction(ResultInduction result,
    // Long employeeId,
    // Long inductionId,
    // Long acknowledgeId,
    // Long assignId) {

    // if (employeeId != null) {
    // Employee employee = employeeRepository.findById(employeeId)
    // .orElseThrow(() -> new RuntimeException("Employee not found with id: " +
    // employeeId));
    // result.setEmployee(employee);
    // }

    // if (inductionId != null) {
    // Inductions induction = inductionsRepo.findById(inductionId)
    // .orElseThrow(() -> new RuntimeException("Induction not found with id: " +
    // inductionId));
    // result.setInduction(induction);
    // }

    // if (acknowledgeId != null) {
    // InductionAck ack = inductionAckRepo.findById(acknowledgeId)
    // .orElseThrow(() -> new RuntimeException("Induction Acknowledgment not found
    // with id: " + acknowledgeId));
    // result.setInductionAck(ack);
    // }

    // if (assignId != null) {
    // AssignInduction assign = assignRepository.findById(assignId)
    // .orElseThrow(() -> new RuntimeException("Assignment not found with id: " +
    // assignId));
    // assign.setCompletionStatus(true);
    // result.setAssignInduction(assign);
    // }

    // return resultRepo.save(result);
    // }

    // public List<ResultInduction> saveResultInductionList(List<ResultInduction>
    // resultInductions,
    // Long employeeId,
    // Long inductionId,
    // Long assignInductionId) {

    // Employee employee = employeeRepository.findById(employeeId)
    // .orElseThrow(() -> new RuntimeException("Employee not found with id: " +
    // employeeId));

    // Inductions induction = inductionsRepo.findById(inductionId)
    // .orElseThrow(() -> new RuntimeException("Induction not found with id: " +
    // inductionId));

    // AssignInduction assignInduction =
    // assignRepository.findById(assignInductionId)
    // .orElseThrow(() -> new RuntimeException("AssignInduction not found with id: "
    // + assignInductionId));

    // assignInduction.setCompletionStatus(true);

    // for (ResultInduction result : resultInductions) {
    // result.setEmployee(employee);
    // result.setInduction(induction);
    // result.setAssignInduction(assignInduction);

    // if (result.getInductionAck() != null && result.getInductionAck().getId() !=
    // null) {
    // Long ackId = result.getInductionAck().getId();
    // InductionAck inductionAck = inductionAckRepo.findById(ackId)
    // .orElseThrow(
    // () -> new RuntimeException("Induction Acknowledgment not found with id: " +
    // ackId));
    // result.setInductionAck(inductionAck);
    // } else {
    // throw new RuntimeException("Induction acknowledgment id is missing in one of
    // the objects!");
    // }
    // }

    // return resultRepo.saveAll(resultInductions);
    // }
    public List<ResultInduction> saveResultInductionList(List<ResultInduction> resultInductions,
            Long employeeId,
            Long inductionId,
            Long assignInductionId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

        Inductions induction = inductionsRepo.findById(inductionId)
                .orElseThrow(() -> new RuntimeException("Induction not found with id: " + inductionId));

        AssignInduction assignInduction = assignRepository.findById(assignInductionId)
                .orElseThrow(() -> new RuntimeException("AssignInduction not found with id: " + assignInductionId));

        assignInduction.setCompletionStatus(true);

        // Fetching the employee who assigned the induction
        Long assignedById = assignInduction.getAssignedBy();
        if (assignedById == null) {
            throw new RuntimeException("AssignedBy ID is missing in AssignInduction!");
        }

        Employee assignedBy = employeeRepository.findById(assignedById)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + assignedById));

        for (ResultInduction result : resultInductions) {
            result.setEmployee(employee);
            result.setInduction(induction);
            result.setAssignInduction(assignInduction);

            if (result.getInductionAck() != null && result.getInductionAck().getId() != null) {
                Long ackId = result.getInductionAck().getId();
                InductionAck inductionAck = inductionAckRepo.findById(ackId)
                        .orElseThrow(
                                () -> new RuntimeException("Induction Acknowledgment not found with id: " + ackId));
                result.setInductionAck(inductionAck);
            } else {
                throw new RuntimeException("Induction acknowledgment id is missing in one of the objects!");
            }
        }

        // Save all results
        List<ResultInduction> savedResults = resultRepo.saveAll(resultInductions);

        // Send email notification to the assigner
        if (assignedBy.getEmail() != null && !assignedBy.getEmail().isEmpty()) {
            String subject = "Induction Completed";
            String message = String.format("Dear %s,\n\nEmployee %s has completed the induction: %s.",
                    assignedBy.getFirstName(), employee.getFirstName(), induction.getHeading());

            emailService.sendEmail(assignedBy.getEmail(), subject, message);
        } else {
            throw new RuntimeException("AssignedBy employee email is missing!");
        }

        return savedResults;
    }

    public List<ResultInduction> getResultsByEmployeeIdAndInductionId(Long employeeId, Long inductionId) {
        return resultRepo.findAllByEmployeeIdAndInductionId(employeeId, inductionId);
    }

    public Optional<ResultInduction> getResultByEmployeeIdAndInductionId(Long employeeId, Long inductionId) {
        return resultRepo.findByEmployeeIdAndInductionId(employeeId, inductionId);
    }

    public List<ResultInduction> getResultsByEmployeeId(Long employeeId) {
        return resultRepo.findAllByEmployeeId(employeeId);
    }
}
