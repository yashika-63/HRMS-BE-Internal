package com.spectrum.Training.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.Training.model.Certification;
import com.spectrum.model.Employee;

public interface CertificationRepositoary extends JpaRepository<Certification,Long>  {
    // boolean existsByEmployee_IdAndTrainingHRMS_IdAndResultTraining_Id(Long employeeId, Long trainingId, Long resultId);  

    boolean existsByEmployee_IdAndTrainingHRMS_Id(Long employeeId, Long trainingHRMSId);


    Certification findByEmployee_IdAndCompany_Id(Long employeeId, Long companyId);

    Certification findByEmployee_Id(Long employeeId);
    Optional<Certification> findByCertificationId(String certificationId);
    Optional<Certification> findByTrainingHRMS_IdAndEmployee_Id(Long trainingId, Long employeeId);

    // Certification findByEmployeeIdAndTrainingId(Long employeeId, Long trainingId);

    ///////////////////////////////////////////////////
    // reports foir certification
    @Query("""
        SELECT 
            t.id, t.heading, t.type, t.date, t.department, t.departmentId, t.description, t.time, t.status, t.region, t.regionId, t.createdByEmpId,
            c.id, c.companyAssignId, c.companyName, c.companyType, c.companyAddress, c.city, c.state, c.country, c.postalCode,
            c.landmark, c.phone, c.email, c.website
        FROM Certification cert
        LEFT JOIN cert.trainingHRMS t
        LEFT JOIN cert.company c
        WHERE t IS NOT NULL
    """)
    Page<Object[]> fetchTrainingReportWithCompanyFields(Pageable pageable);  
    
    
    @Query("SELECT " +
       "cert.id, cert.employeeName, cert.courseDescription, cert.completionDate, cert.empRole, cert.signature, cert.certificationId, cert.applied, " +
       "emp.id, emp.employeeId, emp.firstName, emp.lastName, emp.email, emp.department, emp.designation, " +
       "comp.id, comp.companyAssignId, comp.companyName, comp.companyAddress, comp.city, comp.state, comp.country, " +
       "train.id, train.heading, train.type, train.date, train.department, " +
       "res.id, res.note, res.termsAndCondition, res.completionDate " +
       "FROM Certification cert " +
       "LEFT JOIN cert.employee emp " +
       "LEFT JOIN cert.company comp " +
       "LEFT JOIN cert.trainingHRMS train " +
       "LEFT JOIN cert.resultTraining res")
Page<Object[]> fetchCertificationReport(Pageable pageable);

}
