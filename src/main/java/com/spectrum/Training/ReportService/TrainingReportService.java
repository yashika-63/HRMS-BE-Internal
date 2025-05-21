package com.spectrum.Training.ReportService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spectrum.Training.repository.AssignTrainingRepo;
import com.spectrum.Training.repository.CertificationRepositoary;
import com.spectrum.Training.repository.ResultTrainingRepo;
import com.spectrum.Training.repository.TrainingRepositoary;

@Service

public class TrainingReportService {
    @Autowired
   private  TrainingRepositoary trainingHRMSRepository;



public List<Map<String, Object>> getTrainingReportWithCompanyDetails() {
    List<Object[]> results = trainingHRMSRepository.fetchTrainingReportWithCompanyFields();
    List<Map<String, Object>> response = new ArrayList<>();

    for (Object[] row : results) {
        Map<String, Object> map = new HashMap<>();

        // TrainingHRMS fields
        map.put("trainingId", row[0]);
        map.put("heading", row[1]);
        map.put("type", row[2]);
        map.put("date", row[3]);
        map.put("department", row[4]);
        map.put("departmentId", row[5]);
        map.put("description", row[6]);
        map.put("time", row[7]);
        map.put("status", row[8]);
        map.put("region", row[9]);
        map.put("regionId", row[10]);
        map.put("createdByEmpId", row[11]);

        // Company fields
        map.put("companyId", row[12]);
        map.put("companyAssignId", row[13]);
        map.put("companyName", row[14]);
        map.put("companyType", row[15]);
        map.put("companyAddress", row[16]);
        map.put("city", row[17]);
        map.put("state", row[18]);
        map.put("country", row[19]);
        map.put("postalCode", row[20]);
        map.put("landmark", row[21]);
        map.put("phone", row[22]);
        map.put("email", row[23]);
        map.put("website", row[24]);

        response.add(map);
    }

    return response;
}




         public Page<Map<String, Object>> getTrainingReportWithCompanyDetails(Pageable pageable) {
    Page<Object[]> results = trainingHRMSRepository.fetchTrainingReportWithCompanyFields(pageable);

    return results.map(row -> {
        Map<String, Object> map = new HashMap<>();

        // Map training fields
        map.put("trainingId", row[0]);
        map.put("heading", row[1]);
        map.put("type", row[2]);
        map.put("date", row[3]);
        map.put("department", row[4]);
        map.put("departmentId", row[5]);
        map.put("description", row[6]);
        map.put("time", row[7]);
        map.put("status", row[8]);
        map.put("region", row[9]);
        map.put("regionId", row[10]);
        map.put("createdByEmpId", row[11]);

        // Map company fields
        map.put("companyId", row[12]);
        map.put("companyAssignId", row[13]);
        map.put("companyName", row[14]);
        map.put("companyType", row[15]);
        map.put("companyAddress", row[16]);
        map.put("city", row[17]);
        map.put("state", row[18]);
        map.put("country", row[19]);
        map.put("postalCode", row[20]);
        map.put("landmark", row[21]);
        map.put("phone", row[22]);
        map.put("email", row[23]);
        map.put("website", row[24]);

        return map;
    });


}

@Autowired
    private CertificationRepositoary repository;

