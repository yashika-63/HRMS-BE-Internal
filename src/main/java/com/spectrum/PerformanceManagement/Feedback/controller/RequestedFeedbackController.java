package com.spectrum.PerformanceManagement.Feedback.controller;

import com.spectrum.PerformanceManagement.Feedback.model.RequestedFeedback;
import com.spectrum.PerformanceManagement.Feedback.service.FeedbackAIService;
import com.spectrum.PerformanceManagement.Feedback.service.RequestedFeedbackService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requested-feedback")
public class RequestedFeedbackController {

    @Autowired
    private RequestedFeedbackService requestedFeedbackService;

    @Autowired
    private FeedbackAIService feedbackAIService;

    // Endpoint to generate feedback questions dynamically without saving
    // @GetMapping("/generate-questions")
    // public ResponseEntity<List<String>> generateFeedbackQuestions(@RequestParam
    // String feedbackDescription) {
    // List<String> questions =
    // feedbackAIService.generateFeedbackQuestions(feedbackDescription);
    // return ResponseEntity.ok(questions);
    // }
    
    // @PostMapping("/generate-questions")
    // public ResponseEntity<String> generateFeedbackQuestions(@RequestBody Map<String, String> request) {
    //     String prompt = request.get("prompt");
    //     String response = feedbackAIService.generateFeedbackQuestions(prompt);
    //     return ResponseEntity.ok(response);
    // }

    
    @PostMapping("/create/{employeeId}")
    public ResponseEntity<RequestedFeedback> createFeedback(
            @PathVariable Long employeeId,
            @RequestBody RequestedFeedback requestedFeedback) {

        RequestedFeedback savedFeedback = requestedFeedbackService.saveFeedback(employeeId, requestedFeedback);
        return ResponseEntity.ok(savedFeedback);
    }

    @PutMapping("/approveReject/{feedbackId}")
    public ResponseEntity<String> approveRejectFeedback(@PathVariable Long feedbackId,
            @RequestParam boolean approvalStatus) {
        try {
            // Call the service method to change approval status and send email
            requestedFeedbackService.updateApprovalStatus(feedbackId, approvalStatus);
            return ResponseEntity.ok("Feedback status updated and notification sent successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("/getByStatusAndEmployee/{status}/{requestedToEmployeeId}")
    public ResponseEntity<List<RequestedFeedback>> getFeedbackByStatusAndEmployee(
            @PathVariable boolean status,
            @PathVariable int requestedToEmployeeId) {
        try {
            // Fetch feedback by status and employee ID
            List<RequestedFeedback> feedbackList = requestedFeedbackService
                    .getFeedbackByStatusAndEmployeeId(status, requestedToEmployeeId);

            if (feedbackList.isEmpty()) {
                return ResponseEntity.noContent().build(); // No content found
            }

            return ResponseEntity.ok(feedbackList); // Return the feedback list
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Handle any exceptions
        }
    }

    @PutMapping("/update/{feedbackId}")
    public ResponseEntity<RequestedFeedback> updateFeedback(
            @PathVariable Long feedbackId,
            @RequestBody RequestedFeedback updateRequest) {  // Accept full JSON body
    
        try {
            // Call service method to update feedback
            RequestedFeedback updatedFeedback = requestedFeedbackService.updateFeedbackById(
                    feedbackId,
                    updateRequest.getNotes(),
                    updateRequest.getOverallRating(),
                    updateRequest.getFeedbackDetails(),
                    updateRequest.getRequestedToEmployeeId());
    
            return ResponseEntity.ok(updatedFeedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getByEmployeeIdAndYear/{employeeId}/{year}")
    public ResponseEntity<List<RequestedFeedback>> getFeedbackByEmployeeIdAndYear(
            @PathVariable Long employeeId, @PathVariable int year) {
        List<RequestedFeedback> feedbackList = requestedFeedbackService.getFeedbackByEmployeeIdAndYear(employeeId, year);
        return ResponseEntity.ok(feedbackList);
    }
}