package com.coding.challenge.appdirect.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.coding.challenge.appdirect.bean.Account;

public interface AccountRepository extends CrudRepository<Account, String> {

	 @Query("SELECT a FROM Account a JOIN FETCH a.users")
	 public Set<Account> findAllFetchUsers();
}
