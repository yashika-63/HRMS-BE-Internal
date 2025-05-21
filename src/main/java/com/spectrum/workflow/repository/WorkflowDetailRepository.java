package com.spectrum.workflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.workflow.model.WorkflowDetail;



@Repository
public interface WorkflowDetailRepository extends JpaRepository<WorkflowDetail, Long> {
    WorkflowDetail findFirstByWorkflowMainIdOrderByIdAsc(long workflowMainId);

    WorkflowDetail findFirstByWorkflowMain_WorkflowNameOrderByIdAsc(String workflowName);

    Optional<WorkflowDetail> findByWorkflowFromDivisionAndWorkflowFromDepartmentAndWorkflowPreviousRole(
            String workflowFromDivision, String workflowFromDepartment, String workflowPreviousRole);

  
            Optional<WorkflowDetail> findByWorkflowMainIdAndWorkflowFromDivisionAndWorkflowFromDepartmentAndWorkflowPreviousRole(
                long workflowMainId, String workflowFromDivision, String workflowFromDepartment,
                String workflowPreviousRole);

}