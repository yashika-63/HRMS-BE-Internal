package com.spectrum.PerformanceManagement.Feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.PerformanceManagement.Feedback.model.RequestedFeedbackDetails;
@Repository
public interface RequestedFeedbackDetailsRepository extends JpaRepository <RequestedFeedbackDetails, Long>{

}
