package org.BuyNothingProject.repository;

import java.util.List;

import org.BuyNothingProject.domain.Ask;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AskRepository extends CrudRepository<Ask, Integer> {
	@Query("SELECT e FROM Ask e")
	public List<Ask> getAllAsks();

	@Query("SELECT e FROM Ask e WHERE e.aid = :aid")
	public Ask getAskById(@Param(value = "aid") Integer aid);

	@Query("SELECT e.aid FROM Ask e")
	public List<Integer> getAllAskIds();
}
