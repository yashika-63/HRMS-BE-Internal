package com.spectrum.Induction.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.Induction.Model.ResultInduction;

public interface ResultRepo extends JpaRepository<ResultInduction,Long>{
     @Query("SELECT r FROM ResultInduction r WHERE r.induction.id = :inductionId AND r.induction.createdByEmployeeId = :employeeId")
    List<ResultInduction> findByInductionIdAndEmployeeId(@Param("inductionId") Long inductionId,
                                                         @Param("employeeId") Long employeeId);


 List<ResultInduction> findAllByEmployeeIdAndInductionId(Long employeeId, Long inductionId);

 Optional<ResultInduction> findByEmployeeIdAndInductionId(Long employeeId, Long inductionId);


          List<ResultInduction> findAllByEmployeeId(Long employeeId); 
}


