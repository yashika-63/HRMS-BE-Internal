package com.spectrum.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.model.Master;


@Repository
public interface MasterRepository extends JpaRepository<Master, Long> {
//	  Page<Master> findBykeyvalueContainingIgnoreCase(String keyvalue, Pageable pageable);
  Master findByKeyvalue(String Keyvalue);
//	  
	  List<Master> findByKeyvalueContainingIgnoreCase(String keyvalue);  
	  
	  
	  
	  
	  @Query(value = "SELECT m FROM Master m WHERE " +
	            "LOWER(m.keyvalue) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
	            "LOWER(m.data) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
	            "LOWER(m.category) LIKE LOWER(CONCAT('%', :searchText, '%'))")
	    List<Master> searchByAnyField(@Param("searchText") String searchText);

	  

	  
	  
}
