package org.BuyNothingProject.repository;

import java.util.List;

import org.BuyNothingProject.domain.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends CrudRepository<Account, Integer>{
	@Query("SELECT e FROM Account e")
	public List<Account> getAllAccounts();

	@Query("SELECT e FROM Account e WHERE e.uid = :uid ")
	public Account getAccountById(@Param(value = "uid") Integer uid);
}
