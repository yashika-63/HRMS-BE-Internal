package com.spectrum.workflow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.workflow.model.WorkflowMain;

public interface WorkflowMainRepository extends JpaRepository<WorkflowMain, Long> {

	List<WorkflowMain> findByCompanyIdAndAccountId(long companyId, long accountId);

	// List<WorkflowMain> findByCompanyIdAndAccountId(long companyId, long
	// accountId);
	Optional<WorkflowMain> findByWorkflowName(String workflowName);

	List<WorkflowMain> findByCompanyId(long companyId);


	@Query("SELECT w.id AS id, w.workflowName AS workflowName FROM WorkflowMain w WHERE w.companyId = :companyId")
    List<Object[]> findWorkflowNamesAndIdsByCompanyId(@Param("companyId") long companyId);
}
