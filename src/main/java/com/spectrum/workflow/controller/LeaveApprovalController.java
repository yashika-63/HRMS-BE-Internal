package com.spectrum.workflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.workflow.model.LeaveApproval;
import com.spectrum.workflow.service.LeaveApprovalService;

@RestController
@RequestMapping("/api/leaveApproval")
public class LeaveApprovalController {

        @Autowired
        private LeaveApprovalService leaveApprovalService;

        @PostMapping("/save/{leaveApplicationId}")
    public ResponseEntity<?> saveLeaveApproval(
            @PathVariable Long leaveApplicationId, 
            @RequestBody LeaveApproval leaveApproval) {
        try {
            LeaveApproval savedLeaveApproval = leaveApprovalService.saveAndNotify(leaveApplicationId, leaveApproval);
            return new ResponseEntity<>(savedLeaveApproval, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all/{leaveApplicationId}")
    public ResponseEntity<List<LeaveApproval>> getAllLeaveApprovalsByLeaveApplicationId(
            @PathVariable Long leaveApplicationId) {
        List<LeaveApproval> leaveApprovals = leaveApprovalService
                .getAllLeaveApprovalsByLeaveApplicationId(leaveApplicationId);
        if (leaveApprovals.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(leaveApprovals, HttpStatus.OK);
    }

}
