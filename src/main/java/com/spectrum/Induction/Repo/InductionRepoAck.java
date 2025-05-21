package com.spectrum.Induction.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.Induction.Model.InductionAck;

import jakarta.transaction.Transactional;




public interface InductionRepoAck   extends JpaRepository<InductionAck, Long>{

    List<InductionAck> findByInductionId(Long inductionId);
  
    

}
