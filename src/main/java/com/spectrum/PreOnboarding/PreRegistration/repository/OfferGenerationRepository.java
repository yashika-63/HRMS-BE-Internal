package com.spectrum.PreOnboarding.PreRegistration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.PreOnboarding.PreRegistration.model.OfferGeneration;

public interface OfferGenerationRepository extends JpaRepository<OfferGeneration, Long> {


    
    @Query("SELECT og FROM OfferGeneration og " +
           "LEFT JOIN FETCH og.company c " +
           "LEFT JOIN FETCH og.employees e " +
           "LEFT JOIN FETCH og.preRegistration p " +
           "LEFT JOIN FETCH OfferCTCBreakdownHeader ctc ON ctc.OfferGeneration.id = og.id " +
           "WHERE (:companyId IS NULL OR c.id = :companyId) " +
           "AND (:reportingPersonId IS NULL OR e.id = :reportingPersonId) " +
           "AND (:status IS NULL OR og.status = :status) " +
           "AND (:sendForApproval IS NULL OR og.sendForApproval = :sendForApproval)")
    List<OfferGeneration> findByFilters(
        @Param("companyId") Long companyId,
        @Param("reportingPersonId") Long reportingPersonId,
        @Param("status") Boolean status,
        @Param("sendForApproval") Boolean sendForApproval
    );



    @Query("SELECT o FROM OfferGeneration o WHERE o.company.id = :companyId AND o.preRegistration.preLoginToken = :preLoginToken AND o.preRegistration.id = :preRegistrationId")
    List<OfferGeneration> findByCompanyIdAndPreLoginTokenAndPreRegistrationId(
        @Param("companyId") Long companyId,
        @Param("preLoginToken") String preLoginToken,
        @Param("preRegistrationId") Long preRegistrationId
    );


    List<OfferGeneration> findByPreRegistration_IdAndPreRegistration_PreLoginToken(Long preRegistrationId, String preLoginToken);



    @Query("SELECT og FROM OfferGeneration og " +
       "LEFT JOIN FETCH og.company c " +
       "LEFT JOIN FETCH og.employees e " +
       "LEFT JOIN FETCH og.preRegistration p " +
       "LEFT JOIN FETCH OfferCTCBreakdownHeader ctc ON ctc.OfferGeneration.id = og.id " +
       "WHERE (:companyId IS NULL OR c.id = :companyId) " +
       "AND (:status IS NULL OR og.status = :status) " +
       "AND (:sendForApproval IS NULL OR og.sendForApproval = :sendForApproval)")
List<OfferGeneration> findByCompanyAndStatusAndApproval(
    @Param("companyId") Long companyId,
    @Param("status") Boolean status,
    @Param("sendForApproval") Boolean sendForApproval
);




List<OfferGeneration> findByPreRegistration_Id(Long preRegistrationId);

List<OfferGeneration> findByStatusTrueAndSendForApprovalTrueAndActionTakenByCandidateFalseAndPreRegistrationIdAndPreRegistrationPreLoginToken(
    Long preRegistrationId, String preLoginToken
);


List<OfferGeneration> findByPreRegistration_IdAndPreRegistration_PreLoginTokenAndSendForApprovalTrue(
    Long preRegistrationId, String preLoginToken);


    List<OfferGeneration> findByPreRegistration_IdAndPreRegistration_PreLoginTokenAndActionTakenByCandidateTrue(
    Long preRegistrationId, String preLoginToken);


    List<OfferGeneration> findByCompanyIdAndStatusTrueAndSendForApprovalFalse(Long companyId);

    Optional<OfferGeneration>  findByIdAndPreRegistration_PreLoginToken(
        Long id, String preLoginToken);
   
}
