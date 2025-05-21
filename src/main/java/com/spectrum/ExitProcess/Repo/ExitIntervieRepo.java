package com.spectrum.ExitProcess.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.ExitProcess.Model.ExitInterview;

public interface ExitIntervieRepo extends JpaRepository<ExitInterview,Long>{
    List<ExitInterview> findByOffBoardingId(Long offBoardingId);

    
}
