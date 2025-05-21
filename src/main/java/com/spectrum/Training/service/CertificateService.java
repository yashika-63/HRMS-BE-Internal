package com.spectrum.Training.service;


import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spectrum.Training.model.Certification;
import com.spectrum.Training.model.ResultTraining;
import com.spectrum.Training.model.TrainingHRMS;
import com.spectrum.Training.repository.CertificationRepositoary;
import com.spectrum.Training.repository.ResultTrainingRepo;
import com.spectrum.Training.repository.TrainingRepositoary;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
import com.spectrum.repository.CompanyRegistrationRepository;
import com.spectrum.repository.EmployeeRepository;






@Service
public class CertificateService {

    @Autowired
    private CertificationRepositoary certificateRepo;

    @Autowired
    private EmployeeRepository empRepo;

    @Autowired
    private CompanyRegistrationRepository companyRepo;

    @Autowired 
    private TrainingRepositoary trainingHRMSRepo;

    @Autowired
    private ResultTrainingRepo  resultTrainingRepo;

    String name,designation;

    public Certification getCertificateDetails(Certification certificate, Long empID, Long companyId) {
        



        Employee employee = empRepo.findById(empID)
                .orElseThrow(() -> new RuntimeException("Employee Not Found: " + empID));

        CompanyRegistration company = companyRepo.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not Found: " + companyId));

        // Build employee full name
        String fullName = String.join(" ",
                employee.getFirstName() != null ? employee.getFirstName() : "",
                employee.getMiddleName() != null ? employee.getMiddleName() : "",
                employee.getLastName() != null ? employee.getLastName() : ""
        ).trim().replaceAll(" +", " ");

        // Assign values to the certificate
        certificate.setEmployeeName(fullName);
        certificate.setEmpRole(employee.getDesignation());
        certificate.setEmployee(employee);       // associate Employee entity
        certificate.setCompany(company);  
        // associate CompanyRegistration entity



        // Fetch and set completion date from ResultTraining
        if (certificate.getResultTraining() != null && certificate.getResultTraining().getCompletionDate() != null) {
            certificate.setCompletionDate(certificate.getResultTraining().getCompletionDate());
        } else {
            certificate.setCompletionDate(null);
        }

        // Fetch and set course description from TrainingHRMS
        if (certificate.getTrainingHRMS() != null && certificate.getTrainingHRMS().getDescription() != null) {
            certificate.setCourseDescription(certificate.getTrainingHRMS().getDescription());
        } else {
            certificate.setCourseDescription(null);
        }

        return certificateRepo.save(certificate);
    }



    public Certification getCertificateDetails(Long empID, Long companyId, Long trainingId, Long resultId, Certification certificate) {
        boolean exists = certificateRepo.existsByEmployee_IdAndTrainingHRMS_Id(empID, trainingId);
        if (exists) {
            throw new RuntimeException("Certificate already exists for this Employee and Training combination.");
        }
    Employee employee = empRepo.findById(empID)
            .orElseThrow(() -> new RuntimeException("Employee Not Found: " + empID));

    CompanyRegistration company = companyRepo.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not Found: " + companyId));

    TrainingHRMS training = trainingHRMSRepo.findById(trainingId)
            .orElseThrow(() -> new RuntimeException("TrainingHRMS not found: " + trainingId));

    ResultTraining result = resultTrainingRepo.findById(resultId)
            .orElseThrow(() -> new RuntimeException("ResultTraining not found: " + resultId));

    // Build employee full name
    String fullName = String.join(" ",
            employee.getFirstName() != null ? employee.getFirstName() : "",
            employee.getMiddleName() != null ? employee.getMiddleName() : "",
            employee.getLastName() != null ? employee.getLastName() : ""
    ).trim().replaceAll(" +", " ");

    // Assign values to the certificate
    certificate.setEmployeeName(fullName);
    certificate.setEmpRole(employee.getDesignation());
    certificate.setEmployee(employee);
    certificate.setCompany(company);
    certificate.setTrainingHRMS(training);
    certificate.setResultTraining(result);
    certificate.setApplied(true);
    // certificate.setSignature("Sunil Praladh Jangle");

    // Set completion date from ResultTraining
    certificate.setCompletionDate(result.getCompletionDate());

    // Set course description from TrainingHRMS
    certificate.setCourseDescription(training.getDescription());
certificate.setCertificationId(generateUniqueCertificationId());
    return certificateRepo.save(certificate);
}

private String generateUniqueCertificationId() {
    return "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
}

// public Certification findByEmployeeIdAndCompanyId(String employeeId, Long companyId) {
//     CompanyRegistration company = companyRepo.findById(companyId)
//     .orElseThrow(() -> new RuntimeException("Company not Found: " + companyId));

//     Employee employee = empRepo.findByEmployeeId(employeeId);
//     if (employee == null) {
//         throw new RuntimeException("Employee not found: " + employeeId);
//     }
    
//     return certificateRepo.findByEmployee_EmployeeIdAndCompany_Id(employeeId, companyId);
// }
public Certification certificateServicecertificateService(Long employeeId, Long companyId) {
    Certification certification = certificateRepo.findByEmployee_IdAndCompany_Id(employeeId, companyId);
    if (certification == null) {
        throw new RuntimeException("Certification not found for Employee ID: " + employeeId 
                                   + " and Company ID: " + companyId);
    }
    return certification;
}

public Certification findCertificationByEmployeeId(Long employeeId) {
    Certification certification = certificateRepo.findByEmployee_Id(employeeId);
    if (certification == null) {
        throw new RuntimeException("Certification not found for Employee ID: " + employeeId);
    }
    return certification;
}


public Certification findByCertificateID(String certificateId) {
   return certificateRepo.findByCertificationId(certificateId)
    .orElseThrow(() -> new RuntimeException("Id Not Found"));
    }
    

    
public Optional<Certification> getCertificationByTrainingIdAndEmployeeId(Long trainingId, Long employeeId) {
    TrainingHRMS training = trainingHRMSRepo.findById(trainingId)
            .orElseThrow(() -> new RuntimeException("TrainingHRMS not found: " + trainingId));

            Employee employee = empRepo.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee Not Found: " + employeeId));
    return certificateRepo.findByTrainingHRMS_IdAndEmployee_Id(trainingId, employeeId);
  }
    

}


