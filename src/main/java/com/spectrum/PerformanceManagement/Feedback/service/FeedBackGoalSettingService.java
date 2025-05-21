package com.spectrum.PerformanceManagement.Feedback.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.PerformanceManagement.Feedback.model.FeedBackGoalSetting;
import com.spectrum.PerformanceManagement.Feedback.repository.FeedBackGoalSettingRepository;




@Service
public class FeedBackGoalSettingService {

    @Autowired
    private FeedBackGoalSettingRepository feedBackGoalSettingRepository;



    public List<FeedBackGoalSetting> saveMultipleFeedbackForGoal(List<FeedBackGoalSetting> feedbackList) {
        return feedBackGoalSettingRepository.saveAll(feedbackList);
    }
}
