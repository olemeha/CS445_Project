package org.BuyNothingProject.repository;

import java.util.List;

import org.BuyNothingProject.domain.Thanks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ThanksRepository extends CrudRepository<Thanks, Integer> {
	@Query("SELECT e FROM Thanks e")
	public List<Thanks> getAllThanks();

	@Query("SELECT e FROM Thanks e WHERE e.tid= :tid")
	public Thanks getThankById(@Param(value = "tid") Integer tid);

}
