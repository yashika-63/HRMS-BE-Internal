package com.spectrum.Training.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spectrum.Training.model.EmployeeTrainingHeader;
import com.spectrum.Training.model.ResultTraining;

public interface ResultTrainingRepo extends JpaRepository<ResultTraining, Long>{
    List<ResultTraining> findByEmployeeId(Long employeeId);

     Page<ResultTraining> findByEmployeeIdAndTrainingHRMSId(Long employeeId, Long trainingId, Pageable pageable);   
     /////// Report 
      @Query("SELECT " +
           "rt.id, rt.note, rt.termsAndCondition, rt.completionDate, " +
           "th.id, th.heading, th.type, th.date, th.department, " +
           "e.id, e.firstName, e.lastName, " +
           "ta.id, ta.Question, ta.rating, ta.termAndCondition, " +
           "at.id, at.assignedBy, at.assignDate, at.expiryStatus, at.completionStatus " +
           "FROM ResultTraining rt " +
           "LEFT JOIN rt.trainingHRMS th " +
           "LEFT JOIN rt.employee e " +
           "LEFT JOIN rt.trainingAcknowledge ta " +
           "LEFT JOIN rt.assignTraining at")
    Page<Object[]> fetchResultTrainingReport(Pageable pageable);
}
