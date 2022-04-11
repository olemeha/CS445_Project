package org.BuyNothingProject.repository;

import java.util.List;

import org.BuyNothingProject.domain.Give;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GiveRepository extends CrudRepository<Give, Integer> {
	@Query("SELECT e FROM Give e")
	public List<Give> getAllGives();

	@Query("SELECT e FROM Give e WHERE e.gid = :uid")
	public Give getGiveById(@Param(value = "uid") Integer uid);

	@Query("SELECT e.gid FROM Give e")
	public List<Integer> getAllGiveIds();
}
