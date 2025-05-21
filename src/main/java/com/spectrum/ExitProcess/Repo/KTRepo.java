package com.spectrum.ExitProcess.Repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.ExitProcess.Model.KnowledgeTransfer;

public interface KTRepo extends JpaRepository<KnowledgeTransfer,Long>{
    

    List<KnowledgeTransfer> findByEmployeesId(Long employeeById);


    
 List<KnowledgeTransfer> findByEmployeeId(Long employeeToId);

 List<KnowledgeTransfer> findByDateBetween(LocalDate startDate, LocalDate endDate);
 List<KnowledgeTransfer> findByCompletionStatus(Boolean completionStatus);

 Optional<KnowledgeTransfer> findByIdAndEmployeeIdAndEmployeesId(
    Long id, 
    Long employeeToId, 
    Long employeeById
);

Optional<KnowledgeTransfer> findByIdAndEmployeeId(Long id, Long employeeId);
List<KnowledgeTransfer> findByCompletionStatusAndEmployee_Id(Boolean completionStatus, Long empToId);
//    @Query("SELECT kt FROM KnowledgeTransfer kt WHERE kt.id = :id AND kt.employee.id = :employeeToId")
//     Optional<KnowledgeTransfer> findByIdAndEmployeeToId(
//         @Param("id") Long id, 
//         @Param("employeeToId") Long employeeToId
//     );



List<KnowledgeTransfer> findByOffBoardingId(Long offBoardingId);

}
