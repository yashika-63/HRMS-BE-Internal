package com.spectrum.PreOnboarding.EmployeeVerification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.PreOnboarding.EmployeeVerification.model.EmployeeVerificationDocument;
import com.spectrum.PreOnboarding.EmployeeVerification.model.VerificationTicket;


public interface VerificationTicketRepository extends JpaRepository<VerificationTicket, Long> {


    Optional<VerificationTicket> findByPreRegistrationId(Long preRegistrationId);



    @Query("SELECT v FROM VerificationTicket v " +
           "WHERE v.preRegistration.preLoginToken = :token " +
           "AND v.preRegistration.id = :preRegId " +
           "AND v.status = true " +
           "AND v.sentForEmployeeAction = true")
    Optional<VerificationTicket> findActiveTicketByTokenAndPreRegId(String token, Long preRegId);


    List<VerificationTicket> findByCompany_IdAndEmployees_IdAndStatusTrueAndSendForVerificationTrue(Long companyId, Long employeeId);


    @Query("SELECT v FROM VerificationTicket v WHERE " +
       "v.status = true AND " +
       "v.sentForEmployeeAction = true AND " +
       "v.employeeAction = false AND " +
       "v.preRegistration.id = :preRegistrationId AND " +
       "v.preRegistration.preLoginToken = :preLoginToken")
List<VerificationTicket> findPendingEmployeeActionTickets(
        @Param("preRegistrationId") Long preRegistrationId,
        @Param("preLoginToken") String preLoginToken);



        @Query("SELECT v FROM VerificationTicket v WHERE " +
       "v.status = true AND " +
       "v.sentForEmployeeAction = true AND " +
       "v.employeeAction = true AND " +
       "v.preRegistration.id = :preRegistrationId AND " +
       "v.preRegistration.preLoginToken = :preLoginToken")
List<VerificationTicket> findCompletedEmployeeActionTickets(
        @Param("preRegistrationId") Long preRegistrationId,
        @Param("preLoginToken") String preLoginToken);


        @Query("SELECT v FROM VerificationTicket v WHERE v.status = true AND v.verificationStatus = false")
        List<VerificationTicket> findAllPendingVerification();


        List<VerificationTicket> findByCompanyIdAndStatusTrue(Long companyId);

        List<VerificationTicket> findByEmployeesIdAndSendForVerificationTrueAndReportingPersonActionFalseAndStatusTrue(
                Long reportingPersonId);

                @Query("SELECT v FROM VerificationTicket v " +
                "WHERE v.status = true AND v.verificationStatus = true AND v.company.id = :companyId " +
                "ORDER BY v.id DESC")
         List<VerificationTicket> findTop10VerifiedTicketsByCompanyId(@Param("companyId") Long companyId);
         


         Optional<VerificationTicket> findById(Long id);
       
}
