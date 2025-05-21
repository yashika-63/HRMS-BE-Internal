package com.spectrum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.model.HRMSmaster;

public interface HRMSrepo extends JpaRepository<HRMSmaster,Long>{
    List<HRMSmaster> findBykeyvalue (String val1);
void deleteBykeyvalue(String keyvalue);

    
} 
