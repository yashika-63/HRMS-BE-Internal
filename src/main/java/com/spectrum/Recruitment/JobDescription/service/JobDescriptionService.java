package com.spectrum.Recruitment.JobDescription.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.Recruitment.JobDescription.model.JobDescription;
import com.spectrum.Recruitment.JobDescription.repository.JobDescriptionRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;

@Service
public class JobDescriptionService {

    @Autowired
    private JobDescriptionRepository jobDescriptionRepository;

    @Autowired
    private CompanyRegistrationRepository companyRepository;

    // public JobDescription saveJobDescription(JobDescription jobDescription, Long companyId) {
    //     CompanyRegistration company = companyRepository.findById(companyId)
    //             .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));
    //     jobDescription.setCompany(company);
    //     return jobDescriptionRepository.save(jobDescription);
    // }




    public JobDescription saveJobDescription(JobDescription jobDescription, Long companyId) {
        // Fetch the company by companyId
        CompanyRegistration company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));
        
        // Set the company in the job description
        jobDescription.setCompany(company);
        
        // Generate a unique random key and set it in the job description
        String randomKey = UUID.randomUUID().toString().replace("-", "");
        jobDescription.setJobKey(randomKey);  // Set the generated random key in jobDescription
        
        // Generate the job description link and set it in the job description
        // String jdLink = "http://localhost:5173/career/" + randomKey;
        String jdLink = "http://15.207.163.30:3002/career/" + randomKey;

        jobDescription.setJdLink(jdLink);  // Set the link for the job description
    
        // Save and return the job description
        return jobDescriptionRepository.save(jobDescription);
    }
    



    // public JobDescription getJobDescriptionByKey(String jobKey) {
    //     return jobDescriptionRepository.findByJobKey(jobKey)
    //             .map(jd -> {
    //                 if (!jd.isStatus()) {
    //                     throw new RuntimeException("This opening is closed");
    //                 }
    //                 return jd;
    //             })
    //             .orElseThrow(() -> new RuntimeException("No active job description found"));
    // }




    public Map<String, Object> getJobDescriptionByKey(String jobKey) {
    JobDescription jd = jobDescriptionRepository.findByJobKey(jobKey)
            .orElseThrow(() -> new RuntimeException("No active job description found"));

    if (!jd.isStatus()) {
        throw new RuntimeException("This opening is closed");
    }

    Map<String, Object> response = new LinkedHashMap<>();

    // Job Description fields
    response.put("jobTitle", jd.getJobTitle());
    response.put("jobDesc", jd.getJobDesc());
    response.put("requiredSkills", jd.getRequiredSkills());
    response.put("requiredExperience", jd.getRequiredExperience());
    response.put("contactPerson", jd.getContactPerson());
    response.put("contactPersonEmail", jd.getContactPersonEmail());
    response.put("date", jd.getDate());
    response.put("jdLink", jd.getJdLink());
    response.put("jobKey", jd.getJobKey());
    response.put("JobDescriptionId",jd.getId());

    // Company Info from the mapped entity
    CompanyRegistration company = jd.getCompany();
    response.put("companyId", company.getId());
    response.put("companyName", company.getCompanyName());
    response.put("companyAddress", company.getCompanyAddress());

    return response;
}

    
}
