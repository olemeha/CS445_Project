package org.BuyNothing.BuyNothingProject.repository; 

import java.time.LocalDateTime;
import java.util.List;

import org.BuyNothing.BuyNothingProject.domain.ask;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public interface AskRepository extends CrudRepository<ask, Integer>{
	
	@Query("SELECT e FROM ask e")
	public List <ask> getallAsk();
	
	@Query("from ask e where not(e.created <: from or e.created > :to)")
	public List <ask> findBetween(
			@Param("from") @DateTimeFormat(iso=ISO.DATE_TIME) LocalDateTime start,
			@Param("to") @DateTimeFormat(iso=ISO.DATE_TIME) LocalDateTime end);

}
