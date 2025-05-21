package com.spectrum.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.model.CompanyRegistration;
import com.spectrum.model.Employee;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	
	 @Query("SELECT e FROM Employee e JOIN FETCH e.educations")
	    List<Employee> findAllWithEducations();
	 
	List<Employee> findByFirstName(String firstName);

    List<Employee> findByLastName(String lastName);

    Page<Employee> findAll(Pageable pageable);

    @Query(value = "SELECT e FROM Employee e WHERE " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.middleName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.motherName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.contactNo) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.gender) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.nationality) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.designation) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "e.experience = :experience OR " +
            "LOWER(e.panNo) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.adhaarNo) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            // "e.abscondDate = :abscondDate OR " +
            "e.joiningDate = :joiningDate OR " +
            // "e.exitDate = :exitDate OR " +
            "e.presence = :presence OR " +
            // "e.resign = :resign OR " +
            "e.priorId = :priorId OR " +
            "LOWER(e.employeeType) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.currentHouseNo) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.currentStreet) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.currentCity) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.currentState) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.currentPostelcode) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.currentCountry) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.permanentHouseNo) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.permanentStreet) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.permanentCity) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.permanentState) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.permanentPostelcode) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(e.permanentCountry) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<Employee> searchByAnyField(@Param("searchText") String searchText,
                                     @Param("experience") int experience,
                                     @Param("joiningDate") Date joiningDate,
                                     @Param("presence") boolean presence,
                                     @Param("priorId") long priorId);

    boolean existsByEmployeeIdAndCompanyRegistration(String employeeId, CompanyRegistration companyRegistration);

    Optional<Employee> findByIdAndCompanyRegistration_Id(long id, long companyId);

    List<Employee> findByCompanyRegistration_Id(long companyId);

    Optional<List<Employee>> findByCompanyRegistration_Id(Long companyId);

    Employee findByEmployeeId(String employeeId);

    @Query("SELECT DISTINCT s.firstName , s.lastName FROM Employee s")
    List<String> findDistinctProjectHeadings();

    Page<Employee> findByPresenceAndCompanyRegistration_Id(boolean presence, Long companyId, Pageable pageable);

    List<Employee> findByPresenceAndCompanyRegistration_Id(boolean presence, Long companyId);

    Optional<Employee> findById(long id);

    @Query("SELECT e FROM Employee e WHERE e.companyRegistration.id = :companyId AND " +
            "(e.firstName LIKE %:searchTerm% OR e.lastName LIKE %:searchTerm% OR e.email LIKE %:searchTerm% OR e.employeeId LIKE %:searchTerm%)")
    List<Employee> searchEmployeesByAnyField(@Param("companyId") Long companyId,
            @Param("searchTerm") String searchTerm); 
            @Query("SELECT COUNT(e) FROM Employee e WHERE e.presence = true AND e.companyRegistration.id = :companyId")
            Long countActiveEmployeesByCompanyId(@Param("companyId") Long companyId);


            
}
