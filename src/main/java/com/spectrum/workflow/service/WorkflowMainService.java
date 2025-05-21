package com.spectrum.workflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.workflow.repository.WorkflowMainRepository;

@Service

public class WorkflowMainService {

	
	
	@Autowired
	private WorkflowMainRepository workflowMainRepository;
	
	public void deleteWorkflowMainById(Long id) {
        if (workflowMainRepository.existsById(id)) {
            workflowMainRepository.deleteById(id);
        } else {
            throw new RuntimeException("Workflow record not found with ID: " + id);
        }
    }
	
}
