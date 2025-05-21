package com.spectrum.Recruitment.JobDescription.repository;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.Recruitment.JobDescription.model.ScheduleInterview;

public interface ScheduleInterviewRepository extends JpaRepository<ScheduleInterview, Long> {


    List<ScheduleInterview> findByCandidateRegistrationIdAndInterviewStatusTrue(Long candidateId);


    @Query("SELECT s FROM ScheduleInterview s " +
       "WHERE s.employees.id = :interviewerId " +
       "AND s.interviewDate = :interviewDate " +
       "AND s.interviewTime BETWEEN :startTime AND :endTime " +
       "AND s.interviewStatus = true")
List<ScheduleInterview> findConflictingSchedules(@Param("interviewerId") Long interviewerId,
                                                  @Param("interviewDate") LocalDate interviewDate,
                                                  @Param("startTime") Time startTime,
                                                  @Param("endTime") Time endTime);




    @Query("SELECT s FROM ScheduleInterview s " +
       "WHERE s.company.id = :companyId " +
       "AND s.interviewStatus = true " +
       "AND s.interviewDate > :today")
List<ScheduleInterview> findUpcomingInterviewsByCompany(@Param("companyId") Long companyId,
                                                         @Param("today") LocalDate today);



          @Query("SELECT s FROM ScheduleInterview s " +
         "WHERE s.interviewStatus = :status AND s.employees.id = :interviewerId")
        List<ScheduleInterview> findByStatusAndInterviewerId(@Param("status") boolean status,
         @Param("interviewerId") Long interviewerId);
                                                  



         @Query("SELECT s FROM ScheduleInterview s WHERE s.interviewDate IN :dates AND s.interviewStatus = true")
List<ScheduleInterview> findByInterviewDateInAndInterviewStatusTrue(@Param("dates") List<LocalDate> dates);



List<ScheduleInterview> findByJobDescriptionIdAndInterviewStatusTrue(Long jobDescriptionId);

List<ScheduleInterview> findByInterviewDateAndInterviewStatusTrueAndEmployeesId(LocalDate date, Long interviewerId);


List<ScheduleInterview> findByInterviewDateAndInterviewStatus(LocalDate interviewDate, boolean interviewStatus);



@Query("SELECT s FROM ScheduleInterview s WHERE MONTH(s.interviewDate) = :month AND YEAR(s.interviewDate) = :year AND s.company.id = :companyId")
List<ScheduleInterview> findByMonthYearAndCompany(@Param("month") int month,
                                                   @Param("year") int year,
                                                   @Param("companyId") Long companyId);

                                                   

    List<ScheduleInterview> findByIdAndInterviewDateBetween(Long interviewId, LocalDate startDate, LocalDate endDate);
    List<ScheduleInterview> findByEmployeesIdAndInterviewDateBetween(Long interviewerId, LocalDate startDate, LocalDate endDate);

}