    public Page<Map<String, Object>> getPaginatedCertificationReport(Pageable pageable) {
        Page<Object[]> results = repository.fetchCertificationReport(pageable);
    
        List<Map<String, Object>> reportList = results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
    
            // Certification fields
            map.put("certificationId", row[0]);
            map.put("employeeName", row[1]);
            map.put("courseDescription", row[2]);
            map.put("completionDate", row[3]);
            map.put("empRole", row[4]);
            map.put("signature", row[5]);
            map.put("certificationCode", row[6]);
            map.put("applied", row[7]);
    
            // Employee fields
            map.put("employeeId", row[8]);
            map.put("empCode", row[9]);
            map.put("firstName", row[10]);
            map.put("lastName", row[11]);
            map.put("email", row[12]);
            map.put("department", row[13]);
            map.put("designation", row[14]);
    
            // Company fields
            map.put("companyId", row[15]);
            map.put("companyAssignId", row[16]);
            map.put("companyName", row[17]);
            map.put("companyAddress", row[18]);
            map.put("city", row[19]);
            map.put("state", row[20]);
            map.put("country", row[21]);
    
            // Training fields
            map.put("trainingId", row[22]);
            map.put("heading", row[23]);
            map.put("type", row[24]);
            map.put("trainingDate", row[25]);
            map.put("trainingDepartment", row[26]);
    
            // ResultTraining fields
            map.put("resultTrainingId", row[27]);
            map.put("note", row[28]);
            map.put("termsAndCondition", row[29]);
            map.put("resultCompletionDate", row[30]);
    
            return map;
        }).collect(Collectors.toList());
    
        return new PageImpl<>(reportList, pageable, results.getTotalElements());
    }
    

    public Page<Map<String, Object>> getTrainingReportWithCompanyDetailsCertification(Pageable pageable) {
        Page<Object[]> page = repository.fetchTrainingReportWithCompanyFields(pageable);
    
        Page<Map<String, Object>> mappedPage = page.map(row -> {
            Map<String, Object> map = new HashMap<>();
            // TrainingHRMS fields
            map.put("trainingId", row[0]);
            map.put("heading", row[1]);
            map.put("type", row[2]);
            map.put("date", row[3]);
            map.put("department", row[4]);
            map.put("departmentId", row[5]);
            map.put("description", row[6]);
            map.put("time", row[7]);
            map.put("status", row[8]);
            map.put("region", row[9]);
            map.put("regionId", row[10]);
            map.put("createdByEmpId", row[11]);
    
            // CompanyRegistration fields
            map.put("companyId", row[12]);
            map.put("companyAssignId", row[13]);
            map.put("companyName", row[14]);
            map.put("companyType", row[15]);
            map.put("companyAddress", row[16]);
            map.put("city", row[17]);
            map.put("state", row[18]);
            map.put("country", row[19]);
            map.put("postalCode", row[20]);
            map.put("landmark", row[21]);
            map.put("phone", row[22]);
            map.put("email", row[23]);
            map.put("website", row[24]);
    
            return map;
        });
    
        return mappedPage;
    }
    
    @Autowired
    private  ResultTrainingRepo resultTrainingRepository;


    public Page<Map<String, Object>> getResultTrainingReport(Pageable pageable) {
        Page<Object[]> results = resultTrainingRepository.fetchResultTrainingReport(pageable);

        return results.map(row -> {
            Map<String, Object> map = new HashMap<>();

            // ResultTraining fields
            map.put("resultTrainingId", row[0]);
            map.put("note", row[1]);
            map.put("termsAndCondition", row[2]);
            map.put("completionDate", row[3]);

            // TrainingHRMS fields
            map.put("trainingHRMSId", row[4]);
            map.put("heading", row[5]);
            map.put("type", row[6]);
            map.put("date", row[7]);
            map.put("department", row[8]);

            // Employee fields
            map.put("employeeId", row[9]);
            map.put("employeeFirstName", row[10]);
            map.put("employeeLastName", row[11]);

            // TrainingAcknowledge fields
            map.put("trainingAcknowledgeId", row[12]);
            map.put("question", row[13]);
            map.put("rating", row[14]);
            map.put("acknowledgeTermsAndCondition", row[15]);

            // AssignTraining fields
            map.put("assignTrainingId", row[16]);
            map.put("assignedBy", row[17]);
            map.put("assignDate", row[18]);
            map.put("expiryStatus", row[19]);
            map.put("completionStatus", row[20]);

            return map;
        });
    }


    @Autowired
    private AssignTrainingRepo assignTrainingRepository;


    public Page<Map<String, Object>> getTrainingReportWithCompanyDetailsofAssign(Pageable pageable) {
        Page<Object[]> results = assignTrainingRepository.fetchTrainingReport(pageable);

        return results.map(row -> {
            Map<String, Object> map = new HashMap<>();

            // TrainingHRMS fields
            map.put("trainingId", row[0]);
            map.put("heading", row[1]);
            map.put("type", row[2]);
            map.put("date", row[3]);
            map.put("department", row[4]);
            map.put("departmentId", row[5]);
            map.put("description", row[6]);
            map.put("time", row[7]);
            map.put("status", row[8]);

            // AssignTraining fields
            map.put("assignTrainingId", row[9]);
            map.put("assignDate", row[10]);
            map.put("expiryStatus", row[11]);
            map.put("completionStatus", row[12]);
            map.put("expiryDays", row[13]);
            map.put("assignedBy", row[14]);

            // Employee fields
            map.put("employeeId", row[15]);
            map.put("empCode", row[16]);
            map.put("firstName", row[17]);
            map.put("lastName", row[18]);
            map.put("email", row[19]);

            return map;
        });
    }


        
}

