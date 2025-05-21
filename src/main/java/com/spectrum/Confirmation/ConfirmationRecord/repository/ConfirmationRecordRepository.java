package com.spectrum.Confirmation.ConfirmationRecord.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.Confirmation.ConfirmationRecord.model.ConfirmationRecord;

@Repository
public interface ConfirmationRecordRepository extends JpaRepository<ConfirmationRecord, Long> {



    List<ConfirmationRecord> findByCompanyIdAndStatusTrue(Long companyId);

    List<ConfirmationRecord> findByEmployeesIdAndStatusTrue(Long responsiblePersonId);
    List<ConfirmationRecord> findByEmployeesIdAndResponsiblePersonActionTrue(Long responsiblePersonId);
    List<ConfirmationRecord> findByEmployeesIdAndResponsiblePersonActionFalse(Long responsiblePersonId);
    List<ConfirmationRecord> findByEmployeesIdAndResponsiblePersonActionFalseAndStatusTrue(Long responsiblePersonId);

    


    @Query("SELECT cr FROM ConfirmationRecord cr " +
       "JOIN ConfirmationAction ca ON ca.confirmationRecord.id = cr.id " +
       "WHERE ca.confirm = true " +
       "AND ca.actionTakenBy IS NOT NULL " +
       "AND cr.company.id = :companyId " +
       "AND FUNCTION('YEAR', ca.date) = :year " +
       "AND FUNCTION('MONTH', ca.date) = :month")
List<ConfirmationRecord> findConfirmedActionsByCompanyMonthAndYear(
        @Param("companyId") Long companyId,
        @Param("year") int year,
        @Param("month") int month);




        @Query("SELECT cr FROM ConfirmationRecord cr " +
       "JOIN ConfirmationAction ca ON ca.confirmationRecord.id = cr.id " +
       "WHERE ca.confirm = false " +
       "AND ca.terminate = true " +
       "AND ca.actionTakenBy IS NOT NULL " +
       "AND cr.company.id = :companyId " +
       "AND FUNCTION('YEAR', ca.date) = :year " +
       "AND FUNCTION('MONTH', ca.date) = :month")
List<ConfirmationRecord> findRejectedActionsByCompanyMonthAndYear(
        @Param("companyId") Long companyId,
        @Param("year") int year,
        @Param("month") int month);




        @Query("SELECT cr FROM ConfirmationRecord cr " +
       "JOIN ConfirmationAction ca ON ca.confirmationRecord.id = cr.id " +
       "WHERE ca.confirm = false " +
       "AND ca.extend = true " +
       "AND ca.actionTakenBy IS NOT NULL " +
       "AND cr.company.id = :companyId " +
       "AND FUNCTION('YEAR', ca.date) = :year " +
       "AND FUNCTION('MONTH', ca.date) = :month")
List<ConfirmationRecord> findExtendedActionsByCompanyMonthAndYear(
        @Param("companyId") Long companyId,
        @Param("year") int year,
        @Param("month") int month);









        ///////////////////////
        /// 
        /// 
        /// 
        /// 
        /// 
        /// 
        /// 
        
        @Query("SELECT cr, emp, rp " +
        "FROM ConfirmationRecord cr " +
        "JOIN cr.employee emp " +
        "JOIN cr.employees rp " +
        "JOIN ConfirmationAction ca ON ca.confirmationRecord.id = cr.id " +
        "WHERE ca.confirm = false " +
        "AND ca.extend = true " +
        "AND ca.actionTakenBy IS NOT NULL " +
        "AND cr.company.id = :companyId " +
        "AND FUNCTION('YEAR', ca.date) = :year " +
        "AND FUNCTION('MONTH', ca.date) = :month")
 List<Object[]> findExtendedActionsWithEmployeeDetails(@Param("companyId") Long companyId,
                                                       @Param("year") int year,
                                                       @Param("month") int month);
 




@Query("SELECT cr, emp, rp " +
       "FROM ConfirmationRecord cr " +
       "JOIN cr.employee emp " +
       "JOIN cr.employees rp " +
       "JOIN ConfirmationAction ca ON ca.confirmationRecord.id = cr.id " +
       "WHERE ca.confirm = true " +
       "AND ca.extend = false " +
       "AND ca.actionTakenBy IS NOT NULL " +
       "AND cr.company.id = :companyId " +
       "AND FUNCTION('YEAR', ca.date) = :year " +
       "AND FUNCTION('MONTH', ca.date) = :month")
List<Object[]> findConfirmedActionsWithEmployeeDetails(@Param("companyId") Long companyId,
                                                       @Param("year") int year,
                                                       @Param("month") int month);



    @Query("SELECT cr, emp, rp " +
       "FROM ConfirmationRecord cr " +
       "JOIN cr.employee emp " +
       "JOIN cr.employees rp " +
       "JOIN ConfirmationAction ca ON ca.confirmationRecord.id = cr.id " +
       "WHERE ca.confirm = false " +
       "AND ca.extend = false " +
       "AND ca.actionTakenBy IS NOT NULL " +
       "AND cr.company.id = :companyId " +
       "AND FUNCTION('YEAR', ca.date) = :year " +
       "AND FUNCTION('MONTH', ca.date) = :month")
List<Object[]> findTerminatedActionsWithEmployeeDetails(@Param("companyId") Long companyId,
                                                        @Param("year") int year,
                                                        @Param("month") int month);


}
