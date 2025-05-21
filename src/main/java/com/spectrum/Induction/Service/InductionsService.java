package com.spectrum.Induction.Service;

import java.util.List;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.spectrum.Induction.Model.InductionAck;
import com.spectrum.Induction.Model.Inductions;
import com.spectrum.Induction.Repo.InductionRepoAck;
import com.spectrum.Induction.Repo.InductionsRepository;
import com.spectrum.model.CompanyRegistration;
import com.spectrum.repository.CompanyRegistrationRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Service
public class InductionsService {
     @Autowired
    private InductionsRepository inductionsRepository;

    @Autowired
   private  CompanyRegistrationRepository  cr;

    @Autowired
   private  InductionRepoAck inductionAckRepository;


    // ✅ Save Induction
    public Inductions saveInduction(Inductions inductions) {
        inductions.setStatus(true);
        return inductionsRepository.save(inductions);
    }

    // ✅ Get All Inductions
    public List<Inductions> getAllInductions() {
        return inductionsRepository.findAll();
    }

    // ✅ Get Induction By ID
    public Inductions getInductionById(Long id) {
        return inductionsRepository.findById(id).orElseThrow(()-> new RuntimeException("Id not found"));
    }


    // public Inductions updateInduction(Long id, Inductions updatedInduction) {
    //     Inductions existingInduction = inductionsRepository.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Id not found"));

    //     existingInduction.setRegion(updatedInduction.getRegion());
    //     existingInduction.setHeading(updatedInduction.getHeading());
    //     existingInduction.setDescription(updatedInduction.getDescription());
    //     existingInduction.setStatus(updatedInduction.isStatus());
    //     existingInduction.setDate(updatedInduction.getDate());
    //     existingInduction.setType(updatedInduction.isType());
    //     existingInduction.setCreatedByEmployeeId(updatedInduction.getCreatedByEmployeeId());
    //     // existingInduction.setCompany(updatedInduction.getCompany()); // Updating company reference if needed

    //     return inductionsRepository.save(existingInduction);
    // }
    public Inductions updateInduction(Long id, Inductions updatedInduction) {
        Inductions existingInduction = inductionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id not found"));
 
        existingInduction.setRegion(updatedInduction.getRegion());
        existingInduction.setHeading(updatedInduction.getHeading());
        existingInduction.setDescription(updatedInduction.getDescription());
        existingInduction.setStatus(updatedInduction.isStatus());
        existingInduction.setDate(updatedInduction.getDate());
        existingInduction.setType(updatedInduction.isType());
        existingInduction.setDepartment(updatedInduction.getDepartment());
        existingInduction.setDepartmentId(updatedInduction.getDepartmentId());
        existingInduction.setCreatedByEmployeeId(updatedInduction.getCreatedByEmployeeId());
        // existingInduction.setCompany(updatedInduction.getCompany()); // Updating company reference if needed
 
        return inductionsRepository.save(existingInduction);
    } 

    // ✅ Delete Induction
    public void deleteInduction(Long id) {
    Inductions var1 =inductionsRepository.findById(id).orElseThrow(()-> new RuntimeException("Id not found"));
    inductionsRepository.deleteById(id);
    }   

    /////////////////////////////////////////////////////////////////////////////////////
    // api related with company and induction 
     public Inductions saveInduction(Long companyId, Inductions inductions,Long createdByEmployeeId ) {

        if (createdByEmployeeId == null || createdByEmployeeId <= 0) {
            throw new EntityNotFoundException("Invalid Employee ID: " + createdByEmployeeId);
        } 
        CompanyRegistration company = cr.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company with ID " + companyId + " not found.")); 

                       
                inductions.setStatus(true);

         inductions.setCreatedByEmployeeId(createdByEmployeeId);
        inductions.setCompany(company); // Set the company reference
        return inductionsRepository.save(inductions);
    }


    public List<Inductions> getInductionsByYearAndCompany(int year, Long companyId) {
        List<Inductions> inductions = inductionsRepository.findByYearAndCompany(year, companyId);
    
        if (inductions.isEmpty()) {
            throw new EntityNotFoundException("No inductions found for year " + year + " and company ID " + companyId);
        }
        
        return inductions;
    }


    public List<Inductions> getInductionsByCompanyAndStatus(Long companyId, boolean status) {
        List<Inductions> inductions = inductionsRepository.findByCompanyIdAndStatus(companyId, status);

        if (inductions.isEmpty()) {
            throw new EntityNotFoundException("No inductions found for company ID " + companyId + " with status " + status);
        }

        return inductions;
    }

    // public List<Inductions> getInductionsByYearAndRegion(int year, String region) {
    //     List<Inductions> inductions = inductionsRepository.findByYearAndRegion(year, region);

    //     if (inductions.isEmpty()) {
    //         throw new EntityNotFoundException("No inductions found for year " + year + " and region " + region);
    //     }

    //     return inductions;
    // }


    
    public List<Inductions> getInductionsByYearRegionAndCompany(int year, Long region, Long companyId) {
        List<Inductions> inductions = inductionsRepository.findByYearRegionAndCompany(year, region, companyId);

        if (inductions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No inductions found for the given criteria.");
        }
        return inductions;
    }

    


    public Page<Inductions> searchInductionRecords(Integer year, String description, 
                                            String heading, Long companyId, 
                                            Long regionId, Long departmentId,
                                            Pageable pageable) {
    return inductionsRepository.searchInductions(
        year, 
        description, 
        heading, 
        companyId,
        regionId,
        departmentId, 
        pageable
    );
}

    // public Page<Inductions> searchInductions(Integer year, String description, String heading, Long companyId, Pageable pageable) {
    //     long totalRecords = countInductions(companyId); // Count total records

    //     if (totalRecords > 10000) { // If dataset is large, use Full-Text Search
    //         return searchWithFullText(year, description, heading, companyId, pageable);
    //     } else {
    //         return inductionsRepository.searchInductions(year, description, heading, companyId, pageable); // Use LIKE for small datasets
    //     }

    // }

    // Count total inductions for a specific company
    // public long countInductions(Long companyId) {
    //     Query query = (Query) entityManager.createQuery("SELECT COUNT(i) FROM Inductions i WHERE i.status = true AND i.company.id = :companyId");
    //     query.setParameter("companyId", companyId);
    //     return (long) query.getSingleResult();
    // }

    // // Full-Text Search (FTS) with pagination
    // public Page<Inductions> searchWithFullText(Integer year, String description, String heading, Long companyId, Pageable pageable) {
    //     String jpql = "SELECT i FROM Inductions i WHERE i.status = true AND i.company.id = :companyId AND ("
    //                 + "(:year IS NULL OR YEAR(i.date) = :year) "
    //                 + "OR (:description IS NULL OR FUNCTION('MATCH', i.description, :description)) "
    //                 + "OR (:heading IS NULL OR LOWER(i.heading) LIKE LOWER(CONCAT('%', :heading, '%'))))";

    //     TypedQuery<Inductions> query = entityManager.createQuery(jpql, Inductions.class);
    //     query.setParameter("year", year);
    //     query.setParameter("description", description);
    //     query.setParameter("heading", heading);
    //     query.setParameter("companyId", companyId);

    //     // Pagination handling
    //     query.setFirstResult((int) pageable.getOffset());
    //     query.setMaxResults(pageable.getPageSize());

    //     List<Inductions> results = query.getResultList();
    //     long totalResults = results.size();

    //     return new PageImpl<>(results, pageable, totalResults);
    // }

    /////////////////////////////////////////////////////////////////////////////////
    

}
