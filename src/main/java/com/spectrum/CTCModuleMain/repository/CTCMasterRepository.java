package com.spectrum.CTCModuleMain.repository;

import com.spectrum.CTCModuleMain.model.CTCMaster;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CTCMasterRepository extends JpaRepository<CTCMaster, Long> {

 // Find by companyId and category
 @Query("SELECT c FROM CTCMaster c WHERE c.company.id = :companyId AND c.category = :category")
 List<CTCMaster> findByCompanyIdAndCategory(@Param("companyId") Long companyId, @Param("category") String category);

 // Find all CTCMaster entries by companyId
 List<CTCMaster> findByCompanyId(Long companyId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CTCMaster c WHERE c.company.id = :companyId AND c.category = :category")
    void deleteByCompanyIdAndCategory(@Param("companyId") Long companyId, @Param("category") String category);

}
