package com.coding.challenge.appdirect.repositories;

import org.springframework.data.repository.CrudRepository;

import com.coding.challenge.appdirect.bean.Account;

public interface AccountRepository extends CrudRepository<Account, String> {

}
