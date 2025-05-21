package com.spectrum.Induction.Repo;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.spectrum.Induction.Model.AssignInduction;

public interface AssignInductionRepo
              extends JpaRepository<AssignInduction, Long>, PagingAndSortingRepository<AssignInduction, Long> {
       List<AssignInduction> findByEmployeeId(Long employeeId);

       @Query("SELECT a FROM AssignInduction a " +
                     "WHERE a.companyId = :companyId AND YEAR(a.assignDate) = :year")
       List<AssignInduction> findAllByCompanyIdAndYear(@Param("companyId") Long companyId,
                     @Param("year") int year);

       List<AssignInduction> findAllByCompanyIdAndAssignDateBetween(Long companyId, LocalDate startDate,
                     LocalDate endDate);

       // @Query("FROM AssignInduction ai " +
       // "WHERE ai.employee.id = :employeeId " +
       // "AND (:completionStatus != true OR ai.completionStatus = true) " +
       // "AND (:expiryStatus != true OR ai.expiryStatus = true)")
       // List<AssignInduction> filterInductions(@Param("employeeId") Long employeeId,
       // @Param("completionStatus") Boolean completionStatus,
       // @Param("expiryStatus") Boolean expiryStatus);

       @Query("FROM AssignInduction ai " +
                     "WHERE ai.employee.id = :employeeId " +
                     "AND ai.completionStatus = :completionStatus " +
                     "AND ai.expiryStatus = :expiryStatus")
       List<AssignInduction> filterInductions(@Param("employeeId") Long employeeId,
                     @Param("completionStatus") Boolean completionStatus,
                     @Param("expiryStatus") Boolean expiryStatus);

                     List<AssignInduction> findByCompletionStatusFalseAndExpiryStatusFalse();

                     List<AssignInduction> findAllByCompanyId(Long companyId);
}
