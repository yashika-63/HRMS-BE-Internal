package com.spectrum.workflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.workflow.model.LeaveApproval;

public interface leaveApprovalRepository extends JpaRepository<LeaveApproval, Long> {

    List<LeaveApproval> findByLeaveApplicationId(Long leaveApplicationId);

}
