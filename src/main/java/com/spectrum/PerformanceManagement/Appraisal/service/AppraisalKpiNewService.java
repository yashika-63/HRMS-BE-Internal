package com.spectrum.PerformanceManagement.Appraisal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.PerformanceManagement.Appraisal.model.AppraisalKpi;
import com.spectrum.PerformanceManagement.Appraisal.repository.AppraisalKpiRepository;

import jakarta.transaction.Transactional;

@Service
public class AppraisalKpiNewService {


       @Autowired
    private AppraisalKpiRepository appraisalKpiRepository;

   
    @Transactional
    public List<AppraisalKpi> saveMultipleAppraisalKpi(List<AppraisalKpi> appraisalKpiList) {
        return appraisalKpiRepository.saveAll(appraisalKpiList);
    }



    
}
