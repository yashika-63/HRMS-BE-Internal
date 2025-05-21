package com.spectrum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.model.AccountMaster;
@Repository

public interface AccountMasterRepository extends JpaRepository<AccountMaster, Long> {

}
