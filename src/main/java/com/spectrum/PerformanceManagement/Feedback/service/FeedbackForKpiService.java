package com.spectrum.PerformanceManagement.Feedback.service;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.PerformanceManagement.Feedback.model.FeedbackForKpi;
import com.spectrum.PerformanceManagement.Feedback.repository.FeedbackForKpiRepository;


@Service
public class FeedbackForKpiService {

    @Autowired
    private  FeedbackForKpiRepository feedbackForKpiRepository;




    public FeedbackForKpi saveFeedbackForKpi(FeedbackForKpi feedbackForKpi) {
        return feedbackForKpiRepository.save(feedbackForKpi);
    }


        public List<FeedbackForKpi> saveMultipleFeedbackForKpi(List<FeedbackForKpi> feedbackList) {
            return feedbackForKpiRepository.saveAll(feedbackList);
        }




}
