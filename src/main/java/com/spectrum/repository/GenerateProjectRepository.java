package com.spectrum.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spectrum.model.GenerateProject;

public interface GenerateProjectRepository extends JpaRepository<GenerateProject, Long> {
	  Page<GenerateProject> findByClientNameContainingIgnoreCase(String clientName, Pageable pageable);
	  
	  GenerateProject findByprojectName(String projectName);
	  
	  Page<GenerateProject> searchProjectsByprojectNameContainingIgnoreCase(String projectName, Pageable pageable);
	  
	  List<GenerateProject> findByCompanyRegistration_Id(long companyId);

		Set<GenerateProject> findByEmployees_EmployeeId(String employeeId);


		
	  
	//   @Query(value = "SELECT p FROM GenerateProject p WHERE " +
	//             "LOWER(p.clientName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
	//             "LOWER(p.projectName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
	//             "LOWER(p.projectLead) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
	//             "LOWER(p.deliveryLead) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
	//             "LOWER(p.projectType) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
	//             "LOWER(p.industry) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
	//             "LOWER(p.technologies) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
	//             "LOWER(p.projectStatus) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
	//             "p.startDate = :startDate OR " +
	//             "p.endDate = :endDate OR " +
	//             "p.actualStartDate = :actualStartDate OR " +
	//             "p.expectedEndDate = :expectedEndDate OR " +
	//             "LOWER(p.cityLocation) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
	//             "LOWER(p.currentPhase) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
	//             "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchText, '%'))")
	//     List<GenerateProject> searchByAnyField(@Param("searchText") String searchText);
	
	@Query("SELECT e FROM GenerateProject e WHERE e.companyRegistration.id = :companyId AND " +
	"(e.clientName LIKE %:searchTerm% OR e.projectName LIKE %:searchTerm% OR e.deliveryLead LIKE %:searchTerm% OR e.projectLead LIKE %:searchTerm%)")
List<GenerateProject> searchGenerateProjectByAnyField(@Param("companyId") Long companyId,
	@Param("searchTerm") String searchTerm); 



	
	}


	  
	  
	  
	  

